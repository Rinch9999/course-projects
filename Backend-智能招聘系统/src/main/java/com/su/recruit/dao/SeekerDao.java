package com.su.recruit.dao;

import com.su.recruit.entity.Seeker;
import com.su.recruit.util.DBUtil;

import java.sql.*;

/**
 * 求职者数据访问层（JDBC）。
 */
public class SeekerDao {

    public int insert(Seeker seeker) throws SQLException {
        String sql = "INSERT INTO job_seeker(user_id, name, phone, education) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, seeker.getUserId());
            ps.setString(2, seeker.getName());
            ps.setString(3, seeker.getPhone());
            ps.setString(4, seeker.getEducation());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        }
    }

    public Seeker findByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM job_seeker WHERE user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    private Seeker map(ResultSet rs) throws SQLException {
        Seeker s = new Seeker();
        s.setId(rs.getInt("id"));
        s.setUserId(rs.getInt("user_id"));
        s.setName(rs.getString("name"));
        s.setPhone(rs.getString("phone"));
        s.setEducation(rs.getString("education"));
        return s;
    }
}