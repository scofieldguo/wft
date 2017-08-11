package com.bw30.open.cardpool.service.qq;

public class OfflineResultResp {
	private Integer retCode;
	private String retMsg;
	private Offline retBody;
	
	public OfflineResultResp(){
		
	}

	public Integer getRetCode() {
		return retCode;
	}

	public void setRetCode(Integer retCode) {
		this.retCode = retCode;
	}

	public String getRetMsg() {
		return retMsg;
	}

	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}

	public Offline getRetBody() {
		return retBody;
	}

	public void setRetBody(Offline retBody) {
		this.retBody = retBody;
	}
	
}
