package com.bw30.open.wft.common.cardpool.proto;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Resp {
	private static final SimpleDateFormat SDF_YMD_HMS = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 状态码：成功
	 */
	public static final int RESULT_OK = 9000;
	
	/**
	 * 状态码：参数错误，参考接口文档的非空约束
	 */
	public static final int RESULT_ERROR_PARAM = 9001;
	
	/**
	 * 状态码：卡种类错误
	 */
	public static final int RESULT_ERROR_OP = 9002;
	
	/**
	 * 状态码：卡品错误
	 */
	public static final int RESULT_ERROR_CTYPE = 9003;
	
	/**
	 * 状态码：卡号错误
	 */
	public static final int RESULT_ERROR_CNO = 9004;
	
	/**
	 * 状态码：卡密码错误
	 */
	public static final int RESULT_ERROR_PWD = 9005;
	
	/**
	 * 状态码：ip错误
	 */
	public static final int RESULT_ERROR_IP = 9006;
	
	/**
	 * 状态吗：卡已下线
	 */
	public static final int RESULT_ERROR_OFFLINE = 9007;
	
	/**
	 * 状态码：服务器异常
	 */
	public static final int RESULT_ERROR_OTHER = 9008;
	
	/**
	 * 状态码：开卡数据超过上限
	 */
	public static final int RESULT_ERROR_OUT_CARDSIZE = 9009;
	
	private Integer result;
	private String timestamp;
	private String standby;
	
	public Resp(){
		this.timestamp = SDF_YMD_HMS.format(new Date());
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getStandby() {
		return standby;
	}

	public void setStandby(String standby) {
		this.standby = standby;
	}
	
}
