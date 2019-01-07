package com.itheim.lucene.dao.impl;

import com.itheim.lucene.dao.BookDao;
import com.itheim.lucene.po.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 图书数据访问接口实现类
 */
public class BookDaoImpl implements BookDao {
    /**
     * 查询全部图书
     */
    @Override
    public List <Book> findAllBook() {
        // 创建List集合封装查询结果
        List <Book> list = new ArrayList <>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // 加载驱动
            Class.forName("com.mysql.jdbc.Driver");
            // 创建数据库连接对象
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/lucene_db", "root", "root");
            // 编写sql语句
            String sql = "select * from book";
            // 创建statement
            preparedStatement = connection.prepareStatement(sql);
            // 执行查询
            resultSet = preparedStatement.executeQuery();
            // 处理结果集
            while (resultSet.next()) {
                // 创建图书对象
                Book book = new Book();
                book.setId(resultSet.getInt("id"));
                book.setBookName(resultSet.getString("bookName"));
                book.setPrice(resultSet.getFloat("price"));
                book.setPic(resultSet.getString("pic"));
                book.setBookDesc(resultSet.getString("bookDesc"));
                list.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            try {
                resultSet.close();
                preparedStatement.close();
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
