package com.bw30.open.wft.common.cardpool.rmi.bean;

import java.io.Serializable;

/**
 * 卡使用记录
 * 
 * @author David
 * 
 */
public class CardRecord implements Serializable {
	private static final long serialVersionUID = -6071564721066518966L;

	private String cno;
	private String starttime;
	private String endtime;
	private int timelen;
	private String mack;
	private String nasip;
	private String nasport;
	private String nasporttype;
	private String sessionid;
	private String framedip;

	public CardRecord() {

	}

	public String getCno() {
		return cno;
	}

	public void setCno(String cno) {
		this.cno = cno;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public int getTimelen() {
		return timelen;
	}

	public void setTimelen(int timelen) {
		this.timelen = timelen;
	}

	public String getMack() {
		return mack;
	}

	public void setMack(String mack) {
		this.mack = mack;
	}

	public String getNasip() {
		return nasip;
	}

	public void setNasip(String nasip) {
		this.nasip = nasip;
	}

	public String getNasport() {
		return nasport;
	}

	public void setNasport(String nasport) {
		this.nasport = nasport;
	}

	public String getNasporttype() {
		return nasporttype;
	}

	public void setNasporttype(String nasporttype) {
		this.nasporttype = nasporttype;
	}

	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public String getFramedip() {
		return framedip;
	}

	public void setFramedip(String framedip) {
		this.framedip = framedip;
	}

	public String toString() {
		return new StringBuffer(this.cno).append(",").append(this.starttime)
				.append(",").append(this.endtime).append(",")
				.append(this.timelen).toString();
	}

}
