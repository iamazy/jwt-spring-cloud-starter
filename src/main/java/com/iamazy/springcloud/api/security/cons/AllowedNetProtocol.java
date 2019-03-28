package com.iamazy.springcloud.api.security.cons;

/**
 * @author iamazy
 * @date 2018/12/19
 * @descrition
 **/
public enum AllowedNetProtocol {

    HTTP("http"),
    HTTPS("https")
   ;
    String name;

    public String getName(){
        return this.name;
    }

    AllowedNetProtocol(String name){
        this.name=name;
    }

}
