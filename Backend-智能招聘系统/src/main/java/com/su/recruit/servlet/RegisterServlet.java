package com.su.recruit.servlet;

import com.su.recruit.dao.EnterpriseDao;
import com.su.recruit.dao.SeekerDao;
import com.su.recruit.dao.UserDao;
import com.su.recruit.entity.Enterprise;
import com.su.recruit.entity.Seeker;
import com.su.recruit.entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 注册接口（Servlet）。支持求职者 / 企业两类注册（管理员不开放注册）。
 * 企业注册后默认 PENDING，等待管理员审核。
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final UserDao userDao = new UserDao();
    private final SeekerDao seekerDao = new SeekerDao();
    private final EnterpriseDao enterpriseDao = new EnterpriseDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String role = req.getParameter("role");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String name = req.getParameter("name");

        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/register.jsp?error=1");
            return;
        }

        try {
            // 用户名查重
            if (userDao.findByUsername(username.trim()) != null) {
                resp.sendRedirect(req.getContextPath() + "/register.jsp?error=2");
                return;
            }

            int userId = userDao.insert(new User(username.trim(), password, role));

            if (User.ROLE_SEEKER.equals(role)) {
                seekerDao.insert(new Seeker(userId, name, req.getParameter("phone"),
                        req.getParameter("education")));
            } else if (User.ROLE_ENTERPRISE.equals(role)) {
                Enterprise e = new Enterprise(userId, name, req.getParameter("industry"),
                        Enterprise.STATUS_PENDING);
                enterpriseDao.insert(e);
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "非法角色");
                return;
            }

            resp.sendRedirect(req.getContextPath() + "/index.jsp?registered=1");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}