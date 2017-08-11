package com.bw30.open.common.model.api;

import java.io.Serializable;
import java.util.List;

import com.bw30.open.common.model.WftChannel;
import com.bw30.open.common.model.WftOperator;

public class WftChannelAndOpData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7635671859375505937L;
	
	private List<WftOperator> opList;
	private List<WftChannel> channelList;
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
}
