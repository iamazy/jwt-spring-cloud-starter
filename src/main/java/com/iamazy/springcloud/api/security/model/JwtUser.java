package com.iamazy.springcloud.api.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;


/**
 * @author iamazy
 * @date 2019/3/11
 * @descrition
 **/
@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtUser  {

    String uid;

    Object credential;

    Map<String,Object> properties;

}
