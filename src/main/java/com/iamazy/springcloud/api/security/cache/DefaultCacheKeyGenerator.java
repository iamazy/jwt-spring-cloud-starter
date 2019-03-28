package com.iamazy.springcloud.api.security.cache;


import com.iamazy.springcloud.api.security.annotation.CacheLock;
import com.iamazy.springcloud.api.security.annotation.CacheParam;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author iamazy
 * @date 2018/12/6
 **/
public class DefaultCacheKeyGenerator implements ICacheKeyGenerator {


    @Override
    public String getLockKey(ProceedingJoinPoint joinPoint) {
        MethodSignature signature=(MethodSignature)joinPoint.getSignature();
        Method method=signature.getMethod();
        CacheLock lockAnnotation=method.getAnnotation(CacheLock.class);
        final Object[] args=joinPoint.getArgs();
        final Parameter[] parameters=method.getParameters();
        StringBuilder builder=new StringBuilder();
        for(int i=0;i<parameters.length;i++){
            final CacheParam annotation=parameters[i].getAnnotation(CacheParam.class);
            if(annotation==null){
                continue;
            }
            builder.append(lockAnnotation.delimiter()).append(args[i]);
        }
        if(StringUtils.isEmpty(builder.toString())){
            final Annotation[][] parameterAnnotations=method.getParameterAnnotations();
            for(int i=0;i<parameterAnnotations.length;i++){
                final Object object=args[i];
                final Field[] fields=object.getClass().getDeclaredFields();
                for(Field field:fields){
                    final CacheParam annotation=field.getAnnotation(CacheParam.class);
                    if(annotation==null){
                        continue;
                    }
                    field.setAccessible(true);
                    builder.append(lockAnnotation.delimiter()).append(ReflectionUtils.getField(field,object));
                }
            }
        }
        return lockAnnotation.prefix()+builder.toString();
    }
}
