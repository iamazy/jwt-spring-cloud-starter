package com.wsn.springcloud.api.security.jwt;


import com.wsn.springcloud.api.security.model.JwtUser;

import java.util.Map;

/**
 * @author iamazy
 * @date 2019/3/11
 * @descrition
 **/
public interface JwtUserMapper {

    default JwtUser find(Map<String,Object> properties){
        return null;
    }
}
