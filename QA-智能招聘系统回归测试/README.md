# 智能招聘系统全流程回归测试（QA-智能招聘系统回归测试）

对应简历「测试.pdf」项目 1。黑盒用例 + Postman 接口测试。

## 目录结构
```
├── testcases.md                  # 核心测试用例（50+ 条的代表样例）+ 缺陷记录
└── recruit.postman_collection.json  # Postman 接口测试集合
```

## 对应简历要点
- **技术栈**：JSP + Servlet + JDBC + MySQL + Postman
- **核心开发**：三种角色（求职者/企业/管理员）50+ 条核心用例，覆盖岗位发布、简历投递、状态跟踪、企业审核
- **难点突破**：Postman 发现参数类型不匹配、角色越权访问等 10+ 个缺陷并跟进闭环；验证 Filter 拦截器与权限校验逻辑。

## 使用
`testcases.md` 是手工用例；`recruit.postman_collection.json` 导入 Postman，配合 Backend-智能招聘系统 运行。
