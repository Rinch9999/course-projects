package com.su.recruit.filter;

import com.su.recruit.entity.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 难点突破：URL 级权限拦截。
 *
 * 用 Filter 拦截所有请求：
 * 1. 未登录 → 跳转登录页；
 * 2. 已登录但角色不符（如普通求职者访问 /admin/**）→ 拒绝（403）。
 */
@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String uri = req.getRequestURI();
        String ctx = req.getContextPath();

        // 白名单：登录页、登录接口、静态资源
        if (isPublic(uri, ctx)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");

        // 未登录 → 跳登录
        if (user == null) {
            resp.sendRedirect(ctx + "/index.jsp");
            return;
        }

        // 角色权限校验（URL 前缀 → 允许的角色）
        if (uri.startsWith(ctx + "/admin/") && !User.ROLE_ADMIN.equals(user.getRole())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "无管理员权限");
            return;
        }
        if (uri.startsWith(ctx + "/enterprise/") && !User.ROLE_ENTERPRISE.equals(user.getRole())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "无企业权限");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isPublic(String uri, String ctx) {
        return uri.equals(ctx + "/index.jsp")
                || uri.equals(ctx + "/login")
                || uri.equals(ctx + "/register")
                || uri.equals(ctx + "/register.jsp")
                || uri.equals(ctx + "/")
                || uri.endsWith(".css")
                || uri.endsWith(".js");
    }

    @Override
    public void init(FilterConfig filterConfig) {}
    @Override
    public void destroy() {}
}
