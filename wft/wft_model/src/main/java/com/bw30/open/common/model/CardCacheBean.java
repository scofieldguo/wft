package com.bw30.open.common.model;

import java.io.Serializable;

public class CardCacheBean implements Serializable{
	private static final long serialVersionUID = -7951083350715586393L;
	
	private Integer id; // 卡Id
	private Integer opid; // 运营商Id
	private String no; // 卡号
	private String pwd; // 密码
	private Integer prvid; // 卡的省份
	private Integer tvalue; // 卡可用时长
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getOpid() {
		return opid;
	}
	public void setOpid(Integer opid) {
		this.opid = opid;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public Integer getPrvid() {
		return prvid;
	}
	public void setPrvid(Integer prvid) {
		this.prvid = prvid;
	}
	public Integer getTvalue() {
		return tvalue;
	}
	public void setTvalue(Integer tvalue) {
		this.tvalue = tvalue;
	}
	
}
