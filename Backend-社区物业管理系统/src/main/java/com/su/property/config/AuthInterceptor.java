package com.su.property.config;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * JWT 鉴权拦截器：从请求头 Authorization 取 token，校验后放行，否则 401。
 * 注册为 Spring 组件，由 WebConfig 挂载到指定路径。
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "缺少令牌");
            return false;
        }
        try {
            Claims claims = JwtUtil.parse(header.substring(7));
            request.setAttribute("userId", claims.getSubject());
            request.setAttribute("role", claims.get("role"));
            return true;
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "令牌无效或已过期");
            return false;
        }
    }
}
