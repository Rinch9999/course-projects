# 智能招聘系统前端（Frontend-智能招聘系统前端）

对应简历「前端.pdf」项目 1。JSP + HTML/CSS/JavaScript + Ajax + ECharts。

## 目录结构
```
├── demo.html          # 独立演示（三角色 + 岗位大厅/投递/企业/统计，无需后端可直接打开）
├── login.jsp          # 登录页（含前端表单校验）
├── job_list.jsp       # 岗位列表（Ajax 动态加载）
├── job_hot.jsp        # 岗位热度图表（ECharts）
├── css/style.css
└── js/
    ├── validate.js    # 难点：表单校验（必填/手机号/邮箱/重复投递提示）
    └── charts.js      # 难点：ECharts 热度/活跃度图表
```

## 对应简历要点
- **技术栈**：JSP + HTML/CSS/JavaScript + Ajax + ECharts
- **核心开发**：三种角色页面、表单校验、JSP 动态渲染 + Ajax 数据交互
- **难点突破**：`validate.js` 表单校验与重复投递提示；`charts.js` 用 ECharts 画岗位热度排行。

## 运行
- `demo.html` 直接用浏览器打开即可（内置数据，无需后端）。
- `login.jsp` / `job_list.jsp` / `job_hot.jsp` 依赖 Backend-智能招聘系统 的 Servlet 接口（/job、/login、/application），部署到 Tomcat 配合运行。
