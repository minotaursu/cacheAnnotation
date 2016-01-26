package com.minotaur.cache;

import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by minotaur on 15/11/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath*:test-cache-application.xml" })
public class CacheTest {

    @Autowired
    private CacheDemo  cacheDemo;

    private static int i = 0;

    @Before
    public void testBefore() {
        System.out.println("==============test" + i++ + "====================");
    }

    /**
     * 用来测试非对象参数
     * @return
     */
    @Test
    public void test0() {
        cacheDemo.clearUser("lilei");
        cacheDemo.intUser();
        User u1 = cacheDemo.getUser("lilei");
        assertTrue("lilei".equals(u1.getName()));
        User h1 = cacheDemo.changeUser();
        assertTrue("hanmeimei".equals(h1.getName()));
        User u2 = cacheDemo.getUser("lilei");
        assertTrue("lilei".equals(u2.getName()));
        cacheDemo.clearUser("lilei");
        User u3 = cacheDemo.getUser("lilei");
        assertTrue("hanmeimei".equals(u3.getName()));

    }

    /**
     * 用来测试默认keyPrefix
     * @return
     */
    @Test
    public void test1() {
        cacheDemo.intUser();
        User u1 = cacheDemo.getUser("lilei", 21);
        assertTrue("lilei".equals(u1.getName()));
        cacheDemo.changeUser();
        User u2 = cacheDemo.getUser("lilei", 21);
        assertTrue("lilei".equals(u2.getName()));
    }

    /**
     * 用来测试对象属性
     * @return
     */
    @Test
    public void test2() {
        User user1 = new User();
        user1.age = 21;
        user1.name = "lilei";
        user1.birthDay = new Date();
        cacheDemo.intUser();
        User u1 = cacheDemo.getUser(user1);
        assertTrue("lilei".equals(u1.getName()));
        cacheDemo.changeUser();
        User u2 = cacheDemo.getUser(user1);
        assertTrue("lilei".equals(u2.getName()));
    }

    /**
     * 用来测试默认key
     * @return
     */
    @Test
    public void test3() {
        User user1 = new User();
        user1.age = 21;
        user1.name = "lilei";
        user1.birthDay = new Date();
        cacheDemo.intUser();
        User u1 = cacheDemo.getUserDefaultKey(user1);
        assertTrue("lilei".equals(u1.getName()));
        cacheDemo.changeUser();
        User u2 = cacheDemo.getUserDefaultKey(user1);
        assertTrue("lilei".equals(u2.getName()));
    }

    /**
     * 用来测试异常
     * 会在aop拦截输出日志,继续先上抛出异常
     * @return
     */
    @Test
    public void test4() {
        Exception ex = null;
        try {
            cacheDemo.intUser();
            cacheDemo.getUserThrowEx("tom");
        } catch (Exception e) {
            System.out.println(e);
            ex = e;
        }
        assertTrue(ex != null);
    }

    /**
     * 用来测试返回null
     * 正常执行,不会缓存
     */
    @Test
    public void test5() {
        cacheDemo.intUser();
        User u1 = cacheDemo.getUserNull("lucy");
        assertTrue(u1 == null);
    }

    /**
     * 用来测试key为对象属性时,对象是null
     * 解析key时抛出异常,执行原程序
     * @return
     */
    @Test
    public void test6() {
        User user = null;
        cacheDemo.intUser();
        User u1 = cacheDemo.getUser(user);
        assertTrue("lilei".equals(u1.getName()));
    }

    /**
     * 用来测试key为null
     * 正常执行,不会缓存
     * @return
     */
    @Test
    public void test7() {
        String name = null;
        cacheDemo.intUser();
        User u1 = cacheDemo.getUser(name);
        assertTrue("lilei".equals(u1.getName()));
    }

    /**
     * 用来测试key为null
     * @return
     */
    @Test
    public void test8() {
        User user = null;
        cacheDemo.intUser();
        User u1 = cacheDemo.getUserDefaultKey(user);
        assertTrue("lilei".equals(u1.getName()));
    }

    /**
     * 用来测试无参函数
     */
    @Test
    public void test9() {
        cacheDemo.intUser();
        User u1 = cacheDemo.getUser();
        assertTrue("lilei".equals(u1.getName()));
    }

    /**
     * 混合执行测试
     */
    @Test
    public void test10() {
        User user1 = new User();
        user1.age = 21;
        user1.name = "lilei";
        user1.birthDay = new Date();
        cacheDemo.intUser();
        cacheDemo.getUser("lilei");
        cacheDemo.getUser("lilei", 21);
        cacheDemo.getUser(user1);
        User u1 = cacheDemo.getUserDefaultKey(user1);
        assertTrue("lilei".equals(u1.getName()));
    }

    /**
     * ehcache测试
     */
    @Test
    public void test11() {
        cacheDemo.intUser();
        for (int i = 1; i < 1100; i++) {
            cacheDemo.getUserInMem("lilei"+i);
            cacheDemo.changeUserAge(i);
        }
        User u1 = cacheDemo.getUserInMem("lilei300");
        assertTrue(300==u1.getAge());
    }
}
