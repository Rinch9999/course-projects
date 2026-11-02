<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>登录 - 智能招聘系统</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="login-box">
    <h2>智能招聘系统 · 登录</h2>

    <!-- 难点突破：三角色统一登录入口，后端按账号自动识别角色并跳转对应首页 -->
    <div class="role-hint">
        <b>演示账号</b><br>
        求职者：<b>seeker / 123456</b>（投递简历）<br>
        企业：<b>enterprise / 123456</b>（发布岗位、处理投递）<br>
        管理员：<b>admin / 123456</b>（数据看板）
    </div>

    <form id="loginForm" action="login" method="post" onsubmit="return validateLogin(this)">
        <label>用户名
            <input type="text" name="username" placeholder="请输入用户名" autocomplete="username" required>
        </label>
        <label>密码
            <input type="password" name="password" placeholder="请输入密码" autocomplete="current-password" required>
        </label>
        <p id="loginError" class="error"></p>
        <button type="submit">登 录</button>
    </form>

    <p style="text-align:center;font-size:13px;color:#888;margin-top:14px;">
        还没有账号？<a href="register.jsp" style="color:#1f3b5c;">立即注册</a> ·
        <a href="job_list.jsp" style="color:#1f3b5c;">先去逛逛岗位</a>
    </p>
</div>

<script src="js/validate.js"></script>
</body>
</html>
