package com.wsn.springcloud.api.security.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author iamazy
 * @date 2019/3/11
 * @descrition
 **/
@Slf4j
@RestControllerAdvice
public class GlobalJwtExceptionHandler {


    @ExceptionHandler(JwtException.class)
    public ResponseEntity handleJwtException(JwtException e){
        log.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }
}
