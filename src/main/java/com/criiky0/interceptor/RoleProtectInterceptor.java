package com.criiky0.interceptor;

import com.alibaba.druid.util.StringUtils;
import com.criiky0.utils.JwtHelper;
import com.criiky0.utils.LoginProtectUtil;
import com.criiky0.utils.Result;
import com.criiky0.utils.ResultCodeEnum;
import jakarta.servlet.http.Cookie;
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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            // CORS预检放行
            if ("OPTIONS".equals(request.getMethod().toUpperCase())) {
                return true;
            }

            // cookie处理
            Cookie jwtCookie = LoginProtectUtil.getCookieByName(request, "token");
            // 未登录
            Result<Object> r = Result.build(null, ResultCodeEnum.NOT_LOGIN);
            if (jwtCookie == null) {
                LoginProtectUtil.writeToResponse(response, r);
                return false;
            }
            String jwt = jwtCookie.getValue();
            if (StringUtils.isEmpty(jwt) || jwtHelper.isExpiration(jwt)) {
                LoginProtectUtil.writeToResponse(response, r);
                return false;
            }

            // 验证token权限
            if (!jwtHelper.getUserRole(jwt).equals("admin")) {
                Result<Object> r1 = Result.build(null, ResultCodeEnum.ROLE_NOT_ALLOW);
                LoginProtectUtil.writeToResponse(response, r1);
                return false;
            }

            // 成功后解析用户id并放入头部返回给controller
            Long userId = jwtHelper.getUserId(jwt);
            request.setAttribute("userid", userId);
            return true;
        }catch (Exception e){
            Result.build(null,ResultCodeEnum.UNKNOWN_ERROR);
            return false;
        }
    }
}
