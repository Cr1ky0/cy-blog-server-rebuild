package com.criiky0;


import com.criiky0.utils.JavaMailUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class test {


    @Test
    public void test1() {
        Integer i = JavaMailUtil.sendCode("503094716@qq.com");
        System.out.println(i);
    }

    @Test
    public void test2(){

    }
}
