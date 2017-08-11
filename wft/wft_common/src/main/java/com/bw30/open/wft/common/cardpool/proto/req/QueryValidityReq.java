package com.bw30.open.wft.common.cardpool.proto.req;

import java.util.Date;

import com.bw30.open.wft.common.cardpool.proto.Req;

/**
 * 有效期查询请求
 * @author Jack
 * @date 2014年7月14日 上午11:42:09
 */
public class QueryValidityReq extends Req{
	private Integer op;
	private String cno;
	private String pwd;
	private String timestamp;
	
	public QueryValidityReq(){
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

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
}
