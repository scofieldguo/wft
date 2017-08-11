package com.bw30.open.wft.common.cardpool.rmi.bean;

import java.io.Serializable;

/**
 * 卡池信息
 * @author Jack
 *
 * 2014年9月29日
 */
public class CardPoolBean extends RespBean implements Serializable{
	private static final long serialVersionUID = -1586033963237605225L;
	
	private Long total;//购买总时长， 秒
	private Long consume;//消耗总时长，秒
	private Long balance;//剩余时长，秒
	private Integer num;//开卡数
	
	public CardPoolBean(){
		
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Long getConsume() {
		return consume;
	}

	public void setConsume(Long consume) {
		this.consume = consume;
	}

	public Long getBalance() {
		return balance;
	}

	public void setBalance(Long balance) {
		this.balance = balance;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}
	
}
