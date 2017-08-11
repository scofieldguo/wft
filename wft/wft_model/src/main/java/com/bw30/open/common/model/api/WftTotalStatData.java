package com.bw30.open.common.model.api;

import java.io.Serializable;
import java.util.List;

import com.bw30.open.common.model.WftChannel;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.common.model.stat.WftTotalStat;

public class WftTotalStatData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2077897334019776893L;
	private List<WftOperator> opList;
	private List<WftChannel> channelList;
	private List<WftTotalStat> totalStatList;
	private Integer opid;
	private String channel;
	private String startDate;
	private String endDate;
	public List<WftOperator> getOpList() {
		return opList;
	}
	public void setOpList(List<WftOperator> opList) {
		this.opList = opList;
	}
	public List<WftChannel> getChannelList() {
		return channelList;
	}
	public void setChannelList(List<WftChannel> channelList) {
		this.channelList = channelList;
	}
	public List<WftTotalStat> getTotalStatList() {
		return totalStatList;
	}
	public void setTotalStatList(List<WftTotalStat> totalStatList) {
		this.totalStatList = totalStatList;
	}
	public Integer getOpid() {
		return opid;
	}
	public void setOpid(Integer opid) {
		this.opid = opid;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
}
