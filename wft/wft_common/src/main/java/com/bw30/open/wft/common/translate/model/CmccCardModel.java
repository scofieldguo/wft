package com.bw30.open.wft.common.translate.model;

import java.io.Serializable;

import com.bw30.open.wft.common.translate.inter.FieldMeta;

public class CmccCardModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8132707967719165384L;

	@FieldMeta(name = "卡号")
	private String mobileNo;
	
	@FieldMeta(name = "密码")
	private String password;
	
	@FieldMeta(name = "剩余时长")
	private String bvalue;

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBvalue() {
		return bvalue;
	}

	public void setBvalue(String bvalue) {
		this.bvalue = bvalue;
	}

}
