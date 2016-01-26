package com.minotaur.cache.support.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.minotaur.cache.support.Cache;
import com.minotaur.cache.support.CacheManager;

/**
 * default cache manager
 * Created by minotaur on 15/11/19.
 */
public class DefaultCacheManager implements CacheManager, InitializingBean {

    private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>();

    private Collection<? extends Cache>        caches;

    @Override
    public Cache getCache(String name) {
        return cacheMap.get(name);
    }

    @Override
    public Collection<String> getCacheNames() {
        return Collections.unmodifiableCollection(cacheMap.keySet());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notEmpty(caches, "caches is required, cannot be empty");
        this.cacheMap.clear();

        for (Cache cache : caches) {
            addCache(cache.getCacheName(), cache);
        }
    }

    private void addCache(final String name, final Cache cache) {
        this.cacheMap.put(name, cache);
    }

    public Collection<? extends Cache> getCaches() {
        return caches;
    }

    public void setCaches(Collection<? extends Cache> caches) {
        this.caches = caches;

    }
}
