package com.bw30.open.common.model;

import java.util.Date;

public class WftUser {
	private String uid;
	private String channel;
	private String openid;
	private Date rtime;
	private String version;
	private String aversion;
	private Integer prvid;
	private Date lastlogintime;
	
	public WftUser(){
		
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}


	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public Date getRtime() {
		return rtime;
	}

	public void setRtime(Date rtime) {
		this.rtime = rtime;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getAversion() {
		return aversion;
	}

	public void setAversion(String aversion) {
		this.aversion = aversion;
	}

	public Integer getPrvid() {
		return prvid;
	}

	public void setPrvid(Integer prvid) {
		this.prvid = prvid;
	}

	public Date getLastlogintime() {
		return lastlogintime;
	}

	public void setLastlogintime(Date lastlogintime) {
		this.lastlogintime = lastlogintime;
	}
	
}
