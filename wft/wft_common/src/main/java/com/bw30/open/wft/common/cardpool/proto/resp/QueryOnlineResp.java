package com.bw30.open.wft.common.cardpool.proto.resp;

import com.bw30.open.wft.common.cardpool.proto.Resp;

public class QueryOnlineResp extends Resp{
	private String cno;
//	private String realm;
	private String starttime;
//	private String bindattr;
	private String mack;
	private String nasip;
	private String nasport;
//	private String nasporttype;
	private String sessionid;
	private String framedip;
//	private String roamflag;
	
	public QueryOnlineResp(){
		super();
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

}
