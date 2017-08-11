package com.bw30.open.common.model;

import java.io.Serializable;
import java.util.Date;

public class WftCardActive implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8242537768482989999L;

	private String channel;//用于分表
	
	/**
	 * 卡类型：账期卡
	 */
	public static final int CARD_TYPE_ZHANGQI = 5;
	
	/**
	 * 状态：可用
	 */
	public static final int CSTATUS_AVAILABLE = 2;
	/**
	 * 状态：占用，使用中
	 */
	public static final int CSTATUS_USING = 3;
	/**
	 * 状态：停用
	 */
	public static final int CSTATUS_STOP = 4;
	
	public static final int IN_CACHE=1;
	
	public static final int OUT_CACHE=2;
	
    private Integer id;
    private Integer opid;
    private String ssid;
    private Integer prvid;
    private Integer ctype;
    private String no;
    private String pwd;
    private Integer bvalue;
    private Date vbtime;
    private Date vetime;
    private Integer ustatus;
    private Integer cstatus;
    private Integer tvalue;
    private Date intime;
    private Integer utvalue;
   	private Integer ucount;
    private Integer uscount;
    private Date utime;
    private String uid;
    private Integer cache;
    
    private Date stoptime;
    private String stopcode;
    
    public WftCardActive(){
    	
    }
    
    public WftCardActive(String channel){
    	this.channel = channel;
    }
    
	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getChannel() {
		return channel;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public Integer getPrvid() {
		return prvid;
	}
	public void setPrvid(Integer prvid) {
		this.prvid = prvid;
	}
	public Integer getCtype() {
		return ctype;
	}
	public void setCtype(Integer ctype) {
		this.ctype = ctype;
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
	public Integer getBvalue() {
		return bvalue;
	}
	public void setBvalue(Integer bvalue) {
		this.bvalue = bvalue;
	}
	public Date getVbtime() {
		return vbtime;
	}
	public void setVbtime(Date vbtime) {
		this.vbtime = vbtime;
	}
	public Date getVetime() {
		return vetime;
	}
	public void setVetime(Date vetime) {
		this.vetime = vetime;
	}
	public Integer getUstatus() {
		return ustatus;
	}
	public void setUstatus(Integer ustatus) {
		this.ustatus = ustatus;
	}
	public Integer getCstatus() {
		return cstatus;
	}
	public void setCstatus(Integer cstatus) {
		this.cstatus = cstatus;
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
	public Integer getUtvalue() {
		return utvalue;
	}
	public void setUtvalue(Integer utvalue) {
		this.utvalue = utvalue;
	}
	public Integer getUcount() {
		return ucount;
	}
	public void setUcount(Integer ucount) {
		this.ucount = ucount;
	}
	public Integer getUscount() {
		return uscount;
	}
	public void setUscount(Integer uscount) {
		this.uscount = uscount;
	}
	public Date getUtime() {
		return utime;
	}
	public void setUtime(Date utime) {
		this.utime = utime;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}

	public Integer getCache() {
		return cache;
	}

	public void setCache(Integer cache) {
		this.cache = cache;
	}

	public Date getStoptime() {
		return stoptime;
	}

	public void setStoptime(Date stoptime) {
		this.stoptime = stoptime;
	}

	public String getStopcode() {
		return stopcode;
	}

	public void setStopcode(String stopcode) {
		this.stopcode = stopcode;
	}
    
}