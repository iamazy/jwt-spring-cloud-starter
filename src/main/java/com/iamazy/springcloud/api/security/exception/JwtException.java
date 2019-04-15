package com.iamazy.springcloud.api.security.exception;

/**
 * @author iamazy
 * @date 2019/3/11
 * @descrition
 **/
public class JwtException extends RuntimeException {

    public JwtException(String msg){super(msg);}

    public JwtException(Throwable cause){super(cause);}
}
