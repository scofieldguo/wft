package com.bw30.open.common.model.sale;

import java.util.Date;

import com.bw30.open.wft.common.DateUtil;

public class WftSaleCardOperation {
	private Integer id;
	private String cno;
	private Integer duration;
	private Date optime;
	private Integer type;
	private Integer status;
	private Integer channel_id;
	private String optimestr;
	private String order_id;
	
	public static final Integer STATUS_ON=1;
	public static final Integer STATUS_OFF=0;
	
	public static final Integer OP_CHARGE=0;
	public static final Integer OP_OPEN=1;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCno() {
		return cno;
	}
	public void setCno(String cno) {
		this.cno = cno;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public Date getOptime() {
		return optime;
	}
	public void setOptime(Date optime) {
		this.optime = optime;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getChannel_id() {
		return channel_id;
	}
	public void setChannel_id(Integer channelId) {
		channel_id = channelId;
	}
	public String getOptimestr() {
		return DateUtil.formateDate(getOptime(), "yyyy-MM-dd HH:mm:ss");
	}
	public void setOptimestr(String optimestr) {
		this.optimestr = optimestr;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String orderId) {
		order_id = orderId;
	}
	
	
}
