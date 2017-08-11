package com.bw30.open.wftAdm.util;

import java.util.List;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * 邮件发送
 * 
 * @author jack
 * 2015年1月29日
 */
public class MailService{
	private static final Logger LOG = Logger.getLogger(MailService.class);

	private String from;// 邮件发送方
	private JavaMailSender sender;

	public void setFrom(String from) {
		this.from = from;
	}

	public void setSender(JavaMailSender sender) {
		this.sender = sender;
	}

	public MailService() {
	}
	
	/**
	 * 发送邮件
	 * 
	 * @param emails 收件人
	 * @param subject 邮件主题
	 * @param content 邮件内容，可用html标签设置样式
	 */
	public void sendMail(List<String> emails, String subject, String content){
		if(null == emails || 0 == emails.size()){
			LOG.warn("send mail fail: no email");
			return;
		}
		
		try {
			// 使用JavaMail的MimeMessage，支付更加复杂的邮件格式和内容
			MimeMessage msg = sender.createMimeMessage();
			msg.setFrom(new InternetAddress(this.from));    
			InternetAddress[] recipients = new InternetAddress[emails.size()];
			for(int i = 0; i< emails.size(); i++){
				recipients[i] = new InternetAddress(emails.get(i));
			}
			msg.setRecipients(Message.RecipientType.TO, recipients);
			msg.setSubject(subject);
			Multipart mainPart = new MimeMultipart();
			// 创建一个包含HTML内容的MimeBodyPart
			BodyPart html = new MimeBodyPart();
			html.setContent(content, "text/html; charset=utf-8");
			mainPart.addBodyPart(html);
			msg.setContent(mainPart);
			sender.send(msg);
		} catch (Exception e) {
			LOG.error("seng mail error:", e);
		}
	}
	
}
