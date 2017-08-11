package com.bw30.open.wft.common.cardpool.proto.req;

import com.bw30.open.wft.common.cardpool.proto.Req;

/**
 * 下线通知
 * 
 * @author Jack
 *
 * 2014年9月5日
 */
public class OfflineNotifyReq extends Req{
	private String openid;
	private String csid;
	private Integer flag;
	private String betime;
	private String sign;
	
	public OfflineNotifyReq(){
		super();
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

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public String getBetime() {
		return betime;
	}

	public void setBetime(String betime) {
		this.betime = betime;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
}
