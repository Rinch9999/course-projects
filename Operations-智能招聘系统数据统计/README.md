# 智能招聘系统数据统计与热度分析（Operations-智能招聘系统数据统计）

对应简历「运营.pdf」项目 1。MySQL 复杂 SQL 统计。

## 目录结构
```
└── sql/stats.sql    # 难点：多表关联 + 分组统计（岗位热度/企业活跃度/投递状态分布）
```

## 对应简历要点
- **技术栈**：MySQL + JDBC + Servlet/JSP
- **核心开发**：9 张表，多表关联、分组统计，实现岗位热度排行、企业活跃度统计、投递状态分布
- **难点突破**：`stats.sql` 用 LEFT JOIN + GROUP BY 的复杂 SQL 支撑运营决策。

## 使用
在 Backend-智能招聘系统 的 MySQL 库（recruit）上执行。
