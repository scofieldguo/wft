package com.bw30.open.common.model.stat;

import java.io.Serializable;

public class WftOldTotalStat implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2450750583202695362L;
	private Long outvaluetrue=0l; // 昨日消耗时长
	private Long oconnsuc=0l;//昨日连接成功次数
	private Long opersuc=0l; // 昨日连接成功人数
	public Long getOutvaluetrue() {
		return outvaluetrue;
	}
	public void setOutvaluetrue(Long outvaluetrue) {
		this.outvaluetrue = outvaluetrue;
	}
	public Long getOconnsuc() {
		return oconnsuc;
	}
	public void setOconnsuc(Long oconnsuc) {
		this.oconnsuc = oconnsuc;
	}
	public Long getOpersuc() {
		return opersuc;
	}
	public void setOpersuc(Long opersuc) {
		this.opersuc = opersuc;
	}
}
