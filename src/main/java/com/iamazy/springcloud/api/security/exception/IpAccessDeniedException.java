package com.iamazy.springcloud.api.security.exception;

/**
 * @author iamazy
 * @date 2018/12/19
 * @descrition
 **/
public class IpAccessDeniedException extends RuntimeException {

    public IpAccessDeniedException(String msg, Throwable t) {
        super(msg, t);
    }

    public IpAccessDeniedException(String msg) {
        super(msg);
    }
}
