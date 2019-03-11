jwt-spring-cloud-starter
==========

这是一个可插拔式的jwt组件，为目标接口提供鉴权功能

1.使用的jwt库
---
```xml
<dependency>
    <groupId>org.bitbucket.b_c</groupId>
    <artifactId>jose4j</artifactId>
    <version>0.6.5</version>
</dependency>
```
2.对外暴露接口
----
```java
public interface JwtUserMapper {

    default JwtUser find(Map<String,Object> properties){
        return null;
    }
}
```
接口没有默认实现，用户将本插件集成进项目中后需要实现`JwtUserMapper`接口的`find`方法,并将其声明为一个JavaBean<br/>
例如：
```java
@Service
public class DefaultJwtUserMapper implements JwtUserMapper {

    private List<JwtUser> defaultUsers=new ArrayList<JwtUser>(){
        {
            add(new JwtUser("1","tom",null));
        }
    };

    @Override
    public JwtUser find(Map<String,Object> properties) {
        if(properties.containsKey("uid")) {
            String uid=properties.get("uid").toString();
            Optional<JwtUser> any = defaultUsers.stream().filter(user -> user.getUid().equals(uid)).findAny();
            return any.orElse(null);
        }
        return null;
    }
}
```
3.Jwt默认Header名称(jwt-token)
--------
4.注解方式添加接口保护
--------
##### JwtToken(可作用于类和方法)
添加后接口访问需要提供token
##### TokenPassed(作用于方法)
添加后不需要提供token就可以访问，优先级最高

5.提供enable开关
-------
在`application.yml`文件中指定enable=true才会开启jwt鉴权功能，enable默认为false
