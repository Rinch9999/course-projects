# 社区物业管理系统接口测试与缺陷管理（QA-社区物业接口测试）

对应简历「测试.pdf」项目 3。Postman 接口测试。

## 目录结构
```
├── testcases.md                    # 接口测试用例 + 缺陷记录
└── property.postman_collection.json # Postman 接口测试集合
```

## 对应简历要点
- **技术栈**：MySQL + SpringBoot + MyBatis + Postman
- **核心开发**：40+ 接口联调与数据校验，覆盖登录认证、报修工单、缴费账单
- **难点突破**：配合开发用 Explain 定位慢查询，联合索引把响应从 800ms 降到 150ms；写入 1000+ 条数据回归测试。

## 使用
`property.postman_collection.json` 导入 Postman，配合 Backend-社区物业管理系统 运行。
