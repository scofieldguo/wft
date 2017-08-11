package com.bw30.open.common.model;

import java.io.Serializable;
import java.util.Date;

public class WftCardStore implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5877393874443567582L;
	/**
	 * 卡类型：包月卡
	 */
	public static final int CARD_TYPE_BAOYUE = 1;
	/**
	 * 卡类型：不可跨月时长卡
	 */
	public static final int CARD_TYPE_IN_MONTH_LEN = 2;
	/**
	 * 卡类型：可跨月时长卡
	 */
	public static final int CARD_TYPE_LEN = 3;
	/**
	 * 卡类型：电子卡
	 */
	public static final int CARD_TYPE_ELEC = 4;
	/**
	 * 卡类型：账期卡
	 */
	public static final int CARD_TYPE_ZHANGQI = 5;
	
	/**
	 * 卡状态：未启用
	 */
	public static final int CARD_CSTATUS_UNABLE = 0;
	/**
	 * 卡状态：启用
	 */
	public static final int CARD_CSTATUS_ENABLE = 1;
	/**
	 * 卡状态：可用
	 */
	public static final int CARD_CSTATUS_AVAILABLE = 2;
	/**
	 * 卡状态：占用
	 */
	public static final int CARD_CSTATUS_USING = 3;
	/**
	 * 卡状态：停用
	 */
	public static final int CARD_CSTATUS_STOPPED = 4;
	
	public static final int CARD_IN_STORE = 0;
	
	public static final int CARD_IN_ACTIVE = 1;
	
    private Integer id;
    private Integer opid;
    private String ssid;
    private Integer prvid;
    private Integer ctype;
    private String no;
    private String pwd;
    private Date vbtime;
    private Date vetime;
    private Integer bvalue;
    private Integer tvalue;
    private Date prtime;
    private Integer ustatus;
    private Integer cstatus;
    private Integer inplace;
    private Date atime;
    private Date intime;
    private Integer utvalue;
    private Integer ucount;
    private Integer uscount;
    private Double usp;//不入库
    private String orderid;
    private Date pctime;
    private Date utime;
    private Integer uid;
    
    private String channel;
    
    private Date stoptime;
    private String stopcode;
    
    public WftCardStore(){
    	
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

	public Integer getBvalue() {
		return bvalue;
	}

	public void setBvalue(Integer bvalue) {
		this.bvalue = bvalue;
	}

	public Integer getTvalue() {
		return tvalue;
	}

	public void setTvalue(Integer tvalue) {
		this.tvalue = tvalue;
	}

	public Date getPrtime() {
		return prtime;
	}

	public void setPrtime(Date prtime) {
		this.prtime = prtime;
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

	public Integer getInplace() {
		return inplace;
	}

	public void setInplace(Integer inplace) {
		this.inplace = inplace;
	}

	public Date getAtime() {
		return atime;
	}

	public void setAtime(Date atime) {
		this.atime = atime;
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

	public Double getUsp() {
		return usp;
	}

	public void setUsp(Double usp) {
		this.usp = usp;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public Date getPctime() {
		return pctime;
	}

	public void setPctime(Date pctime) {
		this.pctime = pctime;
	}

	public Date getUtime() {
		return utime;
	}

	public void setUtime(Date utime) {
		this.utime = utime;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
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