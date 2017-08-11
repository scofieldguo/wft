package com.bw30.open.wft.common.cardpool.rmi.bean;

import java.io.Serializable;

public class RespBean implements Serializable{
	private static final long serialVersionUID = -1344167236195735551L;
	
	public static final String RESULT_FAIL = "-1000";
	public static final String RESULT_OK = "1000";
	
	private String result;
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
}
