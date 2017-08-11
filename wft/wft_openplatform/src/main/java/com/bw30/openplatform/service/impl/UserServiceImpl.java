package com.bw30.openplatform.service.impl;

import java.util.Random;

import com.bw30.open.common.dao.mapper.AccountMapper;
import com.bw30.open.common.dao.mapper.UserMapper;
import com.bw30.open.common.dao.mapper.WftChannelMapper;
import com.bw30.open.common.model.OpenPlatformAccount;
import com.bw30.open.common.model.OpenPlatformUser;
import com.bw30.open.common.support.DynamicSqlParameter;
import com.bw30.openplatform.action.UserAction;
import com.bw30.openplatform.service.IUserService;
import com.bw30.openplatform.util.EncrityUtil;
import com.bw30.openplatform.util.SendMail;
import com.bw30.openplatform.util.ServletUtil;

public class UserServiceImpl implements IUserService{
	private UserMapper userMapper;
	private AccountMapper accountMapper;
	private WftChannelMapper wftChannelMapper;
	private SmsService smsService;

	public boolean regist(OpenPlatformUser user,String vertyCode) {
		if(validate(user, vertyCode)){
			try{
				String strMd5=getMd5();
				user.setCode(strMd5);
				user.setStatus(0);
				userMapper.insert(user);
				user.setChannelcode(user.getId().toString());
				userMapper.update(user);
				user.setChannelcode(user.getId().toString());
				OpenPlatformAccount account=new OpenPlatformAccount();
				account.setId(user.getId());
				account.setBuytime("0");
				account.setRetime("0");
				account.setUsetime("0");
				accountMapper.insert(account);
				SendMail.send(user.getMail(),"激活邮件","<a href=\""+ServletUtil.getSessionObject("server_url")+"/user_active.action?email=" + user.getMail() + "&activeCode=" + strMd5+"\" target=\"_blank\">"+ServletUtil.getSessionObject("server_url")+"/user_active.action?email=" + user.getMail() + "&activeCode=" + strMd5+"</a>");
			}catch(Exception e){
				e.printStackTrace();
				return false;
			}
			return true;
		}
		return false;
	}
	public boolean sendMailAgain(OpenPlatformUser user) {
		String md5=getMd5();
		user.setCode(md5);
		try {
			SendMail.send(user.getMail(),"激活邮件","<a href=\""+ServletUtil.getSessionObject("server_url")+"/user_active.action?email=" + user.getMail() + "&activeCode=" + md5+"\" target=\"_blank\">"+ServletUtil.getSessionObject("server_url")+"/user_active.action?email=" + user.getMail() + "&activeCode=" + md5+"</a>");
			userMapper.update(user);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	private String getMd5(){
		Random random = new Random();
		StringBuffer buff = new StringBuffer();
		String str = "0123456789abcdefghijklmnopqrstuvwxyz";
		char[] arrStr = str.toCharArray();
		for (int i = 0; i < 10; i++) {
			buff.append(arrStr[random.nextInt(str.length())]);
		}
		String md5Str = EncrityUtil.encrity(buff.toString());
		md5Str = md5Str.replaceAll("[+]", "").replaceAll("=", "");
		return md5Str;
	}

	public OpenPlatformUser getUser(String email, String pwd,String code) {
		return userMapper.findUser(email, pwd,code);
	}
	
	private boolean validate(OpenPlatformUser user, String vertyCode) {
		boolean flag = true;
		if(user.getMail()==null||"".equals(user.getMail())){
			ServletUtil.setActionContextObject(ERROR_EMAIL, "邮箱不能为空");
			flag = false;
		}
		
		if(user.getPwd()==null||"".equals(user.getPwd())){
			ServletUtil.setActionContextObject(ERROR_PWD, "密码不能为空");
			flag = false;
		}
		
		if(user.getPhone()==null||"".equals(user.getPhone())){
			ServletUtil.setActionContextObject(ERROR_PHONE, "手机不能为空");
			flag = false;
		}
		
		if(user.getQq()==null||"".equals(user.getQq())){
			ServletUtil.setActionContextObject(ERROR_QQ, "QQ不能为空");
			flag = false;
		}
		
		if(vertyCode==null||"".equals(vertyCode)){
			ServletUtil.setActionContextObject(ERROR_CODE, "验证码不能为空");
			flag = false;
		}else if(!vertyCode.equalsIgnoreCase(ServletUtil.getSessionObject("validateCode")+"")){
			ServletUtil.setActionContextObject(ERROR_CODE, "验证码不正确");
			flag = false;
		}
		return flag;
	}
	
	private boolean validate(String email, String verityCode) {
		boolean flag = true;//邮箱，密码找回信息验证
		if(email == null||"".equals(email)){
			ServletUtil.setActionContextObject(ERROR_EMAIL, "邮箱不能为空");
			flag = false;
		}
		if(verityCode==null||"".equals(verityCode)){
			ServletUtil.setActionContextObject(ERROR_CODE, "验证码不能为空");
			flag = false;
		}else if(!verityCode.equalsIgnoreCase(ServletUtil.getSessionObject("validateCode")+"")){
			ServletUtil.setActionContextObject(ERROR_CODE, "验证码不正确");
			flag = false;
		}
		return flag;
	}

	public boolean updateByMap(Integer uid, DynamicSqlParameter params) {
		userMapper.updateByMap(uid, params.getParamMap());
		return false;
	}
	
	public boolean updateByMail(String mail, DynamicSqlParameter params) {
		userMapper.updateByMail(mail, params.getParamMap());
		return false;
	}
	
	public boolean passResetByPhone(String email, String phoneNum, String verification){
		if(validate(email,verification) || UserAction.PHONE_VALIDATE_RESET == verification){
			try{
				Random random = new Random();
				StringBuffer buff = new StringBuffer();
				StringBuffer sessionBuff = new StringBuffer();//session随机码  
				String md5SessionStr = "";
				String str = "0123456789";
				char[] arrStr = str.toCharArray();
				for (int i = 0; i < 6; i++) {
					buff.append(arrStr[random.nextInt(str.length())]);
					sessionBuff.append(arrStr[random.nextInt(str.length())]);
				}
				md5SessionStr = EncrityUtil.encrity(sessionBuff.toString());
				md5SessionStr = md5SessionStr.replaceAll("[+]", "").replaceAll("=", "");
				ServletUtil.setSessionObject(email + phoneNum, buff.toString());
				ServletUtil.setSessionObject(email, md5SessionStr);
				smsService.sendSms("开放平台_密码找回", phoneNum, buff.toString(), null, null);
			}
			catch(Exception e){
				e.printStackTrace();
				return false;
			}
			return true;			
		}
		return false;
	}

	public boolean passResetByEmail(String email, String verification) {
		if(validate(email, verification)){
			try{
				Random random = new Random();
				StringBuffer buff = new StringBuffer();
				StringBuffer sessionBuff = new StringBuffer();//session随机码  使邮件改密链接具有时效性
				String str = "0123456789abcdefghijklmnopqrstuvwxyz";
				char[] arrStr = str.toCharArray();
				for (int i = 0; i < 10; i++) {
					buff.append(arrStr[random.nextInt(str.length())]);
					sessionBuff.append(arrStr[random.nextInt(str.length())]);
				}
				String md5Str = EncrityUtil.encrity(buff.toString());
				md5Str = md5Str.replaceAll("[+]", "").replaceAll("=", "");
//				String md5SessionStr = EncrityUtil.encrity(sessionBuff.toString());
//				md5SessionStr = md5SessionStr.replaceAll("[+]", "").replaceAll("=", "");
//				ServletUtil.setSessionObject(email, md5SessionStr);
				DynamicSqlParameter params = new DynamicSqlParameter(new String[]{"code"}, new Object[]{md5Str});				
				updateByMail(email, params);
				SendMail.send(email,"密码找回","<a href=\""+ServletUtil.getSessionObject("server_url")+"/user_findPWDStep3.action?email=" + email + "&findType=1&activeCode=" + md5Str+"\" target=\"_blank\">"+ServletUtil.getSessionObject("server_url")+"/user_findPWDStep3.action?email=" + email + "&findType=1&activeCode=" + md5Str+"</a>");
			}catch(Exception e){
				e.printStackTrace();
				return false;
			}
			return true;
		}
		return false;
	}	
	
	public boolean updateUser(OpenPlatformUser user){
		try{
			userMapper.update(user);
			ServletUtil.setSessionObject("userData", user);
			return true;			
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public void changePassByEmail(String email, String pwd){
		DynamicSqlParameter params = new DynamicSqlParameter(new String[]{"password"}, new Object[]{pwd});
		userMapper.updateByMail(email, params.getParamMap());
	}
	
	public void setUserMapper(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	public void setSmsService(SmsService smsService) {
		this.smsService = smsService;
	}
	public void setWftChannelMapper(WftChannelMapper wftChannelMapper) {
		this.wftChannelMapper = wftChannelMapper;
	}
	public void setAccountMapper(AccountMapper accountMapper) {
		this.accountMapper = accountMapper;
	}


}
