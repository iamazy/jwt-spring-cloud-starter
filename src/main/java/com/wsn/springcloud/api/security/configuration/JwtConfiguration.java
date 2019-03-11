package com.wsn.springcloud.api.security.configuration;

import com.wsn.springcloud.api.security.configuration.condition.JwtEnabledCondition;
import com.wsn.springcloud.api.security.interceptor.JwtAuthenticationInterceptor;
import com.wsn.springcloud.api.security.jwt.JwtManager;
import com.wsn.springcloud.api.security.jwt.JwtUserMapper;
import org.jose4j.lang.JoseException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author iamazy
 * @date 2019/3/11
 * @descrition
 **/


@Configuration
@Conditional(JwtEnabledCondition.class)
@EnableConfigurationProperties(JwtConfigurationProperties.class)
public class JwtConfiguration implements WebMvcConfigurer {


    @Bean
    public JwtAuthenticationInterceptor jwtAuthenticationInterceptor(){
        return new JwtAuthenticationInterceptor();
    }

    @Bean
    public JwtManager jwtManager() throws JoseException {
        return new JwtManager();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthenticationInterceptor())
                .addPathPatterns("/**");
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtUserMapper defaultJwtUserMapper(){
        return new JwtUserMapper() {};
    }
}
