package com.bw30.open.common.model;

import java.io.Serializable;

public class WftOperator implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 531684388264614857L;
	/**
	 * 运营商id：移动
	 */
	public static final int OP_ID_CMCC = 1;
	/**
	 * 运营商id：电信
	 */
	public static final int OP_ID_CTCC = 2;
	/**
	 * 运营商id：联通
	 */
	public static final int OP_ID_CUCC = 3;
	/**
	 * 运营商id：北纬通信
	 */
	public static final int OP_ID_BWLAN = 4;
	/**
	 * 运营商id：百米生活
	 */
	public static final int OP_ID_100MSH = 5;
	
	/**
	 * 移动ssid：CMCC
	 */
	public static final String OP_SSID_CMCC = "CMCC";
	/**
	 * 联通ssid：ChinaUnicom
	 */
	public static final String OP_SSID_CUCC = "ChinaUnicom";
	/**
	 * 电信ssid：ChinaNet
	 */
	public static final String OP_SSID_CTCC = "ChinaNet";
	/**
	 * 北纬通信ssid：bwlan，用于测试
	 */
	public static final String OP_SSID_BW = "bwlan";
	/**
	 * 百米生活ssid
	 */
	public static final String OP_SSID_100MSH = "100msh";
	
	
    private Integer id;
    private String name;
    private String sname;
    private String ssid;
    private Integer status;
    public WftOperator(){
    	
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
    
}