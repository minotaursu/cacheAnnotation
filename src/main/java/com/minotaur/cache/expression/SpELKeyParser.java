package com.minotaur.cache.expression;

import org.apache.commons.logging.Log;
import org.springframework.expression.EvaluationContext;
import org.springframework.util.StringUtils;

import com.minotaur.cache.support.LogUtils;

/**
 * Support spring expression language
 * Generate @Cacheable @CacheEvict @CachePut's key expression to String
 * Created by minotaur on 15/11/19.
 */
public class SpELKeyParser implements KeyParser {

    private static final Log          logger    = LogUtils.CACHE_LOGGER;
    private static final Log          warnLog   = LogUtils.WARN_LOGGER;

    private final ExpressionEvaluator evaluator = new ExpressionEvaluator();

    @Override
    public String generateKey(CacheExpressionRootObject cacheExpressionRootObject, String key) {
        try {
            if (StringUtils.hasText(key)) {
                EvaluationContext evalContext = evaluator.createEvaluationContext(cacheExpressionRootObject);
                return evaluator.key(key, cacheExpressionRootObject.getMethod(), evalContext);
            }
            return generateDefaultKey(cacheExpressionRootObject);
        } catch (Exception e) {
            warnLog.warn("cache key expression parser error: ", e);
            return null;
        }
    }

    @Override
    public String generateDefaultKey(CacheExpressionRootObject cacheExpressionRootObject) {
        try {
            Object[] args = cacheExpressionRootObject.getArgs();
            if (args == null || args.length == 0)
                return "_";
            StringBuilder sb = new StringBuilder();
            for (Object arg : args) {
                int hashCode = 0;
                if (arg != null) {
                    hashCode = arg.hashCode();
                }
                sb.append(hashCode).append("_");
            }
            return sb.toString();
        } catch (Exception e) {
            warnLog.warn("cache default key error: ", e);
            return null;
        }
    }

    @Override
    public boolean isConditionPassing(CacheExpressionRootObject cacheExpressionRootObject, String condition) {
        try {
            if (StringUtils.hasText(condition)) {
                EvaluationContext evalContext = evaluator.createEvaluationContext(cacheExpressionRootObject);
                return evaluator.condition(condition, cacheExpressionRootObject.getMethod(), evalContext);
            }
            return true;
        } catch (Exception e) {
            warnLog.warn("cache condition expression parser error: ", e);
            return false;
        }
    }
}
