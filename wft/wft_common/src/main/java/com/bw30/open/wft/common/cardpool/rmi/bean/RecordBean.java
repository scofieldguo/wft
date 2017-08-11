package com.bw30.open.wft.common.cardpool.rmi.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 卡使用记录
 * @author Jack
 * @date 2014年7月18日 上午11:17:24
 */
public class RecordBean extends RespBean implements Serializable{
	private static final long serialVersionUID = 228943733524720373L;
	
	private String cno;
	private List<CardRecord> recordList;
	
	public RecordBean(){
		
	}

	public String getCno() {
		return cno;
	}

	public void setCno(String cno) {
		this.cno = cno;
	}

	public List<CardRecord> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<CardRecord> recordList) {
		this.recordList = recordList;
	}

}
