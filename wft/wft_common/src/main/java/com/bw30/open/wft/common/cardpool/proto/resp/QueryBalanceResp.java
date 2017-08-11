package com.bw30.open.wft.common.cardpool.proto.resp;

import com.bw30.open.wft.common.cardpool.proto.Resp;

public class QueryBalanceResp extends Resp{
	private String cno;
	private Integer balance;
	
	public QueryBalanceResp(){
		super();
	}

	public String getCno() {
		return cno;
	}

	public void setCno(String cno) {
		this.cno = cno;
	}

	public Integer getBalance() {
		return balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}
	
}
