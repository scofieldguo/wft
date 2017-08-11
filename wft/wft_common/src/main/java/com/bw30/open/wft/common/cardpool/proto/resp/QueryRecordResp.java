package com.bw30.open.wft.common.cardpool.proto.resp;

import java.util.List;

import com.bw30.open.wft.common.cardpool.proto.Resp;

public class QueryRecordResp extends Resp{
	private String cno;
	private List<Record> recordlist;
	
	public QueryRecordResp(){
		super();
	}

	public String getCno() {
		return cno;
	}

	public void setCno(String cno) {
		this.cno = cno;
	}
	
	public List<Record> getRecordlist() {
		return recordlist;
	}

	public void setRecordlist(List<Record> recordlist) {
		this.recordlist = recordlist;
	}


	/**
	 * 卡使用记录
	 */
	public static class Record{
		private String starttime;
		private String endtime;
		private int timelen;
		public String getStarttime() {
			return starttime;
		}
		public void setStarttime(String starttime) {
			this.starttime = starttime;
		}
		public String getEndtime() {
			return endtime;
		}
		public void setEndtime(String endtime) {
			this.endtime = endtime;
		}
		public int getTimelen() {
			return timelen;
		}
		public void setTimelen(int timelen) {
			this.timelen = timelen;
		}
	}
	
}
