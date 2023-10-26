package com.criiky0.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.Random;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Component
public class JavaMailUtil {

    private static String port;
    private static String host;
    private static String username;
    private static String password;

    @Value("${mail.port}")
    public void setPort(String port) {
        JavaMailUtil.port = port;
    }

    @Value("${mail.host}")
    public void setHost(String host) {
        JavaMailUtil.host = host;
    }

    @Value("${mail.username}")
    public void setUsername(String username) {
        JavaMailUtil.username = username;
    }

    @Value("${mail.password}")
    public void setPassword(String password) {
        JavaMailUtil.password = password;
    }

    private static Integer codeGenerator() {
        Random random = new Random();

        // 生成一个6位整数，范围从100000到999999
        return 100000 + random.nextInt(900000);
    }

    private static Session createSession() {
        // 创建一个配置文件，并保存
        Properties props = new Properties();

        // SMTP服务器连接信息
        props.put("mail.smtp.host", JavaMailUtil.host);// SMTP主机名
        props.put("mail.smtp.port", JavaMailUtil.port);// 主机端口号
        props.put("mail.smtp.auth", "true");// 是否需要用户认证
        props.put("mail.smtp.starttls.enale", "true");// 启用TlS加密

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(JavaMailUtil.username, JavaMailUtil.password);
            }
        });

        // 控制台打印调试信息
        session.setDebug(true);
        return session;
    }

    public static Integer sendCode(String email) {
        // 创建Session会话
        Session session = JavaMailUtil.createSession();

        // 生成6位数字
        Integer code = JavaMailUtil.codeGenerator();

        // 创建邮件对象
        MimeMessage message = new MimeMessage(session);
        try {
            message.setSubject("criik-blog验证码");
            message.setText("验证码为：" + code);
            message.setFrom(new InternetAddress("sklin552@sina.com"));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
            // 发送
            Transport.send(message);
            return code;
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}