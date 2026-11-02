# 图书管理系统（Backend-图书管理系统）

对应简历「后端.pdf」项目 1。Java + JDBC + MySQL。

## 目录结构
```
├── pom.xml                     # Maven 依赖（JDK17 + MySQL 驱动 + H2 可选）
├── sql/schema.sql              # MySQL 建表脚本
└── src/main/java/com/su/library
    ├── entity/                 # 实体类
    │   ├── Book.java
    │   ├── Reader.java
    │   └── BorrowRecord.java
    ├── dao/                    # 数据访问层（JDBC）
    │   ├── BookDao.java
    │   ├── ReaderDao.java
    │   └── BorrowDao.java
    ├── util/DBUtil.java        # 连接工具
    └── Main.java               # 演示入口
```

## 对应简历要点
- **技术栈**：Java + JDBC + MySQL
- **核心开发**：图书/读者/借阅记录的增删改查、借书、还书、逾期统计
- **难点突破**：借还书的事务一致性（`BorrowDao` 中借书/还书用 JDBC 事务，避免并发借阅导致数据不一致）

## 运行前提
- 本地装好 MySQL 并执行 `sql/schema.sql`；或把 `DBUtil` 里的连接串改成 H2 内嵌（见注释）。
