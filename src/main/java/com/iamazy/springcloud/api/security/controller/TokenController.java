package com.iamazy.springcloud.api.security.controller;

import com.iamazy.springcloud.api.security.configuration.condition.JwtEnabledCondition;
import com.iamazy.springcloud.api.security.jwt.JwtManager;
import com.iamazy.springcloud.api.security.model.JwtUser;
import org.jose4j.lang.JoseException;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author iamazy
 * @date 2019/3/19
 * @descrition
 **/
@RequestMapping("/jwt")
@RestController
@Conditional(JwtEnabledCondition.class)
public class TokenController {


    @Resource
    private JwtManager jwtManager;

    @PostMapping("/create")
    public ResponseEntity createJwt(@RequestBody Map<String,Object> params) throws JoseException {
        if(!params.containsKey("uid")||!params.containsKey("credential")){
            return new ResponseEntity<>("用户验证信息不完整!!!", HttpStatus.FORBIDDEN);
        }
        String uid=params.get("uid").toString();
        String credential=params.get("credential").toString();
        String token=jwtManager.createJwt(new JwtUser(uid,credential,null));
        return new ResponseEntity<>(token,HttpStatus.OK);
    }
}
