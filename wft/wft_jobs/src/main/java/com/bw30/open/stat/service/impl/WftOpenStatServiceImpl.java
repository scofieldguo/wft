package com.bw30.open.stat.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bw30.open.common.dao.mapper.WftChannelMapper;
import com.bw30.open.common.dao.mapper.WftOpenMacStatMapper;
import com.bw30.open.common.dao.mapper.WftOpenStatMapper;
import com.bw30.open.common.model.WftChannel;
import com.bw30.open.common.model.WftConnSession;
import com.bw30.open.common.model.WftLoginData;
import com.bw30.open.common.model.stat.WftOpenMacStat;
import com.bw30.open.common.model.stat.WftOpenStat;
import com.bw30.open.stat.service.IWftOpenStatService;
import com.bw30.open.wft.mongo.MongoDBService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MapReduceOutput;

public class WftOpenStatServiceImpl implements IWftOpenStatService {

	private static final int LIMIT = 5000;
	private static final String CMCC = "CMCC";
	private static final String CHINANET = "ChinaNet";
	private WftChannelMapper wftChannelMapper;
	private MongoDBService mongoDBService;
	private WftOpenStatMapper wftOpenStatMapper;
	private WftOpenMacStatMapper wftOpenMacStatMapper;
	private static final Logger logger=Logger.getLogger("OpenStat");

	public void statChannelData(Date startDate, Date endDate, String dayYMD) {
		logger.info("Wft Open Stat Start");
		try {
		List<WftChannel> channelList = wftChannelMapper.findAll();
		statUseTime(startDate, endDate, dayYMD, channelList, CMCC);
		statUseTime(startDate, endDate, dayYMD, channelList, CHINANET);
		statSdkUseper(startDate, endDate, dayYMD, channelList);
		statConncnt(startDate, endDate, dayYMD, channelList);
		statConnper(startDate, endDate, dayYMD, channelList,CMCC);
		statConnper(startDate, endDate, dayYMD, channelList,CHINANET);
		statMacConn(startDate,endDate,dayYMD,channelList,1);
		statMacConn(startDate,endDate,dayYMD,channelList,2);
		statAccessCardFail(startDate,endDate,dayYMD,channelList,CMCC);
		statAccessCardFail(startDate,endDate,dayYMD,channelList,CHINANET);
		statChannelMac(dayYMD,channelList,CMCC);
		statChannelMac(dayYMD,channelList,CHINANET);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Wft Open Stat Error");
		}
		logger.info("Wft Open Stat End");
	}

	private void statChannelMac(String dayYMD, List<WftChannel> channelList,String operator) {
		logger.info("Stat Channel Mac Start");
		try {
		for (WftChannel wftChannel : channelList) {
			logger.info(String.format("Stat Channel=%s Mac Start",wftChannel.getCode()));
			Map<String,Object> paramMap=new HashMap<String, Object>();
			paramMap.put("dayYMD", dayYMD);
			paramMap.put("ssid", operator);
			int succ=wftOpenMacStatMapper.findSuccByParam(paramMap,wftChannel.getCode());
			int fail=wftOpenMacStatMapper.findFailByParam(paramMap,wftChannel.getCode());
			paramMap.put("failcnt",3);
			int o3three=wftOpenMacStatMapper.findFailOTByParam(paramMap,wftChannel.getCode());
			WftOpenStat openStat = new WftOpenStat();
			openStat.setDairy(dayYMD);
			openStat.setChannel(wftChannel.getCode());
			if(operator.equals(CMCC)){
				openStat.setCmfailmac(fail);
				openStat.setCmsuccmac(succ);
				openStat.setCmfaovthree(o3three);
			}else{
				openStat.setCnfailmac(fail);
				openStat.setCnsuccmac(succ);
				openStat.setCnfaovthree(o3three);
			}
			wftOpenStatMapper.insert(openStat);
			logger.info(String.format("Stat Channel=%s Mac Start succ=%s,fail=%s,o3=%s",wftChannel.getCode(),succ,fail,o3three));
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private void statAccessCardFail(Date startDate, Date endDate,
			String dayYMD, List<WftChannel> channelList, String operator) {
		logger.info("Stat Access Card Start");
		try {
		for (WftChannel wftChannel : channelList) {
			Query query=new Query();
			query.addCriteria(Criteria.where("ustatus").is(WftConnSession.USTATUS_CARD_FAIL));
			query.addCriteria(Criteria.where("etime").gte(startDate).lt(endDate));
			query.addCriteria(Criteria.where("ssid").regex(operator,"i"));
			int acessCardFail=mongoDBService.count(query, MongoDBService.CONNDATAKEY+wftChannel.getCode());
			WftOpenStat open=new WftOpenStat();
			if(operator.equals(CMCC)){
			open.setCmaccesscardfail(acessCardFail);
			}else{
				open.setCnaccesscardsucc(acessCardFail);
			}
			open.setChannel(wftChannel.getCode());
			open.setDairy(dayYMD);
			wftOpenStatMapper.insert(open);
			logger.info(String.format("Stat Channel=%s Access Card operator=%s,fail=%s",wftChannel.getCode(),operator,acessCardFail));
		}
		} catch (Exception e) {
				e.printStackTrace();
				logger.info("Stat Access Card Error");
		}
		logger.info("Stat Acess Card End");
	}

	private void statMacConn(Date startDate, Date endDate, String dayYMD,
			List<WftChannel> channelList,Integer flag) {
		logger.info("Stat Mac Conn Start");
		try {
		for (WftChannel wftChannel : channelList) {
		logger.info(String.format("Stat Channel=%s Mac Conn Start",wftChannel.getCode() ));
		DBCollection dbc = mongoDBService
				.getCollection(MongoDBService.CONNDATAKEY+wftChannel.getCode());
		String mapfun = "function(){emit({mac:this.mac},{count:1})}";
		String reducefun = "function(key, values){" + 
				"var count = 0;"+ 
				"for(var i in values) {" + 
					"count+= values[i].count;" +

			"}" + "return {count:count};" + "}";
		DBObject command = new BasicDBObject();
		DBObject query = new BasicDBObject();
		if(flag==1){
			query.put("uflag", WftConnSession.UFLAG_SUCCESS);
			query.put("etime",
					new BasicDBObject("$gte", startDate).append("$lt", endDate));
		}else{
			query.put("uflag", WftConnSession.UFLAG_FAIL);
			query.put("etime",
					new BasicDBObject("$gte", startDate).append("$lt", endDate));
		}
		query.put("ssid", new BasicDBObject("$ne", "bwlan"));
		command.put("mapreduce", MongoDBService.CONNDATAKEY+wftChannel.getCode());
		command.put("map", mapfun);
		command.put("reduce", reducefun);
		command.put("query", query);
		command.put("out", "{inline:1}");
		command.put("keytemp", false);
		command.put("verbose", true);
		MapReduceOutput mapReduceOutput = dbc.mapReduce(command);
		for (DBObject o : mapReduceOutput.results()) {
			JSONObject json = JSON.parseObject(o.toString());
			JSONObject json_id = JSON.parseObject(json.get("_id")
					.toString());
			String mac = json_id.getString("mac");
			JSONObject json_value = JSON.parseObject(json.get("value")
					.toString());
			Integer count = json_value.getIntValue("count");
			WftOpenMacStat wftQQMac = new WftOpenMacStat();
			wftQQMac.setDairy(dayYMD);
			wftQQMac.setMac(mac);
			wftQQMac.setChannel(wftChannel.getCode());
			if(flag==1){
				wftQQMac.setSucccnt(count);
			}else{
				wftQQMac.setFailcnt(count);
			}
			logger.info(String.format("Stat Channel=%s Mac Stat dairy=%s ,flag=%s,count=%s",wftChannel.getCode(),dayYMD,flag,count));
			wftOpenMacStatMapper.creatTable(wftChannel.getCode());
			wftOpenMacStatMapper.insertMac(wftQQMac,wftChannel.getCode());
		}
		wftOpenMacStatMapper.creatTable(wftChannel.getCode());
		mongoDBService.dropCollection("{inline:1}");
		}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Stat Mac Conn Error",e);
		}
		logger.info("Stat Mac Conn End");
	}

	private void statConnper(Date startDate, Date endDate, String dayYMD,
			List<WftChannel> channelList,String operate) {
		for (WftChannel wftChannel : channelList) {
			DBObject query =new BasicDBObject();
			query.put("etime",
					new BasicDBObject("$gte", startDate).append("$lt", endDate));
			query.put("ssid", new BasicDBObject("$regex",operate).append("$options", "i"));
			query.put("uflag", WftConnSession.UFLAG_SUCCESS);
			List succlist=mongoDBService.getCollection(MongoDBService.CONNDATAKEY+wftChannel.getCode()).distinct("uid", query);
			DBObject totalquery =new BasicDBObject();
			totalquery.put("etime",
					new BasicDBObject("$gte", startDate).append("$lt", endDate));
			totalquery.put("ssid", new BasicDBObject("$regex",operate).append("$options", "i"));
		//	totalquery.put("uflag", WftConnSession.UFLAG_FAIL);
			List totallist=mongoDBService.getCollection(MongoDBService.CONNDATAKEY+wftChannel.getCode()).distinct("uid", totalquery);
			WftOpenStat stat=new WftOpenStat();
			stat.setDairy(dayYMD);
			stat.setChannel(wftChannel.getCode());
			if(operate.equals(CMCC)){
				stat.setCmcnsuccper(succlist.size());
				stat.setCmcnfailper(totallist.size()-succlist.size());
			}else{
				stat.setCncnsuccper(succlist.size());
				stat.setCncnfailper(totallist.size()-succlist.size());
			}
			wftOpenStatMapper.insert(stat);
		}
	}

	private void statConncnt(Date startDate, Date endDate, String dayYMD,
			List<WftChannel> channelList) {
		for (WftChannel wftChannel : channelList) {
			String tableName = MongoDBService.CONNDATAKEY
					+ wftChannel.getCode();
			Query cmccQuery = new Query();
			cmccQuery.addCriteria(Criteria.where("etime").gte(startDate)
					.lt(endDate));
			cmccQuery.addCriteria(Criteria.where("ssid").regex(CMCC, "i"));
			cmccQuery.addCriteria(Criteria.where("uflag").is(
					WftConnSession.UFLAG_SUCCESS));
			int succCmccSize = mongoDBService.count(cmccQuery, tableName);
			Query chinaNetQuery = new Query();
			chinaNetQuery.addCriteria(Criteria.where("etime").gte(startDate)
					.lt(endDate));
			chinaNetQuery.addCriteria(Criteria.where("ssid").regex(CHINANET,
					"i"));
			chinaNetQuery.addCriteria(Criteria.where("uflag").is(
					WftConnSession.UFLAG_SUCCESS));
			int succChinaNetSize = mongoDBService.count(chinaNetQuery,
					tableName);
			Query failcmccQuery = new Query();
			failcmccQuery.addCriteria(Criteria.where("etime").gte(startDate)
					.lt(endDate));
			failcmccQuery.addCriteria(Criteria.where("ssid").regex(CMCC, "i"));
			failcmccQuery.addCriteria(Criteria.where("uflag").is(
					WftConnSession.UFLAG_FAIL));
			int failCmccSize = mongoDBService.count(failcmccQuery, tableName);
			Query failChinaNetQuery = new Query();
			failChinaNetQuery.addCriteria(Criteria.where("etime")
					.gte(startDate).lt(endDate));
			failChinaNetQuery.addCriteria(Criteria.where("ssid").regex(
					CHINANET, "i"));
			failChinaNetQuery.addCriteria(Criteria.where("uflag").is(
					WftConnSession.UFLAG_FAIL));
			int failChinaNetSize = mongoDBService.count(failChinaNetQuery,
					tableName);
			WftOpenStat t = new WftOpenStat();
			t.setDairy(dayYMD);
			t.setChannel(wftChannel.getCode());
			t.setCmcnfailcnt(failCmccSize);
			t.setCmcnsucccnt(succCmccSize);
			t.setCncnfailcnt(failChinaNetSize);
			t.setCncnsucccnt(succChinaNetSize);
			wftOpenStatMapper.insert(t);
		}
	}

	/**
	 * 计算sdk打开人数
	 * */
	private void statSdkUseper(Date startDate, Date endDate, String dayYMD,
			List<WftChannel> channelList) {
		logger.info("Stat SDK Useper");
		try {
		for (WftChannel wftChannel : channelList) {
			Query query = new Query();
			query.addCriteria(Criteria.where("logintime").gte(startDate)
					.lt(endDate));
			List<WftLoginData> loginlist = mongoDBService.find(query,
					WftLoginData.class, MongoDBService.LOGINDATAKEY
							+ wftChannel.getCode());
			DBCollection dbc = mongoDBService
					.getCollection(MongoDBService.LOGINDATAKEY+wftChannel.getCode());
			DBObject query1 = new BasicDBObject();
			query1.put("logintime",
					new BasicDBObject("$gte", startDate).append("$lt", endDate));
			List list = dbc.distinct("uid", query1);
			WftOpenStat total = new WftOpenStat();
			total.setDairy(dayYMD);
			total.setChannel(wftChannel.getCode());
			total.setSdkcnt(loginlist.size());
			total.setSdkper(list.size());
			wftOpenStatMapper.insert(total);
		}
		
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Stat SDK Useper Error!!");
		}
	}

	/**
	 * 计算电信,移动使用时长
	 * 
	 * */
	private void statUseTime(Date startDate, Date endDate, String dayYMD,
			List<WftChannel> channelList, String Operator) {
		logger.info(String.format("Stat Use Time Op=%s",Operator ));
		try {
		for (WftChannel wftChannel : channelList) {
			Query query = new Query();
			Criteria criteria = Criteria.where("etime").gte(startDate)
					.lt(endDate);
			criteria.and("ssid").regex(Operator, "i");
			criteria.and("uflag").is(WftConnSession.UFLAG_SUCCESS);
			int count = 0;
			query.addCriteria(criteria).skip(count).limit(LIMIT);
			List<WftConnSession> list = mongoDBService.find(query,
					WftConnSession.class, MongoDBService.CONNDATAKEY
							+ wftChannel.getCode());
			Long utvalue = 0l;
			Long utvalueOp = 0l;
			while (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getBstimeop() != null) {
						utvalueOp += list.get(i).getUtvalueop();
					}
					utvalue += list.get(i).getUtvalue();
				}
				count += 5000;
				Query query1 = new Query();
				query1.addCriteria(criteria).skip(count).limit(LIMIT);
				list = mongoDBService.find(query1, WftConnSession.class,
						MongoDBService.CONNDATAKEY + wftChannel.getCode());
				logger.info(String.format("UseTime Channel=%s,size=%s,utvalue=%s,utvalueop=%s,count=%s", wftChannel.getCode(),list.size(),utvalue,utvalueOp,count));
			}
			WftOpenStat openStat = new WftOpenStat();
			openStat.setDairy(dayYMD);
			openStat.setChannel(wftChannel.getCode());
			if (Operator.endsWith(CMCC)) {
				openStat.setCmutvalue(utvalue);
				openStat.setCmutvalueop(utvalueOp);
			} else {
				openStat.setCnutvalue(utvalue);
				openStat.setCnutvalueop(utvalueOp);
			}
			wftOpenStatMapper.insert(openStat);
		}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("StatUseTime Error !");
		}

	}


	public void setWftChannelMapper(WftChannelMapper wftChannelMapper) {
		this.wftChannelMapper = wftChannelMapper;
	}

	public void setMongoDBService(MongoDBService mongoDBService) {
		this.mongoDBService = mongoDBService;
	}

	public void setWftOpenStatMapper(WftOpenStatMapper wftOpenStatMapper) {
		this.wftOpenStatMapper = wftOpenStatMapper;
	}

	public void setWftOpenMacStatMapper(WftOpenMacStatMapper wftOpenMacStatMapper) {
		this.wftOpenMacStatMapper = wftOpenMacStatMapper;
	}

}
