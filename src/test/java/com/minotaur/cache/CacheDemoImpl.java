package com.minotaur.cache;


import com.minotaur.cache.annotation.CacheEvict;
import com.minotaur.cache.annotation.Cacheable;
import com.minotaur.cache.support.LogUtils;
import org.apache.commons.logging.Log;
import org.joda.time.format.DateTimeFormat;

/**
 * Created by minotaur on 15/11/17.
 */
public class CacheDemoImpl implements CacheDemo {

    private static final Log logger            = LogUtils.CACHE_LOGGER;

    private User user1;

    @Override
    public User intUser() {
        this.user1 = new User();
        user1.age = 21;
        user1.name = "lilei";
        user1.birthDay = DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime("1994-09-23").toDate();
        return user1;
    }

    @Override
    public User changeUser() {
        this.user1 = new User();
        user1.age = 19;
        user1.name = "hanmeimei";
        user1.birthDay = DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime("1996-11-11").toDate();
        return user1;
    }

    @Override
    public User changeUserAge(int age) {
        user1.age=age;
        return user1;
    }

    @Override
    @Cacheable(value = "redisCache", key = "#user.name+#user.age+#user.birthDay.getTime()", keyPrefix = TestConstants.method, expire = 10)
    public User getUser(User user) {
        return user1;
    }

    @Override
    @Cacheable(value = "redisCache", key = "#name", keyPrefix = TestConstants.method,expire = 10)
    public User getUser(String name) {
        return user1;
    }

    @Override
    @Cacheable(value = "redisCache", key = "#name+#age")
    public User getUser(String name, int age) {
        return user1;
    }

    @Override
    @Cacheable(value = "redisCache", key = "#name", keyPrefix = TestConstants.method,expire = 10)
    public User getUserThrowEx(String name) {
        throw new RuntimeException("执行异常");
    }

    @Override
    @Cacheable(value = "redisCache",expire = 10)
    public User getUserDefaultKey(User user) {
        return user1;
    }

    @Override
    @Cacheable(value = "redisCache", key = "#name",expire = 10)
    public User getUserNull(String name) {
        return null;
    }

    @Override
    @CacheEvict(value = "redisCache", key = "#name", keyPrefix = TestConstants.method)
    public User clearUser(String name) {
        return user1;
    }

    @Override
    @Cacheable(value = "redisCache",expire = 10)
    public User getUser() {
        return user1;
    }

    @Override
    @Cacheable(value = "mockCache",expire = 10)
    public User getUserMock(int num) {
        return intUser();
    }

    @Override
    public User getUserWithoutAnno(int num) {
        logger.info("cache "+num);
        logger.info("put "+num);
        return intUser();

    }

    @Override
    @Cacheable(value = "ehCache", key = "#name", keyPrefix = TestConstants.method,expire = 7200)
    public User getUserInMem(String name) {
        return intUser();
    }

}
