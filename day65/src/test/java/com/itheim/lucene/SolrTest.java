package com.itheim.lucene;

import com.itheim.lucene.po.Product;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.junit.Test;

public class SolrTest {
    /**
     * 定义SolrServer，用来操作Solr
     */
    private SolrServer solrServer = new HttpSolrServer("http://localhost:8080/solr/collection1");

    /** 添加或修改索引 */
    @Test
    public void saveorUpdate() throws Exception {
        Product product = new Product();
        product.setPid("8000");
        product.setName("iphone8");
        product.setCatalogName("手机");
        product.setPrice(8000);
        product.setDescription("苹果手机还不错哦!!!!!!!!");
        product.setPicture("1.jpg");
        solrServer.addBean(product);
        // 提交事务
        solrServer.commit();
    }
}
