package com.bw30.open.wftAdm.task;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.bw30.open.common.dao.mapper.WftChannelMapper;
import com.bw30.open.common.model.WftChannel;
import com.bw30.open.common.model.WftConnSession;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.wftAdm.service.CardApiService;
import com.bw30.open.wftAdm.service.CardService;
import com.bw30.open.wftAdm.service.WftConnSessionService;

/**
 * 定时任务：处理ctcc计费的会话
 * 
 * @author Jack
 * 
 *         2014年10月14日
 */
public class ConnActiveCtccTask {
	private static final Logger LOG = Logger
			.getLogger(ConnActiveCtccTask.class);

	@Resource
	private WftConnSessionService wftConnSessionService;

	@Resource
	private CardService cardService;

	@Resource
	private WftChannelMapper wftChannelMapper;

	@Resource
	private CardApiService cardApiService;

	private Integer threadNum;

	private static Iterator<WftConnSession> ctccIterator = null;

	private static Integer currThreadNum = 0;

	public static synchronized void subThreadNum() {
		currThreadNum--;
	}

	/**
	 * 获取要处理的链接会话
	 * 
	 * @return
	 */
	public static synchronized WftConnSession getConnSession() {
		WftConnSession wftConnSession = null;
		if (ctccIterator.hasNext()) {
			wftConnSession = ctccIterator.next();
			ctccIterator.remove();
		}
		return wftConnSession;
	}

	public void doConn() {
		try {
			List<WftChannel> channelList = this.wftChannelMapper.findAll();
			LOG.info("ctcc active session task start......");
			if (null != channelList && 0 < channelList.size()) {
				for (WftChannel channel : channelList) {
					LOG.info("channel:" + channel.getCode());
					// 电信
					this.doActiveCtccSessionForChannel(channel.getCode(),
							channel.getCtccbalance(),
							channel.getCtypeforrechargectcc());
				}
			}
			LOG.info("ctcc active session task end");
		} catch (Exception e) {
			LOG.error("ctcc active session task run error", e);
			return;
		}

	}

	/**
	 * 处理电信计费的会话
	 * 
	 * @param channel
	 * @param rechargeThresold
	 *            充值阈值
	 * @param rechargeCtype
	 *            充值卡品
	 */
	private void doActiveCtccSessionForChannel(String channel,
			Integer rechargeThresold, String rechargeCtype) {
		LOG.info("ctcc active session task start: channel=" + channel);
		try {
			List<WftConnSession> ctccList = this.wftConnSessionService
					.getActiveSession(channel, WftOperator.OP_SSID_CTCC, null,
							null);
			if (null != ctccList && 0 < ctccList.size()) {
				LOG.info(String.format(
						"ctcc active session task: channel=%s, size=%s",
						channel, ctccList.size()));

				ctccIterator = ctccList.iterator();
				currThreadNum = this.threadNum;

				for (int i = 0; i < this.threadNum; i++) {
					if (0 == i % 2) {
						new ConnActiveCtccBalanceThread(this.cardApiService,
								this.cardService, this.wftConnSessionService,
								rechargeThresold, rechargeCtype).start();
					} else {
						new ConnActiveCtccRecordThread(
								this.wftConnSessionService, this.cardService)
								.start();
					}
				}

				while (0 < currThreadNum) {
					Thread.sleep(30 * 1000);
				}

			}
		} catch (Exception e) {
			LOG.error("", e);
		}

		LOG.info("ctcc active session task end: channel=" + channel);
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

	public void setThreadNum(Integer threadNum) {
		this.threadNum = threadNum;
	}

}
