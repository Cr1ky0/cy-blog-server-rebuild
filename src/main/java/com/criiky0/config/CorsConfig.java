package com.criiky0.config;

import com.criiky0.utils.EnvironmentChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    private EnvironmentChecker environmentChecker;

    @Autowired
    public CorsConfig(EnvironmentChecker environmentChecker) {
        this.environmentChecker = environmentChecker;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        if(environmentChecker.isProduction()){
            registry.addMapping("/**")
                    .allowedOrigins("https://www.criiky0.top") // 不能为通配符，不然会报错
                    .allowedMethods("GET","POST","PUT","PATCH","DELETE")
                    .allowedHeaders("*")
                    .allowCredentials(true)
                    .maxAge(1800L);
        }
        else{
            registry.addMapping("/**")
                    .allowedOrigins("http://localhost:3000","http://localhost:3001") // 不能为通配符，不然会报错
                    .allowedMethods("GET","POST","PUT","PATCH","DELETE")
                    .allowedHeaders("*")
                    .allowCredentials(true)
                    .maxAge(1800L);
        }
    }
}
