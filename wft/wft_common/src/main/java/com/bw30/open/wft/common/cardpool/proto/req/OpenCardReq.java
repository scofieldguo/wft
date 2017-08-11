package com.bw30.open.wft.common.cardpool.proto.req;

import java.util.Date;

import com.bw30.open.wft.common.cardpool.proto.Req;

/**
 * 开卡请求
 * @author Jack
 * @date 2014年7月10日 下午4:27:31
 */
public class OpenCardReq extends Req{
	private Integer op;
	private String ctype;
	private String orderid;
	private String timestamp;
	
	public OpenCardReq(){
		super();
		this.timestamp = SDF_YMD_HMS.format(new Date());
	} 

	
	public Integer getOp() {
		return op;
	}


	public void setOp(Integer op) {
		this.op = op;
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
