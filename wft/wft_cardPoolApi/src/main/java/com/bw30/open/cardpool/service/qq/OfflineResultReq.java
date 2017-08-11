package com.bw30.open.cardpool.service.qq;

/**
 * 下线结果通知
 * 
 * @author Jack
 *
 * 2014年9月10日
 */
public class OfflineResultReq {
	private String appid;
	private String openid;
	private String csid;
	private Integer status;
	private String sign;
	
	public OfflineResultReq(){
		
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getCsid() {
		return csid;
	}

	public void setCsid(String csid) {
		this.csid = csid;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
}
