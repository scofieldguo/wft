package com.bw30.open.wft.common.cardpool.proto.req;

import java.util.Date;

import com.bw30.open.wft.common.cardpool.proto.Req;

/**
 * 卡在线状态查询请求
 * 
 * @author Jack
 *
 * 2014年8月26日
 */
public class QueryOnlineReq extends Req{
	private Integer op;
	private String cno;
	private String pwd;
	private String ip;
	private String timestamp;
	
	public QueryOnlineReq(){
		super();
		this.timestamp = SDF_YMD_HMS.format(new Date());
	}

	public Integer getOp() {
		return op;
	}

	public void setOp(Integer op) {
		this.op = op;
	}

	public String getCno() {
		return cno;
	}

	public void setCno(String cno) {
		this.cno = cno;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
}
