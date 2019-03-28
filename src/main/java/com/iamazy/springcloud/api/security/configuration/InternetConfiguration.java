package com.iamazy.springcloud.api.security.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author iamazy
 * @date 2018/12/19
 * @descrition
 **/
@Slf4j
@Configuration
@EnableConfigurationProperties(InternetConfigurationProperties.class)
public class InternetConfiguration {
}
