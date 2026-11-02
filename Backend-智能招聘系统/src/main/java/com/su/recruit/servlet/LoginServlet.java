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
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 登录接口（Servlet）：查询数据库校验账号密码，按角色写入 session。
 * 三种角色登录后跳转不同页面：求职者→岗位列表、企业→岗位列表（可发布/看投递）、管理员→企业审核。
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserDao userDao = new UserDao();
    private final SeekerDao seekerDao = new SeekerDao();
    private final EnterpriseDao enterpriseDao = new EnterpriseDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp?error=1");
            return;
        }

        try {
            User user = userDao.findByUsername(username.trim());
            // 安全说明：演示用明文比较，生产环境应校验 BCrypt 哈希
            if (user == null || !user.getPassword().equals(password)) {
                resp.sendRedirect(req.getContextPath() + "/index.jsp?error=2");
                return;
            }

            HttpSession session = req.getSession();
            session.setAttribute("user", user);

            // 按角色关联业务 id，供后续投递 / 审核使用
            switch (user.getRole()) {
                case User.ROLE_SEEKER: {
                    Seeker seeker = seekerDao.findByUserId(user.getId());
                    if (seeker != null) {
                        session.setAttribute("seekerId", seeker.getId());
                        session.setAttribute("displayName", seeker.getName());
                    }
                    break;
                }
                case User.ROLE_ENTERPRISE: {
                    Enterprise enterprise = enterpriseDao.findByUserId(user.getId());
                    if (enterprise != null) {
                        session.setAttribute("enterpriseId", enterprise.getId());
                        session.setAttribute("displayName", enterprise.getName());
                    }
                    break;
                }
                default:
                    session.setAttribute("displayName", username);
            }

            switch (user.getRole()) {
                case User.ROLE_ADMIN:
                    resp.sendRedirect(req.getContextPath() + "/enterprise?action=audit");
                    break;
                case User.ROLE_ENTERPRISE:
                    resp.sendRedirect(req.getContextPath() + "/job?action=list");
                    break;
                default:
                    resp.sendRedirect(req.getContextPath() + "/job?action=list");
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}