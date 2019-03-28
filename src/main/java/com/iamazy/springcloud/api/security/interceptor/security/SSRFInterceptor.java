package com.iamazy.springcloud.api.security.interceptor.security;

import com.iamazy.springcloud.api.security.configuration.InternetConfigurationProperties;
import com.iamazy.springcloud.api.security.cons.AllowedNetProtocol;
import com.iamazy.springcloud.api.security.exception.IpAccessDeniedException;
import com.iamazy.springcloud.api.security.exception.ProtocolDeniedException;
import com.iamazy.springcloud.api.security.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


/**
 * @author iamazy
 * @date 2018/12/19
 * @descrition 拦截SSRF攻击
 **/
@Slf4j
@Aspect
@Configuration
public class SSRFInterceptor {

    @Resource
    private InternetConfigurationProperties internetProperties;

    @Before(value = "@within(org.springframework.web.bind.annotation.RestController)||@within(org.springframework.stereotype.Controller)")
    public void beforeExecute() throws ProtocolException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        try {
            URL url=new URL(request.getRequestURL().toString());
            if(!url.getProtocol().startsWith(AllowedNetProtocol.HTTP.getName()) &&
               !url.getProtocol().startsWith(AllowedNetProtocol.HTTPS.getName())){
                log.error("Request请求所使用的协议被禁用!!!");
                throw new ProtocolDeniedException("Request请求所使用的协议被禁用!!!");
            }

            String ip= SpringUtils.getRemoteIp(request);
            if(!isWhitelistIp(ip)){
                log.error("客户端请求ip:"+ip+"被禁止访问!!!");
                throw new IpAccessDeniedException("客户端请求ip:"+ip+"被禁止访问!!!");
            }
            /*
            String host=url.getHost().toLowerCase();
            //获取顶级域名
            String rootDomain= InternetDomainName.from(host).topPrivateDomain().toString();
            if(!isWhitelistDomain(rootDomain)){
                log.error("Request请求所在的域名被禁止访问!!!");
                throw new DomainDeniedException("Request请求所在的域名被禁止访问!!!");
            }
            */
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private Boolean isWhitelistDomain(String domain){
        //若白名单为空，则默认允许所有域名访问
        if(internetProperties.getDomainWhiteList().size()==0
                &&internetProperties.getDomainBlackList().size()==0){
            return true;
        }
        if(internetProperties.getDomainWhiteList().contains(domain)){
            return !internetProperties.getDomainBlackList().contains(domain);
        }
        return false;
    }

    private Boolean isWhitelistIp(String ip){
        //若黑白名单为空，则默认允许所有ip访问
        if(internetProperties.getIpWhiteList().size()==0
                &&internetProperties.getIpBlackList().size()==0){
            return true;
        }
        if(internetProperties.getIpWhiteList().contains(ip)){
            return !internetProperties.getIpBlackList().contains(ip);
        }
        return false;
    }
}
