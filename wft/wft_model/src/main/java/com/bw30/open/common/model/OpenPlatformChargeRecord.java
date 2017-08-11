package com.bw30.open.common.model;

import java.io.Serializable;
import java.util.Date;

public class OpenPlatformChargeRecord implements Serializable{

	/**
	 * 用户充值记录
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer userid; 
	private Date dairy; //充值时间
	private float chargehours; //充值时长
	private float chargecost; //充值金额	
	
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
	public Date getDairy() {
		return dairy;
	}
	public void setDairy(Date dairy) {
		this.dairy = dairy;
	}
	public float getChargehours() {
		return chargehours;
	}
	public void setChargehours(float chargehours) {
		this.chargehours = chargehours;
	}
	public float getChargecost() {
		return chargecost;
	}
	public void setChargecost(float chargecost) {
		this.chargecost = chargecost;
	}
	

}
