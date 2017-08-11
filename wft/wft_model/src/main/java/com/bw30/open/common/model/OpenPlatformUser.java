package com.bw30.open.common.model;

import java.io.Serializable;
import java.util.Date;

public class OpenPlatformUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id; // 用户Id
	private String pwd; // 密码
	private String qq; // QQ
	private String phone; // 电话号码
	private String mail;// 邮件
	private String name;// 姓名
	private String company; // 公司
	private String address; // 地址
	private Date ctime; // 创建时间
	private Integer status;//激活状态
	private String code;//激活验证码
	private String opids;
	private String channelcode;
	private WftChannel wftchannel;
	private OpenPlatformAccount openPlatformAccount;//用户时长信息账户
	private String send_mails;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Date getCtime() {
		return ctime;
	}
	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getOpids() {
		return opids;
	}
	public void setOpids(String opids) {
		this.opids = opids;
	}
	public String getChannelcode() {
		return channelcode;
	}
	public void setChannelcode(String channelcode) {
		this.channelcode = channelcode;
	}
	public WftChannel getWftchannel() {
		return wftchannel;
	}
	public void setWftchannel(WftChannel wftchannel) {
		this.wftchannel = wftchannel;
	}
	public OpenPlatformAccount getOpenPlatformAccount() {
		return openPlatformAccount;
	}
	public void setOpenPlatformAccount(OpenPlatformAccount openPlatformAccount) {
		this.openPlatformAccount = openPlatformAccount;
	}
	public String getSend_mails() {
		return send_mails;
	}
	public void setSend_mails(String send_mails) {
		this.send_mails = send_mails;
	}
	
	
}
