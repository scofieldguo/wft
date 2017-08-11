package com.bw30.openplatform.service;

import com.bw30.open.common.model.OpenPlatformUser;
import com.bw30.open.common.support.DynamicSqlParameter;

public interface IUserService {
	public final static String ERROR_KEY = "error";
	public final static String ERROR_EMAIL = "error_email";
	public final static String ERROR_PWD = "error_pwd";
	public final static String ERROR_PHONE = "error_phone";
	public final static String ERROR_QQ = "error_qq";
	public final static String ERROR_CODE = "error_code";
	
	public boolean regist(OpenPlatformUser user,String verityCode);
	public OpenPlatformUser getUser(String email,String pwd,String code);
	public boolean updateByMap(Integer uid,DynamicSqlParameter params);
	public boolean passResetByEmail(String email, String verityCode);
	public boolean passResetByPhone(String email, String phoneNum, String verification);
	public void changePassByEmail(String email, String pwd);
	public boolean updateUser(OpenPlatformUser user);
	public boolean sendMailAgain(OpenPlatformUser user);
	
}
