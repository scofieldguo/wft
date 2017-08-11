package com.bw30.open.common.model;

import java.io.Serializable;
import java.util.Date;

import com.bw30.open.wft.common.DateUtil;

public class OpenPlatformNotice implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4974386769092920209L;
	public static final Integer READ=1;
	public static final Integer UN_READ=0;
	private Integer id;
	private Integer userid;
	private String message;
	private Integer status;
	private Date intime;
	private String intimeStr;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getIntime() {
		return intime;
	}
	public void setIntime(Date intime) {
		this.intime = intime;
	}
	public String getIntimeStr() {
		return DateUtil.formateDate(getIntime(), "yyyy-MM-dd");
	}
	public void setIntimeStr(String intimeStr) {
		this.intimeStr = intimeStr;
	}
	
}
