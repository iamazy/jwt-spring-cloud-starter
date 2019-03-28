package com.iamazy.springcloud.api.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author iamazy
 * @date 2018/12/6
 **/
public class UserNotLoginException extends AuthenticationException {

    public UserNotLoginException(String msg, Throwable t) {
        super(msg, t);
    }

    public UserNotLoginException(String msg) {
        super(msg);
    }
}
