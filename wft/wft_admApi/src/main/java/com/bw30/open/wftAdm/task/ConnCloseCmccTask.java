package com.bw30.open.wftAdm.task;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.bw30.open.common.dao.mapper.WftCardActiveMapper;
import com.bw30.open.common.dao.mapper.WftChannelMapper;
import com.bw30.open.common.dao.mapper.WftConnSessionMapper;
import com.bw30.open.common.model.WftCardActive;
import com.bw30.open.common.model.WftChannel;
import com.bw30.open.common.model.WftConnSession;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.wft.mongo.MongoDBService;

/**
 * 处理移动会话： 处理已关闭的移动会话（前端对于下线成功的移动会话，关闭会话但不释放卡）
 * 
 * @author Jack
 * 
 *         2014年11月13日
 */
public class ConnCloseCmccTask {
	private static final Logger LOG = Logger.getLogger(ConnCloseCmccTask.class);

	@Resource
	private WftChannelMapper wftChannelMapper;
	@Resource
	private WftCardActiveMapper wftCardActiveMapper;
	@Resource
	private WftConnSessionMapper wftConnSessionMapper;
	@Resource
	private MongoDBService mongoDBService;

	public void doConn() {
		List<WftChannel> channelList = this.wftChannelMapper.findAll();
		if (null != channelList && 0 < channelList.size()) {
			for (WftChannel channel : channelList) {
				// 移动
				Integer minute = channel.getCmccinterval();// 秒
				if (-1 == minute) {
					minute = 2;
				} else {
					minute /= 60;
					minute += 2;
				}

				this.doConnForChannel(channel.getCode(), minute);
			}
		}
	}

	private void doConnForChannel(String channel, Integer minute) {
		List<WftConnSession> csList = this.wftConnSessionMapper
				.findCloseConnSession(channel, WftOperator.OP_SSID_CMCC,
						minute, new Date());
		if (null != csList && 0 < csList.size()) {
			for (WftConnSession cs : csList) {
				this.removeSessionAndFreeCard(channel, cs);
			}
		}
	}

	/**
	 * 释放卡，移除会话
	 * 
	 * @param channel
	 * @param cs
	 */
	private void removeSessionAndFreeCard(String channel, WftConnSession cs) {
		try {
			Date bstime = cs.getBstime();
			Date betime = cs.getBetime();
			Long time = 0L;
			if (null != bstime && null != betime) {
				time = betime.getTime() - bstime.getTime();
				time = time / 1000;// 秒
			}

			WftCardActive ca = this.wftCardActiveMapper.findById(channel, cs.getCid());
			if(ca == null){
				return;
			}
			if(WftCardActive.CSTATUS_STOP != ca.getCstatus()){
				this.wftCardActiveMapper.freebackCard(channel, cs.getCid(),
						time.intValue());
			}

			try {
				this.mongoDBService.save(cs, MongoDBService.CONNDATAKEY
						+ channel);
				this.wftConnSessionMapper.delete(channel, cs.getCsid());
			} catch (Exception e) {
				LOG.error("remove session error: channel=" + channel + ", csid=" + cs.getCsid(), e);
			}

		} catch (Exception e) {
			LOG.error("", e);
		}
	}

	public void setWftChannelMapper(WftChannelMapper wftChannelMapper) {
		this.wftChannelMapper = wftChannelMapper;
	}

	public void setWftCardActiveMapper(WftCardActiveMapper wftCardActiveMapper) {
		this.wftCardActiveMapper = wftCardActiveMapper;
	}

	public void setWftConnSessionMapper(
			WftConnSessionMapper wftConnSessionMapper) {
		this.wftConnSessionMapper = wftConnSessionMapper;
	}

	public void setMongoDBService(MongoDBService mongoDBService) {
		this.mongoDBService = mongoDBService;
	}

}
