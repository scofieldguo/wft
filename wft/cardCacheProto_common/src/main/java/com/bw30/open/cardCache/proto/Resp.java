package com.bw30.open.cardCache.proto;

import java.io.Serializable;

public class Resp implements Serializable{
	private static final long serialVersionUID = 7214687606769475683L;
	
	public static final Integer PARAMS_ERROR=9001;
	public static final Integer SUCCESS=1;
	public static final Integer FAIL = 0;
	public static final Integer SERVICE_ERROR=103;
	public static final Integer GETCARD_ERROR=-1;
	
	private Integer result; // 返回状态码
	private String message; // 时间戳
	
	public Integer getResult() {
		return result;
	}
	public void setResult(Integer result) {
		this.result = result;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}
