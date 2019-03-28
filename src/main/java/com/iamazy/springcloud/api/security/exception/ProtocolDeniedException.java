package com.iamazy.springcloud.api.security.exception;

import java.net.ProtocolException;

/**
 * @author iamazy
 * @date 2018/12/19
 * @descrition
 **/
public class ProtocolDeniedException extends ProtocolException {

    public ProtocolDeniedException() {
        super();
    }

    public ProtocolDeniedException(String msg) {
        super(msg);
    }
}
