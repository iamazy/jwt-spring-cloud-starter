package com.iamazy.springcloud.api.security.interceptor;


import com.iamazy.springcloud.api.security.cons.JwtConstants;
import com.iamazy.springcloud.api.security.jwt.JwtManager;
import com.iamazy.springcloud.api.security.annotation.JwtToken;
import com.iamazy.springcloud.api.security.annotation.TokenPassed;
import com.iamazy.springcloud.api.security.exception.JwtException;
import com.iamazy.springcloud.api.security.jwt.JwtUserMapper;
import com.iamazy.springcloud.api.security.model.JwtUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author iamazy
 * @date 2019/3/11
 * @descrition
 **/

@Slf4j
@Component
public class JwtAuthenticationInterceptor implements HandlerInterceptor {

    @Resource
    private JwtManager jwtManager;

    @Resource
    private JwtUserMapper jwtUserMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod=(HandlerMethod)handler;

        Method method = handlerMethod.getMethod();
        String token=request.getHeader(JwtConstants.TOKEN);
        if(method.isAnnotationPresent(TokenPassed.class)){
            TokenPassed passed=method.getAnnotation(TokenPassed.class);
            if(passed.required()){
                return true;
            }
        }
        Class<?> beanType = handlerMethod.getBeanType();
        if(beanType.isAnnotationPresent(JwtToken.class)){
            JwtToken jwtTokenAnnotation=beanType.getAnnotation(JwtToken.class);
            validateToken(jwtTokenAnnotation,token);
        }
        if(method.isAnnotationPresent(JwtToken.class)) {
            JwtToken tokenAnnotation = method.getAnnotation(JwtToken.class);
            validateToken(tokenAnnotation,token);
        }
        return true;
    }


    private void validateToken(JwtToken jwtToken,String token){
        if (jwtToken.required()) {
            if (StringUtils.isNotBlank(token)) {
                throw new JwtException("必须携带token信息，请重新发起请求!!!");
            }
            try {
                JwtClaims claims = jwtManager.decodeJwt(token);
                if(claims==null){
                    throw new JwtException("解析token为无效状态!!!");
                }
                Map<String, Object> claimsMap = claims.getClaimsMap();
                JwtUser jwtUser = jwtUserMapper.find(claimsMap);
                if (jwtUser == null) {
                    throw new JwtException("不存在该用户或token携带验证信息不匹配!!!");
                }
            } catch (MalformedClaimException e) {
                throw new JwtException("解析token错误!!!");
            } catch (Throwable throwable) {
                throw new JwtException(throwable);
            }
        }
    }
}
