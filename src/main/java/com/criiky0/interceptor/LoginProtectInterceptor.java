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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {

        // CORS预检放行
        if("OPTIONS".equals(request.getMethod().toUpperCase())) {
            return true;
        }
        String bearer = request.getHeader("Authorization");

        // 错误相应信息
        Result<Object> r = Result.build(null, ResultCodeEnum.NOT_LOGIN);
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(r);
        if (StringUtils.isEmpty(bearer)) {
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(s);
            return false;
        }
        String token = bearer.trim().replaceFirst("^Bearer\\s+", "");
        if (StringUtils.isEmpty(token) || jwtHelper.isExpiration(token)) {
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(s);
            return false;
        }

        // 成功后解析用户id并放入头部返回给controller
        Long userId = jwtHelper.getUserId(token);
        request.setAttribute("userid", userId);
        return true;
    }
}
