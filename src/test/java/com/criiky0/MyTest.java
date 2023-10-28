package com.criiky0;

import com.criiky0.utils.ElasticSearchUtil;
import com.criiky0.utils.JavaMailUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MyTest {

    @Autowired
    ElasticSearchUtil elasticSearchClientFactory;
    @org.junit.jupiter.api.Test
    public void test1() {
        Integer i = JavaMailUtil.sendCode("503094716@qq.com");
        System.out.println(i);
    }

    @org.junit.jupiter.api.Test
    public void test2() {
        System.out.println(System.getProperty("user.dir"));
    }

    @Test
    public void test4(){
        System.out.println(ElasticSearchUtil.client);
        System.out.println(ElasticSearchUtil.client);
        System.out.println(ElasticSearchUtil.client);
    }

}
