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
    <title>我的投递 - 智能招聘系统</title>
</head>
<body>
<h2>我的投递</h2>
<table border="1" cellspacing="0" cellpadding="6">
    <tr><th>岗位</th><th>状态</th><th>投递时间</th></tr>
<%
    List<Map<String, Object>> apps = (List<Map<String, Object>>) request.getAttribute("apps");
    if (apps != null) {
        for (Map<String, Object> a : apps) {
%>
    <tr>
        <td><%= esc(String.valueOf(a.get("jobTitle"))) %></td>
        <td><%= esc(String.valueOf(a.get("status"))) %></td>
        <td><%= a.get("createdAt") %></td>
    </tr>
<%      }
    }
%>
</table>
<p><a href="job?action=list">返回列表</a></p>
</body>
</html>