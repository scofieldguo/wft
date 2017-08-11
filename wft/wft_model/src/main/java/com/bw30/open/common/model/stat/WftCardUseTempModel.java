package com.bw30.open.common.model.stat;

public class WftCardUseTempModel {

	private String channelName;
	private Integer available;
	private Integer overtime;
	private Integer useing;
	private String ssid;
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public Integer getAvailable() {
		return available;
	}
	public void setAvailable(Integer available) {
		this.available = available;
	}
	public Integer getUseing() {
		return useing;
	}
	public void setUseing(Integer useing) {
		this.useing = useing;
	}
	public String getSsid() {
		return ssid;
	}
	public void setSsid(String ssid) {
		this.ssid = ssid;
	}
	public Integer getOvertime() {
		return overtime;
	}
	public void setOvertime(Integer overtime) {
		this.overtime = overtime;
	}
}
