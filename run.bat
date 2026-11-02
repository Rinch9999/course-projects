@echo off
chcp 65001 >nul
setlocal EnableExtensions

rem 简历项目 · 一键运行（Windows）
rem 前置：已装好 JDK17 / Maven / Python3.12(命令为 python) / MySQL8（已在运行）

set "ROOT=%~dp0"
set "MYSQL=D:\MySQL 8.0.31\mysql-8.0.31-winx64\bin\mysql.exe"

:menu
cls
echo ==============================================
echo    简历项目 · 一键运行
echo ==============================================
echo   前端
echo    [1] 智能招聘系统前端 (demo.html)
echo    [2] 单词记忆卡片
echo    [3] 校园活动报名H5
echo   数据分析
echo    [4] 航班信息查询与检索 (Java)
echo    [5] 知乎热榜爬虫与NLP
echo    [6] 机器学习项目
echo   后端
echo    [7] 图书管理系统 (JDBC)
echo    [8] 社区物业管理系统 (SpringBoot)
echo    [9] 智能招聘系统 (Jetty)
echo   测试 / 运营
echo    [10] 知乎热榜数据清洗
echo    [11] 知乎热榜NLP选题建议
echo    [12] 招聘数据统计 (SQL)
echo    [13] 物业数据指标 (SQL)
echo    [0] 退出
echo ==============================================
set /p "choice=请输入序号后回车: "

if "%choice%"=="1"  goto front_recruit
if "%choice%"=="2"  goto front_words
if "%choice%"=="3"  goto front_h5
if "%choice%"=="4"  goto da_flight
if "%choice%"=="5"  goto da_zhihu
if "%choice%"=="6"  goto da_ml
if "%choice%"=="7"  goto be_library
if "%choice%"=="8"  goto be_property
if "%choice%"=="9"  goto be_recruit
if "%choice%"=="10" goto qa_clean
if "%choice%"=="11" goto op_nlp
if "%choice%"=="12" goto op_stats
if "%choice%"=="13" goto op_metrics
if "%choice%"=="0"  goto :eof
echo 无效输入，请重新选择
pause
goto menu

:front_recruit
start "" "%ROOT%Frontend-智能招聘系统前端\demo.html"
goto menu

:front_words
start "" "%ROOT%Frontend-单词记忆卡片\index.html"
goto menu

:front_h5
start "" "%ROOT%Frontend-校园活动报名H5\index.html"
goto menu

:da_flight
cd /d "%ROOT%Data Analysis-航班信息查询与检索"
echo 正在编译并运行航班查询...
javac -encoding UTF-8 src/main/java/com/su/flight/*.java -d out
java -cp out com.su.flight.Main
pause
goto menu

:da_zhihu
cd /d "%ROOT%Data Analysis-知乎热榜爬虫与NLP"
python main.py
pause
goto menu

:da_ml
cd /d "%ROOT%Data Analysis-机器学习项目"
python train.py
pause
goto menu

:be_library
cd /d "%ROOT%Backend-图书管理系统"
mvn exec:java
pause
goto menu

:be_property
cd /d "%ROOT%Backend-社区物业管理系统"
echo 启动 SpringBoot，浏览器访问 http://localhost:8080 （Ctrl+C 停止）
mvn spring-boot:run
pause
goto menu

:be_recruit
cd /d "%ROOT%Backend-智能招聘系统"
echo 启动 Jetty，浏览器访问 http://localhost:8080/recruit/ （Ctrl+C 停止）
mvn jetty:run
pause
goto menu

:qa_clean
cd /d "%ROOT%QA-知乎热榜数据清洗"
python data_clean.py
pause
goto menu

:op_nlp
cd /d "%ROOT%Operations-知乎热榜NLP"
python content_insight.py
pause
goto menu

:op_stats
cd /d "%ROOT%Operations-智能招聘系统数据统计"
"%MYSQL%" -u root -pyour_mysql_password --table < "sql\stats.sql"
pause
goto menu

:op_metrics
cd /d "%ROOT%Operations-社区物业数据指标"
"%MYSQL%" -u root -pyour_mysql_password --table < "sql\metrics.sql"
pause
goto menu
