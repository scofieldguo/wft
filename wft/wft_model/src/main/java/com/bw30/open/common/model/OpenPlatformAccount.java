package com.bw30.open.common.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class OpenPlatformAccount implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id; // 开放平台用户ID
	private String usetime; // 使用时长
	private String buytime; // 购买时长
	private String retime; // 充值时长
	private String useHours; //使用时长（小时）
	private String buyHours; //购买时长（小时）
	DecimalFormat df=new DecimalFormat("0.0");
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsetime() {
		return usetime;
	}
	public void setUsetime(String usetime) {
		this.usetime = usetime;
	}
	public String getBuytime() {
		return buytime;
	}
	public void setBuytime(String buytime) {
		this.buytime = buytime;
	}
	public String getRetime() {
		return retime;
	}
	public void setRetime(String retime) {
		this.retime = retime;
	}
	public String getUseHours() {
		BigDecimal bd = new BigDecimal(this.getUsetime());
		bd = bd.divide(new BigDecimal(3600), 2, BigDecimal.ROUND_HALF_UP);
		return bd.toString();
	}
	public String getBuyHours() {
		BigDecimal bd = new BigDecimal(this.getBuytime());
		bd = bd.divide(new BigDecimal(3600), 2, BigDecimal.ROUND_HALF_UP);
		return bd.toString();
	}

}
