<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, com.su.recruit.entity.Enterprise" %>
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
    <title>企业审核 - 智能招聘系统</title>
</head>
<body>
<h2>企业审核</h2>
<%
    List<Enterprise> pending = (List<Enterprise>) request.getAttribute("pending");
    if (pending == null || pending.isEmpty()) {
%>
    <p>暂无待审核企业</p>
<% } else { %>
    <table border="1" cellspacing="0" cellpadding="6">
        <tr><th>企业名</th><th>行业</th><th>操作</th></tr>
<%      for (Enterprise e : pending) { %>
        <tr>
            <td><%= esc(e.getName()) %></td>
            <td><%= esc(e.getIndustry()) %></td>
            <td>
                <form action="enterprise" method="post" style="display:inline">
                    <input type="hidden" name="action" value="audit">
                    <input type="hidden" name="id" value="<%= e.getId() %>">
                    <input type="hidden" name="status" value="APPROVED">
                    <button>通过</button>
                </form>
                <form action="enterprise" method="post" style="display:inline">
                    <input type="hidden" name="action" value="audit">
                    <input type="hidden" name="id" value="<%= e.getId() %>">
                    <input type="hidden" name="status" value="REJECTED">
                    <button>拒绝</button>
                </form>
            </td>
        </tr>
<%      } %>
    </table>
<% } %>
<p><a href="job?action=list">返回列表</a></p>
</body>
</html>