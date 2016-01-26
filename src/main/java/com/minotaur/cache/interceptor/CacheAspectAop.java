package com.minotaur.cache.interceptor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import com.minotaur.cache.annotation.CacheEvict;
import com.minotaur.cache.annotation.CachePut;
import com.minotaur.cache.annotation.Cacheable;
import com.minotaur.cache.expression.CacheExpressionRootObject;
import com.minotaur.cache.expression.KeyParser;
import com.minotaur.cache.expression.SpELKeyParser;
import com.minotaur.cache.support.Cache;
import com.minotaur.cache.support.CacheManager;
import com.minotaur.cache.support.CacheOperation;
import com.minotaur.cache.support.LogUtils;
import com.minotaur.cache.support.impl.Operation;

/**
 * NOTE!!
 * 不支持调用内部方法 innerMethod
 * 不支持private,protected方法
 * 不支持类级别的注释,只能注解method
 * 不支持一个method有多个注解
 *
 * 上述不支持源于spring的AOP机制,如需要支持使用BeanNameAutoProxyCreator来创建代理
 * Created by minotaur on 15/11/19.
 */
@Service
@Aspect
public class CacheAspectAop {

    private static final Log    logger            = LogUtils.CACHE_LOGGER;
    private static final Log    warnLog           = LogUtils.WARN_LOGGER;

    private KeyParser           keyParser         = new SpELKeyParser();

    private Map<String, Method> targetMethodCache = new ConcurrentHashMap<>();

    @Autowired
    private CacheManager        cacheManager;

    /**
     * 对annotation进行拦截
     */
    @Around("@annotation (com.minotaur.cache.annotation.Cacheable) ||"
            + "@annotation (com.minotaur.cache.annotation.CachePut) ||"
            + "@annotation (com.minotaur.cache.annotation.CacheEvict)")
    public Object cache(ProceedingJoinPoint pjp) throws Throwable {
        return invoke(pjp);
    }

    /***
     * 需要反射出具体的method,不能使用interface的method
     * @param pjp JoinPoint
     * @return invoke result
     * @throws Throwable
     */
    private Object invoke(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Object target = pjp.getTarget();
        Object[] args = pjp.getArgs();
        Class<?> returnType = methodSignature.getReturnType();

        //cache target method
        Method method = methodSignature.getMethod();
        String mKey = toString(method);
        Method targetMethod = this.targetMethodCache.get(mKey);
        if (targetMethod == null) {
            targetMethod = ReflectionUtils.findMethod(target.getClass(), methodSignature.getName(),
                methodSignature.getParameterTypes());
            if (targetMethod != null) {
                this.targetMethodCache.put(mKey, targetMethod);
            }
        }
        try {
            if (target == null || targetMethod == null) {
                return pjp.proceed();
            }
            CacheExpressionRootObject rootObject = new CacheExpressionRootObject(targetMethod, args, target,
                target.getClass(), returnType);
            Cacheable cacheable = targetMethod.getAnnotation(Cacheable.class);
            if (cacheable != null) {
                CacheOperation cacheOperation = new CacheOperation(cacheable.value(), cacheable.condition(),
                    cacheable.key(), cacheable.keyPrefix(), cacheable.expire(), Operation.CACHE);
                return execute(pjp, rootObject, cacheOperation);
            }
            CacheEvict cacheEvict = targetMethod.getAnnotation(CacheEvict.class);
            if (cacheEvict != null) {
                CacheOperation cacheOperation = new CacheOperation(cacheEvict.value(), cacheEvict.condition(),
                    cacheEvict.key(), cacheEvict.keyPrefix(), -1, Operation.EVICT);
                return execute(pjp, rootObject, cacheOperation);
            }
            CachePut cachePut = targetMethod.getAnnotation(CachePut.class);
            if (cachePut != null) {
                CacheOperation cacheOperation = new CacheOperation(cachePut.value(), cachePut.condition(),
                    cachePut.key(), cachePut.keyPrefix(), cachePut.expire(), Operation.PUT);
                return execute(pjp, rootObject, cacheOperation);
            }
            return pjp.proceed();
        } catch (Throwable e) {
            warnLog.warn("Cache " + method.getName() + " error. args: " + Arrays.toString(args));
            throw e;
        }
    }

    /**
     * 条件校验&生成cacheKey
     * before joinPoint.proceed()
     * after  joinPoint.proceed()
     * @param joinPoint joinPoint
     * @param cacheExpressionRootObject cacheExpressionRootObject
     * @param cacheOperation cacheOperation
     * @return
     * @throws Throwable
     */
    private Object execute(ProceedingJoinPoint joinPoint, CacheExpressionRootObject cacheExpressionRootObject,
                           CacheOperation cacheOperation) throws Throwable {
        Cache cache = cacheManager.getCache(cacheOperation.getCacheName());
        Assert.notNull(cache, "cache is required");

        if (!keyParser.isConditionPassing(cacheExpressionRootObject, cacheOperation.getCondition())) {
            logger.info("cache condition false: " + cacheOperation.getCondition());
            return joinPoint.proceed();
        }
        String key = keyParser.generateKey(cacheExpressionRootObject, cacheOperation.getKey());
        if (StringUtils.isBlank(key)) {
            return joinPoint.proceed();
        }
        String keyPrefix = cacheOperation.getKeyPrefix();
        if (StringUtils.isBlank(keyPrefix)) {
            keyPrefix = cacheExpressionRootObject.getMethodName();
        }
        String cacheKey = keyPrefix + ":" + key;
        Operation operation = cacheOperation.getOperation();

        if (operation == Operation.CACHE) {
            Object object = cache.get(cacheKey, cacheExpressionRootObject.getReturnType());
            if (object != null) {
                logger.info("cache get key: " + cacheKey);
                return object;
            }
        }

        Object object = joinPoint.proceed();
        logger.info("db get key: " + cacheKey);

        if (operation == Operation.EVICT) {
            cache.del(cacheKey);
            logger.info("cache delete key: " + cacheKey);
        }
        if (object != null && (operation == Operation.PUT || operation == Operation.CACHE)) {
            cache.put(cacheKey, object, cacheOperation.getExpire());
            logger.info("cache put key: " + cacheKey);
        }
        return object;
    }

    private String toString(Method m) {
        StringBuilder sb = new StringBuilder();
        sb.append(m.getDeclaringClass().getName());
        sb.append("#");
        sb.append(m.toString());
        return sb.toString();
    }
}
