package com.minotaur.cache.expression;

import java.lang.reflect.Method;

import org.springframework.util.Assert;

/**
 * expression cache bean
 * Created by minotaur on 15/11/19.
 */
public class CacheExpressionRootObject {

    private final Method   method;

    private final Object[] args;

    private final Object   target;

    private final Class<?> targetClass;

    private final Class<?> returnType;

    public CacheExpressionRootObject(Method method, Object[] args, Object target, Class<?> targetClass, Class<?> returnType) {

        Assert.notNull(method, "Method is required");
        Assert.notNull(targetClass, "targetClass is required");
        this.method = method;
        this.target = target;
        this.targetClass = targetClass;
        this.returnType=returnType;
        this.args = args;
    }

    public Method getMethod() {
        return this.method;
    }

    public String getMethodName() {
        return this.method.getName();
    }

    public Object[] getArgs() {
        return this.args;
    }

    public Object getTarget() {
        return this.target;
    }

    public Class<?> getTargetClass() {
        return this.targetClass;
    }

    public Class<?> getReturnType() {
        return returnType;
    }
}
