# 社区物业管理系统接口测试用例

## 接口清单（代表样例，完整覆盖登录/报修/缴费/公告/鉴权）

| 编号 | 接口 | 方法 | 用例 | 预期 |
|---|---|---|---|---|
| API-01 | /api/auth/login | POST | 正确账号密码 | 返回 JWT token + role |
| API-02 | /api/auth/login | POST | 错误密码 | 401 未授权 |
| API-03 | /api/repair | POST | 业主提交报修 | 工单创建，状态=PENDING |
| API-04 | /api/repair | GET | 按状态查报修列表 | 返回列表，命中联合索引 |
| API-05 | /api/repair/{id}/status | PUT | 工单状态流转 | 状态更新，写流转日志 |
| API-06 | /api/repair/{id}/status | PUT | 已完成单再流转 | 拒绝，逻辑校验 |
| API-07 | /api/payment | GET | 查缴费账单 | 返回业主账单列表 |
| API-08 | /api/payment/{id}/pay | POST | 缴费 | 账单置为已支付 + 写缴费记录 |
| API-09 | /api/payment/{id}/pay | POST | 重复缴费 | 拒绝，幂等校验 |
| API-10 | /api/announcement | GET | 查公告 | 返回公告列表 |
| API-11 | /api/** | GET | 无 token 访问 | 401 未授权 |
| API-12 | /api/** | GET | 过期/非法 token | 401 未授权 |

## 缺陷记录（代表样例）

| 缺陷 | 类型 | 处理 |
|---|---|---|
| 报修列表查询慢（全表扫描） | 性能 | Explain 定位后建联合索引，800ms→150ms |
| 已完成工单仍可流转 | 逻辑 | 服务层加状态校验 |
| 无 token 也能访问缴费接口 | 鉴权 | AuthInterceptor 拦截 |
| 提交报修可伪造 ownerId | 越权 | 后端从 JWT 解析业主身份，忽略前端 ownerId |
| 缴费接口非幂等（重复缴费） | 逻辑 | 账单独有状态，重复缴费拒绝 |