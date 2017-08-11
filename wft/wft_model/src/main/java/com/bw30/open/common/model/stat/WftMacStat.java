package com.bw30.open.common.model.stat;

import java.io.Serializable;
import java.util.Date;

public class WftMacStat implements Serializable {
	private static final long serialVersionUID = -6921462393342291868L;
	private Date dairy;
	private String mac;
	private String ssid;
	private Integer opid;
	private String channel;
	private Integer prvid;
	private Integer succcnt=0;
	private Integer failcnt=0;
	
	
	public Date getDairy() {
		return dairy;
	}
	public void setDairy(Date dairy) {
		this.dairy = dairy;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public Integer getSucccnt() {
		return succcnt;
	}
	public void setSucccnt(Integer succcnt) {
		this.succcnt = succcnt;
	}
	public Integer getFailcnt() {
		return failcnt;
	}
	public void setFailcnt(Integer failcnt) {
		this.failcnt = failcnt;
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
	public Integer getPrvid() {
		return prvid;
	}
	public void setPrvid(Integer prvid) {
		this.prvid = prvid;
	}
	public Integer getOpid() {
		return opid;
	}
	public void setOpid(Integer opid) {
		this.opid = opid;
	}
	
}
