package com.su.recruit.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * JDBC 连接工具。
 * 连本地 MySQL（需先执行 sql/schema.sql 建库建表）。
 * 无 MySQL 环境时，可改用 H2 内嵌（见旧版注释）。
 */
public final class DBUtil {

    private static final String URL = "jdbc:mysql://localhost:3306/recruit"
            + "?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8";
    private static final String USER = "root";
    private static final String PASSWORD = "your_mysql_password";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("数据库驱动加载失败", e);
        }
    }

    private DBUtil() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
