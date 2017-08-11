package com.bw30.open.common.model;

import java.io.Serializable;

public class WftCardType implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1277248289908281820L;
	private Integer id;
	private String code;
	private String codeop;
	private Integer opid;
	private Integer balance;
	private String standby;
	
	public WftCardType(){
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getOpid() {
		return opid;
	}

	public void setOpid(Integer opid) {
		this.opid = opid;
	}

	public String getCodeop() {
		return codeop;
	}

	public void setCodeop(String codeop) {
		this.codeop = codeop;
	}

	public Integer getBalance() {
		return balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}

	public String getStandby() {
		return standby;
	}

	public void setStandby(String standby) {
		this.standby = standby;
	}
	
}
