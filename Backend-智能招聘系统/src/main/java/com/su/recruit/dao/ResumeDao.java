package com.su.recruit.dao;

import com.su.recruit.entity.Resume;
import com.su.recruit.util.DBUtil;

import java.sql.*;

/**
 * 简历数据访问层（JDBC）。
 */
public class ResumeDao {

    /** 按求职者查询简历 */
    public Resume findBySeeker(int seekerId) throws SQLException {
        String sql = "SELECT * FROM resume WHERE seeker_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, seekerId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    /**
     * 保存简历：已有则更新，没有则新增。返回简历 id。
     */
    public int save(Resume resume) throws SQLException {
        Resume exist = findBySeeker(resume.getSeekerId());
        if (exist != null) {
            updateContent(exist.getId(), resume.getContent());
            return exist.getId();
        }
        return insert(resume);
    }

    private int updateContent(int id, String content) throws SQLException {
        String sql = "UPDATE resume SET content = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, content);
            ps.setInt(2, id);
            return ps.executeUpdate();
        }
    }

    private int insert(Resume resume) throws SQLException {
        String sql = "INSERT INTO resume(seeker_id, content) VALUES (?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, resume.getSeekerId());
            ps.setString(2, resume.getContent());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        }
    }

    private Resume map(ResultSet rs) throws SQLException {
        Resume r = new Resume();
        r.setId(rs.getInt("id"));
        r.setSeekerId(rs.getInt("seeker_id"));
        r.setContent(rs.getString("content"));
        r.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return r;
    }
}