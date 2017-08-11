package com.bw30.open.common.model;

import java.io.Serializable;

public class CardCacheInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -585265708241566836L;
	
	public static final Integer STATUS_OFF = 0;
	public static final Integer STATUS_ON = 1;
	
	private String channel; // 渠道
	private Integer prvid; // 省份Id
	private Integer type; // 类别
	private Integer opid; // 运营商Id
	private Integer status; // 状态  0：不可用1：可用
	
	
	
	
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public Integer getPrvid() {
		return prvid;
	}
	public void setPrvid(Integer prvid) {
		this.prvid = prvid;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getOpid() {
		return opid;
	}
	public void setOpid(Integer opid) {
		this.opid = opid;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}
