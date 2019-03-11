package com.wsn.springcloud.api.security.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author iamazy
 * @date 2019/3/11
 * @descrition
 **/
@Data
@ConfigurationProperties(prefix = "jwt",ignoreUnknownFields = false)
public class JwtConfigurationProperties {

    private Boolean enabled=false;
    private final Environment environment;

    /**
     * jwt发布者
     */
    String issuer="issuer";
    /**
     * jwt过期时间
     */
    float expirationTimeMinutesInTheFuture=30f;
    /**
     * jwt尚未生效的时间
     */
    float notBeforeMinutesInThePast=2f;
    /**
     * jwt主题
     */
    String subject="subject";
    /**
     * jwt受众[list]
     */
    List<String> audience= Collections.singletonList("audience");

    public JwtConfigurationProperties(Environment environment){
        Assert.notNull(environment, "Environment must not be null");
        this.environment = environment;
    }
}
