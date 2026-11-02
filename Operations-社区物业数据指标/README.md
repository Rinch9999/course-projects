# 社区物业管理系统数据指标与报表支持（Operations-社区物业数据指标）

对应简历「运营.pdf」项目 3。MySQL 数据指标 SQL。

## 目录结构
```
└── sql/metrics.sql    # 难点：报修类型统计 / 缴费状态分布 / 缴费趋势
```

## 对应简历要点
- **技术栈**：MySQL + SpringBoot + MyBatis + Postman
- **核心开发**：12 张表，报修类型统计、缴费状态分布、缴费趋势等指标查询与图表展示
- **难点突破**：Explain 建联合索引把查询从 800ms 降到 150ms；Postman 完成 40+ 接口校验。

## 使用
在 Backend-社区物业管理系统 的 MySQL 库（property）上执行。
