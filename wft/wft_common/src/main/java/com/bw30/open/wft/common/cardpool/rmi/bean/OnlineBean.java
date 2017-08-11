package com.bw30.open.wft.common.cardpool.rmi.bean;

import java.io.Serializable;

public class OnlineBean extends RespBean implements Serializable{
	private static final long serialVersionUID = 7343655287597657673L;
	
	private String realm;
	private String starttime;
	private String bindattr;
	private String mack;
	private String nasip;
	private String nasport;
	private String nasporttype;
	private String sessionid;
	private String framedip;
	private String roamflag;
	
	public OnlineBean(){
		
	}

	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getBindattr() {
		return bindattr;
	}

	public void setBindattr(String bindattr) {
		this.bindattr = bindattr;
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

	public String getRoamflag() {
		return roamflag;
	}

	public void setRoamflag(String roamflag) {
		this.roamflag = roamflag;
	}
	
}
