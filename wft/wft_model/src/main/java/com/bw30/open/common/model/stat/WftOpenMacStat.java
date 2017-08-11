package com.bw30.open.common.model.stat;

public class WftOpenMacStat {
	private String dairy;
	private String mac;
	private String channel;
	private Integer succcnt=0;
	private Integer failcnt=0;
	
	public String getDairy() {
		return dairy;
	}
	public void setDairy(String dairy) {
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
	
	
	
}
