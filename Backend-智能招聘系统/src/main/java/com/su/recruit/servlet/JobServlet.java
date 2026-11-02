package com.su.recruit.servlet;

import com.su.recruit.dao.JobDao;
import com.su.recruit.entity.Job;
import com.su.recruit.entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 岗位接口（Servlet）。
 * GET  /job?action=list|hot|detail
 * POST /job  发布岗位（仅企业角色）
 */
@WebServlet("/job")
public class JobServlet extends HttpServlet {

    private final JobDao jobDao = new JobDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("hot".equals(action)) {
                // 难点：岗位热度排行（多表关联 + 分组统计）
                req.setAttribute("hotJobs", jobDao.listHotJobs(10));
                req.getRequestDispatcher("/job_hot.jsp").forward(req, resp);
            } else if ("detail".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                req.setAttribute("job", jobDao.findById(id));
                req.getRequestDispatcher("/job_detail.jsp").forward(req, resp);
            } else {
                String keyword = req.getParameter("keyword");
                req.setAttribute("keyword", keyword);
                req.setAttribute("jobs", jobDao.findByTitle(keyword));
                req.getRequestDispatcher("/job_list.jsp").forward(req, resp);
            }
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

        // 安全：仅企业角色可发布岗位，且 enterpriseId 取自 session，不信任表单参数
        if (user == null || !User.ROLE_ENTERPRISE.equals(user.getRole())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "仅企业账号可发布岗位");
            return;
        }
        Integer enterpriseId = (Integer) session.getAttribute("enterpriseId");
        if (enterpriseId == null) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "未找到企业档案");
            return;
        }

        Job job = new Job();
        job.setEnterpriseId(enterpriseId);
        job.setTitle(req.getParameter("title"));
        job.setDescription(req.getParameter("description"));
        job.setStatus("OPEN");
        String categoryId = req.getParameter("categoryId");
        if (categoryId != null && !categoryId.trim().isEmpty()) {
            job.setCategoryId(Integer.parseInt(categoryId.trim()));
        }

        try {
            jobDao.add(job);
            resp.sendRedirect(req.getContextPath() + "/job?action=list");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}