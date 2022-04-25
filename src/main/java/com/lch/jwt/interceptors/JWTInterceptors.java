package com.lch.jwt.interceptors;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lch.jwt.utils.JWTUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: lch
 * @time: 2022/4/25 20:15
 */

public class JWTInterceptors implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取header中的token
        String token = request.getHeader("token");
        Map<String,Object> map = new HashMap<>();
        try {
            DecodedJWT verify = JWTUtils.verify(token);
            return true;
        }catch (SignatureVerificationException e){
            //无效签名
            e.printStackTrace();
            map.put("msg","无效签名");
        }catch (TokenExpiredException e){
            //token过期
            e.printStackTrace();
            map.put("msg","token过期");
        }catch(AlgorithmMismatchException e){
            //token算法不一致
            e.printStackTrace();
            map.put("msg","token算法不一致");
        }catch (Exception e){
            e.printStackTrace();
            map.put("msg","token无效");
        }
        map.put("state",false);
        //将map转为json （通过jackson）
        String json = new ObjectMapper().writeValueAsString(map);
        //将错误信息写入响应体
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);
        return false;
    }
}
