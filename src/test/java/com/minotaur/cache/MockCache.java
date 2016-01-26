package com.minotaur.cache;


import com.minotaur.cache.support.Cache;
import org.joda.time.format.DateTimeFormat;

/**
 * Created by minotaur on 15/11/20.
 */
public class MockCache implements Cache {

    @Override
    public String getCacheName() {
        return "mockCache";
    }

    @Override
    public void put(String key, Object value, Integer expire) {

    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        User user1 = new User();
        user1.age = 21;
        user1.name = "lilei";
        user1.birthDay = DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime("1994-09-23").toDate();
        return (T) user1;
    }

    @Override
    public int del(String key) {
        return 0;
    }
}
