package com.su.library.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * JDBC 连接工具。
 * 连本地 MySQL（需先执行 sql/schema.sql 建库建表）。
 * 无 MySQL 环境时，可改用 H2 内嵌：URL 换成 jdbc:h2:mem:library;DB_CLOSE_DELAY=-1;MODE=MySQL，
 * 驱动换成 org.h2.Driver，并在启动时建表。
 */
public final class DBUtil {

    private static final String URL = "jdbc:mysql://localhost:3306/library"
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
