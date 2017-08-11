package com.bw30.open.wftAdm.task;

import java.util.Date;
import java.util.Iterator;
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
 * 定时任务：处理ctcc连接超时会话。
 * 超过2分钟没有收到计费请求，则查询ctcc卡在线状态，在线则修改计费状态；不在线则查询卡使用记录对账。
 *
 */
public class ConnTimeoutCtccTask {
	public static final Logger LOG = Logger.getLogger(ConnTimeoutCtccTask.class);

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
	
	public static synchronized void subThreadNum(){
		currThreadNum--;
	}
	
	/**
	 * 获取要处理的链接会话
	 * @return
	 */
	public static synchronized WftConnSession getConnSession(){
		WftConnSession wftConnSession = null;
		if(ctccIterator.hasNext()){
			wftConnSession = ctccIterator.next();
			ctccIterator.remove();
		}
		return wftConnSession;
	}

	public void doConn() {
		Date nowDate = new Date();
		try {
			List<WftChannel> channelList = this.wftChannelMapper.findAll();
			LOG.info("conntimeoutsession ctcc task start......");
			if (null != channelList && 0 < channelList.size()) {
				for (WftChannel channel : channelList) {
					LOG.info("channel:" + channel.getCode());
					//电信
					this.closeTimeOutConnForCtcc(channel.getCode(), nowDate);
					
					//bwlan
					this.closeTimeOutConnForBw(channel.getCode(), nowDate);
				}
			}
			LOG.info("conntimeoutsession ctcc task end");
		} catch (Exception e) {
			LOG.error("conntimeoutsession ctcc task run error", e);
			return;
		}

	}

	/**
	 * 处理未关闭且未计费的会话，若距分卡时间小于2分钟，则不处理，否则查询卡的在线状态： 若卡在线，则写入计费时间、修改计费状态；
	 * 若卡不在线，则认为连接失败，关闭会话并释放卡。
	 * 
	 * @param channel
	 * @param ssid
	 * @param nowDate
	 */
	private void closeTimeOutConnForCtcc(String channel, Date nowDate) {
		LOG.info("timeout ctcc session task start: channel=" + channel);
		try {
			/*
			 * 获取未关闭且未计费，并且超时的会话； 超时阈值太大会丢失使用时间短的成功使用记录， 阈值太小则没法区分登陆失败和正在登陆中，
			 * 最好设置为从分卡到连接成功的极大时间
			 */
			List<WftConnSession> timeOutList = wftConnSessionService.getTimeoutConnSession(channel,
					WftOperator.OP_SSID_CTCC, 2, new Date());
			if (null != timeOutList && 0 < timeOutList.size()) {
				LOG.info(String.format("conntimeoutsession ctcc size : channel=%s, size=%s", channel, timeOutList.size()));
				
				ctccIterator = timeOutList.iterator();
				currThreadNum = this.threadNum;
				
				for(int i = 0; i < this.threadNum; i++){
					new ConnTimeoutCtccThread(this.cardApiService, this.cardService, this.wftConnSessionService).start();
				}
				
				while(0 < currThreadNum){
					Thread.sleep(5 * 1000);
				}
				
			} 
		} catch (Exception e) {
			LOG.error("", e);
		}
		
		LOG.info("timeout ctcc session task end: channel=" + channel);
	}
	
	
	
	private void closeTimeOutConnForBw(String channel, Date nowDate) {
		LOG.info("timeout bwlan session task start: channel=" + channel);
		try {
			List<WftConnSession> timeOutList = wftConnSessionService.getTimeoutConnSession(channel,
					WftOperator.OP_SSID_BW, 2, new Date());
			if (null != timeOutList && 0 < timeOutList.size()) {
				LOG.info("timeout connSession size : " + timeOutList.size());
				for (WftConnSession wftConnSession : timeOutList) {
					WftCardActive card = this.cardService.findCardById(channel,
							wftConnSession.getCid());
					if (null == card) {
						LOG.error(String.format(
								"no card find: channel=%s, cid=%s", channel,
								wftConnSession.getCid()));
						continue;
						//return;
					}

					cardService.freebackAvailable(card.getId(),
							WftCardActive.CSTATUS_AVAILABLE, channel, wftConnSession.getUid(), null,null,null);
					wftConnSessionService.colseTimeOutConnSession(
							wftConnSession, nowDate);
				}
			} else {
				LOG.info("no timeout connSession");
			}
		} catch (Exception e) {
			LOG.error("", e);
		}
		
		LOG.info("timeout bwlan session task end: channel=" + channel);
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
