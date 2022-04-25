package com.lch.jwt.service.impl;

import com.lch.jwt.dao.UserDao;
import com.lch.jwt.entity.User;
import com.lch.jwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: lch
 * @time: 2022/4/25 20:29
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Override
    public User login(User user) {
        User userDB = userDao.login(user);
        if(userDB!=null){
            return userDB;
        }
        throw new RuntimeException("登录失败");
    }
}
