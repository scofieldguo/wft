package com.bw30.open.wft.common.cardpool.proto.req;

import com.bw30.open.wft.common.cardpool.proto.Req;

/**
 * 上线通知
 * 
 * @author Jack
 *
 * 2014年9月5日
 */
public class OnlineNotifyReq extends Req{
	private String openid;
	private String csid;
	private String bstime;
	private String sign;
	
	public OnlineNotifyReq(){
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

	public String getBstime() {
		return bstime;
	}

	public void setBstime(String bstime) {
		this.bstime = bstime;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
}
