package com.criiky0.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") // 不能为通配符，不然会报错
                .allowedMethods("GET","POST","PUT","PATCH","DELETE")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(1800L);
    }
}
