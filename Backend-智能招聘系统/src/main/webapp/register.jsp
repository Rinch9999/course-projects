<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>注册 - 智能招聘系统</title>
</head>
<body>
<h2>用户注册</h2>
<form action="register" method="post">
    <label>角色：
        <select name="role">
            <option value="SEEKER">求职者</option>
            <option value="ENTERPRISE">企业</option>
        </select>
    </label><br>
    <label>用户名：<input type="text" name="username"></label><br>
    <label>密码：<input type="password" name="password"></label><br>
    <label>名称：<input type="text" name="name"></label><br>
    <label>电话：<input type="text" name="phone"></label><br>
    <label>学历（求职者填）：<input type="text" name="education"></label><br>
    <label>行业（企业填）：<input type="text" name="industry"></label><br>
    <button type="submit">注册</button>
</form>
<% if ("1".equals(request.getParameter("error"))) { %>
    <p style="color:red">用户名和密码不能为空</p>
<% } else if ("2".equals(request.getParameter("error"))) { %>
    <p style="color:red">用户名已存在</p>
<% } %>
<p><a href="index.jsp">返回登录</a></p>
</body>
</html>