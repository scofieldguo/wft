package com.bw30.open.wft.common.translate.model;

import com.bw30.open.wft.common.translate.inter.FieldMeta;

public class ExcelModel {

	@FieldMeta(name = "用户号码")
	private String mobileNum; // 用户号码

	@FieldMeta(name = "登陆省份")
	private String province; // 登陆省份

	@FieldMeta(name = "登陆日期")
	private String date; // 登陆日期

	@FieldMeta(name = "上线时间")
	private String online_time; // 上线时间

	@FieldMeta(name = "下线时间")
	private String offline_time; // 下线时间

	@FieldMeta(name = "合并标示")
	private String flag;

	public String getMobileNum() {
		return mobileNum;
	}

	public void setMobileNum(String mobileNum) {
		this.mobileNum = mobileNum;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getOnline_time() {
		return online_time;
	}

	public void setOnline_time(String online_time) {
		this.online_time = online_time;
	}

	public String getOffline_time() {
		return offline_time;
	}

	public void setOffline_time(String offline_time) {
		this.offline_time = offline_time;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

}
