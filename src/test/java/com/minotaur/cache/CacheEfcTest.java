package com.minotaur.cache;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by minotaur on 15/11/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath*:/spring/test-cache-application.xml" })
public class CacheEfcTest {

    @Autowired
    private CacheDemo cacheDemo;

    @Test
    public void testMillionsExeWithAnnotation(){
        Stopwatch stopwatch=Stopwatch.createStarted();
        int size=1000*1000;
        for(int i=0;i<size;i++) {
            cacheDemo.getUserMock(i);
        }
        stopwatch.stop();
        System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    @Test
    public void testMillionsExe(){
        Stopwatch stopwatch=Stopwatch.createStarted();
        stopwatch.start();
        int size=1000*1000;
        for(int i=0;i<size;i++) {
            cacheDemo.getUserWithoutAnno(i);
        }
        stopwatch.stop();
        System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }
}
