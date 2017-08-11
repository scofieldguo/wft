package com.bw30.open.common.model;

import java.io.Serializable;
import java.util.Date;

public class WftHotspot  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 455969846233719327L;
	/**
	 * 热点类型：运营商热点
	 */
	public static final int AP_TYPE_OPERATOR = 1;
	/**
	 * 热点类型：云热点
	 */
	public static final int AP_TYPE_CLOUD = 0;
	
	/**
	 * 热点加密类型：无密码
	 */
	public static final int AP_CTYPE_NO = 0;
	/**
	 * 热点加密类型：wpa、wpa2
	 */
	public static final int AP_CTYPE_WPA = 1;
	/**
	 * 热点加密类型：wep
	 */
	public static final int AP_CTYPE_WEP = 2;
	
	/**
	 * 热点状态：不可用
	 */
	public static final int FLAG_INVALID = 0;
	/**
	 * 热点状态：可用
	 */
	public static final int FLAG_VALID = 1;
	
    private Integer id;
    private Integer opid;
    private String ssid;
    private Integer ctype;
    private String sspwd;
    private String mac;
    private Double lat;
    private Double lon;
    private Integer prvid;
    private Date ctime;
    private Integer type;
    private Integer flag;
    private Integer health;
    
    public WftHotspot(){
    	
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
	public Integer getCtype() {
		return ctype;
	}
	public void setCtype(Integer ctype) {
		this.ctype = ctype;
	}
	public String getSspwd() {
		return sspwd;
	}
	public void setSspwd(String sspwd) {
		this.sspwd = sspwd;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public Double getLon() {
		return lon;
	}
	public void setLon(Double lon) {
		this.lon = lon;
	}
	public Integer getPrvid() {
		return prvid;
	}
	public void setPrvid(Integer prvid) {
		this.prvid = prvid;
	}
	public Date getCtime() {
		return ctime;
	}
	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	public Integer getHealth() {
		return health;
	}
	public void setHealth(Integer health) {
		this.health = health;
	}
    
}