package com.bw30.open.wft.common;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

public class WifiTongUtils {
	private static final Logger logger = Logger.getLogger(WifiTongUtils.class);
	
	private static final SimpleDateFormat SDF_YMD_HMS = new SimpleDateFormat("yyyyMMddHHmmss"); 
	private static final String ORDER_PREFIX = "WFT";
	private static final String[] ORDER_CODE = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
	
	/**
	 * 错误码-提示信息
	 */
	private static final Map<Integer, String> CODE_MSG = new HashMap<Integer, String>();
	
	public static final void main(String[] args){
		for(int i = 0;i<10;i++){
			System.out.println(genOrderNum());
		}
		
		System.out.println(genId());
	}
	
	/**
	 * 生成12位订单号
	 * @return
	 */
	public static String genOrderNum(){
		StringBuffer sb = new StringBuffer(ORDER_PREFIX);
		Random random = new Random();
		for(int i = 0; i < 9; i++){
			sb.append(ORDER_CODE[random.nextInt(10)]);
		}
		return sb.toString();
	}
	
	/**
	 * 生成32位id
	 * 
	 * @return
	 */
	public static String genId(){
		StringBuffer sb = new StringBuffer(SDF_YMD_HMS.format(new Date()));
		Random random = new Random();
		for(int i = 0; i < 18; i++){
			sb.append(ORDER_CODE[random.nextInt(10)]);
		}
		return sb.toString();
	}
	
	/**
	 * 判断字符串是否为空或者为空串
	 * @param s
	 * @return
	 */
	public static boolean isEmpty(String s){
		if(s == null || s.isEmpty())
			return true;
		return false;
	}
	
	public static boolean isNotEmpty(String s){
		if(s != null && !s.isEmpty())
			return true;
		return false;
	}
	/**
	 * 将字符串转换成int类型，如果字符串为空或者字符串有非数字字符，则抛出异常。
	 * 
	 * @param s
	 * @return
	 * @throws NumberFormatException
	 */
	public static int StringToInteger(String s,int defaultValue) {
			try {
				 return Integer.parseInt(s);
			} catch (Exception e) {
				return defaultValue;
			} 
		
	}
	/**
	 * 读取资源文件
	 * @param fileName
	 * @return
	 */
	public static String readProperties(String fileName,String code){
		try {
			String path = WifiTongUtils.class.getClassLoader().getResource(fileName).getPath().replace("%20", " ");
			PropertiesConfiguration config = new PropertiesConfiguration();
			config.setEncoding("UTF-8");
			config.setFileName(path);  
			config.load(); 
			
			Iterator<String> keys = config.getKeys();
			String k = null;
			while(keys.hasNext()){
				k = keys.next();
				CODE_MSG.put(Integer.valueOf(k), config.getString(k));
			}
			return CODE_MSG.get(Integer.parseInt(code));
		} catch (ConfigurationException e) {
			logger.info("该文件不存在,或者读取文件错误", e);
			return null;
		} catch (Exception e){
			logger.info("获取配置文件中资源错误", e);
			return null;
		}
	}
	
	public static String readProperties(int code){
		String msg = CODE_MSG.get(code);
		if(isEmpty(msg)){
			return readProperties("code.properties", String.valueOf(code));
		}
		return msg;
	}
	
	/**
	 * 是否是手机号
	 * 验证规则：以1开头，11为数字
	 * 
	 * @param mobile
	 * @return ture是，false不是
	 */
	public static boolean isMobile(String mobile){
		String regex = "^1\\d{10}$";
		if(null != mobile && mobile.matches(regex)){
			return true;
		}
		return false;
	}
	
	/**
	 * 是否是email
	 * 
	 * @param email
	 * @return true是，false否
	 */
	public static boolean isEmail(String email){
		String regex = "^([a-z0-9A-Z]+[\\-_\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		if(null != email && email.matches(regex)){
			return true;
		}
		return false;
	}
	
	private static final SimpleDateFormat SDF_YMD = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 判断两个日期是否是同一天
	 * 
	 * @param date_1
	 * @param date_2
	 * @return 是同一天返回true
	 */
	public static boolean isOneDay(Date date_1, Date date_2){
		if(null == date_1 || null == date_2){
			return false;
		}
		
		if(SDF_YMD.format(date_1).equalsIgnoreCase(SDF_YMD.format(date_2))){
			return true;
		}
		
		return true;
	}
	
	
}
