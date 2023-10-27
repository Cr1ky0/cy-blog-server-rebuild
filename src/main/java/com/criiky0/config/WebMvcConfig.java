package com.criiky0.config;

import com.criiky0.interceptor.RoleProtectInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.criiky0.interceptor.LoginProtectInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private LoginProtectInterceptor loginProtectInterceptor;
    private RoleProtectInterceptor roleProtectInterceptor;

    @Autowired
    public WebMvcConfig(LoginProtectInterceptor loginProtectInterceptor,RoleProtectInterceptor roleProtectInterceptor) {
        this.loginProtectInterceptor = loginProtectInterceptor;
        this.roleProtectInterceptor = roleProtectInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginProtectInterceptor)
                .addPathPatterns("/api/user/info","/api/user/avatar")
                .excludePathPatterns("");

        registry.addInterceptor(roleProtectInterceptor)
                .addPathPatterns("/api/user/role")
                .excludePathPatterns("");
    }
}
