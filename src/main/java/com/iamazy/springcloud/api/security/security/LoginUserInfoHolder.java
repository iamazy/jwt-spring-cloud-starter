package com.iamazy.springcloud.api.security.security;

import com.iamazy.springcloud.api.security.security.userdetails.AnonymousUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author iamazy
 * @date 2018/12/6
 * @description 获取系统当前登陆的用户信息
 **/
@Slf4j
public class LoginUserInfoHolder {

    public static UserDetails getLoginUserInfo(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null){
            return new AnonymousUser();
        }
        Object principal=authentication.getPrincipal();
        if(principal instanceof UserDetails){
            return (UserDetails) principal;
        }
        return new AnonymousUser();
    }
}
