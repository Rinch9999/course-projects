<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, java.util.Map" %>
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
    <title>岗位热度排行 - 智能招聘系统</title>
</head>
<body>
<h2>岗位热度排行</h2>
<ol>
<%
    List<Map<String, Object>> hotJobs = (List<Map<String, Object>>) request.getAttribute("hotJobs");
    if (hotJobs != null) {
        for (Map<String, Object> row : hotJobs) {
%>
    <li><%= esc(String.valueOf(row.get("title"))) %> —— 投递数 <%= row.get("applyCount") %></li>
<%      }
    }
%>
</ol>
<p><a href="job?action=list">返回列表</a></p>
</body>
</html>