package com.mail.test;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

/**
 * 访问地址：http://127.0.0.1/email.jsp
 */

@WebServlet("/mail.do")
public class MailServlet extends HttpServlet {

    private Properties properties;
    // 用户名没有@符号以及后面的内容
    private String username = "1379057501";
    // qq邮箱这里要使用授权码
    private String password = "nubkttfibrvkjjeb";

    @Override
    public void init() {
        properties = new Properties();
        properties.put("mail.smtp.host", "smtp.qq.com");
        properties.setProperty("mail.smtp.port", "465");
        properties.setProperty("mail.smtp.socketFactory.port", "465");
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.smtp.socketFactory.fallback", "false");
        properties.setProperty("mail.smtp.auth", "true");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String from = request.getParameter("from");
        String to = request.getParameter("to");
        String subject = request.getParameter("subject");
        String text = request.getParameter("text");
        try {
            Message message = getMessage(from, to, subject, text);
            // 发送邮件
            Transport.send(message);
            response.getWriter().println("邮件发送成功");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private Message getMessage(String from, String to, String subject, String text) throws MessagingException {
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        message.setSentDate(new Date());
        message.setText(text);
        return message;
    }

}
