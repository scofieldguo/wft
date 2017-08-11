package com.bw30.open.wft.mongo.model;

import java.util.Date;

/**
 * 合作方开卡订单表
 * 
 * @author Jack
 * @date 2014年7月16日 下午5:10:12
 */
public class CardOpenRecord {
	/**
	 * 订单状态：提交
	 */
	public static final int STATUS_SUBMIT = 0;
	/**
	 * 订单状态：成功
	 */
	public static final int STATUS_OK = 1;
	/**
	 * 订单状态：失败
	 */
	public static final int STATUS_FAIL =2;
	
	private String orderid;
	private String partner;
	private String tradesn;
	private Integer status = STATUS_SUBMIT;
	private Integer cid;
	private Integer op;
	private String cno;
	private String ctype;
	private Date stime;
	private Date etime;
	private String standby;
	private Date intime;
	
	private String error;
	
	public CardOpenRecord(){
		
	}
	
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}
	public String getTradesn() {
		return tradesn;
	}
	public void setTradesn(String tradesn) {
		this.tradesn = tradesn;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getCid() {
		return cid;
	}
	public void setCid(Integer cid) {
		this.cid = cid;
	}
	public Integer getOp() {
		return op;
	}
	public void setOp(Integer op) {
		this.op = op;
	}
	public String getCno() {
		return cno;
	}
	public void setCno(String cno) {
		this.cno = cno;
	}
	public String getCtype() {
		return ctype;
	}
	public void setCtype(String ctype) {
		this.ctype = ctype;
	}
	public Date getStime() {
		return stime;
	}
	public void setStime(Date stime) {
		this.stime = stime;
	}
	public Date getEtime() {
		return etime;
	}
	public void setEtime(Date etime) {
		this.etime = etime;
	}
	public String getStandby() {
		return standby;
	}
	public void setStandby(String standby) {
		this.standby = standby;
	}
	public Date getIntime() {
		return intime;
	}
	public void setIntime(Date intime) {
		this.intime = intime;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
}
