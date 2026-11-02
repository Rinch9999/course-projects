-- 图书管理系统建表脚本（MySQL）
-- 三张表：图书 book、读者 reader、借阅记录 borrow_record

CREATE DATABASE IF NOT EXISTS library DEFAULT CHARACTER SET utf8mb4;
USE library;

-- 图书表
CREATE TABLE book (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    title    VARCHAR(128) NOT NULL,
    author   VARCHAR(64)  NOT NULL,
    isbn     VARCHAR(32)  UNIQUE,
    status   VARCHAR(16)  NOT NULL DEFAULT 'AVAILABLE'  -- AVAILABLE / BORROWED
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 读者表
CREATE TABLE reader (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(64) NOT NULL,
    phone      VARCHAR(20),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 借阅记录表（核心：借还书的业务数据）
CREATE TABLE borrow_record (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    book_id     INT NOT NULL,
    reader_id   INT NOT NULL,
    borrow_date DATE NOT NULL,
    due_date    DATE NOT NULL,
    return_date DATE,                                     -- NULL 表示未归还
    CONSTRAINT fk_borrow_book   FOREIGN KEY (book_id)   REFERENCES book(id),
    CONSTRAINT fk_borrow_reader FOREIGN KEY (reader_id) REFERENCES reader(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
