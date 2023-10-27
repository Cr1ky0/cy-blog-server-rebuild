package com.criiky0;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CharacterEncodingFilter;

@SpringBootApplication
@MapperScan("com.criiky0.mapper")
public class SpringApplicationRunner {
    public static void main(String[] args) {
        SpringApplication.run(SpringApplicationRunner.class,args);
    }
}
