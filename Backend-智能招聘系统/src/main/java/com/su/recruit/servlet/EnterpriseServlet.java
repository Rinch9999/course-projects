package com.su.recruit.servlet;

import com.su.recruit.dao.EnterpriseDao;
import com.su.recruit.entity.Enterprise;
import com.su.recruit.entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 企业审核接口（管理员）。
 * GET  /enterprise?action=audit         待审核企业列表
 * POST /enterprise?action=audit         通过 / 拒绝（写入 audit 日志）
 */
@WebServlet("/enterprise")
public class EnterpriseServlet extends HttpServlet {

    private static final List<String> VALID_STATUS =
            Arrays.asList(Enterprise.STATUS_APPROVED, Enterprise.STATUS_REJECTED);

    private final EnterpriseDao enterpriseDao = new EnterpriseDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) (session == null ? null : session.getAttribute("user"));
        if (user == null || !User.ROLE_ADMIN.equals(user.getRole())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "仅管理员可审核企业");
            return;
        }
        try {
            req.setAttribute("pending", enterpriseDao.listPending());
            req.getRequestDispatcher("/enterprise_audit.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);
        User user = (User) (session == null ? null : session.getAttribute("user"));
        if (user == null || !User.ROLE_ADMIN.equals(user.getRole())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "仅管理员可审核企业");
            return;
        }

        try {
            int id = Integer.parseInt(req.getParameter("id"));
            String status = req.getParameter("status");
            if (!VALID_STATUS.contains(status)) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "非法状态");
                return;
            }
            enterpriseDao.updateStatus(id, status);
            enterpriseDao.insertAudit(id, status);
            resp.sendRedirect(req.getContextPath() + "/enterprise?action=audit");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}