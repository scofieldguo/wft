package com.bw30.open.wft.common.cardpool.rmi.bean;

import java.io.Serializable;
import java.util.Date;

public class CardBean extends RespBean implements Serializable{
	private static final long serialVersionUID = 3317170978888061658L;
	
	//开放平台卡id
	private Integer id;
	//省份id
	private Integer prvId;
	//卡类型
	private Integer opid;
	//卡类型
	private String ssid;
	//卡号
	private String no;
	//卡密
	private String pwd;
	//卡品
	private int cardType = 0;
	//时长，秒
	private int balance = 0;
	//有效截止日期
	private Date validity;
	
	public CardBean(){
		
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPrvId() {
		return prvId;
	}

	public void setPrvId(Integer prvId) {
		this.prvId = prvId;
	}

	public Integer getOpid() {
		return opid;
	}

	public void setOpid(Integer opid) {
		this.opid = opid;
	}

	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
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

	public int getCardType() {
		return cardType;
	}

	public void setCardType(int cardType) {
		this.cardType = cardType;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public Date getValidity() {
		return validity;
	}

	public void setValidity(Date validity) {
		this.validity = validity;
	}
	
}
