package com.minotaur.cache.support.impl;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.minotaur.cache.support.Cache;
import com.minotaur.cache.support.LogUtils;

/**
 * ehcache cache
 * Created by minotaur on 15/11/19.
 */
public class EHCache implements Cache, InitializingBean, DisposableBean {

    private static final Log warnLog = LogUtils.WARN_LOGGER;

    private String           name;

    private Ehcache          cache;

    private CacheManager     cacheManager;

    @Override
    public String getCacheName() {
        return name;
    }

    @Override
    public void put(String key, Object value, Integer expire) {
        try {
            cache.put(new Element(key, value, expire, expire));
        } catch (Exception e) {
            warnLog.warn("cache put fail, key: " + key + ",value: " + value + ",expire: " + expire, e);
        }
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        try {
            Element element = cache.get(key);
            return (element != null ? (T) element.getObjectValue() : null);
        } catch (Exception e) {
            warnLog.warn("cache get fail,key: " + key + ",class: " + clazz, e);
            return null;
        }
    }

    @Override
    public int del(String key) {
        try {
            return cache.remove(key) ? 1 : 0;
        } catch (Exception e) {
            warnLog.warn("cache put fail, key: " + key, e);
            return 0;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        int maxEntriesLocalHeap = 1000;
        this.cacheManager = new CacheManager();
        net.sf.ehcache.Cache cache = new net.sf.ehcache.Cache(new CacheConfiguration(name, maxEntriesLocalHeap)
            .memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LFU).eternal(false).overflowToOffHeap(false)
            .timeToLiveSeconds(3600).timeToIdleSeconds(3600));
        cacheManager.addCache(cache);
        this.cache = cache;
    }

    @Override
    public void destroy() throws Exception {
        warnLog.warn("Shutting down EHCache CacheManager");
        this.cacheManager.shutdown();
    }
}
