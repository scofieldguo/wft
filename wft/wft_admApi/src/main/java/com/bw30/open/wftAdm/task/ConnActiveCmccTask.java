package com.bw30.open.wftAdm.task;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.bw30.open.common.dao.mapper.WftChannelMapper;
import com.bw30.open.common.model.WftChannel;
import com.bw30.open.common.model.WftConnSession;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.wftAdm.service.CardService;
import com.bw30.open.wftAdm.service.WftConnSessionService;

/**
 * 定时任务：处理cmcc计费中的会话。
 * 关闭超过2小时且还未结束的会话并释放卡
 * 
 * @author Jack
 * 
 *         2014年9月4日
 */
public class ConnActiveCmccTask {
	private static final Logger LOG = Logger
			.getLogger(ConnActiveCmccTask.class);

	@Resource
	private WftConnSessionService wftConnSessionService;

	@Resource
	private CardService cardService;

	@Resource
	private WftChannelMapper wftChannelMapper;

	private String remotelogoutUrl;
	
	private Integer threadNum;
	
	private Integer minute;
	
	private static Iterator<WftConnSession> cmccIterator = null;
	
	private static Integer currThreadNum = 0;
	
	public static synchronized void subThreadNum(){
		LOG.info("cmcc thread subThreadNum=" + currThreadNum);
		currThreadNum--;
	}
	
	/**
	 * 获取要处理的链接会话
	 * @return
	 */
	public static synchronized WftConnSession getConnSession(){
		WftConnSession wftConnSession = null;
		if(cmccIterator.hasNext()){
			wftConnSession = cmccIterator.next();
			cmccIterator.remove();
		}
		return wftConnSession;
	}
	
	public void doConn() {
		Date nowDate = new Date();
		try {
			List<WftChannel> channelList = this.wftChannelMapper.findAll();
			LOG.info("check invalid session task[" + nowDate + "] start......");
			if (null != channelList && 0 < channelList.size()) {
				for (WftChannel channel : channelList) {
					LOG.info("channel:" + channel.getCode());
					this.closeInvalidSession(channel.getCode(),
							WftOperator.OP_SSID_CMCC, nowDate);// cmcc
				}
			}
			LOG.info("check invalid session task[" + nowDate + "] end");
		} catch (Exception e) {
			LOG.error("check invalid session task[" + nowDate + "] run error",
					e);
			return;
		}
	}

	private void closeInvalidSession(String channel, String ssid, Date nowDate) {
		LOG.info("check invalid session task start: channel = " + channel);
		List<WftConnSession> cmccList = this.wftConnSessionService
				.getInvalidConnSession(channel, ssid, this.minute, nowDate);
		LOG.info("cmcc active connSession num="+cmccList.size());
		if (null == cmccList || 0 == cmccList.size()) {
			LOG.info("no invalid conn session:channel=" + channel + ", ssid="
					+ ssid);
			return;
		}
		cmccIterator = cmccList.iterator();
		
		currThreadNum = this.threadNum;
		try {
			for (int i = 0; i < this.threadNum; i++) {
				new ConnActiveCmccThread(wftConnSessionService, cardService, this.remotelogoutUrl).start();
			}
			
			while(0 < currThreadNum){
				Thread.sleep(1 * 60 * 1000);
			}
			
		}catch (Exception e) {
			// TODO: handle exception
			LOG.error("xian cheng error", e);
		}
		LOG.info("check invalid session task end: channel = " + channel);
	}

	public void setWftConnSessionService(
			WftConnSessionService wftConnSessionService) {
		this.wftConnSessionService = wftConnSessionService;
	}

	public void setCardService(CardService cardService) {
		this.cardService = cardService;
	}

	public void setWftChannelMapper(WftChannelMapper wftChannelMapper) {
		this.wftChannelMapper = wftChannelMapper;
	}

	public void setRemotelogoutUrl(String remotelogoutUrl) {
		this.remotelogoutUrl = remotelogoutUrl;
	}

	public void setThreadNum(Integer threadNum) {
		this.threadNum = threadNum;
	}

	public void setMinute(Integer minute) {
		this.minute = minute;
	}
	

}
