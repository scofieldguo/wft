package com.bw30.openplatform.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.log4j.Logger;

import com.bw30.open.wft.common.Md5Encrypt;


/**
 * 发短信
 *
 * @version 2013-12-18 下午07:56:25
 *
 */
public class SmsService {
private static final Logger LOGGER=Logger.getLogger(SmsService.class);
	
	private String smsUrl;
	private String username;
	private String password;
	
	public void setSmsUrl(String smsUrl) {
		this.smsUrl = smsUrl;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * 发送短信
	 * 
	 * @param usage 验证码用途
	 * @param mobile 手机号
	 * @param verifyCode 验证码
	 * @param extno 扩展号码，可选
	 * @param addseqno 可选
	 * @return
	 */
	public boolean sendSms(String usage, String mobile, String verifyCode, String extno, String addseqno){
		LOGGER.info(String.format("send verifyCode sms for [%s] to [%s] :%s", usage, mobile, verifyCode));
		try {
			//md5加密
			String pwd=Md5Encrypt.md5Enc(this.password, "UTF-8");
			pwd = pwd.toLowerCase();
			
			String sms = this.genSmsMsg(verifyCode);
//			if(usage.equalsIgnoreCase(WftConstant.VERIFY_CODE_USAGE_LOGIN)){
//				sms = this.genSmsForLogin(verifyCode);
//			}else if(usage.equalsIgnoreCase(WftConstant.VERIFY_CODE_USAGE_REGISTER)){
//				sms = this.genSmsForRegister(verifyCode);
//			}else if(usage.equalsIgnoreCase(WftConstant.VERIFY_CODE_USAGE_PASSWORD)){
//				sms = this.genSmsForPassword(verifyCode);
//			}else if(usage.equalsIgnoreCase(WftConstant.VERIFY_CODE_USAGE_UPDATE_MOBILE)){
//				sms = this.genSmsForUpdateMobile(verifyCode);
//			}else if(usage.equalsIgnoreCase(WftConstant.VERIFY_CODE_USAGE_UPDATE_EMAIL)){
//				sms = this.genSmsForUpdateMobile(verifyCode);
//			}else if(usage.equalsIgnoreCase(WftConstant.CONTENT_USAGE_ACTIVITY)){
//				sms=this.getSmsForActivity(verifyCode);
//			}
//			else {
//				LOGGER.warn(String.format("no sms service for usage[%s]", usage));
//				return false;
//			}
			
			LOGGER.info(String.format("[%s] sms message: %s", mobile, sms));
			
			sms=URLEncoder.encode(sms, "GBK");//发送内容要URLEncode用GBK编码
			
			URL url = new URL(this.smsUrl);
			URLConnection connection = url.openConnection();
			connection.setDoOutput(true);
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(),"utf-8");
			
			extno = null == extno ? "":extno;
			addseqno = null == addseqno ? "":addseqno;
			String queryStr = new StringBuffer("user=").append(this.username).append("&password=").append(pwd)
								.append("&tele=").append(mobile).append("&msg=").append(sms).append("&extno=")
								.append(extno).append("&addseqno=").append(addseqno).toString();
			out.write(queryStr); 
			
			LOGGER.info(String.format("[%s] req[%s]:%s", mobile, url, queryStr));
			out.flush();
			out.close();

			
			int len = 0;
			byte[] buf = new byte[10240];
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			InputStream is = connection.getInputStream();
			while ((len = is.read(buf)) != -1) {
				bos.write(buf, 0, len);
			}
			is.close();
			String respStr = bos.toString("GBK");
			
			LOGGER.info(String.format("[%s] response:%s", mobile, respStr));
			if(respStr.contains("ok") || respStr.contains("OK")){
				return true;
			}
		} catch (Exception e) {
			LOGGER.error("", e);
		}
		return false;
	}
	
	private String genSmsMsg(String vCode){
		return new StringBuffer("【蜂巢移动_开放平台】验证码：").append(vCode)
				.append("，请及时验证。").toString();
	}
	
	/**
	 * 生成短信内容：推荐应用
	 * 
	 * */
	@Deprecated
	private String getSmsForActivity(String content) {
		return new StringBuffer("【WiFi通】").append(content)
		.append("（注册验证码），请及时验证。为了保证您的账户安全，请勿将验证码告知其他人。")
		.toString();
	}
	/**
	 * 生成短信内容：手机号注册
	 * @param verifyCode
	 * @return
	 */
	@Deprecated
	private String genSmsForRegister(String verifyCode){
		return new StringBuffer("【WiFi通】").append(verifyCode)
					.append("（注册验证码），请及时验证。为了保证您的账户安全，请勿将验证码告知其他人。")
					.toString();
	}
	
	/**
	 * 生成短信内容：手机号登录
	 * @param verifyCode
	 * @return
	 */
	@Deprecated
	private String genSmsForLogin(String verifyCode){
		return new StringBuffer("【WiFi通】").append(verifyCode)
					.append("（登录验证码），请及时验证。为保证您的账号安全，请勿将验证码转告他人。")
					.toString();
	}
	/**
	 * 生成短信内容：找回密码
	 * @param verifyCode
	 * @return
	 */
	@Deprecated
	private String genSmsForPassword(String verifyCode){
		return new StringBuffer("【WiFi通】")
					.append(verifyCode).append("（验证码），请及时验证。为了保证您的账户安全，请勿将验证码告知其他人。")
					.toString();
	}
//	/**
//	 * 生成短信内容：邮箱找回密码
//	 * @param verifyCode
//	 * @return
//	 */
//	private String genSmsForTemPassword(String verifyCode){
//		return new StringBuffer("欢迎使用wifi通，您新的临时密码为：")
//					.append(verifyCode).append("，请及时验证。如非本人操作，请忽略此信息。【wifi通】")
//					.toString();
//	}
//	
	/**
	 * 生成短信内容：修改手机号
	 * @param verifyCode
	 * @return
	 */
	@Deprecated
	private String genSmsForUpdateMobile(String verifyCode){
		return new StringBuffer("【WiFi通】").append(verifyCode)
					.append("（验证码），请及时验证。为了保证您的账户安全，请勿将验证码告知其他人。")
					.toString();
	}

}
