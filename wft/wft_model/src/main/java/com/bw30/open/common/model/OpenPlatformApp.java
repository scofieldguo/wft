package com.bw30.open.common.model;

import java.io.Serializable;
import java.util.Date;

import com.bw30.open.wft.common.DateUtil;

public class OpenPlatformApp implements Serializable{

	/**
	 * 
	 */
	
	public static final Integer STATUS_TEST=0; //测试
	public static final Integer STATUS_ONLINE=1; // 正式
	public static final Integer STATUS_STOP=2; // 已停止
	public static final Integer STATUS_EXAMINE=3; // 审核
	public static final Integer STATUS_NOPASS=4;// 未通过
	
	private static final long serialVersionUID = 1L;
	

	private Integer id; // 应用ID
	private String openkey; // 应用Key
	private String name; // 应用名
	private Integer uid; // 开放平台用户ID
	private String packages; // 应用包名
	private String opids; // WIFI运营商字符串
	private Integer type; // 应用类别
	private String website; // 应用官网
	private String introduction; // 应用简介
	private Integer status; // 应用状态
	private Date ctime; // 应用创建时间
	private String ctimestr;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getOpenkey() {
		return openkey;
	}
	public void setOpenkey(String openkey) {
		this.openkey = openkey;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getOpids() {
		return opids;
	}
	public void setOpids(String opids) {
		this.opids = opids;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getCtime() {
		return ctime;
	}
	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}
	public String getPackages() {
		return packages;
	}
	public void setPackages(String packages) {
		this.packages = packages;
	}
	public String getCtimestr() {
		return DateUtil.formateDate(getCtime(), "yyyy-MM-dd");
	}
	public void setCtimestr(String ctimestr) {
		this.ctimestr = ctimestr;
	}
	
	
}
