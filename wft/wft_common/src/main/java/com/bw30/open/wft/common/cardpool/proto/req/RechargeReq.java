package com.bw30.open.wft.common.cardpool.proto.req;

import java.util.Date;

import com.bw30.open.wft.common.cardpool.proto.Req;

/**
 * 充值请求
 * @author Jack
 * @date 2014年7月14日 上午11:42:40
 */
public class RechargeReq extends Req{
	private Integer op;
	private String cno;
	private String ctype;
	private String orderid;
	private String timestamp;
	
	public RechargeReq(){
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

	public String getCtype() {
		return ctype;
	}

	public void setCtype(String ctype) {
		this.ctype = ctype;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
}
