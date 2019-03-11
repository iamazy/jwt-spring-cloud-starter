package com.wsn.springcloud.api.security.configuration.condition;

import com.wsn.springcloud.api.security.configuration.JwtConfigurationProperties;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author iamazy
 * @date 2019/3/11
 * @descrition
 **/
public class JwtEnabledCondition extends SpringBootCondition {

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        JwtConfigurationProperties jwtConfigurationProperties=getJwtProperties(context);
        if(!jwtConfigurationProperties.getEnabled()){
            return ConditionOutcome.noMatch("Jwt功能被禁用,因为jwt.enabled=false");
        }
        return ConditionOutcome.match();
    }

    private JwtConfigurationProperties getJwtProperties(ConditionContext context){
        JwtConfigurationProperties auditProperties=new JwtConfigurationProperties(context.getEnvironment());
        Binder.get(context.getEnvironment()).bind("jwt", Bindable.ofInstance(auditProperties));
        return auditProperties;
    }
}
