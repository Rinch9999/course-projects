<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, com.su.recruit.entity.Job, com.su.recruit.entity.User" %>
<%!
    // XSS 防护：HTML 转义
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
    <title>岗位列表 - 智能招聘系统</title>
</head>
<body>
<h2>岗位列表</h2>
<%
    User user = (User) session.getAttribute("user");
    String role = user == null ? "" : user.getRole();
    String displayName = (String) session.getAttribute("displayName");
    String keyword = (String) request.getAttribute("keyword");
%>
<% if (displayName != null) { %>
    <p>欢迎，<%= esc(displayName) %>（<%= esc(role) %>） <a href="index.jsp">退出</a></p>
<% } %>

<form action="job" method="get">
    <input type="hidden" name="action" value="list">
    <input type="text" name="keyword" value="<%= esc(keyword) %>" placeholder="搜索岗位">
    <button type="submit">搜索</button>
    <a href="job?action=hot">🔥 热度排行</a>
</form>

<% if (User.ROLE_ENTERPRISE.equals(role)) { %>
    <fieldset>
        <legend>发布岗位</legend>
        <form action="job" method="post">
            <label>标题：<input type="text" name="title"></label><br>
            <label>描述：<textarea name="description"></textarea></label><br>
            <label>分类ID：<input type="text" name="categoryId" size="4"></label><br>
            <button type="submit">发布</button>
        </form>
        <a href="application?action=enterprise">查看收到的投递</a>
    </fieldset>
<% } else if (User.ROLE_SEEKER.equals(role)) { %>
    <p><a href="application?action=mine">我的投递</a></p>
<% } else if (User.ROLE_ADMIN.equals(role)) { %>
    <p><a href="enterprise?action=audit">企业审核</a></p>
<% } %>

<hr>
<%
    List<Job> jobs = (List<Job>) request.getAttribute("jobs");
    if (jobs == null || jobs.isEmpty()) {
%>
    <p>暂无岗位</p>
<% } else {
       for (Job j : jobs) {
%>
    <div style="border:1px solid #ccc;padding:8px;margin:6px 0">
        <b><a href="job?action=detail&id=<%= j.getId() %>"><%= esc(j.getTitle()) %></a></b>
        <span>[<%= esc(j.getStatus()) %>]</span>
        <p><%= esc(j.getDescription()) %></p>
    </div>
<%     }
   } %>
</body>
</html>