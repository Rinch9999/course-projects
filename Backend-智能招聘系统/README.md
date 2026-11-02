# 智能招聘系统（Backend-智能招聘系统）

对应简历「后端.pdf」项目 2。JavaWeb（Servlet + JDBC + MySQL + JSP）。

## 目录结构
```
├── pom.xml                          # war 打包 + Servlet/JSP/MySQL 依赖
├── sql/schema.sql                   # 9 张表建表脚本
└── src/main/java/com/su/recruit
    ├── entity/    User / Job / Application
    ├── dao/       JobDao / ApplicationDao
    ├── servlet/   JobServlet
    ├── filter/    AuthFilter        # ← 难点：URL 级权限拦截
    └── util/      DBUtil
```

## 对应简历要点
- **技术栈**：Servlet + JDBC + MySQL + JSP
- **核心开发**：求职者/企业/管理员三种角色权限模型，岗位发布、简历投递、状态跟踪、企业审核等用例，9 张数据表
- **难点突破**：
  1. `AuthFilter` 用 Filter 做 URL 级权限拦截，防止未授权访问；
  2. `JobDao.listHotJobs()` 用多表关联 + 分组统计的复杂 SQL 实现岗位热度排行。

## 运行前提
- war 项目，部署到 Tomcat 9+；先执行 `sql/schema.sql`。
