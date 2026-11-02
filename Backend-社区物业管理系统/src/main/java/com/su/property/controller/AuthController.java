package com.su.property.controller;

import com.su.property.config.JwtUtil;
import com.su.property.entity.User;
import com.su.property.mapper.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证接口：登录成功后签发 JWT。
 * 难点突破：JWT 登录认证，后续接口凭 Authorization 头鉴权。
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserMapper userMapper;

    public AuthController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        User user = userMapper.findByUsername(username);
        // 安全说明：演示用明文比较，生产环境应校验 BCrypt 哈希
        if (user == null || !user.getPassword().equals(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "用户名或密码错误");
        }

        String token = JwtUtil.generate(user.getId(), user.getRole());
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("role", user.getRole());
        result.put("username", user.getUsername());
        return result;
    }
}