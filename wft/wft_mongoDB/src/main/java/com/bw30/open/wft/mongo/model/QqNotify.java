package com.bw30.open.wft.mongo.model;

import java.util.Date;

public class QqNotify {
	private String uid;
	private String csid;
	private String csidqq;
	private String ssid;
	private Integer flag;
	private Date bstime;
	private Date betime;
	private Integer timelen;
	
	public QqNotify(){
		
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getCsid() {
		return csid;
	}

	public void setCsid(String csid) {
		this.csid = csid;
	}

	public String getCsidqq() {
		return csidqq;
	}

	public void setCsidqq(String csidqq) {
		this.csidqq = csidqq;
	}

	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Date getBstime() {
		return bstime;
	}

	public void setBstime(Date bstime) {
		this.bstime = bstime;
	}

	public Date getBetime() {
		return betime;
	}

	public void setBetime(Date betime) {
		this.betime = betime;
	}

	public Integer getTimelen() {
		return timelen;
	}

	public void setTimelen(Integer timelen) {
		this.timelen = timelen;
	}
	
}
