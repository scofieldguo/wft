package com.bw30.open.wftAdm.controller.card;

public class CardUpdateModel {
	private Integer id;
	private String channel;
	private String no;
	private Integer status;
	private String pwd;
	
	//验证信息
	private String msg;
	
	public CardUpdateModel(){
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
