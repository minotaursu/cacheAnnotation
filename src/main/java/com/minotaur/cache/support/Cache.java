package com.minotaur.cache.support;

/**
 * cache
 * Created by minotaur on 15/11/19.
 */
public interface Cache {

    /**
     * return the cache name
     * @return
     */
    String getCacheName();

    /**
     * put the value with key into cache
     * @param key
     * @param value
     * @param expire
     */
    void put(String key, Object value, Integer expire);

    /**
     * get value with the key in the cache
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T get(String key, Class<T> clazz);
    /**
     * delete value with the key
     * @param key
     * @return
     */
    int del(String key);

}
