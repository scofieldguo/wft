package com.bw30.open.wft.common.cardpool.proto;

import java.text.SimpleDateFormat;

public class Req {
	protected static final SimpleDateFormat SDF_YMD_HMS = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 卡类型：移动
	 */
	public static final int OP_CMCC = 1;
	/**
	 * 卡类型：电信
	 */
	public static final int OP_CTCC = 2;
	/**
	 * 卡类型：联通
	 */
	public static final int OP_CUCC = 3;
	
	private String partner;
	private String standby;
	
	public Req() {
		
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getStandby() {
		return standby;
	}

	public void setStandby(String standby) {
		this.standby = standby;
	}

}
