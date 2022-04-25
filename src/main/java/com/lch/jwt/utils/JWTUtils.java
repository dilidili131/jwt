package com.lch.jwt.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.yaml.snakeyaml.nodes.ScalarNode;

import java.util.Calendar;
import java.util.Map;

/**
 * @description:
 * @author: lch
 * @time: 2022/4/25 20:01
 */

public class JWTUtils {
    //用于加密header和payload形成signature，引号内自己指定
    private static final String SIGN = "qwe123!@#";

    //用于获取token
    public static String getToken(Map<String,String> map){
        //用于指定token过期日期，此处指定为3天
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE,3);

        JWTCreator.Builder builder = JWT.create();
        //map用于指定payload内容
        map.forEach((k,v)->{
            builder.withClaim(k,v);
        });
        String token = builder.withExpiresAt(instance.getTime())//指定token过期时间
                .sign(Algorithm.HMAC256(SIGN));//指定算法

        return token;
    }

    //用于验证token并返回token中的信息
    public static DecodedJWT verify(String token){
        return JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);
    }
}
