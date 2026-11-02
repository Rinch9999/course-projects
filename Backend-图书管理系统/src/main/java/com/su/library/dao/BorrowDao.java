package com.su.library.dao;

import com.su.library.entity.Book;
import com.su.library.entity.BorrowRecord;
import com.su.library.util.DBUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 借阅数据访问层（JDBC）。
 *
 * 难点突破：借书/还书涉及「借阅记录写入 + 图书状态更新」两步，
 * 必须放在同一个事务里，否则并发借阅会导致数据不一致
 * （例如一本书被两个人同时借走）。
 */
public class BorrowDao {

    /**
     * 借书（事务）：
     * 1. 校验图书存在且状态为 AVAILABLE；
     * 2. 写入借阅记录；
     * 3. 把图书状态改为 BORROWED。
     */
    public void borrow(int bookId, int readerId, int borrowDays) throws SQLException {
        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false); // 开启事务
            try {
                // 1. 校验图书可借（加锁，防止并发借同一本）
                String checkSql = "SELECT status FROM book WHERE id = ? FOR UPDATE";
                String status;
                try (PreparedStatement check = conn.prepareStatement(checkSql)) {
                    check.setInt(1, bookId);
                    try (ResultSet rs = check.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException("图书不存在：id=" + bookId);
                        }
                        status = rs.getString("status");
                    }
                }
                if (!Book.STATUS_AVAILABLE.equals(status)) {
                    throw new SQLException("图书不可借，当前状态：" + status);
                }

                // 2. 写入借阅记录
                String insertSql = "INSERT INTO borrow_record(book_id, reader_id, borrow_date, due_date) VALUES (?, ?, ?, ?)";
                try (PreparedStatement insert = conn.prepareStatement(insertSql)) {
                    insert.setInt(1, bookId);
                    insert.setInt(2, readerId);
                    insert.setDate(3, Date.valueOf(LocalDate.now()));
                    insert.setDate(4, Date.valueOf(LocalDate.now().plusDays(borrowDays)));
                    insert.executeUpdate();
                }

                // 3. 更新图书状态
                String updateSql = "UPDATE book SET status = ? WHERE id = ?";
                try (PreparedStatement update = conn.prepareStatement(updateSql)) {
                    update.setString(1, Book.STATUS_BORROWED);
                    update.setInt(2, bookId);
                    update.executeUpdate();
                }

                conn.commit(); // 全部成功才提交
            } catch (SQLException e) {
                conn.rollback(); // 任一步失败都回滚
                throw e;
            }
        }
    }

    /**
     * 还书（事务）：
     * 1. 找到该书未归还的借阅记录；
     * 2. 写入归还日期；
     * 3. 把图书状态改回 AVAILABLE。
     */
    public void returnBook(int bookId) throws SQLException {
        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);
            try {
                String updateRecord = "UPDATE borrow_record SET return_date = ? "
                        + "WHERE book_id = ? AND return_date IS NULL";
                try (PreparedStatement ps = conn.prepareStatement(updateRecord)) {
                    ps.setDate(1, Date.valueOf(LocalDate.now()));
                    ps.setInt(2, bookId);
                    ps.executeUpdate();
                }

                String updateBook = "UPDATE book SET status = ? WHERE id = ?";
                try (PreparedStatement ps = conn.prepareStatement(updateBook)) {
                    ps.setString(1, Book.STATUS_AVAILABLE);
                    ps.setInt(2, bookId);
                    ps.executeUpdate();
                }

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    /** 查询某读者未归还的记录（可扩展为逾期统计） */
    public List<BorrowRecord> listUnreturnedByReader(int readerId) throws SQLException {
        String sql = "SELECT * FROM borrow_record WHERE reader_id = ? AND return_date IS NULL";
        List<BorrowRecord> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, readerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        }
        return list;
    }

    /**
     * 逾期统计：查询所有「应还日期已过但仍未归还」的借阅记录。
     * 难点突破：逾期判定用 due_date < CURDATE()，未归还用 return_date IS NULL。
     */
    public List<BorrowRecord> listOverdue() throws SQLException {
        String sql = "SELECT * FROM borrow_record "
                + "WHERE return_date IS NULL AND due_date < CURDATE() "
                + "ORDER BY due_date ASC";
        List<BorrowRecord> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
        }
        return list;
    }

    private BorrowRecord map(ResultSet rs) throws SQLException {
        BorrowRecord r = new BorrowRecord();
        r.setId(rs.getInt("id"));
        r.setBookId(rs.getInt("book_id"));
        r.setReaderId(rs.getInt("reader_id"));
        r.setBorrowDate(rs.getDate("borrow_date").toLocalDate());
        r.setDueDate(rs.getDate("due_date").toLocalDate());
        Date returnDate = rs.getDate("return_date");
        r.setReturnDate(returnDate == null ? null : returnDate.toLocalDate());
        return r;
    }
}
