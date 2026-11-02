<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.su.recruit.entity.Job, com.su.recruit.entity.User" %>
<%!
    private String esc(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&#39;");
    }
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>岗位详情 - 智能招聘系统</title>
</head>
<body>
<%
    Job job = (Job) request.getAttribute("job");
    User user = (User) session.getAttribute("user");
    String role = user == null ? "" : user.getRole();
%>
<% if (job != null) { %>
    <h2><%= esc(job.getTitle()) %></h2>
    <p><%= esc(job.getDescription()) %></p>
    <p>状态：<%= esc(job.getStatus()) %></p>
    <% if (User.ROLE_SEEKER.equals(role)) { %>
        <form action="application" method="post">
            <input type="hidden" name="action" value="apply">
            <input type="hidden" name="jobId" value="<%= job.getId() %>">
            <label>简历内容：<br><textarea name="content" rows="5" cols="40"></textarea></label><br>
            <button type="submit">投递简历</button>
        </form>
    <% } %>
<% } else { %>
    <p>岗位不存在</p>
<% } %>
<p><a href="job?action=list">返回列表</a></p>
</body>
</html>