package com.lch.jwt.controller;

import com.lch.jwt.entity.User;
import com.lch.jwt.service.UserService;
import com.lch.jwt.utils.CommonResult;
import com.lch.jwt.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: lch
 * @time: 2022/4/25 20:30
 */
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/user/login")
    public CommonResult<Map> login(User user){
        Map<String,String> payload = new HashMap<>();
        Map<String,Object> map = new HashMap<>();
        try{
            User login = userService.login(user);
            payload.put("id",login.getId());
            payload.put("username", login.getUsername());

            String token = JWTUtils.getToken(payload);
            map.put("state",true);
            map.put("token",token);
            return new CommonResult<>(200,"success",map);
        }catch (Exception e){
            map.put("state",false);
            return new CommonResult<>(400,"error",map);
        }
    }

    @PostMapping("/user/test")
    public CommonResult test(){
        return new CommonResult(200,"success");
    }
}
