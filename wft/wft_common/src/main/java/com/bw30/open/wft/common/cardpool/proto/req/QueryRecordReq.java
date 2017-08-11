package com.bw30.open.wft.common.cardpool.proto.req;

import java.util.Date;

import com.bw30.open.wft.common.cardpool.proto.Req;

/**
 * 卡使用记录查询请求
 * @author Jack
 * @date 2014年7月14日 下午1:10:15
 */
public class QueryRecordReq extends Req{
	private Integer op;
	private String cno;
	private String pwd;
	private String startdate;
	private String enddate;
	private String timestamp;
	
	public QueryRecordReq(){
		super();
		this.timestamp = SDF_YMD_HMS.format(new Date());
	}

	public Integer getOp() {
		return op;
	}

	public void setOp(Integer op) {
		this.op = op;
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

	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
}
