package com.iamazy.springcloud.api.security.configuration.limiter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;

/**
 * @author iamazy
 * @date 2018/12/6
 **/
@Configuration
public class RedisRateLimiterConfiguration {


    @Bean
    public RedisTemplate<String, Serializable> limitRedisTemplate(LettuceConnectionFactory connectionFactory){
        RedisTemplate<String,Serializable> template=new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(connectionFactory);
        return template;
    }
}
