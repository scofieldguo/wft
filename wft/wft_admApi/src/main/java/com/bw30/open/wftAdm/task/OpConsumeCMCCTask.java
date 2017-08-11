package com.bw30.open.wftAdm.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.bw30.open.common.dao.mapper.WftChannelMapper;
import com.bw30.open.common.model.WftCardActive;
import com.bw30.open.common.model.WftChannel;
import com.bw30.open.common.model.WftConnSession;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.wft.common.DateUtil;
import com.bw30.open.wft.common.cardpool.rmi.bean.CardRecord;
import com.bw30.open.wft.mongo.MongoDBService;
import com.bw30.open.wftAdm.service.CardService;
import com.bw30.open.wftAdm.service.WftConnSessionService;

public class OpConsumeCMCCTask {
	private static final Logger LOG = Logger.getLogger(OpConsumeCMCCTask.class);
	
	private static final SimpleDateFormat SDF_HMD_HMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Resource
	private MongoDBService mongoDBService;
	@Resource
	private CardService cardService;
	@Resource
	private WftConnSessionService wftConnSessionService;
	@Resource
	private WftChannelMapper wftChannelMapper;
	
	public void cardRecordOp(){
		Date nowDate = new Date();
		try {
			List<WftChannel> channelList = this.wftChannelMapper.findAll();
			if (null != channelList && 0 < channelList.size()) {
				for (WftChannel channel : channelList) {
					doCardRecordOp(nowDate, channel.getCode(), WftOperator.OP_SSID_CMCC);
				}
			}
			
		} catch (Exception e) {
			LOG.error("card cardRecordOp task run error", e);
			return;
		}
		
	}

	public void doCardRecordOp(Date nowDate, String channel, String ssid){
		try {
			LOG.info(String.format("cardRecordOpTask: channel=%s, ssid=%s", channel, ssid));
			Date statDate = DateUtil.getNextNumDay(nowDate, -1);
			Date endDate = DateUtil.getNextNumDay(statDate, 1);
//			Date recordSDate = DateUtil.getNextNumDay(statDate, -1);
			Query query = new Query();
			query.addCriteria(Criteria.where("ssid").regex(ssid + ".*", "i"));
			query.addCriteria(Criteria.where("etime").gte(statDate)
					.lt(endDate));
			query.addCriteria(Criteria.where("mflag").is(WftConnSession.CONN_RECORD_FALG_ERROR));
			List<WftConnSession> list = mongoDBService.find(query, WftConnSession.class, MongoDBService.CONNDATAKEY+channel);
			if(null != list && 0 < list.size()){
				LOG.info(String.format("cardRecordOpTask: channel=%s, ssid=%s, size=%s", channel, ssid, list.size()));
				for(WftConnSession cc : list){
					Map<String, Object> pv = new HashMap<String, Object>();
					CardRecord record = null;
					WftCardActive card = this.cardService
							.findCardById(channel,cc.getCid());
					record = wftConnSessionService.getRecordFromOp(cc.getCsid(), channel,
							card.getOpid(), card.getNo(), card.getPwd(),
							statDate, nowDate, cc.getBstime());
					if(null != record){
						LOG.info(String.format("this usedcard  opRecord is cno:%s,csid=%s,ssid=%s", cc.getCno(),cc.getCsid(),cc.getSsid()));
						pv.put("bstimeop", SDF_HMD_HMS.parse(record.getStarttime()));
						pv.put("betimeop", SDF_HMD_HMS.parse(record.getEndtime()));
						pv.put("utvalueop", record.getTimelen());
						pv.put("uhourop", ((record.getTimelen()/3600)+1));
						pv.put("mflag", WftConnSession.CONN_RECORD_FALG_YES);
						
					}else{
						LOG.info(String.format("this not usedcard  opRecord is cno:%s,csid=%s,ssid=%s", cc.getCno(),cc.getCsid(),cc.getSsid()));
						pv.put("mflag", WftConnSession.CONN_RECORD_FALG_ERROR);
					}
					this.mongoDBService.update(new Query(Criteria.where("csid")
							.is(cc.getCsid()).and("cid").is(cc.getCid())), pv, MongoDBService.CONNDATAKEY+channel);
				}
			}else{
				LOG.info(String.format("cardRecordOpTask: channel=%s, ssid=%s, size=0", channel, ssid));
			}
		}catch (Exception e) {
			LOG.error("OpConsumeTask run error", e);
		}
	}
	
	public void setMongoDBService(MongoDBService mongoDBService) {
		this.mongoDBService = mongoDBService;
	}

	public void setCardService(CardService cardService) {
		this.cardService = cardService;
	}

	public void setWftConnSessionService(WftConnSessionService wftConnSessionService) {
		this.wftConnSessionService = wftConnSessionService;
	}

	public void setWftChannelMapper(WftChannelMapper wftChannelMapper) {
		this.wftChannelMapper = wftChannelMapper;
	}
	
}
