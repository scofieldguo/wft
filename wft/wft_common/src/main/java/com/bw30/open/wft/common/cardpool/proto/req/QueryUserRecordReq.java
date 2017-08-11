package com.bw30.open.wft.common.cardpool.proto.req;

import java.util.Date;

import com.bw30.open.wft.common.cardpool.proto.Req;

/**
 * 查询用户的使用记录
 * 
 * @author Jack
 *
 * 2014年8月25日
 */
public class QueryUserRecordReq extends Req{
	private String openid;
	private String startdate;
	private String enddate;
	private String timestamp;
	private String sign;
	
	public QueryUserRecordReq(){
		super();
		this.timestamp = SDF_YMD_HMS.format(new Date());
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
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
