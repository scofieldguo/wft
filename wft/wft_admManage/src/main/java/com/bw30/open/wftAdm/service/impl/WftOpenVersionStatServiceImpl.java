package com.bw30.open.wftAdm.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bw30.open.common.dao.mapper.WftChannelMapper;
import com.bw30.open.common.dao.mapper.WftOpenVersionMacStatMapper;
import com.bw30.open.common.dao.mapper.WftOpenVersionStatMapper;
import com.bw30.open.common.model.WftChannel;
import com.bw30.open.common.model.WftConnSession;
import com.bw30.open.common.model.versionstat.WftOpenVersionMacStat;
import com.bw30.open.common.model.versionstat.WftOpenVersionStat;
import com.bw30.open.wft.mongo.MongoDBService;
import com.bw30.open.wftAdm.service.operate.IWftOpenVersionStatService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MapReduceOutput;

public class WftOpenVersionStatServiceImpl implements
		IWftOpenVersionStatService {
	@Resource
	private MongoDBService mongoDBService;
	@Resource
	private WftOpenVersionStatMapper wftOpenVersionStatMapper;
	@Resource
	private WftOpenVersionMacStatMapper wftOpenVersionMacStatMapper;

	private WftChannelMapper wftChannelMapper;

	@Override
	public void stat(Date startDate, Date endDate, String dayYMD,
			String version, String channel) {
		statConncnt(startDate, endDate, dayYMD, version, channel);
		statConnper(startDate, endDate, dayYMD, channel, version, "CMCC");
		statConnper(startDate, endDate, dayYMD, channel, version, "ChinaNet");
		statMacConn(startDate, endDate, dayYMD, channel, version, 1);
		statMacConn(startDate, endDate, dayYMD, channel, version, 2);
		statMacToTotal(dayYMD, channel, version);

	}

	private void statMacToTotal(String dayYMD, String channel, String version) {
		int cm_succ = wftOpenVersionMacStatMapper.statMac(dayYMD, channel,
				version, 1, "CMCC");
		int cm_fail = wftOpenVersionMacStatMapper.statMac(dayYMD, channel,
				version, 2, "CMCC");
		int cn_succ = wftOpenVersionMacStatMapper.statMac(dayYMD, channel,
				version, 1, "ChinaNet");
		int cn_fail = wftOpenVersionMacStatMapper.statMac(dayYMD, channel,
				version, 2, "ChinaNet");
		WftOpenVersionStat t = new WftOpenVersionStat();
		t.setDairy(dayYMD);
		t.setVersion(version);
		t.setChannel(channel);
		t.setCm_succmac(cm_succ);
		t.setCm_failmac(cm_fail);
		t.setCn_succmac(cn_succ);
		t.setCn_failmac(cn_fail);
		wftOpenVersionStatMapper.insert(t);
	}

	private void statConncnt(Date startDate, Date endDate, String dayYMD,
			String version, String channel) {
		String tableName = MongoDBService.CONNDATAKEY + channel;
		Query cmccQuery = new Query();
		cmccQuery.addCriteria(Criteria.where("etime").gte(startDate)
				.lt(endDate));
		cmccQuery.addCriteria(Criteria.where("ssid").regex("CMCC", "i"));
		cmccQuery.addCriteria(Criteria.where("uflag").is(
				WftConnSession.UFLAG_SUCCESS));
		cmccQuery.addCriteria(Criteria.where("version").is(version));
		int succCmccSize = mongoDBService.count(cmccQuery, tableName);
		Query chinaNetQuery = new Query();
		chinaNetQuery.addCriteria(Criteria.where("etime").gte(startDate)
				.lt(endDate));
		chinaNetQuery
				.addCriteria(Criteria.where("ssid").regex("CHINANET", "i"));
		chinaNetQuery.addCriteria(Criteria.where("uflag").is(
				WftConnSession.UFLAG_SUCCESS));
		chinaNetQuery.addCriteria(Criteria.where("version").is(version));
		int succChinaNetSize = mongoDBService.count(chinaNetQuery, tableName);
		Query failcmccQuery = new Query();
		failcmccQuery.addCriteria(Criteria.where("etime").gte(startDate)
				.lt(endDate));
		failcmccQuery.addCriteria(Criteria.where("ssid").regex("CMCC", "i"));
		failcmccQuery.addCriteria(Criteria.where("uflag").is(
				WftConnSession.UFLAG_FAIL));
		failcmccQuery.addCriteria(Criteria.where("version").is(version));
		int failCmccSize = mongoDBService.count(failcmccQuery, tableName);
		Query failChinaNetQuery = new Query();
		failChinaNetQuery.addCriteria(Criteria.where("etime").gte(startDate)
				.lt(endDate));
		failChinaNetQuery.addCriteria(Criteria.where("ssid").regex("CHINANET",
				"i"));
		failChinaNetQuery.addCriteria(Criteria.where("uflag").is(
				WftConnSession.UFLAG_FAIL));
		failChinaNetQuery.addCriteria(Criteria.where("version").is(version));
		int failChinaNetSize = mongoDBService.count(failChinaNetQuery,
				tableName);
		WftOpenVersionStat t = new WftOpenVersionStat();
		t.setDairy(dayYMD);
		t.setVersion(version);
		t.setChannel(channel);
		t.setCm_failcnt(failCmccSize);
		t.setCm_succcnt(succCmccSize);
		t.setCn_failcnt(failChinaNetSize);
		t.setCn_succcnt(succChinaNetSize);
		wftOpenVersionStatMapper.insert(t);
	}

	private void statConnper(Date startDate, Date endDate, String dayYMD,
			String channel, String version, String operate) {
		DBObject query = new BasicDBObject();
		query.put("etime",
				new BasicDBObject("$gte", startDate).append("$lt", endDate));
		query.put("ssid",
				new BasicDBObject("$regex", operate).append("$options", "i"));
		query.put("version", version);
		query.put("uflag", WftConnSession.UFLAG_SUCCESS);
		List succlist = mongoDBService.getCollection(
				MongoDBService.CONNDATAKEY + channel).distinct("uid", query);
		DBObject totalquery = new BasicDBObject();
		totalquery.put("etime",
				new BasicDBObject("$gte", startDate).append("$lt", endDate));
		totalquery.put("ssid",
				new BasicDBObject("$regex", operate).append("$options", "i"));
		totalquery.put("version", version);
		List totallist = mongoDBService.getCollection(
				MongoDBService.CONNDATAKEY + channel).distinct("uid",
				totalquery);
		WftOpenVersionStat stat = new WftOpenVersionStat();
		stat.setDairy(dayYMD);
		stat.setChannel(channel);
		stat.setVersion(version);
		if (operate.equals("CMCC")) {
			stat.setCm_succper(succlist.size());
			stat.setCm_failper(totallist.size() - succlist.size());
		} else {
			stat.setCn_succper(succlist.size());
			stat.setCn_failper(totallist.size() - succlist.size());
		}
		wftOpenVersionStatMapper.insert(stat);
	}

	private void statMacConn(Date startDate, Date endDate, String dayYMD,
			String channel, String version, Integer flag) {
		DBCollection dbc = mongoDBService
				.getCollection(MongoDBService.CONNDATAKEY + channel);
		String mapfun = "function(){emit({mac:this.mac},{count:1})}";
		String reducefun = "function(key, values){" + "var count = 0;"
				+ "for(var i in values) {" + "count+= values[i].count;" +

				"}" + "return {count:count};" + "}";
		DBObject command = new BasicDBObject();
		DBObject query = new BasicDBObject();
		query.put("version", version);
		if (flag == 1) {
			query.put("uflag", WftConnSession.UFLAG_SUCCESS);
			query.put("etime",
					new BasicDBObject("$gte", startDate).append("$lt", endDate));
		} else {
			query.put("uflag", WftConnSession.UFLAG_FAIL);
			query.put("etime",
					new BasicDBObject("$gte", startDate).append("$lt", endDate));
		}
		query.put("ssid", new BasicDBObject("$ne", "bwlan"));
		command.put("mapreduce", MongoDBService.CONNDATAKEY + channel);
		command.put("map", mapfun);
		command.put("reduce", reducefun);
		command.put("query", query);
		command.put("out", "{inline:1}");
		command.put("keytemp", false);
		command.put("verbose", true);
		MapReduceOutput mapReduceOutput = dbc.mapReduce(command);
		for (DBObject o : mapReduceOutput.results()) {
			JSONObject json = JSON.parseObject(o.toString());
			JSONObject json_id = JSON.parseObject(json.get("_id").toString());
			String mac = json_id.getString("mac");
			JSONObject json_value = JSON.parseObject(json.get("value")
					.toString());
			Integer count = json_value.getIntValue("count");
			WftOpenVersionMacStat wftMac = new WftOpenVersionMacStat();
			wftMac.setDairy(dayYMD);
			wftMac.setMac(mac);
			wftMac.setChannel(channel);
			wftMac.setVersion(version);
			if (flag == 1) {
				wftMac.setSucccnt(count);
			} else {
				wftMac.setFailcnt(count);
			}
			wftOpenVersionMacStatMapper.creatTable(channel);
			wftOpenVersionMacStatMapper.insertMac(wftMac, channel);
		}
		wftOpenVersionMacStatMapper.creatTable(channel);
		mongoDBService.dropCollection("{inline:1}");
	}

	@Override
	public List<WftChannel> findAllChannel() {
		return wftChannelMapper.findAll();
	}

	@Override
	public List<WftOpenVersionStat> getVersionStat(String dairy,
			String version, String channel) {
		return wftOpenVersionStatMapper
				.findVersionStat(dairy, version, channel);
	}

	public void setMongoDBService(MongoDBService mongoDBService) {
		this.mongoDBService = mongoDBService;
	}

	public void setWftOpenVersionStatMapper(
			WftOpenVersionStatMapper wftOpenVersionStatMapper) {
		this.wftOpenVersionStatMapper = wftOpenVersionStatMapper;
	}

	public void setWftOpenVersionMacStatMapper(
			WftOpenVersionMacStatMapper wftOpenVersionMacStatMapper) {
		this.wftOpenVersionMacStatMapper = wftOpenVersionMacStatMapper;
	}

	public void setWftChannelMapper(WftChannelMapper wftChannelMapper) {
		this.wftChannelMapper = wftChannelMapper;
	}

}
