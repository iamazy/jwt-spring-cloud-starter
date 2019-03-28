package com.iamazy.springcloud.api.security.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author iamazy
 * @date 2018/12/19
 * @descrition
 **/
@Data
@ConfigurationProperties(prefix = "net",ignoreUnknownFields = false)
public class InternetConfigurationProperties {

    /**
     * 访问ip的白名单
     */
    private List<String> ipWhiteList=new ArrayList<>(0);

    /**
     * 访问ip的黑名单，级别比白名单高
     */
    private List<String> ipBlackList=new ArrayList<>(0);

    /**
     * 域名的白名单
     */
    private List<String> domainWhiteList=new ArrayList<>(0);

    /**
     * 域名的黑名单
     */
    private List<String> domainBlackList=new ArrayList<>(0);

}
