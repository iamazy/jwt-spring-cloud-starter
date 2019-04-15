package com.iamazy.springcloud.api.security.jwt;

import com.iamazy.springcloud.api.security.configuration.JwtConfigurationProperties;
import com.iamazy.springcloud.api.security.configuration.condition.JwtEnabledCondition;
import com.iamazy.springcloud.api.security.model.JwtUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.ErrorCodes;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author iamazy
 * @date 2019/3/11
 * @descrition
 **/
@Slf4j
@Component
@Conditional(JwtEnabledCondition.class)
public class JwtManager {

    private RsaJsonWebKey rsaJsonWebKey;

    public JwtManager() throws JoseException {
        rsaJsonWebKey=RsaJwkGenerator.generateJwk(2048);
        rsaJsonWebKey.setKeyId(RandomStringUtils.randomAlphabetic(5));
    }


    @Resource
    private JwtConfigurationProperties jwtConfigurationProperties;

    public String createJwt(JwtUser user) throws JoseException {

        JwtClaims claims=new JwtClaims();
        claims.setIssuer(jwtConfigurationProperties.getIssuer());
        claims.setAudience(jwtConfigurationProperties.getAudience());
        claims.setExpirationTimeMinutesInTheFuture(jwtConfigurationProperties.getExpirationTimeMinutesInTheFuture());
        claims.setGeneratedJwtId();
        if(MapUtils.isNotEmpty(user.getProperties())) {
            for (Map.Entry<String, Object> entry : user.getProperties().entrySet()) {
                claims.setClaim(entry.getKey(), entry.getValue());
            }
        }
        claims.setStringClaim("uid",user.getUid());
        claims.setClaim("credential",user.getCredential());
        /*
         * token发布的时间
         */
        claims.setIssuedAtToNow();
        claims.setNotBeforeMinutesInThePast(jwtConfigurationProperties.getNotBeforeMinutesInThePast());
        claims.setSubject(jwtConfigurationProperties.getSubject());

        JsonWebSignature jws=new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKey(rsaJsonWebKey.getPrivateKey());
        jws.setKeyIdHeaderValue(rsaJsonWebKey.getKeyId());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
        return jws.getCompactSerialization();
    }


    public JwtClaims decodeJwt(String token) throws MalformedClaimException {
        JwtConsumer jwtConsumer=new JwtConsumerBuilder()
                .setRequireExpirationTime()
                .setAllowedClockSkewInSeconds(30)
                .setRequireSubject()
                .setRequireIssuedAt()
                .setRequireJwtId()
                .setRequireNotBefore()
                .setExpectedAudience(jwtConfigurationProperties.getAudience().toArray(new String[0]))
                .setExpectedSubject(jwtConfigurationProperties.getSubject())
                .setExpectedIssuer(jwtConfigurationProperties.getIssuer())
                .setVerificationKey(rsaJsonWebKey.getKey())
                .setJwsAlgorithmConstraints(new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.WHITELIST,
                        AlgorithmIdentifiers.RSA_USING_SHA256))
                .build();
        try {
            return jwtConsumer.processToClaims(token);
        }catch (InvalidJwtException e){
            if(e.hasExpired()){
                log.error("jwt失效时间:"+e.getJwtContext().getJwtClaims().getExpirationTime());
            }

            if(e.hasErrorCode(ErrorCodes.AUDIENCE_INVALID)){
                log.error("jwt的audience无效,audience:"+e.getJwtContext().getJwtClaims().getAudience());
            }
            return null;
        }
    }

}
