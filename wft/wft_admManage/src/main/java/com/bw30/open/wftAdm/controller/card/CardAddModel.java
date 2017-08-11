package com.bw30.open.wftAdm.controller.card;

/**
 * model：添加新卡
 *
 * @version 2014-4-25 上午11:25:24
 *
 */
public class CardAddModel {
	private String no;
	private String op;
	private String prv;
	private String flag;
	private String type;
	private String unum;
	private String vbtime;
	private String vetime;
	private String mvalue;
	private String bvalue;
	private String pwd;
	
	//验证信息
	private String msg;
	
	public CardAddModel(){
		
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getPrv() {
		return prv;
	}

	public void setPrv(String prv) {
		this.prv = prv;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUnum() {
		return unum;
	}

	public void setUnum(String unum) {
		this.unum = unum;
	}

	public String getVbtime() {
		return vbtime;
	}

	public void setVbtime(String vbtime) {
		this.vbtime = vbtime;
	}

	public String getVetime() {
		return vetime;
	}

	public void setVetime(String vetime) {
		this.vetime = vetime;
	}

	public String getMvalue() {
		return mvalue;
	}

	public void setMvalue(String mvalue) {
		this.mvalue = mvalue;
	}

	public String getBvalue() {
		return bvalue;
	}

	public void setBvalue(String bvalue) {
		this.bvalue = bvalue;
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
