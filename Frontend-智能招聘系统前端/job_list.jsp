<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>岗位列表 - 智能招聘系统</title>
    <link rel="stylesheet" href="css/style.css">
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

    <!-- 难点突破：Ajax 调用后端 RESTful 接口动态渲染 + 本地关键词/分类二次筛选 -->
    <div class="toolbar">
        <input type="text" id="keyword" placeholder="搜索岗位名 / 公司" oninput="render()">
        <select id="category" onchange="render()">
            <option value="">全部分类</option>
            <option>技术</option>
            <option>数据</option>
            <option>产品</option>
            <option>运营</option>
        </select>
        <button class="btn ghost" onclick="loadJobs()">刷新</button>
    </div>

    <p id="jobCount" style="color:#888;font-size:13px;margin:0 0 8px;"></p>

    <table id="jobTable">
        <thead>
        <tr>
            <th>岗位</th><th>公司</th><th>分类</th><th>薪资</th><th>状态</th><th>投递人数</th><th>操作</th>
        </tr>
        </thead>
        <tbody></tbody>
    </table>
    <div id="empty" class="empty" style="display:none;">暂无匹配岗位，换个关键词试试</div>
</div>

<script src="js/validate.js"></script>
<script>
    // 示例数据（后端不可达时兜底，保证前端演示可独立运行）
    var SAMPLE_JOBS = [
        { id: 1, title: 'Java 后端开发工程师', company: '星辰科技', category: '技术', salary: '15-25K', desc: '负责核心业务后端服务设计与开发。', applyCount: 42, status: 'OPEN' },
        { id: 2, title: '前端开发工程师', company: '云帆网络', category: '技术', salary: '12-20K', desc: '负责 Web 前端页面与数据可视化开发。', applyCount: 35, status: 'OPEN' },
        { id: 3, title: '数据分析师', company: '数智咨询', category: '数据', salary: '14-22K', desc: '负责业务数据分析与报表建设。', applyCount: 28, status: 'OPEN' },
        { id: 4, title: '测试工程师', company: '云帆网络', category: '技术', salary: '10-18K', desc: '负责接口测试、回归测试与缺陷跟进。', applyCount: 21, status: 'OPEN' },
        { id: 5, title: '产品经理', company: '创新工场', category: '产品', salary: '15-28K', desc: '负责产品规划与需求落地。', applyCount: 30, status: 'OPEN' },
        { id: 6, title: '运营专员', company: '青柠传媒', category: '运营', salary: '8-15K', desc: '负责内容运营与用户增长。', applyCount: 18, status: 'CLOSED' }
    ];

    var jobs = [];

    // 拉取后端 /job?action=list，失败回退示例数据（与 charts.js 的 fetchJSON 一致）
    async function loadJobs() {
        try {
            var resp = await fetch('job?action=list');
            if (resp.ok) {
                var data = await resp.json();
                if (Array.isArray(data) && data.length) { jobs = data; }
            }
        } catch (e) { /* 无后端：使用示例数据 */ }
        render();
    }

    function statusBadge(s) {
        if (s === 'CLOSED') return '<span class="badge closed">已关闭</span>';
        return '<span class="badge open">招聘中</span>';
    }

    function render() {
        var kw = document.getElementById('keyword').value.trim().toLowerCase();
        var cat = document.getElementById('category').value;
        var list = jobs.filter(function (j) {
            var okKw = !kw || (j.title + ' ' + (j.company || '')).toLowerCase().indexOf(kw) !== -1;
            var okCat = !cat || j.category === cat;
            return okKw && okCat;
        });

        document.getElementById('jobCount').textContent = '共 ' + list.length + ' 个岗位';
        var tbody = document.querySelector('#jobTable tbody');
        tbody.innerHTML = list.map(function (j) {
            return '<tr>' +
                '<td><b style="color:#1f3b5c;">' + escapeHtml(j.title) + '</b></td>' +
                '<td>' + escapeHtml(j.company || '—') + '</td>' +
                '<td>' + escapeHtml(j.category || '—') + '</td>' +
                '<td style="color:#ef6c00;font-weight:700;">' + escapeHtml(j.salary || '面议') + '</td>' +
                '<td>' + statusBadge(j.status) + '</td>' +
                '<td>' + (j.applyCount || 0) + ' 人</td>' +
                '<td><button class="btn primary" onclick="applyJob(this, ' + j.id + ')">投递</button></td>' +
                '</tr>';
        }).join('');
        document.getElementById('empty').style.display = list.length ? 'none' : 'block';
        document.getElementById('jobTable').style.display = list.length ? '' : 'none';
    }

    // 投递后把按钮置为「已投递」，防止重复投递（难点：前端防抖 + 后端校验）
    async function applyJob(btn, id) {
        if (appliedJobs.has(id)) { alert('您已投递过该岗位，请勿重复投递'); return; }
        await apply(id);
        if (appliedJobs.has(id)) {
            btn.disabled = true;
            btn.textContent = '已投递';
            btn.className = 'btn applied';
        }
    }

    loadJobs();
</script>
</body>
</html>
