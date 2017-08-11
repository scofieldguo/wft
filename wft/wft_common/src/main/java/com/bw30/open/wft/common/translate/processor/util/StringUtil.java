package com.bw30.open.wft.common.translate.processor.util;


/**
 * 字符串工具类
 * @author raymond
 * @version 1.0
 */
public class StringUtil {
	public static String getStringValue(Object obj){
		if(obj==null){
			return "";
		}
		return obj.toString();
	}
	
	public static int getStringLength(String str){
		if(str==null){
			return 0;
		}
		return str.getBytes().length;
	}
	
	public static boolean hasValue(Object o){
		if(o==null||o.toString().trim().equals("")){
			return false;
		}
		return true;
	}
	
	public static boolean isNumber(String str){
		String reg="^[-|+]?\\d*([.]\\d+)?$";
		return str.matches(reg);
	}
	
	public static void main(String[] args) {
		String str="0123";
		System.out.println(isNumber(str));
	}
}
