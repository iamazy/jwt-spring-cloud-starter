package com.iamazy.springcloud.api.security.annotation;


import java.lang.annotation.*;

/**
 * @author iamazy
 * @date 2018/12/6
 **/

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RateLimit {

    /**
     * 资源的key
     * @return
     */
    String key() default "";

    /**
     * 给定的时间段
     * @return
     */
    int period() default 10;

    /**
     * 最多的访问次数
     * @return
     */
    int count() default 5;
}



