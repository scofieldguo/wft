package com.bw30.open.common.model.stat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WftTenMinLoginStat implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date dairy; //日期年月日
	private String dairyStr;
	private String channel; // 渠道
	private String ssid; // ssid
	private Integer opid; // 运营商Id
	private Integer hour; // 小时
	private Integer min; // 分钟段
	private Integer conncnt=0; // 连接次数
	private Integer connsuc=0; // 连接成功次数
	private Integer nocard=0; // 取卡失败次数
	private String connlv; // 连接成功率
	DecimalFormat df= new DecimalFormat("0.00");
	public Date getDairy() {
		return dairy;
	}
	public void setDairy(Date dairy) {
		this.dairy = dairy;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getSsid() {
		return ssid;
	}
	public void setSsid(String ssid) {
		this.ssid = ssid;
	}
	public Integer getOpid() {
		return opid;
	}
	public void setOpid(Integer opid) {
		this.opid = opid;
	}
	public Integer getHour() {
		return hour;
	}
	public void setHour(Integer hour) {
		this.hour = hour;
	}
	public Integer getMin() {
		return min;
	}
	public void setMin(Integer min) {
		this.min = min;
	}
	public Integer getConncnt() {
		return conncnt;
	}
	public void setConncnt(Integer conncnt) {
		this.conncnt = conncnt;
	}
	public Integer getConnsuc() {
		return connsuc;
	}
	public void setConnsuc(Integer connsuc) {
		this.connsuc = connsuc;
	}
	public Integer getNocard() {
		return nocard;
	}
	public void setNocard(Integer nocard) {
		this.nocard = nocard;
	}
	public String getConnlv() {
		if((getConncnt())==0 ){
			return "0.00";
		}
		return df.format(get45((getConnsuc().floatValue()/(getConncnt())*100)));
	}
	public void setConnlv(String connlv) {
		this.connlv = connlv;
	}
	
	private float get45(float a){
		int   scale   =   2;//设置位数   
		  int   roundingMode   =   4;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.   
		  BigDecimal   bd   =   new   BigDecimal((double)a);   
		  bd   =   bd.setScale(scale,roundingMode); 
		  float aa   =   bd.floatValue();  
		  return aa;
	}
	public String getDairyStr() {
		return formateDate(getDairy(),"yyyy-MM-dd");
	}
	public void setDairyStr(String dairyStr) {
		this.dairyStr = dairyStr;
	}
	public String formateDate(Date date, String pattern){
    	SimpleDateFormat formatter = new SimpleDateFormat(pattern);
    	date = null == date ? new Date() : date;
        return formatter.format(date);
}
}
