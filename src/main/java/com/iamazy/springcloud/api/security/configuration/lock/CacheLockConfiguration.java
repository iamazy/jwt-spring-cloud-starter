package com.iamazy.springcloud.api.security.configuration.lock;



import com.iamazy.springcloud.api.security.cache.DefaultCacheKeyGenerator;
import com.iamazy.springcloud.api.security.cache.ICacheKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author iamazy
 * @date 2018/12/6
 **/
@Configuration
public class CacheLockConfiguration {

    @Bean
    public ICacheKeyGenerator cacheKeyGenerator(){
        return new DefaultCacheKeyGenerator();
    }
}
