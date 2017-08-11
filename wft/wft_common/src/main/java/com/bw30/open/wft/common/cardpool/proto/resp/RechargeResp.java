package com.bw30.open.wft.common.cardpool.proto.resp;

import com.bw30.open.wft.common.cardpool.proto.Resp;

public class RechargeResp extends Resp{
	private String cno;
	private Integer balance;
	private Integer oldbalance;
	private Integer newbalance;
	private String validity;
	
	public RechargeResp(){
		super();
	}

	public String getCno() {
		return cno;
	}

	public void setCno(String cno) {
		this.cno = cno;
	}

	public Integer getBalance() {
		return balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}

	public Integer getOldbalance() {
		return oldbalance;
	}

	public void setOldbalance(Integer oldbalance) {
		this.oldbalance = oldbalance;
	}

	public Integer getNewbalance() {
		return newbalance;
	}

	public void setNewbalance(Integer newbalance) {
		this.newbalance = newbalance;
	}

	public String getValidity() {
		return validity;
	}

	public void setValidity(String validity) {
		this.validity = validity;
	}
	
}
