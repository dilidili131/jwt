package com.lch.jwt.dao;

import com.lch.jwt.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @description:
 * @author: lch
 * @time: 2022/4/25 20:27
 */
@Mapper
public interface UserDao {
    User login(User user);
}
