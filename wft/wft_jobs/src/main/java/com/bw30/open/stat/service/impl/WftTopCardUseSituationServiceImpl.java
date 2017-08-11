package com.bw30.open.stat.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.bw30.open.common.dao.mapper.WftChannelMapper;
import com.bw30.open.common.dao.mapper.WftConnSessionMapper;
import com.bw30.open.common.dao.mapper.WftOpenTopCardUseMapper;
import com.bw30.open.common.model.WftChannel;
import com.bw30.open.common.model.WftConnSession;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.common.model.stat.WftOpenTopCardUse;
import com.bw30.open.stat.service.IWftTopCardUseSituationService;
import com.bw30.open.wft.mongo.MongoDBService;

public class WftTopCardUseSituationServiceImpl implements IWftTopCardUseSituationService{
	
	private WftChannelMapper wftChannelMapper;
	private WftConnSessionMapper wftConnSessionMapper;
	private MongoDBService mongoDBService;
	private WftOpenTopCardUseMapper wftOpenTopCardUseMapper;
	
	@Override
	public void stat(String dayYMD, String hour, String minute,
			Date startMinute, Date endMinute) {
		try {
		List<WftChannel> channleList=wftChannelMapper.findAll();
		for (WftChannel wftChannel : channleList) {
			int CMCC_startChargeUsingCard=wftConnSessionMapper.countCardUseSituation(wftChannel.getCode(),WftConnSession.STATUS_ALIVE,WftOperator.OP_ID_CMCC);
			int CN_startChargeUsingCard=wftConnSessionMapper.countCardUseSituation(wftChannel.getCode(),WftConnSession.STATUS_ALIVE,WftOperator.OP_ID_CTCC);
			int CMCC_no_startChargeUsingCard=wftConnSessionMapper.countCardUseSituation(wftChannel.getCode(),WftConnSession.STATUS_NEW,WftOperator.OP_ID_CMCC);
			int CN_no_startChargeUsingCard=wftConnSessionMapper.countCardUseSituation(wftChannel.getCode(),WftConnSession.STATUS_NEW,WftOperator.OP_ID_CTCC);
			CMCC_no_startChargeUsingCard+=wftConnSessionMapper.countCardUseSituation(wftChannel.getCode(),WftConnSession.STATUS_CLOSE, WftOperator.OP_ID_CMCC);
			CN_no_startChargeUsingCard+=wftConnSessionMapper.countCardUseSituation(wftChannel.getCode(),WftConnSession.STATUS_CLOSE, WftOperator.OP_ID_CTCC);
			Query query=new Query();
			query.addCriteria(Criteria.where("time").lte(endMinute).gte(startMinute));
			query.addCriteria(Criteria.where("opid").is(WftOperator.OP_ID_CMCC));
			int CM_NO_ACCESS_CARD=mongoDBService.count(query, MongoDBService.KEY_NO_CARD+wftChannel.getCode());
			Query cnquery=new Query();
			cnquery.addCriteria(Criteria.where("time").lte(endMinute).gte(startMinute));
			cnquery.addCriteria(Criteria.where("opid").is(WftOperator.OP_ID_CTCC));
			int CN_NO_ACCESS_CARD=mongoDBService.count(cnquery, MongoDBService.KEY_NO_CARD+wftChannel.getCode());
			WftOpenTopCardUse cardUse=new WftOpenTopCardUse();
			cardUse.setChannel(wftChannel.getCode());
			cardUse.setDairy(dayYMD);
			cardUse.setHour(hour);
			cardUse.setMinute(minute);
			cardUse.setCm_chargeusingcard(CMCC_startChargeUsingCard);
			cardUse.setCm_nochargeusingcard(CMCC_no_startChargeUsingCard);
			cardUse.setCm_noacesscard(CM_NO_ACCESS_CARD);
			cardUse.setCn_chargeusingcard(CN_startChargeUsingCard);
			cardUse.setCn_nochargeusingcard(CN_no_startChargeUsingCard);
			cardUse.setCn_noacesscard(CN_NO_ACCESS_CARD);
			wftOpenTopCardUseMapper.insert(cardUse);
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void setWftChannelMapper(WftChannelMapper wftChannelMapper) {
		this.wftChannelMapper = wftChannelMapper;
	}
	public void setWftConnSessionMapper(WftConnSessionMapper wftConnSessionMapper) {
		this.wftConnSessionMapper = wftConnSessionMapper;
	}
	public void setMongoDBService(MongoDBService mongoDBService) {
		this.mongoDBService = mongoDBService;
	}
	public void setWftOpenTopCardUseMapper(
			WftOpenTopCardUseMapper wftOpenTopCardUseMapper) {
		this.wftOpenTopCardUseMapper = wftOpenTopCardUseMapper;
	}

}
