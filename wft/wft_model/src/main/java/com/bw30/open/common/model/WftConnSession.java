package com.bw30.open.common.model;

import java.io.Serializable;
import java.util.Date;

public class WftConnSession implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6407997252325516304L;
	/** 连接会话创建状态 */
	public static final int STATUS_NEW = 1;
	/** 连接会话活动状态 */
	public static final int STATUS_ALIVE = 2;
	/** 连接会话结束*/
	public static final int STATUS_CLOSE = 3;
	
	/**
	 * 使用状态：0，未知
	 */
	public static final int UFLAG_UNKNOWN = 0;
	/**
	 * 使用状态：1，成功
	 */
	public static final int UFLAG_SUCCESS = 1;
	/**
	 * 使用状态：2，失败
	 */
	public static final int UFLAG_FAIL = 2;
	
	/**
	 * 取卡失败
	 */
	public static final int USTATUS_CARD_FAIL = 0;
	/**
	 * 取卡成功，登录中
	 */
	public static final int USTATUS_LOGINING = 1;
	/** 
	 * 登录失败 
	 */
	public static final int USTATUS_LOGIN_FAIL = 2;
	/**
	 * 登录成功
	 */
	public static final int USTATUS_LOGIN_SUCCESS = 3;
	/**
	 * 正常退出
	 */
	public static final int USTATUS_CLOSED_NORMAL = 4;
	/**
	 * 服务器强制断开 
	 */
	public static final int USTATUS_CLOSED_FORCE = 5;
	
	/**
	 * 远程下线状态：无需下线
	 */
	public static final int LSTATUS_UNNEED = 0;
	/**
	 * 远程下线：等待下线
	 */
	public static final int LSTATUS_WAITING = 1;
	/**
	 * 远程下线状态：已下线
	 */
	public static final int LSTATUS_OK = 2;
	
	/** 比对标志,未对比 */
	public static final int CONN_RECORD_FLAG_NO = 0;
	/** 比对标志，已比对 */
	public static final int CONN_RECORD_FALG_YES = 1;
	
	public static final int CONN_RECORD_FALG_ERROR=2;
	
	/**系统类型：android*/
	public static final int TYPE_OS_ANDROID = 1;
	/**系统类型：ios*/
	public static final int TYPE_OS_IOS = 2;
	
	
    private String csid; // 连接会话ID
    private String uid; //用户ID
    private Date stime; // 会话开始时间
    private Date etime; // 会话结束时间
    private Integer status; //会话状态
    private Integer ustatus; //使用状态
    private Date bstime; // 计费开始时间
    private Date betime; // 计费结束时间
    private Integer cid; // 卡id
    private String cno; // 卡号
    private Integer hsid; // 热点ID
    private String ssid; // 热点标识
    private String mac; // 热点mac
    private Integer uflag; // 使用标识
    private String note; // 登录错误码
    private String ip; // IP地址
    private Integer prvid; // 省份ID
    private String channel; // 渠道code
    private String version; // 版本
    private Integer lstatus;
    private String logouturl; // 下线动态地址
    private Integer conntime;//热点连接时间
    private String wifiip;//wifi接入ip
    
    private Integer utvalue; // 使用时长，秒
	private Date bstimeop; // 运营商计费开始时间
	private Date betimeop; // 运营商计费结束时间
	private Integer utvalueop; // 运营商使用时长，秒
	private Integer uhourop; // 运营商使用时长，小时
	private Integer mflag;  // 对比标识
    
	private Date bstimeqq;//用户上线时间，合作方记录的时间
	private Date betimeqq;//用户下线时间，合作方记录的时间
	
	private String csidqq;
	private Integer flagqq;
	
	private Integer mvalue = 0;//修正时长，秒
	
	private Integer opid;//运营商id
	
	private Integer ostype;//系统类型
	private String osversion;//系统版本
	
	private String imei;
	
	public String getCsid() {
		return csid;
	}
	public void setCsid(String csid) {
		this.csid = csid;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getUstatus() {
		return ustatus;
	}
	public void setUstatus(Integer ustatus) {
		this.ustatus = ustatus;
	}
	public Date getBstime() {
		return bstime;
	}
	public void setBstime(Date bstime) {
		this.bstime = bstime;
	}
	public Date getBetime() {
		return betime;
	}
	public void setBetime(Date betime) {
		this.betime = betime;
	}
	public Integer getCid() {
		return cid;
	}
	public void setCid(Integer cid) {
		this.cid = cid;
	}
	public String getCno() {
		return cno;
	}
	public void setCno(String cno) {
		this.cno = cno;
	}
	public Integer getHsid() {
		return hsid;
	}
	public void setHsid(Integer hsid) {
		this.hsid = hsid;
	}
	public String getSsid() {
		return ssid;
	}
	public void setSsid(String ssid) {
		this.ssid = ssid;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public Integer getUflag() {
		return uflag;
	}
	public void setUflag(Integer uflag) {
		this.uflag = uflag;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Integer getPrvid() {
		return prvid;
	}
	public void setPrvid(Integer prvid) {
		this.prvid = prvid;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Integer getLstatus() {
		return lstatus;
	}
	public void setLstatus(Integer lstatus) {
		this.lstatus = lstatus;
	}
	public String getLogouturl() {
		return logouturl;
	}
	public void setLogouturl(String logouturl) {
		this.logouturl = logouturl;
	}
	public void setConntime(Integer conntime) {
		this.conntime = conntime;
	}
	public Integer getConntime() {
		return conntime;
	}
	public Integer getUtvalue() {
		return utvalue;
	}
	public void setUtvalue(Integer utvalue) {
		this.utvalue = utvalue;
	}
	public Date getBstimeop() {
		return bstimeop;
	}
	public void setBstimeop(Date bstimeop) {
		this.bstimeop = bstimeop;
	}
	public Date getBetimeop() {
		return betimeop;
	}
	public void setBetimeop(Date betimeop) {
		this.betimeop = betimeop;
	}
	public Integer getUtvalueop() {
		return utvalueop;
	}
	public void setUtvalueop(Integer utvalueop) {
		this.utvalueop = utvalueop;
	}
	public Integer getUhourop() {
		return uhourop;
	}
	public void setUhourop(Integer uhourop) {
		this.uhourop = uhourop;
	}
	public Integer getMflag() {
		return mflag;
	}
	public void setMflag(Integer mflag) {
		this.mflag = mflag;
	}
	public String getWifiip() {
		return wifiip;
	}
	public void setWifiip(String wifiip) {
		this.wifiip = wifiip;
	}
	public Date getBstimeqq() {
		return bstimeqq;
	}
	public void setBstimeqq(Date bstimeqq) {
		this.bstimeqq = bstimeqq;
	}
	public Date getBetimeqq() {
		return betimeqq;
	}
	public void setBetimeqq(Date betimeqq) {
		this.betimeqq = betimeqq;
	}
	public String getCsidqq() {
		return csidqq;
	}
	public void setCsidqq(String csidqq) {
		this.csidqq = csidqq;
	}
	public void setFlagqq(Integer flagqq) {
		this.flagqq = flagqq;
	}
	public Integer getFlagqq() {
		return flagqq;
	}
	public Integer getMvalue() {
		return mvalue;
	}
	public void setMvalue(Integer mvalue) {
		this.mvalue = mvalue;
	}
	
	public Integer getOpid() {
		return opid;
	}
	public void setOpid(Integer opid) {
		this.opid = opid;
	}
	public Integer getOstype() {
		return ostype;
	}
	public void setOstype(Integer ostype) {
		this.ostype = ostype;
	}
	public String getOsversion() {
		return osversion;
	}
	public void setOsversion(String osversion) {
		this.osversion = osversion;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	
}
