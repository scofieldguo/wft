package com.bw30.open.wft.mongo.model;

import java.util.Date;

/**
 * 取卡失败记录
 * @author Jack
 *
 * 2014年9月16日
 */
public class NoCardRecord {
	private Date time;
	private Integer opid;
	private String ssid;
	
	public NoCardRecord(){
		
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
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
	
}
