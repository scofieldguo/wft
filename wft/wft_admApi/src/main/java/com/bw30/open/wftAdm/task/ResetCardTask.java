package com.bw30.open.wftAdm.task;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.bw30.open.common.dao.mapper.WftCardActiveMapper;
import com.bw30.open.common.dao.mapper.WftChannelMapper;
import com.bw30.open.common.dao.mapper.WftConnSessionMapper;
import com.bw30.open.common.model.WftCardActive;
import com.bw30.open.common.model.WftChannel;
import com.bw30.open.common.model.WftConnSession;

public class ResetCardTask {

	@Resource
	private WftChannelMapper wftChannelMapper;
	
	@Resource
	private WftCardActiveMapper wftCardActiveMapper;
	
	@Resource
	private WftConnSessionMapper wftConnSessionMapper;
	private static final Logger logger = Logger.getLogger(ResetCardTask.class);
	/**
	 * 获取卡使用时间是一天以前或者为空，卡状态不是停卡的。
	 * @param channel
	 * @return
	 */
	private List<WftCardActive> getCardList(String channel,Integer opid,Integer hour){
		List<WftCardActive> list = wftCardActiveMapper.findByUtimeAndCstatus(channel,opid,hour);
		return list;
	}
	
	
	private void reSetCard(List<WftCardActive> list,String channel){
		for(WftCardActive wftCardActive : list){
			Integer cid = wftCardActive.getId();
			List<WftConnSession> wftConnSessions = wftConnSessionMapper.findByCid(cid, null, channel);
			if(wftConnSessions !=null && wftConnSessions.size()>0){
				continue;
			}else{
				logger.info(String.format("channel=%s reset card cardId =%s,no=%s utime=%s", channel,cid,wftCardActive.getNo(), wftCardActive.getUtime()));
				WftCardActive cardActive =  new WftCardActive();
				cardActive.setId(cid);
				cardActive.setCstatus(WftCardActive.CSTATUS_AVAILABLE);
				cardActive.setCache(WftCardActive.OUT_CACHE);
				cardActive.setChannel(channel);
				wftCardActiveMapper.update(cardActive);
			}
		}
	}
	
	public void reSetCardStatusAndCache(){
		logger.info("reSetCard start");
		List<WftChannel> channelList = wftChannelMapper.findAll();
		for(WftChannel wftChannel : channelList){
			// 移动卡是12个小时没有使用过的卡进行重置
			List<WftCardActive> cmccList = getCardList(wftChannel.getCode(),1,12);
			logger.info("resetcard cmccList size" + ((cmccList !=null)?cmccList.size():0));
			reSetCard(cmccList,wftChannel.getCode());
			// 电信卡是48小时没有使用过的卡进行重置
			List<WftCardActive> ctccList = getCardList(wftChannel.getCode(),2,48);
			logger.info("resetcard ctccList size" + ((ctccList !=null)?ctccList.size():0));
			reSetCard(ctccList,wftChannel.getCode());
		}
		logger.info("reSetCard end");
	}


	public void setWftChannelMapper(WftChannelMapper wftChannelMapper) {
		this.wftChannelMapper = wftChannelMapper;
	}


	public void setWftCardActiveMapper(WftCardActiveMapper wftCardActiveMapper) {
		this.wftCardActiveMapper = wftCardActiveMapper;
	}


	public void setWftConnSessionMapper(WftConnSessionMapper wftConnSessionMapper) {
		this.wftConnSessionMapper = wftConnSessionMapper;
	}
	
}
