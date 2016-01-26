package com.minotaur.cache.expression;

/**
 * generate key.
 * the core of expression
 * Created by minotaur on 15/11/19.
 */
public interface KeyParser {

    /**
     * 自定义key时生成key
     * @param cacheExpressionRootObject
     * @param key
     * @return
     */
    public String generateKey(CacheExpressionRootObject cacheExpressionRootObject, String key);

    /**
     * 默认生成key
     * 生成规则: methodName_arg0.hashcode_arg1.hashcode_+_....
     * @param cacheExpressionRootObject
     * @return
     */
    public String generateDefaultKey(CacheExpressionRootObject cacheExpressionRootObject);

    /**
     * 是否满足条件
     * @param cacheExpressionRootObject
     * @param condition
     * @return
     */
    public boolean isConditionPassing(CacheExpressionRootObject cacheExpressionRootObject, String condition);
}
