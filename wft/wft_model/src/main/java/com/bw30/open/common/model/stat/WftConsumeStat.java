package com.bw30.open.common.model.stat;

import java.util.Date;

/**
 * 运营商实际消耗时长统计，运营商平台统计
 * @author Jack
 *
 * 2014年9月29日
 */
public class WftConsumeStat {
	private Integer id;
	private String dairy;//yyyy-MM-dd
	private String channel;
	private String channelname;
	
	private Integer opid;
	private Integer tvalue;//秒
	private String tvalueStr;
	private Long utvaluel;
	
	private Date intime;
	
	public WftConsumeStat(){
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDairy() {
		return dairy;
	}

	public void setDairy(String dairy) {
		this.dairy = dairy;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getChannelname() {
		return channelname;
	}

	public void setChannelname(String channelname) {
		this.channelname = channelname;
	}

	public Integer getOpid() {
		return opid;
	}

	public void setOpid(Integer opid) {
		this.opid = opid;
	}

	public Integer getTvalue() {
		return tvalue;
	}

	public void setTvalue(Integer tvalue) {
		this.tvalue = tvalue;
	}

	public Date getIntime() {
		return intime;
	}

	public void setIntime(Date intime) {
		this.intime = intime;
	}

	public String getTvalueStr() {
		return tvalueStr;
	}

	public void setTvalueStr(String tvalueStr) {
		this.tvalueStr = tvalueStr;
	}

	public Long getUtvaluel() {
		return utvaluel;
	}

	public void setUtvaluel(Long utvaluel) {
		this.utvaluel = utvaluel;
	}
	
}
