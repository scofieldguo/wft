package com.bw30.open.wft.common.cardpool.rmi.bean;

import java.io.Serializable;
import java.util.Date;

public class RechargeBean extends RespBean implements Serializable{
	private static final long serialVersionUID = 7579562334861132076L;
	
	private String cno;
	private Integer balance = 0;
	private Integer oldbalance = 0;
	private Integer newbalance = 0;
	private Date validity;
	
	public RechargeBean(){
		
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

	public Date getValidity() {
		return validity;
	}

	public void setValidity(Date validity) {
		this.validity = validity;
	}
	
}
