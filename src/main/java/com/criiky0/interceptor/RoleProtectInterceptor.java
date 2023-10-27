package com.criiky0.interceptor;

import com.alibaba.druid.util.StringUtils;
import com.criiky0.utils.JwtHelper;
import com.criiky0.utils.Result;
import com.criiky0.utils.ResultCodeEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RoleProtectInterceptor implements HandlerInterceptor {
    private JwtHelper jwtHelper;

    @Autowired
    public RoleProtectInterceptor(JwtHelper jwtHelper) {
        this.jwtHelper = jwtHelper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String bearer = request.getHeader("Authorization");

        // 验证token是否存在
        Result<Object> r = Result.build(null, ResultCodeEnum.NOT_LOGIN);
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(r);
        if (StringUtils.isEmpty(bearer)) {
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(s);
            return false;
        }

        // 验证token是否过期
        String token = bearer.trim().replaceFirst("^Bearer\\s+", "");
        if (StringUtils.isEmpty(token) || jwtHelper.isExpiration(token)) {
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(s);
            return false;
        }

        // 验证token权限
        System.out.println(jwtHelper.getUserRole(token));
        if (!jwtHelper.getUserRole(token).equals("admin")) {
            Result<Object> r1 = Result.build(null, ResultCodeEnum.ROLE_NOT_ALLOW);
            ObjectMapper objectMapper1 = new ObjectMapper();
            String s1 = objectMapper1.writeValueAsString(r1);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(s1);
            return false;
        }

        // 成功后解析用户id并放入头部返回给controller
        Long userId = jwtHelper.getUserId(token);
        request.setAttribute("userid", userId);
        return true;
    }
}
