package com.criiky0.utils;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.alibaba.druid.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "jwt.token")
public class JwtHelper {

    private long tokenExpiration; // 有效时间,单位毫秒 1000毫秒 == 1秒
    private static final SecretKey KEY = Jwts.SIG.HS512.key().build(); // 当前程序签名秘钥（直接生成，每次重启服务器都会改变）

    // 生成token字符串
    public String createToken(Long userId, String userRole) {
        return Jwts.builder().subject("YYGH-USER")
            .expiration(new Date(System.currentTimeMillis() + tokenExpiration * 1000 * 60)).claim("userId", userId)
            .claim("userRole", userRole).signWith(KEY).compressWith(Jwts.ZIP.GZIP).compact();
    }

    // 从token字符串获取userid
    public Long getUserId(String token) {
        if (StringUtils.isEmpty(token) || isExpiration(token))
            return null;

        Jws<Claims> claimsJws = Jwts.parser().verifyWith(KEY).build().parseSignedClaims(token);
        Claims claims = claimsJws.getPayload();

        // 如果userId长度不够会被自动转为Integer类型，这里兼容一下
        Object userId = claims.get("userId");
        if (userId instanceof Integer) {
            return ((Integer)userId).longValue();
        }
        return (Long)userId;
    }

    // 从token字符串中获取userRole
    public String getUserRole(String token){
        if (StringUtils.isEmpty(token) || isExpiration(token))
            return null;

        Jws<Claims> claimsJws = Jwts.parser().verifyWith(KEY).build().parseSignedClaims(token);
        Claims claims = claimsJws.getPayload();

        return claims.get("userRole").toString();
    }

    // 判断token是否有效
    public boolean isExpiration(String token) {
        try {
            // 没有过期，有效，返回false，过期返回true
            return Jwts.parser().verifyWith(KEY).build().parseSignedClaims(token).getPayload().getExpiration()
                .before(new Date());
        } catch (Exception e) {
            // 过期出现异常，返回true
            return true;
        }
    }

    // 返回到期时间
    public Long getExpiration(String token){
        try {
            // 没有过期，有效，返回false，过期返回true
            Date expiration = Jwts.parser().verifyWith(KEY).build().parseSignedClaims(token).getPayload().getExpiration();
            return expiration.getTime();
        } catch (Exception e) {
            // 出现异常则返回当前时间
            return System.currentTimeMillis();
        }
    }
}