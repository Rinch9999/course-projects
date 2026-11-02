package com.su.library.dao;

import com.su.library.entity.Book;
import com.su.library.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 图书数据访问层（JDBC）。
 * 实现图书的增删改查。
 */
public class BookDao {

    /** 新增图书 */
    public int add(Book book) throws SQLException {
        String sql = "INSERT INTO book(title, author, isbn, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            ps.setString(4, book.getStatus());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        }
    }

    /** 按 id 查询 */
    public Book findById(int id) throws SQLException {
        String sql = "SELECT * FROM book WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    /** 按书名/作者模糊搜索 */
    public List<Book> findByTitle(String keyword) throws SQLException {
        String sql = "SELECT * FROM book WHERE title LIKE ? OR author LIKE ? ORDER BY id DESC";
        String kw = "%" + (keyword == null ? "" : keyword.trim()) + "%";
        List<Book> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kw);
            ps.setString(2, kw);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        }
        return list;
    }

    /** 查询全部图书 */
    public List<Book> findAll() throws SQLException {
        String sql = "SELECT * FROM book";
        List<Book> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
        }
        return list;
    }

    /** 更新图书信息 */
    public int update(Book book) throws SQLException {
        String sql = "UPDATE book SET title = ?, author = ?, isbn = ?, status = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            ps.setString(4, book.getStatus());
            ps.setInt(5, book.getId());
            return ps.executeUpdate();
        }
    }

    /** 删除图书 */
    public int delete(int id) throws SQLException {
        String sql = "DELETE FROM book WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        }
    }

    private Book map(ResultSet rs) throws SQLException {
        Book b = new Book();
        b.setId(rs.getInt("id"));
        b.setTitle(rs.getString("title"));
        b.setAuthor(rs.getString("author"));
        b.setIsbn(rs.getString("isbn"));
        b.setStatus(rs.getString("status"));
        return b;
    }
}
