package com.itheim.lucene.dao;

import com.itheim.lucene.po.Book;

import java.util.List;

/**
 * 图书数据访问接口
 */
public interface BookDao {
    /**
     * 查询全部图书
     */
    List <Book> findAllBook();
}
