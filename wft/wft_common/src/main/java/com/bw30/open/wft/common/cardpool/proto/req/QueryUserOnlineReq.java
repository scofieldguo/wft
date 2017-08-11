package com.bw30.open.wft.common.cardpool.proto.req;

import java.util.Date;

import com.bw30.open.wft.common.cardpool.proto.Req;

/**
 * 查询用户是否在线
 * 
 * @author Jack
 *
 * 2014年8月25日
 */
public class QueryUserOnlineReq extends Req{
	private String openid;
	private String timestamp;
	private String sign;
	
	public QueryUserOnlineReq(){
		super();
		this.timestamp = SDF_YMD_HMS.format(new Date());
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	

}
