package com.criiky0.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.alibaba.druid.util.StringUtils;
import com.criiky0.utils.JwtHelper;
import com.criiky0.utils.Result;
import com.criiky0.utils.ResultCodeEnum;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class LoginProtectInterceptor implements HandlerInterceptor {

    private JwtHelper jwtHelper;

    @Autowired
    public LoginProtectInterceptor(JwtHelper jwtHelper) {
        this.jwtHelper = jwtHelper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if(StringUtils.isEmpty(token) || jwtHelper.isExpiration(token)){
            Result r = Result.build(null, ResultCodeEnum.NOT_LOGIN);
            ObjectMapper objectMapper = new ObjectMapper();
            String s = objectMapper.writeValueAsString(r);
            response.getWriter().print(s);
            return false;
        }
        return true;
    }
}
