package com.bw30.open.common.model;

import java.io.Serializable;
import java.util.Date;

public class WftLoginData implements Serializable {
	private static final long serialVersionUID = 1137592443122567437L;
	private String lsid;  // 登录Id
	private String uid;  // 用户Id
	private String version; // 版本号
	private Date logintime; // 登录时间
	private Double lat; // 经度
	private Double lon; // 维度
	private Integer prvid; // 省份ID
	private String ip; // IP
	private String channel;
	private String osver;
	
	public String getLsid() {
		return lsid;
	}
	public void setLsid(String lsid) {
		this.lsid = lsid;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Date getLogintime() {
		return logintime;
	}
	public void setLogintime(Date logintime) {
		this.logintime = logintime;
	}
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public Double getLon() {
		return lon;
	}
	public void setLon(Double lon) {
		this.lon = lon;
	}
	public Integer getPrvid() {
		return prvid;
	}
	public void setPrvid(Integer prvid) {
		this.prvid = prvid;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getOsver() {
		return osver;
	}
	public void setOsver(String osver) {
		this.osver = osver;
	}
}
