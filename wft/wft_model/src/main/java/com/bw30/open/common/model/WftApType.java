package com.bw30.open.common.model;

import java.io.Serializable;

/**
 * 热点类型
 * 
 * @author Jack
 *
 * 2014年11月20日
 */
public class WftApType implements Serializable {
	private static final long serialVersionUID = 4075315024043795976L;
	private String ssid;
	private Integer opid;
	private Integer cflag;
	
	/**取卡标识：取卡*/
	public static final int CFLAG_GET_CARD = 1;
	/**取卡标识：不取卡*/
	public static final int CFLAG_NO_CARD = 0;
	
	public WftApType(){
		
	}

	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	public Integer getOpid() {
		return opid;
	}

	public void setOpid(Integer opid) {
		this.opid = opid;
	}

	public Integer getCflag() {
		return cflag;
	}

	public void setCflag(Integer cflag) {
		this.cflag = cflag;
	}

}
