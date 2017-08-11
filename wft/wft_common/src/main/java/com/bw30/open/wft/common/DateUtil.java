package com.bw30.open.wft.common;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 方法说明:
 * 
 */
public class DateUtil {
    
	/**
     * 根据传入的时间格式格式化时间
     * 
     * @param date 时间，若为null则格式化当前时间
     * @param pattern
     * @return
     */
    public static String formateDate(Date date, String pattern){
    	SimpleDateFormat formatter = new SimpleDateFormat(pattern);
    	date = null == date ? new Date() : date;
        return formatter.format(date);
    }

    /**
     * 将短时间格式字符串转换为时间 yyyy-MM-dd
     * 
     * @param strDate
     * @return
     */
    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }
    
    /**
    /**
     * 得到一个时间延后或前移几天的时间,nowdate为时间,delay为前移或后延的天数
     */
    public static Date getNextNumDay(Date date, Integer delay) {
    	   Calendar c = Calendar.getInstance();
    	   c.setTime(date);
           c.set(Calendar.HOUR_OF_DAY, 0);
           c.set(Calendar.MINUTE, 0);
           c.set(Calendar.SECOND, 0);
           c.set(Calendar.MILLISECOND, 0);
           c.add(Calendar.DATE, delay);
           return c.getTime();
    }
    
    
    /**
     * 得到当前时间前一小时的开始时间
     * */
	public static Date getBeforeHourDate() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.HOUR,  c.get(Calendar.HOUR)-1);
		c.set(Calendar.MINUTE,0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}
	
	/**
     * 得到当前时间前半个小时的时间
     * */
	public static Date getBeforeHalfHourDate(){
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.MINUTE,c.get(Calendar.MINUTE) - 30);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}
	
	/**
     * 得到当前时间前N分钟的时间
     * */
	public static Date getBeforeNMinuteDate(int n){
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.MINUTE,c.get(Calendar.MINUTE) - n);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}
	
	
	/**
     * 得到当前时间前一小时的结束时间
     * */
	public static Date getAfterHourDate() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.HOUR,  c.get(Calendar.HOUR)-1);
		c.set(Calendar.MINUTE,59);
		c.set(Calendar.SECOND, 59);
		return c.getTime();
	}
    /**
     * 得到给定小时
     */
    public static String getHour(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(date);
        String hour;
        hour = dateString.substring(11, 13);
        return hour;
    }
    /**
     * 得到给定分钟
     * 
     * */
    public static String getMinute(Date date){
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(date);
        String minute;
        minute = dateString.substring(14, 16);
        return minute;
    }
    /**
     * 得到当前时间前一分钟的开始时间
     * */
    public static Date getBeforeMinuteDate() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.MINUTE,c.get(Calendar.MINUTE)-1);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
    
    /**
     * 得到当前时间前一分钟的结束时间
     * */
    public static Date getAfterMinuteDate() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.MINUTE,c.get(Calendar.MINUTE)-1);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
    /**
     * 得到当前小时开始时间
     * 
     * */
    public static Date getHourStartDate(Date date,Integer hour){
    	Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR, hour);
		c.set(Calendar.MINUTE,0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
    }
    
    /**
     * 得到当月第一天
     * */
    public static Date getFirstDayOfMonth(){
    	Calendar c = Calendar.getInstance();
    	c.setTime(new Date());
    	c.set(Calendar.DAY_OF_MONTH,1);
    	return c.getTime();
    	
    }
    
	public static void main(String[] args) {
		System.out.print(DateUtil.formateDate(getFirstDayOfMonth(),"yyyy-MM-dd"));
	}
}
