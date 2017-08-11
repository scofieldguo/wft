package com.bw30.open.wftAdm.task;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.bw30.open.common.dao.mapper.WftChannelMapper;
import com.bw30.open.common.model.WftChannel;
import com.bw30.open.common.model.WftConnSession;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.wftAdm.service.WftConnSessionService;

public class ConnTimeout100MSHTask {
	public static final Logger LOG = Logger.getLogger(ConnTimeoutCmccTask.class);

	@Resource
	private WftConnSessionService wftConnSessionService;

	@Resource
	private WftChannelMapper wftChannelMapper;

	public void doConn() {
		Date nowDate = new Date();
		try {
			List<WftChannel> channelList = this.wftChannelMapper.findAll();
			LOG.info("conntimeoutsession 100MSH task start......");
			if (null != channelList && 0 < channelList.size()) {
				for (WftChannel channel : channelList) {
					this.closeTimeOutConn(channel.getCode(), WftOperator.OP_SSID_100MSH, 2, nowDate);
				}
			}
			LOG.info("conntimeoutsession 100MSH task end");
		} catch (Exception e) {
			LOG.error("conntimeoutsession 100MSH task run error", e);
			return;
		}

	}

	
	/**
	 * 处理未关闭且未计费的会话
	 * 
	 * @param channel
	 * @param ssid
	 * @param nowDate
	 */
	private void closeTimeOutConn(String channel, String ssid, Integer minute, Date nowDate) {
		try {
			List<WftConnSession> timeOutList = wftConnSessionService.getTimeoutConnSession(channel,
					ssid, minute, new Date());
			if (null != timeOutList && 0 < timeOutList.size()) {
				LOG.info(String.format("conntimeoutsession 100MSH size : channel=%s, size=%s", channel, timeOutList.size()));
				for (WftConnSession wftConnSession : timeOutList) {
					// 关闭链接会话
					wftConnSession.setUflag(WftConnSession.UFLAG_FAIL);
					wftConnSessionService.colseTimeOutConnSession(
							wftConnSession, nowDate);
				}
			} else {
				LOG.info(String.format("conntimeoutsession 100MSH size : channel=%s, size=0", channel));
			}
		} catch (Exception e) {
			LOG.error("", e);
		}
	}

	public void setWftConnSessionService(
			WftConnSessionService wftConnSessionService) {
		this.wftConnSessionService = wftConnSessionService;
	}

	public void setWftChannelMapper(WftChannelMapper wftChannelMapper) {
		this.wftChannelMapper = wftChannelMapper;
	}

}
