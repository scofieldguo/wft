package com.bw30.open.cardpool.service.qq;

public class WiFiQueryCgiServerResp {
	private Integer ecode;
	private WiFiQueryCgiServerData data;
	
	public WiFiQueryCgiServerResp(){
		
	}

	public Integer getEcode() {
		return ecode;
	}

	public void setEcode(Integer ecode) {
		this.ecode = ecode;
	}

	public WiFiQueryCgiServerData getData() {
		return data;
	}

	public void setData(WiFiQueryCgiServerData data) {
		this.data = data;
	}
	
}
