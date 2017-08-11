package com.bw30.open.wft.common.cardpool.proto.resp;

import com.bw30.open.wft.common.cardpool.proto.Resp;

/**
 * 下线通知
 * 
 * @author Jack
 *
 * 2014年9月5日
 */
public class OfflineNotifyResp extends Resp{
	private String openid;
	
	public OfflineNotifyResp(){
		super();
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
}
