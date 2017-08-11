package com.bw30.open.wft.common.cardpool.proto.resp;

import com.bw30.open.wft.common.cardpool.proto.Resp;

/**
 * 用户在线状态查询响应信息
 * @author Jack
 *
 * 2014年8月25日
 */
public class QueryUserOnlineResp extends Resp{
	private String openid;
	private Integer opid;
	private Integer status;
	
	public QueryUserOnlineResp(){
		
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public Integer getOpid() {
		return opid;
	}

	public void setOpid(Integer opid) {
		this.opid = opid;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
