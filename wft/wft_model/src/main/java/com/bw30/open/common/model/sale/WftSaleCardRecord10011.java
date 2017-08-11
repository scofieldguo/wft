package com.bw30.open.common.model.sale;

import java.io.Serializable;
import java.util.Date;

import com.bw30.open.wft.common.DateUtil;

public class WftSaleCardRecord10011 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String channel_id;
	private String cno; // 卡号
	private String pwd; // 密码
	private Date open_time; // 开卡时间
	private Integer card_type; //卡类型
	private Integer duration;// 时长
	private Integer total_value;//累计时长
	private Date start_time; //卡的开始时间
	private Date end_time; // 卡的结束时间
	private String startStr;
	private String endStr;
	private String balace;
	private String order_id;
	public static final Integer CHARGE=1;
	public static final Integer OPEN=2;
	public static final Integer STATUS_ON=1;
	public static final Integer STATUS_OFF=2;
	private String validity;
	public String getChannel_id() {
		return channel_id;
	}
	public void setChannel_id(String channelId) {
		channel_id = channelId;
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
	public Date getOpen_time() {
		return open_time;
	}
	public void setOpen_time(Date openTime) {
		open_time = openTime;
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
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String orderId) {
		order_id = orderId;
	}
	public Integer getCard_type() {
		return card_type;
	}
	public void setCard_type(Integer cardType) {
		card_type = cardType;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public String getValidity() {
		return validity;
	}
	public void setValidity(String validity) {
		this.validity = validity;
	}
	public String getStartStr() {
		if(getOpen_time()!=null){
			return DateUtil.formateDate(getOpen_time(), "yyyy-MM-dd HH:mm:ss");
		}
		return null;
	}
	public void setStartStr(String startStr) {
		this.startStr = startStr;
	}
	public String getEndStr() {
		if(getEnd_time()!=null){
			return DateUtil.formateDate(getEnd_time(), "yyyy-MM-dd HH:mm:ss");
		}
		return null;
	}
	public void setEndStr(String endStr) {
		this.endStr = endStr;
	}
	public String getBalace() {
		if(getEnd_time()!=null&&getOpen_time()!=null){
			long end=getEnd_time().getTime();
			long start=getOpen_time().getTime();
			long diff=(end-start)/(1000*24*60*60);
			return diff+"天";
		}
		return null;
	}
	
	
	public void setBalace(String balace) {
		this.balace = balace;
	}
	public Integer getTotal_value() {
		return total_value;
		}
	public void setTotal_value(Integer totalValue) {
		total_value = totalValue;
	}
	
	public static void main(String[] args) {
		new WftSaleCardRecord10011().setOpen_time(null);
	}
	
}
