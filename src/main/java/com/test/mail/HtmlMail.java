package com.test.mail;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.util.Date;
import java.util.Properties;

/**
 * 纯文字的邮件改为html的邮件只需修改两个地方：
 * 1、把 text 内容设置为html
 * 2、新增方法 setHtmlContent
 */

public class HtmlMail {
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
        String text = "<html>" +
                "<head>" +
                "<meta http-equiv=\"content-type\" content=\"text/html;charset=utf-8\">" +
                "<title>测试html邮件发送</title>" +
                "</head>" +
                "<body>" +
                "<a href=\"https://www.cnblogs.com/Mike_Chang/\">我的网站</a>" +
                "</body>" +
                "</html>";
        try {
            Message message = getMessage(from, to, subject, text);
            // 发送邮件
            Transport.send(message);
            System.out.println("邮件发送成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Message getMessage(String from, String to, String subject, String text) throws MessagingException, IOException {
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
        // 要发送html邮件，只要把下一行替换为方法调用即可
        // message.setText(text);
        setHtmlContent(message, text);
        return message;
    }

    // 发送html内容
    public static void setHtmlContent(MimeMessage message, String text) throws MessagingException, IOException {
        // 创建可包括多重内容的邮件内容
        MimeMultipart multiPart = new MimeMultipart();
        // 代表html内容类型的对象
        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(text, "text/html;charset=UTF-8");
        // 新增html内容类型
        multiPart.addBodyPart(htmlPart);
        // 新增附件
        // 设定为邮件内容
        message.setContent(multiPart);
    }
}
