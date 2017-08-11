package com.bw30.open.wft.common;

import java.util.UUID;

public final class SessionUtil {
	
	public final static int LOGIN_SESSION_TAG = 1;
	public final static int CONNECT_SESSION_TAG = 2;
	
	public static String getSessionId(){
//		String date = DateUtil.formateDate(null, "yyMMddHHmmssSSS");
//		String random = IdUtils.randomAlphanumericString(5);
//		return date+random;
		
		String uuid = UUID.randomUUID().toString();
		uuid = uuid.replaceAll("-", "");
		return uuid;
	}
	
	/**
	 * 截取字符串前6位
	 * @param s
	 * @return
	 */
	public static String getTableDate(String s){
		return s.substring(0, 6);
	}
	
}


