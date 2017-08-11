package com.bw30.open.wftAdm.task;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.bw30.open.common.dao.mapper.WftChannelMapper;
import com.bw30.open.common.model.WftCardActive;
import com.bw30.open.common.model.WftChannel;
import com.bw30.open.common.model.WftConnSession;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.wftAdm.service.CardApiService;
import com.bw30.open.wftAdm.service.CardService;
import com.bw30.open.wftAdm.service.WftConnSessionService;

/**
 * 定时任务：处理cmcc连接超时会话。
 * 超过2分钟没有收到计费请求，则认为连接失败，关闭会话并释放卡。
 *
 */
public class ConnTimeoutCmccTask {
	public static final Logger LOG = Logger.getLogger(ConnTimeoutCmccTask.class);

	@Resource
	private WftConnSessionService wftConnSessionService;

	@Resource
	private CardService cardService;

	@Resource
	private WftChannelMapper wftChannelMapper;

	@Resource
	private CardApiService cardApiService;

	public void doConnCmcc() {
		Date nowDate = new Date();
		try {
			List<WftChannel> channelList = this.wftChannelMapper.findAll();
			LOG.info("conntimeoutsession CMCC task start......");
			if (null != channelList && 0 < channelList.size()) {
				for (WftChannel channel : channelList) {
					//移动
					Integer minute = channel.getCmccinterval();//秒
					if(-1 == minute){
						minute = 2;
					}else{
						minute /= 60;
						minute += 2;
					}
					this.closeTimeOutConn(channel.getCode(), WftOperator.OP_SSID_CMCC, minute, nowDate);
				}
			}
			LOG.info("conntimeoutsession CMCC end");
		} catch (Exception e) {
			LOG.error("conntimeoutsession CMCC task run error", e);
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
				LOG.info(String.format("conntimeoutsession CMCC size : channel=%s, size=%s", channel, timeOutList.size()));
				for (WftConnSession wftConnSession : timeOutList) {
					WftCardActive card = this.cardService.findCardById(channel,
							wftConnSession.getCid());
					if (null != card && WftCardActive.CSTATUS_STOP != card.getCstatus()) {
						cardService.freebackAvailable(card.getId(),
								WftCardActive.CSTATUS_AVAILABLE, channel,wftConnSession.getUid(), card.getTvalue(),card.getNo(),card.getOpid());
					}
					
					// 关闭链接会话
					wftConnSession.setUflag(WftConnSession.UFLAG_FAIL);
					wftConnSessionService.colseTimeOutConnSession(
							wftConnSession, nowDate);
				}
			} else {
				LOG.info(String.format("conntimeoutsession CMCC size : channel=%s, size=0", channel));
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

	public void setCardService(CardService cardService) {
		this.cardService = cardService;
	}

	public void setCardApiService(CardApiService cardApiService) {
		this.cardApiService = cardApiService;
	}

}
