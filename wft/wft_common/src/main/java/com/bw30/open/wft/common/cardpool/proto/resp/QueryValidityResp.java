package com.bw30.open.wft.common.cardpool.proto.resp;

import com.bw30.open.wft.common.cardpool.proto.Resp;

/**
 * 
 * @author Jack
 * @date 2014年7月14日 上午11:43:49
 */
public class QueryValidityResp extends Resp{
	private String cno;
	private String effectdate;
	private String expiredate;
	
	public QueryValidityResp(){
		super();
	}

	public String getCno() {
		return cno;
	}

	public void setCno(String cno) {
		this.cno = cno;
	}

	public String getEffectdate() {
		return effectdate;
	}

	public void setEffectdate(String effectdate) {
		this.effectdate = effectdate;
	}

	public String getExpiredate() {
		return expiredate;
	}

	public void setExpiredate(String expiredate) {
		this.expiredate = expiredate;
	}
	
}
