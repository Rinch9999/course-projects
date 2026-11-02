package com.su.recruit.dao;

import com.su.recruit.entity.User;
import com.su.recruit.util.DBUtil;

import java.sql.*;

/**
 * 账号数据访问层（JDBC）。
 * 登录校验 + 注册。
 *
 * 安全说明：生产环境密码必须用 BCrypt 等加盐哈希存储，此处为演示保持明文比较，
 * 所有 SQL 均已使用 PreparedStatement 防注入。
 */
public class UserDao {

    /** 按用户名查询账号 */
    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM user WHERE username = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    /** 注册新账号 */
    public int insert(User user) throws SQLException {
        String sql = "INSERT INTO user(username, password, role) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        }
    }

    private User map(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        u.setRole(rs.getString("role"));
        return u;
    }
}