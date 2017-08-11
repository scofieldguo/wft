package com.bw30.open.wft.common.cardpool.proto.resp;

import com.bw30.open.wft.common.cardpool.proto.Resp;

public class OpenCardResp extends Resp{
	private String cno;
	private String pwd;
	private Integer balance;
	private String validity;
	
	public OpenCardResp(){
		super();
	}

	public String getCno() {
		return cno;
	}

	public void setCno(String cno) {
		this.cno = cno;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public Integer getBalance() {
		return balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}

	public String getValidity() {
		return validity;
	}

	public void setValidity(String validity) {
		this.validity = validity;
	}
	
}
