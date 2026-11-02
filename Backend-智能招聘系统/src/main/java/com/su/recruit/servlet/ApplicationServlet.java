package com.su.recruit.servlet;

import com.su.recruit.dao.ApplicationDao;
import com.su.recruit.dao.ResumeDao;
import com.su.recruit.entity.Application;
import com.su.recruit.entity.Resume;
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
 * 投递接口（Servlet）。
 * GET  /application?action=mine        求职者查看我的投递
 * GET  /application?action=enterprise  企业查看收到的投递
 * POST /application?action=apply       求职者投递简历
 * POST /application?action=updateStatus 企业更新投递状态（状态跟踪）
 */
@WebServlet("/application")
public class ApplicationServlet extends HttpServlet {

    private static final List<String> VALID_STATUS =
            Arrays.asList("SUBMITTED", "REVIEWING", "ACCEPTED", "REJECTED");

    private final ApplicationDao applicationDao = new ApplicationDao();
    private final ResumeDao resumeDao = new ResumeDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        HttpSession session = req.getSession(false);
        User user = (User) (session == null ? null : session.getAttribute("user"));
        try {
            if ("mine".equals(action) && user != null && User.ROLE_SEEKER.equals(user.getRole())) {
                Integer seekerId = (Integer) session.getAttribute("seekerId");
                req.setAttribute("apps", applicationDao.listBySeekerWithJob(seekerId));
                req.getRequestDispatcher("/my_applications.jsp").forward(req, resp);
            } else if ("enterprise".equals(action) && user != null && User.ROLE_ENTERPRISE.equals(user.getRole())) {
                Integer enterpriseId = (Integer) session.getAttribute("enterpriseId");
                req.setAttribute("apps", applicationDao.listByEnterprise(enterpriseId));
                req.getRequestDispatcher("/enterprise_applications.jsp").forward(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "无权限");
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        HttpSession session = req.getSession(false);
        User user = (User) (session == null ? null : session.getAttribute("user"));
        try {
            if ("apply".equals(action)) {
                // 仅求职者能投递
                if (user == null || !User.ROLE_SEEKER.equals(user.getRole())) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "仅求职者可投递");
                    return;
                }
                Integer seekerId = (Integer) session.getAttribute("seekerId");
                int jobId = Integer.parseInt(req.getParameter("jobId"));
                String content = req.getParameter("content");

                // 保存 / 更新在线简历，得到 resume_id 后再投递
                int resumeId = resumeDao.save(new Resume(seekerId, content));

                Application app = new Application();
                app.setJobId(jobId);
                app.setSeekerId(seekerId);
                app.setResumeId(resumeId);
                app.setStatus("SUBMITTED");
                applicationDao.add(app);

                resp.sendRedirect(req.getContextPath() + "/application?action=mine");
            } else if ("updateStatus".equals(action)) {
                // 仅企业能更新投递状态
                if (user == null || !User.ROLE_ENTERPRISE.equals(user.getRole())) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "仅企业可处理投递");
                    return;
                }
                String status = req.getParameter("status");
                // 安全：白名单校验，防止写入非法状态
                if (!VALID_STATUS.contains(status)) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "非法状态");
                    return;
                }
                int id = Integer.parseInt(req.getParameter("id"));
                applicationDao.updateStatus(id, status);
                resp.sendRedirect(req.getContextPath() + "/application?action=enterprise");
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}