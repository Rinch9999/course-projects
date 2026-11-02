#!/usr/bin/env bash
# 简历项目 · 一键运行（Git Bash / macOS / Linux）
# 前置：JDK17 / Maven / Python3.12(命令 python) / MySQL8 已装好并在运行

ROOT="$(cd "$(dirname "$0")" && pwd)"
MYSQL="D:/MySQL 8.0.31/mysql-8.0.31-winx64/bin/mysql.exe"

# 跨平台打开浏览器/文件
open_file() {
  case "$(uname -s)" in
    Darwin) open "$1" ;;
    Linux)  xdg-open "$1" >/dev/null 2>&1 || echo "请手动打开: $1" ;;
    MINGW*|MSYS*|CYGWIN*) start "" "$1" ;;
    *) echo "请手动打开: $1" ;;
  esac
}

menu() {
  echo "=============================================="
  echo "   简历项目 · 一键运行"
  echo "=============================================="
  echo "  前端"
  echo "   [1] 智能招聘系统前端 (demo.html)"
  echo "   [2] 单词记忆卡片"
  echo "   [3] 校园活动报名H5"
  echo "  数据分析"
  echo "   [4] 航班信息查询与检索 (Java)"
  echo "   [5] 知乎热榜爬虫与NLP"
  echo "   [6] 机器学习项目"
  echo "  后端"
  echo "   [7] 图书管理系统 (JDBC)"
  echo "   [8] 社区物业管理系统 (SpringBoot)"
  echo "   [9] 智能招聘系统 (Jetty)"
  echo "  测试 / 运营"
  echo "   [10] 知乎热榜数据清洗"
  echo "   [11] 知乎热榜NLP选题建议"
  echo "   [12] 招聘数据统计 (SQL)"
  echo "   [13] 物业数据指标 (SQL)"
  echo "   [0] 退出"
  echo "=============================================="
}

while true; do
  menu
  read -r -p "请输入序号后回车: " choice
  case "$choice" in
    1) open_file "$ROOT/Frontend-智能招聘系统前端/demo.html" ;;
    2) open_file "$ROOT/Frontend-单词记忆卡片/index.html" ;;
    3) open_file "$ROOT/Frontend-校园活动报名H5/index.html" ;;
    4) cd "$ROOT/Data Analysis-航班信息查询与检索" && javac -encoding UTF-8 src/main/java/com/su/flight/*.java -d out && java -cp out com.su.flight.Main ;;
    5) cd "$ROOT/Data Analysis-知乎热榜爬虫与NLP" && python main.py ;;
    6) cd "$ROOT/Data Analysis-机器学习项目" && python train.py ;;
    7) cd "$ROOT/Backend-图书管理系统" && mvn exec:java ;;
    8) cd "$ROOT/Backend-社区物业管理系统" && echo "启动 SpringBoot：http://localhost:8080" && mvn spring-boot:run ;;
    9) cd "$ROOT/Backend-智能招聘系统" && echo "启动 Jetty：http://localhost:8080/recruit/" && mvn jetty:run ;;
    10) cd "$ROOT/QA-知乎热榜数据清洗" && python data_clean.py ;;
    11) cd "$ROOT/Operations-知乎热榜NLP" && python content_insight.py ;;
    12) cd "$ROOT/Operations-智能招聘系统数据统计" && "$MYSQL" -u root -pyour_mysql_password --table < sql/stats.sql ;;
    13) cd "$ROOT/Operations-社区物业数据指标" && "$MYSQL" -u root -pyour_mysql_password --table < sql/metrics.sql ;;
    0) exit 0 ;;
    *) echo "无效输入，请重新选择" ;;
  esac
  echo
  read -r -p "按回车返回菜单..." _
done
