package com.iamazy.springcloud.api.security.interceptor.lock;


import com.iamazy.springcloud.api.security.annotation.CacheLock;
import com.iamazy.springcloud.api.security.cache.ICacheKeyGenerator;
import com.iamazy.springcloud.api.security.security.LoginUserInfoHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author iamazy
 * @date 2018/12/6
 **/
@Slf4j
@Aspect
@Component
public class LockMethodInterceptor {

    private final StringRedisTemplate lockRedisTemplate;
    private final ICacheKeyGenerator cacheKeyGenerator;

    @Autowired
    public LockMethodInterceptor(StringRedisTemplate lockRedisTemplate, ICacheKeyGenerator cacheKeyGenerator){
        this.lockRedisTemplate=lockRedisTemplate;
        this.cacheKeyGenerator=cacheKeyGenerator;
    }

    @Around("@annotation(com.iamazy.springcloud.api.security.annotation.CacheLock)||@within(com.iamazy.springcloud.api.security.annotation.CacheLock)")
    public Object interceptor(ProceedingJoinPoint joinPoint){
        MethodSignature signature=(MethodSignature)joinPoint.getSignature();
        Method method=signature.getMethod();
        Class<?> declaringClass = method.getDeclaringClass();
        CacheLock lockClass=declaringClass.getAnnotation(CacheLock.class);
        CacheLock lock=method.getAnnotation(CacheLock.class);

        if(lock==null){
            lock=lockClass;
        }

        if(StringUtils.isEmpty(lock.prefix())){
            throw new RuntimeException("锁的key不能为空!!!");
        }
        final String lockKey=cacheKeyGenerator.getLockKey(joinPoint);
        try {
            final Boolean success=lockRedisTemplate.opsForValue().setIfAbsent(lockKey,"1");
            if(!success){
                throw new RuntimeException("请勿重复请求!!!");
            }
            String username= LoginUserInfoHolder.getLoginUserInfo().getUsername();
            lockRedisTemplate.expire(username+"_"+lockKey,lock.expire(),lock.timeUnit());
            try {
                return joinPoint.proceed();
            }catch (Throwable cause){
                throw new RuntimeException("系统内部异常!!!");
            }
        }finally {
            //演示的时候应该注释下面这句话，正式环境应该打开这句话
            lockRedisTemplate.delete(lockKey);
        }
    }
}
