package com.bw30.open.wftAdm.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckMobile {
	// \b 是单词边界(连着的两个(字母字符 与 非字母字符) 之间的逻辑上的间隔),    
    // 字符串在编译时会被转码一次,所以是 "\\b"    
    // \B 是单词内部逻辑间隔(连着的两个字母字符之间的逻辑上的间隔)    
    private static final String PHONE_REG = "\\b(ip(hone|od)|android|opera m(ob|in)i"    
            +"|windows (phone|ce)|blackberry"    
            +"|s(ymbian|eries60|amsung)|p(laybook|alm|rofile/midp"    
            +"|laystation portable)|nokia|fennec|htc[-_]"    
            +"|mobile|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";    
    private static final String PAD_REG = "\\b(ipad|tablet|(Nexus 7)|up.browser"    
            +"|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";    
      
    //移动设备正则匹配：手机端、平板  
    static Pattern phonePat = Pattern.compile(PHONE_REG, Pattern.CASE_INSENSITIVE);    
    static Pattern tablePat = Pattern.compile(PAD_REG, Pattern.CASE_INSENSITIVE);    
        
    /** 
     * 检测是否是移动设备访问 
     * @param userAgent 浏览器标识 
     * @return true:移动设备接入，false:pc端接入 
     */  
    public static boolean check(String userAgent){    
        if(null == userAgent){    
            userAgent = "";    
        }    
        System.out.println(userAgent+"=================");
        // 匹配
        Matcher matcherPhone = phonePat.matcher(userAgent);    
        Matcher matcherTable = tablePat.matcher(userAgent);    
        if(matcherPhone.find() || matcherTable.find()){    
            return true;    
        } else {    
            return false;    
        }    
    }  
    
    public static void main(String[] args) {
    	String str = "mozilla/5.0 (iphone; cpu iphone os 9_1 like mac os x) applewebkit/601.1.46 (khtml, like gecko) mobile/13b143 micromessenger/6.3.13 nettype/wifi language/zh_cn";
		System.out.println(check(str));
	}
}
