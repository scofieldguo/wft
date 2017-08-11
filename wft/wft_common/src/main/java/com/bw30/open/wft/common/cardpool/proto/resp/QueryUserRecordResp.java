package com.bw30.open.wft.common.cardpool.proto.resp;

import java.util.List;

import com.bw30.open.wft.common.cardpool.proto.Resp;

/**
 * 用户使用记录查询响应
 * 
 * @author Jack
 *
 * 2014年8月25日
 */
public class QueryUserRecordResp extends Resp{
	private String openid;
	private List<Record> detaillist;
	
	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public List<Record> getDetaillist() {
		return detaillist;
	}

	public void setDetaillist(List<Record> detaillist) {
		this.detaillist = detaillist;
	}


	public static class Record{
		private Integer opid;
		private String starttime;
		private String endtime;
		private int timelen;
		
		public Integer getOpid() {
			return opid;
		}
		public void setOpid(Integer opid) {
			this.opid = opid;
		}
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
