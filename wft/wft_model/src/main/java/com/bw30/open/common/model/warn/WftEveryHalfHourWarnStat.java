package com.bw30.open.common.model.warn;

import java.util.Date;

public class WftEveryHalfHourWarnStat {

	private Integer channel; //渠道
	private String dairy; //日期
	private Integer hour;// 小时
	private Integer minute; // 分钟
	private Integer ctccConnNum; //电信连接数
	private Integer ctccConnSuc; // 电信连接成功数
	private Integer ctccNoCard; // 电信取卡失败数
	private Integer cmccConnNum; // 移动连接数
	private Integer cmccConnSuc; // 移动连接成功数
	private Integer cmccNoCard; // 移动取卡失败数
	public Integer getChannel() {
		return channel;
	}
	public void setChannel(Integer channel) {
		this.channel = channel;
	}
	public Integer getHour() {
		return hour;
	}
	public void setHour(Integer hour) {
		this.hour = hour;
	}
	public Integer getMinute() {
		return minute;
	}
	public void setMinute(Integer minute) {
		this.minute = minute;
	}
	public Integer getCtccConnNum() {
		return ctccConnNum;
	}
	public void setCtccConnNum(Integer ctccConnNum) {
		this.ctccConnNum = ctccConnNum;
	}
	public Integer getCtccConnSuc() {
		return ctccConnSuc;
	}
	public void setCtccConnSuc(Integer ctccConnSuc) {
		this.ctccConnSuc = ctccConnSuc;
	}
	public Integer getCtccNoCard() {
		return ctccNoCard;
	}
	public void setCtccNoCard(Integer ctccNoCard) {
		this.ctccNoCard = ctccNoCard;
	}
	public Integer getCmccConnNum() {
		return cmccConnNum;
	}
	public void setCmccConnNum(Integer cmccConnNum) {
		this.cmccConnNum = cmccConnNum;
	}
	public Integer getCmccConnSuc() {
		return cmccConnSuc;
	}
	public void setCmccConnSuc(Integer cmccConnSuc) {
		this.cmccConnSuc = cmccConnSuc;
	}
	public Integer getCmccNoCard() {
		return cmccNoCard;
	}
	public void setCmccNoCard(Integer cmccNoCard) {
		this.cmccNoCard = cmccNoCard;
	}
	
}
