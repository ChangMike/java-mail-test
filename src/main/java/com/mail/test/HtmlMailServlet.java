package com.mail.test;

import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

/**
 * 纯文字的邮件改为html的邮件只需修改两个地方：
 * 1、把 text 内容设置为html
 * 2、新增方法 setHtmlContent
 *
 * 访问地址：http://127.0.0.1/email.jsp
 */

@WebServlet("/mail.do")
@MultipartConfig // 文件上传
public class HtmlMailServlet extends HttpServlet {
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
        Part part = request.getPart("file");
        try {
            Message message = (Message) getMessage(from, to, subject, text, part);
            // 发送邮件
            Transport.send(message);
            response.getWriter().println("邮件发送成功");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private Message getMessage(String from, String to, String subject, String text, Part part) throws MessagingException, IOException {
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
        // 要发送html邮件，只要把下一行替换为方法调用即可
        // message.setText(text);
        setHtmlContent(message, text, part);
        return message;
    }

    // 发送html内容
    public void setHtmlContent(MimeMessage message, String text, Part part) throws MessagingException, IOException {
        // 创建可包括多重内容的邮件内容
        MimeMultipart multiPart = new MimeMultipart();
        // 代表html内容类型的对象
        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(text, "text/html;charset=UTF-8");
        // 新增html内容类型
        multiPart.addBodyPart(htmlPart);
        // 新增附件
        setEnclosure(multiPart, part);
        // 设定为邮件内容
        message.setContent(multiPart);
    }

    // 发送附件
    public void setEnclosure(MimeMultipart multiPart, Part part) throws IOException, MessagingException {
        // 获取文件名
        String header = part.getHeader("Content-Disposition");
        String filename = header.substring(header.indexOf("filename=\"") + 10, header.lastIndexOf("\""));
        // 获取文件内容
        InputStream inputStream = part.getInputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = -1;
        while ((length = inputStream.read(buffer)) != -1) {
            out.write(buffer, 0, length);
        }
        inputStream.close();
        out.close();
        byte[] bytes = out.toByteArray();
        // 添加附件
        if (!"".equals(filename)) {
            MimeBodyPart filePart = new MimeBodyPart();
            // 必须替换掉空格，否则文件名乱码
            filename = filename.replaceAll("\r", "").replaceAll("\n", "").replaceAll(" ", "");
            filename = MimeUtility.encodeText(filename, "UTF-8", "B");
            filePart.setFileName(filename);
            filePart.setContent(bytes, part.getContentType());
            multiPart.addBodyPart(filePart);
        }
    }
}
