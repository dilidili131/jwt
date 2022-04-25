package com.lch.jwt.service;

import com.lch.jwt.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User login(User user);
}
