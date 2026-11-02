package com.su.library.entity;

/**
 * 图书实体，对应表 book。
 */
public class Book {
    public static final String STATUS_AVAILABLE = "AVAILABLE";
    public static final String STATUS_BORROWED = "BORROWED";

    private int id;
    private String title;
    private String author;
    private String isbn;
    private String status;

    public Book() {}

    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.status = STATUS_AVAILABLE;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Book{id=" + id + ", title='" + title + "', author='" + author
                + "', isbn='" + isbn + "', status=" + status + "}";
    }
}
