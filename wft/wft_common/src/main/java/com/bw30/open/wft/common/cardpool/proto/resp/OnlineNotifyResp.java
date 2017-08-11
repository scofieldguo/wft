package com.bw30.open.wft.common.cardpool.proto.resp;

import com.bw30.open.wft.common.cardpool.proto.Resp;

/**
 * 上线通知
 * 
 * @author Jack
 *
 * 2014年9月5日
 */
public class OnlineNotifyResp extends Resp{
	private String openid;
	
	public OnlineNotifyResp(){
		super();
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
}
