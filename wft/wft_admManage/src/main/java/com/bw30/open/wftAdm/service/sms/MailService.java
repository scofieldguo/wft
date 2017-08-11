package com.bw30.open.wftAdm.service.sms;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;

import com.bw30.open.common.model.OpenPlatformUser;

/**
 * 邮件发送
 *
 * @author zhouwei@bw30.com
 * @version 2013-12-18 上午09:40:11
 *
 */
public class MailService{
	private static final Logger LOGGER = Logger.getLogger(MailService.class);

	private String from;// 邮件发送方
	private String emailVerifyUrl;//邮箱验证请求url
	private JavaMailSender sender;

	public void setFrom(String from) {
		this.from = from;
	}

	public void setEmailVerifyUrl(String emailVerifyUrl) {
		this.emailVerifyUrl = emailVerifyUrl;
	}

	public void setSender(JavaMailSender sender) {
		this.sender = sender;
	}

	public MailService() {
	}

	
	private String constructDate(String date){
		StringBuilder sb = new StringBuilder();
		String dates[]=date.split("-");
		sb.append(dates[0]).append("年");
		sb.append(dates[1]).append("月").append(dates[2]).append("日");
		return sb.toString();
	}
	
	private String genMailText(String balace_hour, String now, String startDate, String yesteUseTime, OpenPlatformUser user) {
		StringBuilder sb = new StringBuilder();
		sb.append("<p>合作伙伴, 您好:</p></n>");
		String lastDay=constructDate(startDate);
		String nowDay =constructDate(now);
		sb.append("<p>&nbsp;&nbsp;&nbsp;&nbsp;"+lastDay+"消耗:"+yesteUseTime+"小时。截止"+nowDay+", <b>贵公司卡池还剩约"+balace_hour+"小时, 为避免时长不足造成服务中断 ,请在时长用尽之前尽快进行后续采购事宜!</b></p>");
		sb.append("<p>北纬通信 蜂巢互联</p>");
		return sb.toString();
	}

	public boolean sendWarnMailToCustorme(String email, String balace_hour,
			String now, String startDate, String yesteUseTime, OpenPlatformUser user) {
		try {
			// 使用JavaMail的MimeMessage，支付更加复杂的邮件格式和内容
			MimeMessage msg = sender.createMimeMessage();
			msg.setFrom(new InternetAddress(this.from));    
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
			msg.setSubject("北纬通信-"+user.getCompany()+"卡池预警");
			Multipart mainPart = new MimeMultipart();
			// 创建一个包含HTML内容的MimeBodyPart
			BodyPart html = new MimeBodyPart();
			// 设置HTML内容 建立第一部分： 文本正文
			String content = null;
			content = this.genMailText(balace_hour,now,startDate,yesteUseTime,user);
			LOGGER.info(String.format("sent mail to [%s]:%s", email, content));
			html.setContent(content, "text/html; charset=utf-8");
			mainPart.addBodyPart(html);
			msg.setContent(mainPart);
			sender.send(msg);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("sengMail for get password ERROR", e);
		}
		return false;
	}

	public boolean sendchargeMailToCustorme(String email, Integer chargeHours,
			String now, OpenPlatformUser user, String balace_hour) {
		try {
			// 使用JavaMail的MimeMessage，支付更加复杂的邮件格式和内容
			MimeMessage msg = sender.createMimeMessage();
			msg.setFrom(new InternetAddress(this.from));  
			String nowDay =constructDate(now);
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
			msg.setSubject(nowDay+user.getCompany()+"卡池"+chargeHours+"小时已成功充值");
			Multipart mainPart = new MimeMultipart();
			// 创建一个包含HTML内容的MimeBodyPart
			BodyPart html = new MimeBodyPart();
			// 设置HTML内容 建立第一部分： 文本正文
			String content = null;
			content = this.genChargeMailText(balace_hour,nowDay,user,chargeHours);
			LOGGER.info(String.format("sent mail to [%s]:%s", email, content));
			html.setContent(content, "text/html; charset=utf-8");
			mainPart.addBodyPart(html);
			msg.setContent(mainPart);
			sender.send(msg);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("sengMail for get password ERROR", e);
		}
		return false;
		
	}

	private String genChargeMailText(String balace_hour, String nowDay,
			OpenPlatformUser user,Integer charhour) {
		StringBuilder sb = new StringBuilder();
		sb.append("<p>合作伙伴, 您好:</p>");
		sb.append("<p>&nbsp;&nbsp;&nbsp;&nbsp;"+nowDay+"已充值: "+charhour+"小时,充值后, "+user.getCompany()+"卡池剩余时长:"+balace_hour+"小时。合作愉快!</p>");
		sb.append("<p>北纬通信 蜂巢互联</p>");
		return sb.toString();
	}

}