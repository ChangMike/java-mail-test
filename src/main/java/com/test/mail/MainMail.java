package com.test.mail;

import java.util.Date;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MainMail {

    private static Properties properties;
    // 用户名没有@符号以及后面的内容
    private static String username = "1379057501";
    // qq邮箱这里要使用授权码
    private static String password = "nubkttfibrvkjjeb";

    public static void init() {
        properties = new Properties();
        properties.put("mail.smtp.host", "smtp.qq.com");
        properties.setProperty("mail.smtp.port", "465");
        properties.setProperty("mail.smtp.socketFactory.port", "465");
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.smtp.socketFactory.fallback", "false");
        properties.setProperty("mail.smtp.auth", "true");
    }

    public static void main(String[] args) {
        init();
        String from = "1379057501@qq.com";
        String to = "zhangrunwei@aliyun.com";
        String subject = "测试";
        String text = "这是一封测试邮件！";
        try {
            Message message = getMessage(from, to, subject, text);
            // 发送邮件
            Transport.send(message);
            System.out.println("邮件发送成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Message getMessage(String from, String to, String subject, String text) throws MessagingException {
        // 创建邮件会话对象
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        // 创建邮件信息
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        message.setSentDate(new Date());
        message.setText(text);
        return message;
    }


}
