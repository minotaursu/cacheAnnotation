package com.minotaur.cache.support.impl;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.alibaba.fastjson.JSON;
import com.minotaur.cache.support.Cache;
import com.minotaur.cache.support.LogUtils;

/**
 * redis cache
 * Created by minotaur on 15/11/19.
 */
public class RedisCache implements Cache {

    private static final Log warnLog = LogUtils.WARN_LOGGER;

    private String           name;

    @Autowired
    private JedisPool        jedisPool;

    @Override
    public String getCacheName() {
        return name;
    }

    @Override
    public void put(String key, Object value, Integer expire) {
        try {
            String valueStr = JSON.toJSONString(value);
            Jedis jedis=jedisPool.getResource();
            try {
                jedis.setex(key, expire, valueStr);
            }finally {
                jedisPool.returnResource(jedis);
            }
        } catch (Exception e) {
            warnLog.warn("cache put fail, key: " + key + ",value: " + value + ",expire: " + expire, e);
        }
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        try {
            Jedis jedis=jedisPool.getResource();
            String value=null;
            try {
                value=jedis.get(key);
            }finally {
                jedisPool.returnResource(jedis);
            }
            return JSON.parseObject(value, clazz);
        } catch (Exception e) {
            warnLog.warn("cache get fail,key: " + key + ",class: " + clazz, e);
            return null;
        }
    }

    @Override
    public int del(String key) {
        try {
            Jedis jedis=jedisPool.getResource();
            try {
                jedis.del(key);
            }finally {
                jedisPool.returnResource(jedis);
            }
        } catch (Exception e) {
            warnLog.warn("cache put fail, key: " + key, e);
            return 0;
        }
        return 1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
