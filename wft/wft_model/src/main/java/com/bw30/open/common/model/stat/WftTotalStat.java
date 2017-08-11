package com.bw30.open.common.model.stat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WftTotalStat implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8510955098878489713L;
	
	private String channel; // 渠道
	private Date  dairy; // 日期年月日
	private String dairyStr;
	private Integer opid; // 运用商ID
	private String ssid; // 热点类别
	private Integer connsuc=0; // 链接成功数
	private Integer connfail=0; // 链接失败数
	private Integer persuc=0; // 成功人数
	private Integer perfail=0; // 失败人数
	private Long utvalue=0l; // 平台统计时长
	private Long utvalueop=0l; // 运营商统计时长
	private Integer macsuc=0; // 成功热点数
	private Integer macfail=0; // 失败热点数
	private Integer overnfail=0; // 失败n次以上的热点数
	private Integer sdkcnt=0; // SDK打开次数
	private Integer sdkper=0; // SDK打开人数
	private Integer nocard=0; // 取卡失败
	private Long utvaluetrue =0l; // 消耗时长
	private Long outvaluetrue=0l; // 昨日消耗时长
	private String outvaluetrueshow;
	private Long oconnsuc=0l;//昨日连接成功次数
	private String ococcsucshow;
	private Long opersuc=0l; // 昨日连接成功人数
	private String opersucshow;
	private Integer prvid; // 省份Id
	private String prvname; //省份名
	private String perlv ;
	private String connlv;
	private String daylv;
	private String hutvalue;
	DecimalFormat df= new DecimalFormat("0.00");
	private String avghour;
	private String maclv;
	private String dayconnlv;
	private String dayperlv;
	private WftOldTotalStat wftOldTotalStat = new WftOldTotalStat();
	
	public Date getDairy() {
		return dairy;
	}
	public void setDairy(Date dairy) {
		this.dairy = dairy;
	}
	public Integer getOpid() {
		return opid;
	}
	public void setOpid(Integer opid) {
		this.opid = opid;
	}
	public String getSsid() {
		return ssid;
	}
	public void setSsid(String ssid) {
		this.ssid = ssid;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public Integer getConnsuc() {
		return connsuc;
	}
	public void setConnsuc(Integer connsuc) {
		this.connsuc = connsuc;
	}
	public Integer getConnfail() {
		return connfail;
	}
	public void setConnfail(Integer connfail) {
		this.connfail = connfail;
	}
	public Integer getPersuc() {
		return persuc;
	}
	public void setPersuc(Integer persuc) {
		this.persuc = persuc;
	}
	public Integer getPerfail() {
		return perfail;
	}
	public void setPerfail(Integer perfail) {
		this.perfail = perfail;
	}
	public Long getUtvalue() {
		return utvalue;
	}
	public void setUtvalue(Long utvalue) {
		this.utvalue = utvalue;
	}
	public Long getUtvalueop() {
		return utvalueop;
	}
	public void setUtvalueop(Long utvalueop) {
		this.utvalueop = utvalueop;
	}
	public Integer getMacsuc() {
		return macsuc;
	}
	public void setMacsuc(Integer macsuc) {
		this.macsuc = macsuc;
	}
	public Integer getMacfail() {
		return macfail;
	}
	public void setMacfail(Integer macfail) {
		this.macfail = macfail;
	}
	public Integer getSdkcnt() {
		return sdkcnt;
	}
	public void setSdkcnt(Integer sdkcnt) {
		this.sdkcnt = sdkcnt;
	}
	public Integer getSdkper() {
		return sdkper;
	}
	public void setSdkper(Integer sdkper) {
		this.sdkper = sdkper;
	}
	public Integer getNocard() {
		return nocard;
	}
	public void setNocard(Integer nocard) {
		this.nocard = nocard;
	}
	public Integer getOvernfail() {
		return overnfail;
	}
	public void setOvernfail(Integer overnfail) {
		this.overnfail = overnfail;
	}
	public Long getUtvaluetrue() {
		if(utvaluetrue==null){
			utvaluetrue=0l;
		}
		return utvaluetrue;
	}
	public void setUtvaluetrue(Long utvaluetrue) {
		this.utvaluetrue = utvaluetrue;
	}
	public Long getOutvaluetrue() {
		if(outvaluetrue==null){
			outvaluetrue = 0l;
		}
		return outvaluetrue;
	}
	public void setOutvaluetrue(Long outvaluetrue) {
		this.outvaluetrue = outvaluetrue;
	}
	public Integer getPrvid() {
		return prvid;
	}
	public void setPrvid(Integer prvid) {
		this.prvid = prvid;
	}
	public String getPerlv() {
		if((getPerfail()+getPersuc())==0 ){
			return "0.00";
		}
		return df.format(get45((getPersuc().floatValue()/(getPerfail()+getPersuc())*100)));
	}
	public void setPerlv(String perlv) {
		this.perlv = perlv;
	}
	public String getConnlv() {
		if((getConnfail()+getConnsuc())==0 ){
			return "0.00";
		}
		return df.format(get45((getConnsuc().floatValue()/(getConnfail()+getConnsuc())*100)));
	}
	public void setConnlv(String connlv) {
		this.connlv = connlv;
	}
	public String getDaylv() {
		if(getUtvaluetrue()==0){
			return "0.00";
		}else if(getOutvaluetrue()==null || getOutvaluetrue()==0){
			return "100.00";
		}else{
			return df.format(get45(((getUtvaluetrue().floatValue()-getOutvaluetrue().floatValue())/(getOutvaluetrue())*100)));
		}
	}
	public void setDaylv(String daylv) {
		this.daylv = daylv;
	}
	public String getHutvalue() {
		if((getUtvaluetrue())==0 ){
			return "0.00";
		}
		return df.format(get45(((getUtvaluetrue().floatValue()/(3600)))));
	}
	public void setHutvalue(String hutvalue) {
		this.hutvalue = hutvalue;
	}
	public String getAvghour() {
		if(getUtvaluetrue()==0){
			return "0.00";
		}
		if(getPersuc()==0){
			return "0.00";
		}
		return df.format(get45((getUtvaluetrue().floatValue()/getPersuc()/3600)));
	}
	public void setAvghour(String avghour) {
		this.avghour = avghour;
	}

	private float get45(float a){
		int   scale   =   2;//设置位数   
		  int   roundingMode   =   4;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.   
		  BigDecimal   bd   =   new   BigDecimal((double)a);   
		  bd   =   bd.setScale(scale,roundingMode); 
		  float aa   =   bd.floatValue();  
		  return aa;
	}
	public String getMaclv() {
		if(getMacsuc()==0){
			return "0.00";
		}else{
			return df.format(get45((getMacsuc().floatValue()/(getMacfail()+getMacsuc())*100)));
		}
	}
	public void setMaclv(String maclv) {
		this.maclv = maclv;
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
	public Long getOconnsuc() {
		if(getWftOldTotalStat() == null){
			return 0l;
		}else{
			return getWftOldTotalStat().getOconnsuc();
		}
		//return oconnsuc;
	}
	public void setOconnsuc(Long oconnsuc) {
		this.oconnsuc = oconnsuc;
	}
	public Long getOpersuc() {
		if(getWftOldTotalStat() == null){
			return 0l;
		}else{
			return getWftOldTotalStat().getOpersuc();
		}
		//return opersuc;
	}
	public void setOpersuc(Long opersuc) {
		this.opersuc = opersuc;
	}
	public String getDayconnlv() {
		if(getConnsuc()==0){
			return "0.00";
		}else if(getOconnsuc()==null || getOconnsuc()==0){
			return "100.00";
		}else{
			return df.format(get45(((getConnsuc().floatValue()-getOconnsuc().floatValue())/(getOconnsuc())*100)));
		}
	}
	public void setDayconnlv(String dayconnlv) {
		this.dayconnlv = dayconnlv;
	}
	public String getDayperlv() {
		if(getPersuc()==0){
			return "0.00";
		}else if(getOpersuc()==null || getOpersuc()==0){
			return "100.00";
		}else{
			return df.format(get45(((getPersuc().floatValue()-getOpersuc().floatValue())/(getOpersuc())*100)));
		}
	}
	public void setDayperlv(String dayperlv) {
		this.dayperlv = dayperlv;
	}
	public String getOutvaluetrueshow() {
		if(getUtvaluetrue()/3600>10000){
			float result=getUtvaluetrue().floatValue()/(10000*3600);
			return df.format(result)+"w";
		}else{
			return df.format(getUtvaluetrue().floatValue()/3600).toString();
		}
	}
	public void setOutvaluetrueshow(String outvaluetrueshow) {
		this.outvaluetrueshow = outvaluetrueshow;
	}
	public String getOcoccsucshow() {
		if(getConnsuc()>100000){
			float result=getConnsuc().floatValue()/10000;
			return df.format(result)+"w";
		}else{
			return getConnsuc().toString();
		}
	}
	public void setOcoccsucshow(String ococcsucshow) {
		this.ococcsucshow = ococcsucshow;
	}
	public String getOpersucshow() {
		if(getPersuc()>100000){
			float result=getPersuc().floatValue()/10000;
			return df.format(result)+"w";
		}else{
			return getPersuc().toString();
		}
	}
	public void setOpersucshow(String opersucshow) {
		this.opersucshow = opersucshow;
	}

	public String getPrvname() {
		return prvname;
	}
	public void setPrvname(String prvname) {
		this.prvname = prvname;
	}

	public WftOldTotalStat getWftOldTotalStat() {
		return wftOldTotalStat;
	}
	public void setWftOldTotalStat(WftOldTotalStat wftOldTotalStat1) {
		this.wftOldTotalStat = wftOldTotalStat1;
	}
	
	
}
