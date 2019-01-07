package com.itheim.lucene;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;

public class QueryTest {
    /**
     * TermQuery关键词查询
     * 需求：查询图书名称域中包含有java的图书。
     */
    @Test
    public void testTermQuery() throws Exception {
        // 创建查询对象
        //public TermQuery(Term t)
        //public Term(String fld, String text)
        Query query = new TermQuery(new Term("bookName", "java"));
        // 执行搜索
        search(query);
    }

    /**
     * NumericRangeQuery数值范围查询
     * 需求：查询图书价格在80到100之间的图书
     */
    @Test
    public void testNumericrRangeQuery() throws Exception {
        // 创建查询对象
        /**
         * 参数说明
         *  field：域的名称
         *  min：最小范围边界值
         *  max：最大范围边界值
         *  minInclusive:是否包含最小边界值
         *  maxInclusive:是否包含最大边界值
         */
        //Query query = NumericRangeQuery.newDoubleRange("price", 80d, 100d, false, false);
        //Query query = NumericRangeQuery.newDoubleRange("price", 80d, 100d, true, false);
        //Query query = NumericRangeQuery.newDoubleRange("price", 80d, 100d, false, true);
        Query query = NumericRangeQuery.newDoubleRange("price", 80d, 100d, true, true);
        // 执行搜索
        search(query);
    }

    /**
     * BooleanQuery布尔查询
     * 需求：查询图书名称域中包含有java的图书，并且价格在80到100之间（包含边界值）。
     */
    @Test
    public void testBooleanQuery() throws Exception {
        // 创建查询对象一
        Query query1 = new TermQuery(new Term("bookName", "java"));
        // 创建查询对象二
        Query query2 = NumericRangeQuery.newDoubleRange("price", 80d, 100d, true, true);
        // 创建组合查询条件对象
        BooleanQuery booleanQuery = new BooleanQuery();
        booleanQuery.add(query1, BooleanClause.Occur.MUST);
        booleanQuery.add(query2, BooleanClause.Occur.MUST);
        // 执行搜索
        search(booleanQuery);
    }

    /**
     * 使用QueryParser
     * 需求：查询图书名称域中包含有java，并且图书名称域中包含有lucene的图书
     */
    @Test
    public void testQueryParser() throws Exception {
        // 创建分析器，用于分词
        Analyzer analyzer = new IKAnalyzer();
        // 创建QueryParser解析对象
        //public QueryParser(String f, Analyzer a)
        QueryParser queryParser = new QueryParser("bookName", analyzer);
        //public Query parse(String query)
        // 解析表达式，创建Query对象
        // +bookName:java +bookName:lucene
        //Query query = queryParser.parse("bookName:java  bookName:lucene");
        //Query query = queryParser.parse("bookName:java OR bookName:lucene");
        //Query query = queryParser.parse("bookName:java - bookName:lucene");
        //Query query = queryParser.parse("bookName:java not bookName:lucene");
        //Query query = queryParser.parse("+ bookName:java + bookName:lucene");
        Query query = queryParser.parse("bookName:java AND bookName:lucene");
        // 执行搜索
        search(query);
    }


    /**
     * 定义搜索方法
     */
    public void search(Query query) throws Exception {
        // 查询语法
        System.out.println("查询语法：" + query);
        // 创建索引库存储目录
        //public abstract class FSDirectory extends BaseDirectory
        Directory directory = FSDirectory.open(new File("E:\\lucene\\Index_Directory(索引仓库)"));
        // 创建IndexReader读取索引库对象
        //public static DirectoryReader open(final Directory directory)
        IndexReader indexReader = DirectoryReader.open(directory);
        // 创建IndexSearcher，执行搜索索引库
        //public IndexSearcher(IndexReader r)
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        /**
         * search方法：执行搜索
         * 参数一：查询对象
         * 参数二：指定搜索结果排序后的前n个（前10个）
         */
        //public class IndexSearcher
        //public TopDocs search(Query query, int n)
        TopDocs topDocs = indexSearcher.search(query, 10);
        // 处理结果集
        System.out.println("总命中的记录数：" + topDocs.totalHits);
        // 获取搜索到得文档数组
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        // ScoreDoc对象：只有文档id和分值信息
        for (ScoreDoc scoreDoc : scoreDocs) {
            System.out.println("-------华丽分割线----------");
            System.out.println("文档id: " + scoreDoc.doc + "\t文档分值：" + scoreDoc.score);
            Document doc = indexSearcher.doc(scoreDoc.doc);
            System.out.println("图书Id：" + doc.get("id"));
            System.out.println("图书名称：" + doc.get("bookName"));
            System.out.println("图书价格：" + doc.get("price"));
            System.out.println("图书图片：" + doc.get("pic"));
            System.out.println("图书描述：" + doc.get("bookDesc"));
        }
        // 释放资源
        indexReader.close();
    }
}
