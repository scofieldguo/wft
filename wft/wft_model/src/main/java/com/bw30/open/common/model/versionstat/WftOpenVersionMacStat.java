package com.bw30.open.common.model.versionstat;

public class WftOpenVersionMacStat {

	private String channel;
	private String dairy;
	private String version;
	private Integer succcnt=0;
	private Integer failcnt=0;
	private String mac;
	
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getDairy() {
		return dairy;
	}
	public void setDairy(String dairy) {
		this.dairy = dairy;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
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
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	
	
	
}
