package com.iamazy.springcloud.api.security.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author iamazy
 * @date 2018/12/6
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CacheLock {

    /**
     * redis 锁key的前缀
     * @return
     */
    String prefix() default "";

    /**
     * 过期秒数，默认为5秒
     * @return
     */
    int expire() default 5;

    /**
     * 超时时间单位
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * Key的分隔符,默认为:
     * @return
     */
    String delimiter() default ":";
}
