package com.su.library.dao;

import com.su.library.entity.Reader;
import com.su.library.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 读者数据访问层（JDBC）。
 * 实现读者的增删改查。
 */
public class ReaderDao {

    public int add(Reader reader) throws SQLException {
        String sql = "INSERT INTO reader(name, phone) VALUES (?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, reader.getName());
            ps.setString(2, reader.getPhone());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        }
    }

    public Reader findById(int id) throws SQLException {
        String sql = "SELECT * FROM reader WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public List<Reader> findAll() throws SQLException {
        String sql = "SELECT * FROM reader";
        List<Reader> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
        }
        return list;
    }

    public int update(Reader reader) throws SQLException {
        String sql = "UPDATE reader SET name = ?, phone = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, reader.getName());
            ps.setString(2, reader.getPhone());
            ps.setInt(3, reader.getId());
            return ps.executeUpdate();
        }
    }

    public int delete(int id) throws SQLException {
        String sql = "DELETE FROM reader WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        }
    }

    private Reader map(ResultSet rs) throws SQLException {
        Reader r = new Reader();
        r.setId(rs.getInt("id"));
        r.setName(rs.getString("name"));
        r.setPhone(rs.getString("phone"));
        r.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return r;
    }
}
