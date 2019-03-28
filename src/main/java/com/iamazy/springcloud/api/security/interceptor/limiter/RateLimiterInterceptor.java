package com.iamazy.springcloud.api.security.interceptor.limiter;

import com.google.common.collect.ImmutableList;
import com.iamazy.springcloud.api.security.annotation.RateLimit;
import com.iamazy.springcloud.api.security.security.LoginUserInfoHolder;
import com.iamazy.springcloud.api.security.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @author iamazy
 * @date 2018/12/6
 **/
@Slf4j
@Aspect
@Component
public class RateLimiterInterceptor {

    private final RedisTemplate<String, Serializable> redisLimiterTemplate;


    @Autowired
    public RateLimiterInterceptor(RedisTemplate<String, Serializable> redisLimiterTemplate) {
        this.redisLimiterTemplate = redisLimiterTemplate;
    }

    @Around("@annotation(com.iamazy.springcloud.api.security.annotation.RateLimit)|| @within(com.iamazy.springcloud.api.security.annotation.RateLimit)")
    public Object interceptor(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Class<?> declaringClass = method.getDeclaringClass();
        RateLimit limitClassAnnotation = declaringClass.getAnnotation(RateLimit.class);
        RateLimit limitAnnotation = method.getAnnotation(RateLimit.class);

        if (limitAnnotation == null) {
            limitAnnotation = limitClassAnnotation;
        }

        String username = LoginUserInfoHolder.getLoginUserInfo().getUsername();
        String key = limitAnnotation.key();
        int limitPeriod = limitAnnotation.period();
        int limitCount = limitAnnotation.count();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = SpringUtils.getRemoteIp(request);
        ImmutableList<String> keys = ImmutableList.of(StringUtils.join(ip, username, key));
        try {
            String luaScript = buildLuaRateLimitScript();
            RedisScript<Number> redisScript = new DefaultRedisScript<>(luaScript, Number.class);
            Number count = redisLimiterTemplate.execute(redisScript, keys, limitCount, limitPeriod);
            if (count != null && count.intValue() <= limitCount) {
                log.info("ip为{}的用户{}的尝试访问次数是{}", ip, username, count);
                return joinPoint.proceed();
            } else {
                throw new RuntimeException("用户:[" + ip + "]" + username + "已经被拉进黑名单了!!!");
            }
        } catch (Throwable e) {
            if (e instanceof RuntimeException) {
                throw new RuntimeException("用户:[" + ip + "]" + username + "暂时无法访问该接口!!!");
            }
            throw new RuntimeException("系统内部发生错误!!!");
        }

    }

    private String buildLuaRateLimitScript() {
        // 调用不超过最大值，则直接返回
        // 执行计算器自加
        // 从第一次调用开始限流，设置对应键值的过期
        return "local c" +
                "\nc = redis.call('get',KEYS[1])" +
                "\nif c and tonumber(c) > tonumber(ARGV[1]) then" +
                "\nreturn c;" +
                "\nend" +
                "\nc = redis.call('incr',KEYS[1])" +
                "\nif tonumber(c) == 1 then" +
                "\nredis.call('expire',KEYS[1],ARGV[2])" +
                "\nend" +
                "\nreturn c;";
    }
}
