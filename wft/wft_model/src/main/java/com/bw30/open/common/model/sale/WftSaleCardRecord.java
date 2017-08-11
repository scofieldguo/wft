package com.bw30.open.common.model.sale;

import java.io.Serializable;
import java.util.Date;

public class WftSaleCardRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cno; // 卡号
	private String pwd; // 密码
	private String shop_name; //店铺名称
	private Date open_time; // 开卡时间
	private int card_type; //卡类型
	private int duration;// 时长
	private String order_id; //淘宝单号
	private Date start_time; //卡的开始时间
	private Date end_time; // 卡的结束时间
	private String validity;
	private Integer type;// 操作类型
	private Integer stauts;//状态
	public static final Integer CHARGE=1;
	public static final Integer OPEN=2;
	public static final Integer STATUS_ON=1;
	public static final Integer STATUS_OFF=2;
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
	public String getShop_name() {
		return shop_name;
	}
	public void setShop_name(String shopName) {
		shop_name = shopName;
	}
	public Date getOpen_time() {
		return open_time;
	}
	public void setOpen_time(Date openTime) {
		open_time = openTime;
	}
	public int getCard_type() {
		return card_type;
	}
	public void setCard_type(int cardType) {
		card_type = cardType;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String orderId) {
		order_id = orderId;
	}
	public Date getStart_time() {
		return start_time;
	}
	public void setStart_time(Date startTime) {
		start_time = startTime;
	}
	public Date getEnd_time() {
		return end_time;
	}
	public void setEnd_time(Date endTime) {
		end_time = endTime;
	}
	public String getValidity() {
		return validity;
	}
	public void setValidity(String validity) {
		this.validity = validity;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getStauts() {
		return stauts;
	}
	public void setStauts(Integer stauts) {
		this.stauts = stauts;
	}
}
