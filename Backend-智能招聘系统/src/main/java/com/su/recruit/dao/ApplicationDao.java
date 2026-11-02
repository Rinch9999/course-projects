package com.su.recruit.dao;

import com.su.recruit.entity.Application;
import com.su.recruit.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 投递记录数据访问层（JDBC）。
 */
public class ApplicationDao {

    /** 投递简历 */
    public int add(Application app) throws SQLException {
        String sql = "INSERT INTO application(job_id, seeker_id, resume_id, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, app.getJobId());
            ps.setInt(2, app.getSeekerId());
            ps.setObject(3, app.getResumeId());
            ps.setString(4, app.getStatus());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        }
    }

    /** 更新投递状态（状态跟踪） */
    public int updateStatus(int id, String status) throws SQLException {
        String sql = "UPDATE application SET status = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate();
        }
    }

    /** 按求职者查询投递列表（状态跟踪） */
    public List<Application> listBySeeker(int seekerId) throws SQLException {
        String sql = "SELECT * FROM application WHERE seeker_id = ? ORDER BY created_at DESC";
        List<Application> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, seekerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        }
        return list;
    }

    /** 求职者投递列表（带岗位标题） */
    public List<Map<String, Object>> listBySeekerWithJob(int seekerId) throws SQLException {
        String sql = "SELECT a.id, a.job_id, j.title, a.status, a.created_at "
                + "FROM application a JOIN job j ON a.job_id = j.id "
                + "WHERE a.seeker_id = ? ORDER BY a.created_at DESC";
        List<Map<String, Object>> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, seekerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("id", rs.getInt("id"));
                    row.put("jobId", rs.getInt("job_id"));
                    row.put("jobTitle", rs.getString("title"));
                    row.put("status", rs.getString("status"));
                    row.put("createdAt", rs.getTimestamp("created_at"));
                    list.add(row);
                }
            }
        }
        return list;
    }

    /** 企业收到的投递列表（带岗位标题 + 求职者姓名） */
    public List<Map<String, Object>> listByEnterprise(int enterpriseId) throws SQLException {
        String sql = "SELECT a.id, a.job_id, j.title, s.name AS seeker_name, a.status, a.created_at "
                + "FROM application a "
                + "JOIN job j ON a.job_id = j.id "
                + "JOIN job_seeker s ON a.seeker_id = s.id "
                + "WHERE j.enterprise_id = ? ORDER BY a.created_at DESC";
        List<Map<String, Object>> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, enterpriseId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("id", rs.getInt("id"));
                    row.put("jobTitle", rs.getString("title"));
                    row.put("seekerName", rs.getString("seeker_name"));
                    row.put("status", rs.getString("status"));
                    row.put("createdAt", rs.getTimestamp("created_at"));
                    list.add(row);
                }
            }
        }
        return list;
    }

    private Application map(ResultSet rs) throws SQLException {
        Application a = new Application();
        a.setId(rs.getInt("id"));
        a.setJobId(rs.getInt("job_id"));
        a.setSeekerId(rs.getInt("seeker_id"));
        a.setResumeId(rs.getInt("resume_id"));
        a.setStatus(rs.getString("status"));
        a.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return a;
    }
}
