package com.iamazy.springcloud.api.security.configuration;

import com.iamazy.springcloud.api.security.configuration.condition.JwtEnabledCondition;
import com.iamazy.springcloud.api.security.interceptor.JwtAuthenticationInterceptor;
import com.iamazy.springcloud.api.security.jwt.JwtUserMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author iamazy
 * @date 2019/3/11
 * @descrition
 **/


@Configuration
@Conditional(JwtEnabledCondition.class)
@EnableConfigurationProperties(JwtConfigurationProperties.class)
public class JwtConfiguration implements WebMvcConfigurer {


    @Resource
    public JwtAuthenticationInterceptor jwtAuthenticationInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthenticationInterceptor)
                .addPathPatterns("/**");
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtUserMapper defaultJwtUserMapper(){
        return new JwtUserMapper() {};
    }
}
