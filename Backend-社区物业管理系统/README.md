# 社区物业管理系统（Backend-社区物业管理系统）

对应简历「后端.pdf」项目 3。SpringBoot + MyBatis + MySQL + Nacos + JWT。

## 目录结构
```
├── pom.xml                          # SpringBoot + MyBatis + MySQL + JWT + Nacos Discovery
├── sql/schema.sql                   # 12 张表 + 报修联合索引
└── src/main/java/com/su/property
    ├── entity/    Owner / RepairOrder
    ├── mapper/    RepairOrderMapper（接口 + XML）
    ├── service/   RepairService      # 报修工单流转
    ├── controller/RepairController   # RESTful 接口
    ├── controller/DiscoveryController # 服务发现演示（Nacos）
    ├── config/    JwtUtil / AuthInterceptor  # JWT 鉴权
    └── PropertyApplication.java
```

## 对应简历要点
- **技术栈**：SpringBoot + MyBatis + MySQL + Nacos + JWT
- **核心开发**：12 张表、微服务模块拆分（业主/报修/缴费/认证）、JWT 登录认证与接口鉴权、报修工单流转
- **难点突破**：
  1. `sql/schema.sql` 里 `repair_order` 建 `(status, created_at)` 联合索引，配合 EXPLAIN 把报修列表查询从 800ms 降到 150ms；
  2. `JwtUtil` + `AuthInterceptor` 实现基于 JWT 的登录认证与接口鉴权；
  3. `@EnableDiscoveryClient` + Nacos Discovery 实现服务注册与发现（默认关闭，可单机跑）。

> Nacos 微服务（演示版）：应用以 `property-service` 注册到 Nacos。默认 `spring.cloud.nacos.discovery.enabled=false`，单机 `mvn spring-boot:run` 即可跑通；改成 `true` 并启动本地 Nacos（8848）后，访问 `/api/discovery/services` 可见服务清单。当前骨架为单应用，按包名划分微服务模块（业主/报修/缴费/认证）。
