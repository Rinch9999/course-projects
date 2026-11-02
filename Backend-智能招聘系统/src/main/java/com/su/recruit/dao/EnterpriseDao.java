package com.su.recruit.dao;

import com.su.recruit.entity.Enterprise;
import com.su.recruit.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 企业数据访问层（JDBC）。
 */
public class EnterpriseDao {

    public int insert(Enterprise e) throws SQLException {
        String sql = "INSERT INTO enterprise(user_id, name, industry, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, e.getUserId());
            ps.setString(2, e.getName());
            ps.setString(3, e.getIndustry());
            ps.setString(4, e.getStatus());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        }
    }

    public Enterprise findByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM enterprise WHERE user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    /** 待审核企业列表 */
    public List<Enterprise> listPending() throws SQLException {
        String sql = "SELECT * FROM enterprise WHERE status = ? ORDER BY id ASC";
        List<Enterprise> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, Enterprise.STATUS_PENDING);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        }
        return list;
    }

    /** 更新企业审核状态 */
    public int updateStatus(int id, String status) throws SQLException {
        String sql = "UPDATE enterprise SET status = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate();
        }
    }

    /** 写入企业审核日志 */
    public int insertAudit(int enterpriseId, String status) throws SQLException {
        String sql = "INSERT INTO enterprise_audit(enterprise_id, status, audit_time) VALUES (?, ?, NOW())";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, enterpriseId);
            ps.setString(2, status);
            return ps.executeUpdate();
        }
    }

    private Enterprise map(ResultSet rs) throws SQLException {
        Enterprise e = new Enterprise();
        e.setId(rs.getInt("id"));
        e.setUserId(rs.getInt("user_id"));
        e.setName(rs.getString("name"));
        e.setIndustry(rs.getString("industry"));
        e.setStatus(rs.getString("status"));
        return e;
    }
}