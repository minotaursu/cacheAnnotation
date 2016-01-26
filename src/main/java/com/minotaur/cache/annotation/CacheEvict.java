package com.minotaur.cache.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * a cache invalidate operation.
 * evict cache after method execution
 * Created by minotaur on 15/11/19.
 */
@Target(METHOD)
@Retention(RUNTIME)
@Inherited
@Documented
public @interface CacheEvict {

    /**
     * the name of cache
     * @return
     */
    String value();

    /**
     * Spring Expression Language (SpEL) attribute for computing the key dynamically.
     * <p>Default is "", meaning all method parameters are considered as a key.
     */
    String key() default "";

    /**
     * key prefix. if keyPrefix not exists ,will default use methodName
     * @return
     */
    String keyPrefix() default "";

    /**
     * Spring Expression Language (SpEL) attribute used for conditioning the method caching.
     * <p>Default is "", meaning the method is always cached.
     */
    String condition() default "";

}
