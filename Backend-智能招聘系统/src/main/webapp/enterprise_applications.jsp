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
    <title>收到的投递 - 智能招聘系统</title>
</head>
<body>
<h2>收到的投递</h2>
<table border="1" cellspacing="0" cellpadding="6">
    <tr><th>岗位</th><th>求职者</th><th>状态</th><th>操作</th></tr>
<%
    List<Map<String, Object>> apps = (List<Map<String, Object>>) request.getAttribute("apps");
    if (apps != null) {
        for (Map<String, Object> a : apps) {
%>
    <tr>
        <td><%= esc(String.valueOf(a.get("jobTitle"))) %></td>
        <td><%= esc(String.valueOf(a.get("seekerName"))) %></td>
        <td><%= esc(String.valueOf(a.get("status"))) %></td>
        <td>
            <form action="application" method="post" style="display:inline">
                <input type="hidden" name="action" value="updateStatus">
                <input type="hidden" name="id" value="<%= a.get("id") %>">
                <input type="hidden" name="status" value="REVIEWING">
                <button>标记审核</button>
            </form>
            <form action="application" method="post" style="display:inline">
                <input type="hidden" name="action" value="updateStatus">
                <input type="hidden" name="id" value="<%= a.get("id") %>">
                <input type="hidden" name="status" value="ACCEPTED">
                <button>录用</button>
            </form>
            <form action="application" method="post" style="display:inline">
                <input type="hidden" name="action" value="updateStatus">
                <input type="hidden" name="id" value="<%= a.get("id") %>">
                <input type="hidden" name="status" value="REJECTED">
                <button>拒绝</button>
            </form>
        </td>
    </tr>
<%      }
    }
%>
</table>
<p><a href="job?action=list">返回列表</a></p>
</body>
</html>