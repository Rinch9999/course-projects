package com.su.library;

import com.su.library.dao.BookDao;
import com.su.library.dao.BorrowDao;
import com.su.library.dao.ReaderDao;
import com.su.library.entity.Book;
import com.su.library.entity.BorrowRecord;
import com.su.library.entity.Reader;

import java.util.List;

/**
 * 演示入口：跑通图书管理系统的完整主流程。
 * 新增图书/读者 → 借书（事务，加锁防并发）→ 还书（事务）→ 搜索 → 逾期统计 → 读者信息更新。
 */
public class Main {
    public static void main(String[] args) throws Exception {
        BookDao bookDao = new BookDao();
        ReaderDao readerDao = new ReaderDao();
        BorrowDao borrowDao = new BorrowDao();

        // 1. 新增图书与读者
        int javaId = bookDao.add(new Book("Java 核心技术", "Cay S. Horstmann", "9787111636663"));
        int mysqlId = bookDao.add(new Book("高性能 MySQL", "施瓦茨", "9787121400384"));
        int readerId = readerDao.add(new Reader("苏然", "15604760898"));
        System.out.println("新增图书 id=" + javaId + "/" + mysqlId + "，读者 id=" + readerId);

        // 2. 借书（难点：JDBC 事务，写借阅记录 + 更新图书状态，加锁防并发借同一本）
        //    借期 -1 天用于制造「逾期未还」演示数据
        borrowDao.borrow(javaId, readerId, -1);
        borrowDao.borrow(mysqlId, readerId, 30);
        System.out.println("借书后：" + bookDao.findById(javaId));

        // 3. 还书（难点：事务，写归还日期 + 恢复图书状态）
        borrowDao.returnBook(mysqlId);
        System.out.println("还书后（MySQL 已还）：" + bookDao.findById(mysqlId));

        // 4. 书名/作者搜索
        System.out.println("搜索「Java」：" + bookDao.findByTitle("Java"));

        // 5. 逾期统计（难点：return_date IS NULL 且 due_date 已过）
        List<BorrowRecord> overdue = borrowDao.listOverdue();
        System.out.println("逾期未还记录数：" + overdue.size() + " → " + overdue);

        // 6. 更新读者信息
        Reader reader = readerDao.findById(readerId);
        reader.setPhone("13800138000");
        readerDao.update(reader);
        System.out.println("更新后读者：" + readerDao.findById(readerId));

        // 7. 全部图书
        System.out.println("当前图书列表：" + bookDao.findAll());
    }
}