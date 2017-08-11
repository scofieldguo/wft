package com.bw30.open.common.model;

import java.io.Serializable;

public class BillCompare implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

	private String mobile_num;

	private String record_date;

	private String online_time;

	private String offline_time;

	private Integer from_id;

	private Integer compare_id;

	private Integer merge_flag;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMobile_num() {
		return mobile_num;
	}

	public void setMobile_num(String mobile_num) {
		this.mobile_num = mobile_num;
	}

	public String getRecord_date() {
		return record_date;
	}

	public void setRecord_date(String record_date) {
		this.record_date = record_date;
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

	public Integer getFrom_id() {
		return from_id;
	}

	public void setFrom_id(Integer from_id) {
		this.from_id = from_id;
	}

	public Integer getCompare_id() {
		return compare_id;
	}

	public void setCompare_id(Integer compare_id) {
		this.compare_id = compare_id;
	}

	public Integer getMerge_flag() {
		return merge_flag;
	}

	public void setMerge_flag(Integer merge_flag) {
		this.merge_flag = merge_flag;
	}


}
