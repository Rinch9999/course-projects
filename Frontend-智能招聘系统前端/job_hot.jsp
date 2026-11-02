<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>热度排行 - 智能招聘系统</title>
    <link rel="stylesheet" href="css/style.css">
    <script src="https://cdn.jsdelivr.net/npm/echarts@5/dist/echarts.min.js"></script>
</head>
<body>
<div class="container">
    <header class="header">
        <h1>智能招聘系统</h1>
        <div class="nav">
            <a href="job_list.jsp">岗位列表</a>
            <a href="job_hot.jsp">热度排行</a>
            <a href="login.jsp">登录</a>
        </div>
    </header>

    <!-- 难点突破：ECharts 柱状图展示岗位热度（投递量）+ 饼图展示企业活跃度 -->
    <div class="chart-box">
        <h3>岗位热度排行</h3>
        <div id="hotChart" class="chart"></div>
    </div>

    <div class="chart-box">
        <h3>企业活跃度（发布岗位数）</h3>
        <div id="activeChart" class="chart"></div>
    </div>
</div>

<script src="js/charts.js"></script>
<script>
    // 拉取后端 /job?action=hot，失败回退示例数据；企业活跃度使用示例数据兜底
    renderHotChart();
    renderActivePie('activeChart');
</script>
</body>
</html>
