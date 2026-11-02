# 简历项目 · 运行手册

15 个课设项目的代码实现，按「简历名-项目名」扁平命名，对应 5 份简历（后端 / 前端 / 数据分析 / 测试 / 运营）。

---

## 一、目录总览

```
Backend-图书管理系统          后端.pdf 项目1（Java + JDBC + MySQL）
Backend-智能招聘系统          后端.pdf 项目2（Servlet + JSP + MySQL）
Backend-社区物业管理系统      后端.pdf 项目3（SpringBoot + MyBatis + MySQL + JWT）
Frontend-智能招聘系统前端     前端.pdf 项目1（JSP + Ajax + ECharts）
Frontend-单词记忆卡片         前端.pdf 项目2（HTML/CSS/JS + localStorage）
Frontend-校园活动报名H5       前端.pdf 项目3（HTML5 + CSS3 + JS）
Data Analysis-航班信息查询与检索   数据分析.pdf 项目1（Java + 数据结构）
Data Analysis-知乎热榜爬虫与NLP   数据分析.pdf 项目2（Python 爬虫 + NLP）
Data Analysis-机器学习项目         数据分析.pdf 项目3（Python + scikit-learn）
QA-智能招聘系统回归测试       测试.pdf 项目1（测试用例 + Postman）
QA-知乎热榜数据清洗           测试.pdf 项目2（Python）
QA-社区物业接口测试           测试.pdf 项目3（接口用例 + Postman）
Operations-智能招聘系统数据统计    运营.pdf 项目1（SQL）
Operations-知乎热榜NLP             运营.pdf 项目2（Python）
Operations-社区物业数据指标        运营.pdf 项目3（SQL）
```

---

## 二、环境（已装好）

| 环境 | 版本 | 说明 |
|---|---|---|
| Java | OpenJDK 17 | 后端项目用 |
| Maven | 3.8.1 | 后端项目构建/运行 |
| Python | 3.12（`python` 命令） | 数据分析/测试/运营用；**别用 `python3`**（那是商店版 3.14，没装库） |
| MySQL | 8.0.31 | 服务名 `MySQL80`，已运行 |

Python 已装依赖：`requests` `bs4` `jieba` `snownlp` `scikit-learn` `pandas` `matplotlib` `numpy`

### 数据库连接信息

```
主机：localhost:3306
用户：root
密码：your_mysql_password
数据库：library / recruit / property（已建好，含示例数据）
```

---

## 三、运行方式

> 一键运行：双击根目录 `run.bat`（Windows）或 `bash run.sh`，按菜单序号选择项目即可。以下为各项目的命令行运行方式。

### 1. 前端（浏览器直接打开，无需命令行）

| 项目 | 打开 |
|---|---|
| 单词记忆卡片 | `Frontend-单词记忆卡片\index.html` |
| 校园活动报名H5 | `Frontend-校园活动报名H5\index.html` |
| 智能招聘前端 | `Frontend-智能招聘系统前端\demo.html`（独立演示，无需后端） |

### 2. 数据分析（命令行）

```bash
# 航班查询（纯 Java）
cd "E:/1--苏个人-各文档/简历/Data Analysis-航班信息查询与检索"
javac -encoding UTF-8 src/main/java/com/su/flight/*.java -d out
java -cp out com.su.flight.Main

# 知乎热榜爬虫与 NLP
cd "E:/1--苏个人-各文档/简历/Data Analysis-知乎热榜爬虫与NLP"
python main.py

# 机器学习
cd "E:/1--苏个人-各文档/简历/Data Analysis-机器学习项目"
python train.py
```

### 3. 后端（Maven，需 MySQL 已启动）

```bash
# 图书管理系统（跑完即退出）
cd "E:/1--苏个人-各文档/简历/Backend-图书管理系统"
mvn exec:java

# 社区物业管理系统（SpringBoot，常驻，浏览器访问 http://localhost:8080）
cd "E:/1--苏个人-各文档/简历/Backend-社区物业管理系统"
mvn spring-boot:run

# 智能招聘系统（Jetty，常驻，浏览器访问 http://localhost:8080/recruit/）
cd "E:/1--苏个人-各文档/简历/Backend-智能招聘系统"
mvn jetty:run
```

> `spring-boot:run` / `jetty:run` 是常驻进程，`Ctrl+C` 停止；8080 被占用时先停掉之前跑的进程。

### 4. 测试 / 运营（Python）

```bash
# 测试 - 数据清洗
cd "E:/1--苏个人-各文档/简历/QA-知乎热榜数据清洗"
python data_clean.py

# 运营 - 知乎 NLP 选题建议
cd "E:/1--苏个人-各文档/简历/Operations-知乎热榜NLP"
python content_insight.py
```

### 5. 运营 SQL（在 MySQL 执行）

```bash
MYSQL="D:/MySQL 8.0.31/mysql-8.0.31-winx64/bin/mysql.exe"
"$MYSQL" -u root -pyour_mysql_password --table < "E:/1--苏个人-各文档/简历/Operations-智能招聘系统数据统计/sql/stats.sql"
"$MYSQL" -u root -pyour_mysql_password --table < "E:/1--苏个人-各文档/简历/Operations-社区物业数据指标/sql/metrics.sql"
```

### 6. 测试文档类（Postman / 用例）

- `QA-智能招聘系统回归测试\recruit.postman_collection.json` → 导入 Postman
- `QA-社区物业接口测试\property.postman_collection.json` → 导入 Postman
- 各 `testcases.md` → 手工测试用例，浏览器/编辑器直接看

---

## 四、注意事项

1. 后端三个项目当前连的是**本地 MySQL**（`DBUtil.java` / `application.properties` 里配好了）；无 MySQL 时可切回 H2（文件里留了注释）。
2. 知乎两个爬虫项目真实抓取会被知乎 403 拦截，已做「失败回退示例数据」，无网也能跑全流程。
3. 数据库密码已用占位符 `your_mysql_password` 替换，运行前请改成你自己的 MySQL 密码。
