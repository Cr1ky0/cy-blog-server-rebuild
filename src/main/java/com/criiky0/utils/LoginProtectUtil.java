package com.criiky0.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginProtectUtil {

    // 获取指定cookie
    public static Cookie getCookieByName(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie; // 返回指定名称的 Cookie
                }
            }
        }

        return null; // 没有找到指定名称的 Cookie
    }

    // 将Result写入Response
    public static void writeToResponse(HttpServletResponse response, Result<Object> result){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String s = objectMapper.writeValueAsString(result);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(s);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
