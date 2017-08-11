package com.bw30.openplatform.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSON;
import com.bw30.open.common.model.OpenPlatformNotice;
import com.bw30.open.common.model.OpenPlatformUser;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.common.support.DynamicSqlParameter;
import com.bw30.open.wft.common.CookieUtil;
import com.bw30.openplatform.service.IOpenPlayFormNoticeService;
import com.bw30.openplatform.service.IUserService;
import com.bw30.openplatform.service.IWftOperatorService;
import com.bw30.openplatform.util.ServletUtil;

public class UserAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7875734227502850097L;
	
	public final static Integer EMAIL_PASS_RESET = 1;
	public final static Integer PHONE_PASS_RESET = 2;
	public final static String PHONE_VALIDATE_RESET = "phone_validate_ajax";
	
	private OpenPlatformUser user;
	private String verityCode;
	private String verification;
	private String activeCode;
	private String sessionCode;//session随机码，具有时效性。
	private String email;
	private String pwd;
	private String pwdRepeat;
	private String loginMailServer;
	private String passResetResult;
	private String activeResult;
	private Integer activeFlag;
	private String phoneNum;
	private IUserService userService;
	private Integer findType;
	private String auto;
	private IOpenPlayFormNoticeService openPlayFormNoticeService;
	private IWftOperatorService wftOperatorService;
	private List<WftOperator> opList;
	private static final String COOKIE_KEY="DATA";
	public String registUI(){
		opList=wftOperatorService.findOperator();
		return "registUI";
	}
	
	public String phoneValidateReset(){
		userService.passResetByPhone(email, phoneNum, PHONE_VALIDATE_RESET);
		return null;
	}

	public String regist(){
		opList=wftOperatorService.findOperator();
		if(userService.regist(user,verityCode)){
			loginMailServer = "http://mail." + user.getMail().substring(user.getMail().indexOf("@") + 1);
			return "registVer";
		}
		return "registUI";
	}
	
	public void sendMailAgain(){
		PrintWriter out=null;
		try {
			out=ServletUtil.getResponse().getWriter();
			user=new OpenPlatformUser();
			user.setMail(email);
			if(userService.sendMailAgain(user)){
				out.print("1");
			}else{
			out.print("0");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	public void login(){
		PrintWriter out = null;
		try {
			out = ServletActionContext.getResponse().getWriter();
			user = userService.getUser(email, pwd,null);
		    String mail=user.getMail().split("@")[0];
			Integer unnoticecount=openPlayFormNoticeService.findNotice(user.getId(),OpenPlatformNotice.UN_READ,null).size();
			if (user != null) {
				if(user.getStatus()==0){
					out.print("0");
				}else{
					ServletUtil.setSessionObject("unnoticecount", unnoticecount);
					ServletUtil.setSessionObject("mail", mail);
					ServletUtil.setSessionObject("userData", user);
					if(!auto.equals("Y")){
						CookieUtil.addCookie(COOKIE_KEY, null+","+auto, ServletUtil.getResponse());
					}else{
						CookieUtil.addCookie(COOKIE_KEY, user.getMail()+","+auto, ServletUtil.getResponse());
					}
					out.print("1");
				}
			} else {
				out.print("2");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	} 
	
	public void getUserFromCookie(){
	Cookie cookie=CookieUtil.getCookie(ServletUtil.getRequest(), COOKIE_KEY);
	PrintWriter out=null;
	try {
	 out=getResponse().getWriter();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	if(cookie!=null){
		String data=cookie.getValue();
		System.out.println("data=============="+data);
		if (data != null && !"".equals(data)) {
				out.print(data);
			}
		}
	out.print("");    
	}
	
	public String logout(){
		ServletUtil.setSessionObject("userData", null);
		return "logout";
	}
	
	public String active(){
		activeResult = "激活失败，请重试！";
		activeFlag=0;
		user = userService.getUser(email, null, activeCode);
		if(user!=null){
			if (user.getStatus() == 1) {
				activeResult = "用户已经激活,请您直接登录！";
				ServletUtil.setSessionObject("userData", user);
				activeFlag=1;
			}else{
				try{
					DynamicSqlParameter params = new DynamicSqlParameter(new String[]{"status"}, new Object[]{1});
					userService.updateByMap(user.getId(), params);
					activeResult = "用户激活成功";
					ServletUtil.setSessionObject("userData", user);
					activeFlag=1;
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		return "activeResult";
	}
	
	public String findPWDStep1(){
		return "findPWDStep1";
	}
	
	public String findPWDStep2(){
		try{
			if(findType == EMAIL_PASS_RESET){
				if(userService.passResetByEmail(email, verification));{
					loginMailServer = "http://mail." + email.substring(email.indexOf("@") + 1);
					return "findPWDStep2";	
				}							
			}
			else if(findType == PHONE_PASS_RESET){
				user = userService.getUser(email, null, null);
				phoneNum = user.getPhone();
				if(userService.passResetByPhone(email, phoneNum, verification)){
					return "findPWDStep2";
				}				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return "top";	
	}			
	
	public String findPWDStep3(){
		try{
			if(findType == EMAIL_PASS_RESET){
//				if(ServletUtil.getSessionObject(email) == null){
//					passResetResult = "链接失效！请重新获取"; //session对象被清除 随机码无效。
//					return "top";
//				}
//				String sessionStr = ServletUtil.getSessionObject(email).toString();
//				if(!sessionCode.equals(sessionStr.trim())){
//					passResetResult = "链接失效！请重新获取";//session随机码 超出时效范围
//					return "top";
//				}
				user = userService.getUser(email, null, activeCode);
				if(user != null && activeCode != null){
					passResetResult = "验证成功！请重新设置密码";
					return "findPWDStep3";
				}
			}
			else if(findType == PHONE_PASS_RESET){
				if(ServletUtil.getSessionObject(email + phoneNum) == null){
					passResetResult = "验证码失效！请重新获取"; //session对象被清除 验证码无效。
					return "top";
				}
				String sessionStr = ServletUtil.getSessionObject(email + phoneNum).toString();
				if(!verification.equals(sessionStr.trim())){
					passResetResult = "验证码失效！请重新获取";//验证码 超出时效范围
					return "top";
				}
				sessionCode = ServletUtil.getSessionObject(email).toString();
				return "findPWDStep3";					
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		passResetResult = "链接失效！请重新获取";
		return "top";
	}
	
	public String passReset(){
		try{
//			if(ServletUtil.getSessionObject(email) == null){
//				passResetResult = "链接失效！请重新获取"; //session对象被清除 随机码无效。
//				return "top";
//			}
//			String sessionStr = ServletUtil.getSessionObject(email).toString();
//			if(!sessionCode.equals(sessionStr.trim())){
//				passResetResult = "链接失效！请重新获取";//session随机码 超出时效范围
//				return "top";
//			}
			if(pwd.equals(pwdRepeat)){
				userService.changePassByEmail(email, pwd);
				return "succPassReset";
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		passResetResult = "密码重置失败。";
		return "top";	
	}

	public void checkEmail(){
		PrintWriter out = null;
		try {
			out = ServletActionContext.getResponse().getWriter();
			user = userService.getUser(email, null,null);
			if (user != null) {
				out.print("0");// 用户已经存在
			} else {
				out.print("1");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}
	
	public void checkVerityCode(){
		PrintWriter out = null;
		try {
			out = ServletActionContext.getResponse().getWriter();
			if(verityCode!=null){
				if(verityCode.equalsIgnoreCase(ServletUtil.getSessionObject("validateCode")+"")){
					out.print("0");
				}else{
					out.print("1");
				}
			}else{
				out.print("1");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}
	
	public void checkPhoneVerityCode(){
		PrintWriter out = null;
		try{
			out = ServletActionContext.getResponse().getWriter();
			if(verification !=null && verification.equals(ServletUtil.getSessionObject(email+phoneNum))){
				out.print("0");
			}else{
				out.print("1");
			}			
		}
		catch (Exception e) {
			e.printStackTrace();	
		} finally {
			out.close();
		}
	}

	public OpenPlatformUser getUser() {
		return user;
	}

	public void setUser(OpenPlatformUser user) {
		this.user = user;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public String getVerityCode() {
		return verityCode;
	}

	public void setVerityCode(String verityCode) {
		this.verityCode = verityCode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getLoginMailServer() {
		return loginMailServer;
	}

	public void setLoginMailServer(String loginMailServer) {
		this.loginMailServer = loginMailServer;
	}

	public String getActiveCode() {
		return activeCode;
	}

	public void setActiveCode(String activeCode) {
		this.activeCode = activeCode;
	}

	public String getActiveResult() {
		return activeResult;
	}

	public void setActiveResult(String activeResult) {
		this.activeResult = activeResult;
	}
	
	public void setFindType(Integer findType) {
		this.findType = findType;
	}

	public Integer getFindType() {
		return findType;
	}

	public String getVerification() {
		return verification;
	}

	public void setVerification(String verification) {
		this.verification = verification;
	}

	public String getPwdRepeat() {
		return pwdRepeat;
	}

	public void setPwdRepeat(String pwdRepeat) {
		this.pwdRepeat = pwdRepeat;
	}

	public String getSessionCode() {
		return sessionCode;
	}

	public void setSessionCode(String sessionCode) {
		this.sessionCode = sessionCode;
	}

	public String getPassResetResult() {
		return passResetResult;
	}

	public void setPassResetResult(String passResetResult) {
		this.passResetResult = passResetResult;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public static String getPhoneValidateReset() {
		return PHONE_VALIDATE_RESET;
	}

	public void setWftOperatorService(IWftOperatorService wftOperatorService) {
		this.wftOperatorService = wftOperatorService;
	}

	public List<WftOperator> getOpList() {
		return opList;
	}

	public void setOpList(List<WftOperator> opList) {
		this.opList = opList;
	}

	public String getAuto() {
		return auto;
	}

	public void setAuto(String auto) {
		this.auto = auto;
	}

	public Integer getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(Integer activeFlag) {
		this.activeFlag = activeFlag;
	}

	public void setOpenPlayFormNoticeService(
			IOpenPlayFormNoticeService openPlayFormNoticeService) {
		this.openPlayFormNoticeService = openPlayFormNoticeService;
	}
	
}
