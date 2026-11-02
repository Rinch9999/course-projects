package com.su.library.entity;

import java.time.LocalDate;

/**
 * 借阅记录实体，对应表 borrow_record。
 */
public class BorrowRecord {
    private int id;
    private int bookId;
    private int readerId;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;   // null = 未归还

    public BorrowRecord() {}

    public BorrowRecord(int bookId, int readerId, LocalDate borrowDate, LocalDate dueDate) {
        this.bookId = bookId;
        this.readerId = readerId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public int getReaderId() { return readerId; }
    public void setReaderId(int readerId) { this.readerId = readerId; }

    public LocalDate getBorrowDate() { return borrowDate; }
    public void setBorrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    @Override
    public String toString() {
        return "BorrowRecord{id=" + id + ", bookId=" + bookId + ", readerId=" + readerId
                + ", borrowDate=" + borrowDate + ", dueDate=" + dueDate
                + ", returnDate=" + returnDate + "}";
    }
}
