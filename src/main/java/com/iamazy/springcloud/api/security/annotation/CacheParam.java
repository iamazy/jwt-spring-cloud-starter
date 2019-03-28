package com.iamazy.springcloud.api.security.annotation;

import java.lang.annotation.*;

/**
 * @author iamazy
 * @date 2018/12/6
 **/
@Target({ElementType.PARAMETER,ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CacheParam {

    /**
     * 字段名称
     * @return
     */
    String name() default "";
}
