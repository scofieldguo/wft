package com.bw30.open.wft.common.cardpool.proto.req;

import java.util.Date;

import com.bw30.open.wft.common.cardpool.proto.Req;

/**
 * 修改密码请求
 * @author Jack
 * @date 2014年7月14日 上午11:49:28
 */
public class UpdatePwdReq extends Req{
	private Integer op;
	private String cno;
	private String pwd;
	private String newpwd;
	private String timestamp;
	
	public UpdatePwdReq(){
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

	public String getNewpwd() {
		return newpwd;
	}

	public void setNewpwd(String newpwd) {
		this.newpwd = newpwd;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

}
