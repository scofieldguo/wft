package com.bw30.open.wftAdm.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.bw30.open.common.dao.mapper.WftConnSessionMapper;
import com.bw30.open.common.dao.mapper.WftHotspotMapper;
import com.bw30.open.common.model.WftConnSession;
import com.bw30.open.common.model.WftHotspot;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.wft.common.cardpool.rmi.bean.CardRecord;
import com.bw30.open.wft.mongo.MongoDBService;

public class WftConnSessionService {

	public static final Logger LOG = Logger
			.getLogger(WftConnSessionService.class);
	public static final Logger CONN_LOGGER = Logger.getLogger("CONNSESSION");
	
	@Resource
	private WftHotspotMapper wftHotspotMapper;
	@Resource
	private WftConnSessionMapper wftConnSessionMapper;
	@Resource
	private MongoDBService mongoDBService;
	@Resource
	private CardService cardService;
	@Resource
	private CardApiService cardApiService;


	public void deleteConnSession(String channel, String csid) {
		this.wftConnSessionMapper.delete(channel, csid);
	}
	
	public List<WftConnSession> getActiveSession(String channel, String ssid,Integer minute,Date nowDate){
		return this.wftConnSessionMapper.findActiveSession(channel, ssid, minute, nowDate);
	}
	
	/**
	 * 获取未关闭且未开始计费，并且超时的连接会话
	 * 
	 * @param channel
	 * @param ssid
	 * @param minute 超时时间，分钟
	 * @param nowTime
	 * 
	 * @return
	 */
	public List<WftConnSession> getTimeoutConnSession(String channel,
			String ssid, Integer minute, Date nowTime) {
		return this.wftConnSessionMapper.findTimeoutConnSession(channel, ssid, 
				minute, nowTime);
	}

	/**
	 * 获取计费超过minute的连接会话
	 * 
	 * @param channel
	 * @param ssid
	 * @param minute 分钟
	 * @param nowTime
	 * 
	 * @return
	 */
	public List<WftConnSession> getInvalidConnSession(String channel,
			String ssid, Integer minute, Date nowTime) {
		return this.wftConnSessionMapper.findInvalidConnSession(channel, ssid,
				minute, nowTime);
	}

	public void setWftConnSessionMapper(
			WftConnSessionMapper wftConnSessionMapper) {
		this.wftConnSessionMapper = wftConnSessionMapper;
	}

	public void updateConnSession(String channel, WftConnSession connSession) {
		connSession.setChannel(channel);
		this.wftConnSessionMapper.update(connSession);
	}

	/**
	 * 关闭链接超时的会话
	 * 
	 * @param cs
	 * @param nowDate
	 */
	public void colseTimeOutConnSession(WftConnSession cs, Date nowDate) {
		cs.setEtime(nowDate);
		cs.setStatus(WftConnSession.STATUS_CLOSE);
		cs.setUstatus(WftConnSession.USTATUS_CLOSED_FORCE);
		// 把关闭的链接会话写入mongodb
		insertConnMongodb(cs);
		// 删除链接会话
		this.wftConnSessionMapper.delete(cs.getChannel(), cs.getCsid());
	}

	/**
	 * 获取运营商对应的卡使用记录<br/>
	 * ctcc:平台与运营商卡使用记录的开始使用时间相差在1分钟之内，则认为两条记录相对应
	 * 
	 * cmcc:平台与运营商卡使用记录的开始使用时间相差在2分钟之内，则认为两条记录相对应
	 * 
	 * @param channel
	 * @param aesKey
	 * @param opid
	 * @param cno
	 * @param pwd
	 * @param bsTime
	 *            计费开始时间
	 * @return
	 */
	public CardRecord getRecordFromOp(String csid, String channel, Integer op,
			String cno, String pwd, Date startDate, Date endDate, Date bstime) {
		LOG.info(String.format("match conn record[csid=%s, op=%s, no=%s]",
				csid, op, cno));
		try {
			String sday = new SimpleDateFormat("yyyy-MM-dd").format(startDate);
			String eday = new SimpleDateFormat("yyyy-MM-dd").format(endDate);

			if (WftOperator.OP_ID_CMCC == op) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(endDate);
				calendar.add(Calendar.DAY_OF_MONTH, 1);
				eday = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
			}

			List<CardRecord> list = this.cardApiService.queryRecord(channel,
					op, cno, pwd, sday, eday);
			if (null != list && list.size() > 0) {
				Long len = 0L;
				int i = 1;
				i = (WftOperator.OP_ID_CMCC == op ? 2 : 1);
				for (CardRecord r : list) {
					Date bsTimeOp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(r.getStarttime());
					len = bsTimeOp.getTime() - bstime.getTime();// 毫秒
					len /= 1000;// 秒
					LOG.info(String
							.format("match conn record[csid=%s, op=%s, no=%s]: timelen=%s second",
									csid, op, cno, len));
					if (-1 * i * 60 <= len && len <= i * 60) {
						LOG.info(String
								.format("match conn record[csid=%s, op=%s, no=%s]: match ok, timelen=%s second",
										csid, op, cno, len));
						return r;
					}
				}
				LOG.info(String.format(
						"match conn record[csid=%s, op=%s, no=%s]: match fail",
						csid, op, cno));
			} else {
				LOG.info(String
						.format("match conn record[csid=%s, op=%s, no=%s]: no op record",
								csid, op, cno));
			}
			return null;
		} catch (Exception e) {
			LOG.error("query record api error", e);
			return null;
		}

	}

	/**
	 * 关闭的链接会话写入mongodb
	 * 
	 * @param wftConnSession
	 */
	public void insertConnMongodb(WftConnSession wftConnSession) {
		try {
			if(0 == wftConnSession.getPrvid()){
				Integer prvid = 0;
				List<WftHotspot> hpList = this.wftHotspotMapper.findByMac(wftConnSession.getMac());
				if(null != hpList && 0 < hpList.size()){
					prvid = hpList.get(0).getPrvid();
				}
				wftConnSession.setPrvid(prvid);
			}
			this.mongoDBService.save(wftConnSession, MongoDBService.CONNDATAKEY
					+ wftConnSession.getChannel());
			CONN_LOGGER.info("OK;"
					+ JSON.toJSONString(wftConnSession).toString());
		} catch (Exception e) {
			e.printStackTrace();
			CONN_LOGGER.info("ERROR;"
					+ JSON.toJSONString(wftConnSession).toString());
		}
	}

	public void setWftHotspotMapper(WftHotspotMapper wftHotspotMapper) {
		this.wftHotspotMapper = wftHotspotMapper;
	}

	public WftConnSessionMapper getWftConnSessionMapper() {
		return wftConnSessionMapper;
	}

	public void setMongoDBService(MongoDBService mongoDBService) {
		this.mongoDBService = mongoDBService;
	}

	public void setCardService(CardService cardService) {
		this.cardService = cardService;
	}

	public void setCardApiService(CardApiService cardApiService) {
		this.cardApiService = cardApiService;
	}

}
