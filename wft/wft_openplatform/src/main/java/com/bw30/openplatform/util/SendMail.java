package com.bw30.openplatform.util;

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendMail {
	private static String MAIL_ADDRESS = "combmobile@bw30.com";
	private static String MAIL_USERNAME = "combmobile@bw30.com";
	private static String MAIL_PASSWORD = "Bwlan68317777";
	private static String MAIL_SERVER = "smtp.bw30.com";
	public static void send(String toTarget, String subject, String content) {
		// 判断是否需要身份认证
		MyAuthenticator authenticator = null;
		Properties pro = new Properties();
		// 存储发送邮件服务器的信息
		pro.put("mail.smtp.host", MAIL_SERVER);
		// 同时通过验证
		pro.put("mail.smtp.auth", "true");
		// 如果需要身份认证，则创建一个密码验证器
		authenticator = new MyAuthenticator(MAIL_USERNAME,MAIL_PASSWORD);
		// 根据属性新建一个邮件会话
		Session s = Session.getInstance(pro);
		// 打印一些调试信息。
		s.setDebug(true); 
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session
				.getDefaultInstance(pro, authenticator);
		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			Address from = new InternetAddress(MAIL_ADDRESS);
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);
			// 创建邮件的接收者地址，并设置到邮件消息中
			Address to = new InternetAddress(toTarget);
			// Message.RecipientType.TO属性表示接收者的类型为TO
			mailMessage.setRecipient(Message.RecipientType.TO, to);
			// 设置邮件消息的主题
			mailMessage.setSubject(subject);
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());
			// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
			Multipart mainPart = new MimeMultipart();
			// 创建一个包含HTML内容的MimeBodyPart
			BodyPart html = new MimeBodyPart();
			// 设置HTML内容
			html.setContent(content, "text/html; charset=utf-8");
			mainPart.addBodyPart(html);
			// 将MiniMultipart对象设置为邮件内容
			mailMessage.setContent(mainPart);
			// 发送邮件
			Transport.send(mailMessage);
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {
		send("xugx@bw30.com", "标题", "<b>测试</b>");
	}
}
