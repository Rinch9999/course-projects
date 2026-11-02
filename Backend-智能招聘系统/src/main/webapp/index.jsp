<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>登录 - 智能招聘系统</title>
</head>
<body>
<h2>智能招聘系统</h2>
<form action="login" method="post">
    <label>用户名：<input type="text" name="username"></label><br>
    <label>密码：<input type="password" name="password"></label><br>
    <button type="submit">登录</button>
</form>
<% if ("1".equals(request.getParameter("error"))) { %>
    <p style="color:red">用户名和密码不能为空</p>
<% } else if ("2".equals(request.getParameter("error"))) { %>
    <p style="color:red">用户名或密码错误</p>
<% } %>
<% if ("1".equals(request.getParameter("registered"))) { %>
    <p style="color:green">注册成功，请登录</p>
<% } %>
<p>还没有账号？<a href="register.jsp">立即注册</a></p>
<p style="color:gray">演示账号：求职者 seeker/123456，企业 enterprise/123456，管理员 admin/123456</p>
</body>
</html>