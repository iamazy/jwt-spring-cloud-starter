package com.iamazy.springcloud.api.security.cache;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author iamazy
 * @date 2018/12/6
 **/
public interface ICacheKeyGenerator {

    /**
     * 获取AOP参数，生成指定缓存
     * @param joinPoint
     * @return
     */
    String getLockKey(ProceedingJoinPoint joinPoint);
}
