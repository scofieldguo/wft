package com.bw30.open.stat.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.bw30.open.common.dao.mapper.WftAcessCardMapper;
import com.bw30.open.common.dao.mapper.WftChannelMapper;
import com.bw30.open.common.model.WftChannel;
import com.bw30.open.common.model.stat.WftOpenAcessCardStat;
import com.bw30.open.stat.service.IWftAcessCardStatService;
import com.bw30.open.wft.mongo.MongoDBService;

public class WftAcessCardServiceImpl implements IWftAcessCardStatService

{
	private MongoDBService mongoDBService;
	private WftChannelMapper wftChannelMapper;
	private WftAcessCardMapper wftAcessCardMapper;

	@Override
	public void statCardUse(String dayYMD,String hour, Date startHour, Date endHour) {
		List<WftChannel> channelList = wftChannelMapper.findAll();
		for (WftChannel wftChannel : channelList) {
		Query cmccCardQuery=new Query();
		cmccCardQuery.addCriteria(Criteria.where("ssid").regex("cmcc","i"));
		cmccCardQuery.addCriteria(Criteria.where("stime").gte(startHour).lt(endHour));
		int cmccCount=mongoDBService.count(cmccCardQuery, MongoDBService.CONNDATAKEY+wftChannel.getCode());
		Query chinaNetCardQuery=new Query();
		chinaNetCardQuery.addCriteria(Criteria.where("ssid").regex("chinaNet","i"));
		chinaNetCardQuery.addCriteria(Criteria.where("stime").gte(startHour).lt(endHour));
		int chinaNetCount=mongoDBService.count(chinaNetCardQuery, MongoDBService.CONNDATAKEY+wftChannel.getCode());
		WftOpenAcessCardStat cardStat=new WftOpenAcessCardStat();
		cardStat.setChannel(wftChannel.getCode());
		cardStat.setDairy(dayYMD);
		cardStat.setHour(hour);
		cardStat.setCmcccnt(cmccCount);
		cardStat.setChcnt(chinaNetCount);
		wftAcessCardMapper.insert(cardStat);
		}
	}

	public void setMongoDBService(MongoDBService mongoDBService) {
		this.mongoDBService = mongoDBService;
	}

	public void setWftChannelMapper(WftChannelMapper wftChannelMapper) {
		this.wftChannelMapper = wftChannelMapper;
	}

	public void setWftAcessCardMapper(WftAcessCardMapper wftAcessCardMapper) {
		this.wftAcessCardMapper = wftAcessCardMapper;
	}

}
