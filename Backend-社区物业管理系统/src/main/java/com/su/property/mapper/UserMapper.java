package com.su.property.mapper;

import com.su.property.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 账号 Mapper（登录认证用）。
 */
@Mapper
public interface UserMapper {

    @Select("SELECT id, username, password, role FROM `user` WHERE username = #{username}")
    User findByUsername(String username);
}