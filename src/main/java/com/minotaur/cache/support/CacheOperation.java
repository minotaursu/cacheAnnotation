package com.minotaur.cache.support;


import com.minotaur.cache.support.impl.Operation;

/**
 * cache operation
 * Created by minotaur on 15/11/18.
 */
public class CacheOperation {

    private String cacheName;
    private String condition;
    private String key;
    private String keyPrefix;
    private int expire;
    private Operation operation;

    public CacheOperation(String cacheName,String condition,String key,String keyPrefix,int expire,Operation operation){
        this.cacheName=cacheName;
        this.condition=condition;
        this.key=key;
        this.keyPrefix=keyPrefix;
        this.expire=expire;
        this.operation=operation;
    }
    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }
}
