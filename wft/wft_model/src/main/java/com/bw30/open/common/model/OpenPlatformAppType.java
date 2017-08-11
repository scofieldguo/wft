package com.bw30.open.common.model;

import java.io.Serializable;
import java.util.Date;

public class OpenPlatformAppType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id; // 应用类型Id
	private String name; // 应用类型名
	private Integer status; // 状态 0：不可用1：可用
	private Date ctime; // 创建时间
	
	public static final Integer STATUS_ON=1; // 可用
	public static final Integer STATUS_OFF=0; // 不可用
	
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getCtime() {
		return ctime;
	}
	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}
	
}
