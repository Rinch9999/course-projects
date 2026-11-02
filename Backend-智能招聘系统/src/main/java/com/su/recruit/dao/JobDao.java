package com.su.recruit.dao;

import com.su.recruit.entity.Job;
import com.su.recruit.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 岗位数据访问层（JDBC）。
 * 含「岗位热度排行」的复杂 SQL（多表关联 + 分组统计）。
 */
public class JobDao {

    /** 新增岗位 */
    public int add(Job job) throws SQLException {
        String sql = "INSERT INTO job(enterprise_id, category_id, title, description, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, job.getEnterpriseId());
            if (job.getCategoryId() == null) {
                ps.setNull(2, Types.INTEGER);
            } else {
                ps.setInt(2, job.getCategoryId());
            }
            ps.setString(3, job.getTitle());
            ps.setString(4, job.getDescription());
            ps.setString(5, job.getStatus());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        }
    }

    /** 按 id 查询岗位 */
    public Job findById(int id) throws SQLException {
        String sql = "SELECT * FROM job WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    /** 岗位列表（条件搜索） */
    public List<Job> findByTitle(String keyword) throws SQLException {
        String sql = "SELECT * FROM job WHERE title LIKE ? ORDER BY created_at DESC";
        List<Job> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + (keyword == null ? "" : keyword) + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        }
        return list;
    }

    /**
     * 难点突破：岗位热度排行。
     * 用 LEFT JOIN 关联投递表 + GROUP BY 分组统计，按投递数降序。
     */
    public List<Map<String, Object>> listHotJobs(int limit) throws SQLException {
        String sql = "SELECT j.id, j.title, COUNT(a.id) AS apply_count "
                + "FROM job j "
                + "LEFT JOIN application a ON a.job_id = j.id "
                + "GROUP BY j.id, j.title "
                + "ORDER BY apply_count DESC "
                + "LIMIT ?";
        List<Map<String, Object>> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("jobId", rs.getInt("id"));
                    row.put("title", rs.getString("title"));
                    row.put("applyCount", rs.getLong("apply_count"));
                    list.add(row);
                }
            }
        }
        return list;
    }

    private Job map(ResultSet rs) throws SQLException {
        Job j = new Job();
        j.setId(rs.getInt("id"));
        j.setEnterpriseId(rs.getInt("enterprise_id"));
        j.setCategoryId(rs.getInt("category_id"));
        j.setTitle(rs.getString("title"));
        j.setDescription(rs.getString("description"));
        j.setStatus(rs.getString("status"));
        return j;
    }
}
