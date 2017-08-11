package com.bw30.open.stat.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bw30.open.common.dao.mapper.UserMapper;
import com.bw30.open.common.dao.mapper.WftApTypeMapper;
import com.bw30.open.common.dao.mapper.WftChannelMapper;
import com.bw30.open.common.dao.mapper.WftMacStatMapper;
import com.bw30.open.common.dao.mapper.WftOpenPlatFormStatMapper;
import com.bw30.open.common.dao.mapper.WftProvinceMapper;
import com.bw30.open.common.dao.mapper.WftTotalStatMapper;
import com.bw30.open.common.model.OpenPlatformUser;
import com.bw30.open.common.model.WftApType;
import com.bw30.open.common.model.WftChannel;
import com.bw30.open.common.model.WftConnSession;
import com.bw30.open.common.model.WftProvince;
import com.bw30.open.common.model.stat.WftMacStat;
import com.bw30.open.common.model.stat.WftOpenPlatFormStat;
import com.bw30.open.common.model.stat.WftTotalStat;
import com.bw30.open.wft.common.DateUtil;
import com.bw30.open.wft.mongo.MongoDBService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MapReduceOutput;

public class WftTotalStatServiceImpl{

	private static final int LIMIT = 5000;
	private MongoDBService mongoDBService;
	private WftApTypeMapper wftApTypeMapper;
	private WftTotalStatMapper wftTotalStatMapper;
	private WftProvinceMapper wftProvinceMapper;
	private WftMacStatMapper wftMacStatMapper;
	private UserMapper userMapper;
	private WftChannelMapper wftChannelMapper;
	private WftOpenPlatFormStatMapper wftOpenPlatFormStatMapper;

	private static final Logger logger = Logger.getLogger("OpenStat");

	/**
	 * 获取正式的用户
	 * 
	 * @return
	 */
	private List<OpenPlatformUser> getStatUser() {
		List<OpenPlatformUser> list = userMapper.findAll();
		List<OpenPlatformUser> statUser = new ArrayList<OpenPlatformUser>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("status", WftChannel.STATUS_ENABLE);
		paramMap.put("isbill", WftChannel.ISBILL_YES);
		List<WftChannel> channelList = wftChannelMapper.findByParam(paramMap);
		for (WftChannel wftChannel : channelList) {
			for(OpenPlatformUser openPlatformUser:list){
				if(wftChannel.getCode().equals(openPlatformUser.getChannelcode())){
					statUser.add(openPlatformUser);
				}
			}
		}
		return statUser;
	}
	
	private List<WftProvince> getProvinces(){
		List<WftProvince> wftProvinces = wftProvinceMapper.findAll();
		return wftProvinces;
	}

	/**
	 * 获取该应用得所有运行商的热点类别
	 * 
	 * @param opids
	 * @return
	 */
	private List<WftApType> getApTypeOfUser(OpenPlatformUser openPlatformUser) {
		String opids = openPlatformUser.getOpids();
		if (null != opids && !"".endsWith(opids)) {
			List<WftApType> allList = new ArrayList<WftApType>();

			String opArray[] = opids.split(",");
			for (String opidStr : opArray) {
				try {
					Integer opid = Integer.parseInt(opidStr.trim());
					List<WftApType> list = getApTypesByOpid(opid);
					allList.addAll(list);
				} catch (Exception e) {
					continue;
					// TODO: handle exception
				}
			}
			return allList;
		}
		return null;

	}

	/**
	 * 获取运行下的热点类型
	 * 
	 * @param opid
	 * @return
	 */
	private List<WftApType> getApTypesByOpid(Integer opid) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("opid", opid);
		List<WftApType> list = wftApTypeMapper.findByParam(paramMap);
		return list;
	}

	/**
	 * 数据统计
	 * 
	 * @param startDate
	 * @param endDate
	 * @param dayYMD
	 */
	public void statAppData(Date date) {
		Date startDate = DateUtil.getNextNumDay(date, 0);
		Date endDate = DateUtil.getNextNumDay(startDate, 1);
		String dayYMD=DateUtil.formateDate(startDate, "yyyy-MM-dd");
		Date day = DateUtil.strToDate(dayYMD);
		List<WftProvince> proList = getProvinces();
		List<OpenPlatformUser> userList = getStatUser();
		for (OpenPlatformUser openPlatformUser : userList) {
			List<WftApType> apTypeList = getApTypeOfUser(openPlatformUser);
			for (WftApType wftApType : apTypeList) {
				for(WftProvince wftProvince : proList){
					String channel = openPlatformUser.getChannelcode();
					// 链接人数统计
					boolean flag = statConnper(startDate, endDate, day, null, wftApType.getSsid(), channel, wftApType.getOpid(),wftProvince.getId());
					if(!flag){
						continue;
					}
					// 链接数统计
					statConncnt(startDate, endDate, day, null, wftApType.getSsid(), channel, wftApType.getOpid(),wftProvince.getId());
					// 时长统计
					statUseTime(startDate, endDate, day,null,
							 wftApType.getSsid(), channel,
							wftApType.getOpid(),wftProvince.getId());
					// 取卡失败
					statAccessCardFail(startDate, endDate, day,null, wftApType.getSsid(), channel, wftApType.getOpid(),wftProvince.getId());
					// 热点统计（成功失败）
					wftMacStatMapper.creatTable(channel);
					statMacConn(startDate, endDate, day, null, wftApType.getSsid(), channel, WftConnSession.UFLAG_SUCCESS,wftProvince.getId(), wftApType.getOpid());
					statMacConn(startDate, endDate, day, null, wftApType.getSsid(), channel, WftConnSession.UFLAG_FAIL,wftProvince.getId(), wftApType.getOpid());
					// mac汇总统计
					statChannelMac(day, null, wftApType.getSsid(), channel,wftApType.getOpid(),wftProvince.getId());
				// statSdkUseper(startDate, endDate, dayYMD, channelList);
				}
			}
		}
		openPlatFormStat(dayYMD);

	}
	
	
	private void openPlatFormStat(String day){
		List<WftTotalStat> wftTotalStats  =wftTotalStatMapper.findByDairy(day);
		if(null != wftTotalStats && wftTotalStats.size()>0){
			for (WftTotalStat wftTotalStat : wftTotalStats) {
				WftOpenPlatFormStat wftOpenPlatFormStat = new WftOpenPlatFormStat();
				wftOpenPlatFormStat.setChannel(wftTotalStat.getChannel());
				wftOpenPlatFormStat.setOpid(wftTotalStat.getOpid());
				wftOpenPlatFormStat.setDairy(wftTotalStat.getDairy());
				wftOpenPlatFormStat.setConnfail(wftTotalStat.getConnfail());
				wftOpenPlatFormStat.setConnsuc(wftTotalStat.getConnsuc());
				wftOpenPlatFormStat.setPersuc(wftTotalStat.getPersuc());
				wftOpenPlatFormStat.setPerfail(wftTotalStat.getPerfail());
				wftOpenPlatFormStat.setMacfail(wftTotalStat.getMacfail());
				wftOpenPlatFormStat.setMacsuc(wftTotalStat.getMacsuc());
				wftOpenPlatFormStat.setOvernfail(wftTotalStat.getOvernfail());
				wftOpenPlatFormStat.setUtvalue(wftTotalStat.getUtvalue());
				wftOpenPlatFormStat.setUtvalueop(wftTotalStat.getUtvalueop());
				wftOpenPlatFormStat.setNocard(wftTotalStat.getNocard());
				wftOpenPlatFormStatMapper.insert(wftOpenPlatFormStat);
			}
		}
	}
	
	/**
	 * 数据统计
	 * 
	 * @param startDate
	 * @param endDate
	 * @param dayYMD
	 */
	public void statAppDataAuto() {
		logger.info("stat job start");
		Date date = new Date();
		Date startDate = DateUtil.getNextNumDay(date, -1);
		Date endDate = DateUtil.getNextNumDay(date, 0);
		String dayYMD=DateUtil.formateDate(startDate, "yyyy-MM-dd");
		Date day = DateUtil.strToDate(dayYMD);
		List<WftProvince> proList = getProvinces();
		List<OpenPlatformUser> userList = getStatUser();
		for (OpenPlatformUser openPlatformUser : userList) {
			List<WftApType> apTypeList = getApTypeOfUser(openPlatformUser);
			for (WftApType wftApType : apTypeList) {
				for(WftProvince wftProvince : proList){
				String channel = openPlatformUser.getChannelcode();
				// 链接人数统计
				boolean flag = statConnper(startDate, endDate, day, null, wftApType.getSsid(), channel, wftApType.getOpid(),wftProvince.getId());
				if(!flag){
					continue;
				}
				// 链接数统计
				statConncnt(startDate, endDate, day, null, wftApType.getSsid(), channel, wftApType.getOpid(),wftProvince.getId());
				// 时长统计
				statUseTime(startDate, endDate, day,null,
						 wftApType.getSsid(), channel,
						wftApType.getOpid(),wftProvince.getId());
				// 取卡失败
				statAccessCardFail(startDate, endDate, day,null, wftApType.getSsid(), channel, wftApType.getOpid(),wftProvince.getId());
				// 热点统计（成功失败）
				wftMacStatMapper.creatTable(channel);
				statMacConn(startDate, endDate, day, null, wftApType.getSsid(), channel, WftConnSession.UFLAG_SUCCESS,wftProvince.getId(), wftApType.getOpid());
				statMacConn(startDate, endDate, day, null, wftApType.getSsid(), channel, WftConnSession.UFLAG_FAIL,wftProvince.getId(), wftApType.getOpid());
				// mac汇总统计
				statChannelMac(day, null, wftApType.getSsid(), channel,wftApType.getOpid(),wftProvince.getId());
				
				// statSdkUseper(startDate, endDate, dayYMD, channelList);
			}
			}
		}
		openPlatFormStat(dayYMD);
	}

	/**
	 * 时长统计
	 * @param startDate
	 * @param endDate
	 * @param dayYMD
	 * @param appId
	 * @param ssid
	 * @param channel
	 * @param opid
	 */
	private void statUseTime(Date startDate, Date endDate, Date dayYMD,
			Integer appId, String ssid, String channel, Integer opid,Integer prvid) {
		logger.info(String.format("Stat Use Time Op=%s", ssid));
		try {
			logger.info(String.format("ssid=%s,channel=%s,opid=%s,prvid=%s", ssid,channel,opid,prvid));
			Query query = new Query();
			Criteria criteria = Criteria.where("etime").gte(startDate).lt(
					endDate);
			criteria.and("ssid").is(ssid);
			criteria.and("prvid").is(prvid);
			criteria.and("uflag").is(WftConnSession.UFLAG_SUCCESS);
			int count = 0;
			query.addCriteria(criteria).skip(count).limit(LIMIT);
			List<WftConnSession> list = mongoDBService.find(query,
					WftConnSession.class, MongoDBService.CONNDATAKEY + channel);
			Long utvalue = 0l;
			Long utvalueOp = 0l;
			if(list == null || list.size()==0){
				return;
			}
			while (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getBstimeop() != null) {
						utvalueOp += list.get(i).getUtvalueop();
					}
					utvalue += list.get(i).getUtvalue();
				}
				count += LIMIT;
				Query query1 = new Query();
				query1.addCriteria(criteria).skip(count).limit(LIMIT);
				list = mongoDBService.find(query1, WftConnSession.class,
						MongoDBService.CONNDATAKEY + channel);
				logger
						.info(String
								.format(
										"UseTime Channel=%s,size=%s,utvalue=%s,utvalueop=%s,count=%s",
										channel, list.size(), utvalue, utvalueOp,
										count));
			}
			WftTotalStat wftTotalStat = new WftTotalStat();
			wftTotalStat.setDairy(dayYMD);
			wftTotalStat.setSsid(ssid);
			wftTotalStat.setUtvalue(utvalue);
			wftTotalStat.setUtvalueop(utvalueOp);
			wftTotalStat.setChannel(channel);
			wftTotalStat.setPrvid(prvid);
			wftTotalStat.setOpid(opid);
			wftTotalStatMapper.insert(wftTotalStat);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("StatUseTime Error !");
		}

	}

	/**
	 * 取卡失败统计
	 * @param startDate
	 * @param endDate
	 * @param dayYMD
	 * @param appId
	 * @param ssid
	 * @param channel
	 * @param opid
	 */
	private void statAccessCardFail(Date startDate, Date endDate, Date dayYMD,
			Integer appId, String ssid, String channel, Integer opid,Integer prvid) {
		logger.info("Stat Access Card Start");
		try {
			logger.info(String.format("ssid=%s,channel=%s,opid=%s,prvid=%s", ssid,channel,opid,prvid));
			Query query = new Query();
			query.addCriteria(Criteria.where("ustatus").is(
					WftConnSession.USTATUS_CARD_FAIL));
			query.addCriteria(Criteria.where("etime").gte(startDate)
					.lt(endDate));
			query.addCriteria(Criteria.where("prvid").is(prvid));
			query.addCriteria(Criteria.where("ssid").is(ssid));
			int acessCardFail = mongoDBService.count(query,
					MongoDBService.CONNDATAKEY + channel);
			if(acessCardFail==0){
				return;
			}
			WftTotalStat wftTotalStat = new WftTotalStat();
			wftTotalStat.setNocard(acessCardFail);
			wftTotalStat.setSsid(ssid);
			wftTotalStat.setOpid(opid);
			wftTotalStat.setChannel(channel);
			wftTotalStat.setDairy(dayYMD);
			wftTotalStat.setPrvid(prvid);
			wftTotalStatMapper.insert(wftTotalStat);
			logger.info(String.format(
					"Stat Channel=%s Access Card operator=%s,fail=%s", channel,
					ssid, acessCardFail));
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Stat Access Card Error");
		}
		logger.info("Stat Acess Card End");
	}

	/**
	 * 热点统计
	 * @param startDate
	 * @param endDate
	 * @param dayYMD
	 * @param appId
	 * @param ssid
	 * @param channel
	 * @param flag
	 */
	private void statMacConn(Date startDate, Date endDate, Date dayYMD,
		Integer appId,String ssid,String channel, Integer flag,Integer prvid,Integer opid) {
		logger.info("Stat Mac Conn Start");
		try {
			logger.info(String.format("ssid=%s,channel=%s,opid=%s,prvid=%s", ssid,channel,opid,prvid));
			DBCollection dbc = mongoDBService
					.getCollection(MongoDBService.CONNDATAKEY
							+ channel);
			String mapfun = "function(){emit({mac:this.mac},{count:1})}";
			String reducefun = "function(key, values){" + "var count = 0;"
					+ "for(var i in values) {" + "count+= values[i].count;" +

					"}" + "return {count:count};" + "}";
			DBObject command = new BasicDBObject();
			DBObject query = new BasicDBObject();
			query.put("etime", new BasicDBObject("$gte", startDate).append(
					"$lt", endDate));
			query.put("ssid", ssid);
			query.put("prvid",prvid);
			if (flag == 1) {
				query.put("uflag", WftConnSession.UFLAG_SUCCESS);
				
			} else {
				query.put("uflag", WftConnSession.UFLAG_FAIL);
				
			}
			command.put("mapreduce", MongoDBService.CONNDATAKEY
					+ channel);
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
				WftMacStat wftMacStat = new WftMacStat();
				wftMacStat.setDairy(dayYMD);
				wftMacStat.setMac(mac);
				wftMacStat.setSsid(ssid);
				wftMacStat.setPrvid(prvid);
				wftMacStat.setChannel(channel);
				wftMacStat.setOpid(opid);
				if (flag == 1) {
					wftMacStat.setSucccnt(count);
				} else {
					wftMacStat.setFailcnt(count);
				}
				logger.info(String.format(
						"Stat Channel=%s Mac Stat dairy=%s ,flag=%s,count=%s",
						channel, dayYMD, flag, count));
				wftMacStatMapper.insertMac(wftMacStat, channel);
			}
			//wftMacStatMapper.creatTable(channel);
			mongoDBService.dropCollection("{inline:1}");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Stat Mac Conn Error", e);
		}
		logger.info("Stat Mac Conn End");
	}

	/**
	 * 热点汇总统计
	 * @param dayYMD
	 * @param appId
	 * @param ssid
	 * @param channel
	 */
	private void statChannelMac(Date dayYMD, Integer appId,String ssid,String channel,Integer opid,Integer prvid) {
		logger.info("Stat Channel Mac Start");
		try {
				logger.info(String.format("ssid=%s,channel=%s,opid=%s,prvid=%s", ssid,channel,opid,prvid));
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("dairy", dayYMD);
				paramMap.put("ssid", ssid);
				paramMap.put("prvid", prvid);
				int succ = wftMacStatMapper.findSuccByParam(paramMap,
						channel);
				int fail = wftMacStatMapper.findFailByParam(paramMap,
						channel);
				paramMap.put("failcnt", 3);
				int o3three = wftMacStatMapper.findFailOTByParam(paramMap,
						channel);
				WftTotalStat wftTotalStat = new WftTotalStat();
				wftTotalStat.setOpid(opid);
				wftTotalStat.setSsid(ssid);
				wftTotalStat.setChannel(channel);
				wftTotalStat.setDairy(dayYMD);
				wftTotalStat.setMacfail(fail);
				wftTotalStat.setMacsuc(succ);
				wftTotalStat.setOvernfail(o3three);
				wftTotalStat.setPrvid(prvid);
				wftTotalStatMapper.insert(wftTotalStat);
				logger.info(String.format(
						"Stat Channel=%s Mac Start succ=%s,fail=%s,o3=%s",
						appId, succ, fail, o3three));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 人数统计
	 * @param startDate
	 * @param endDate
	 * @param dayYMD
	 * @param appId
	 * @param ssid
	 * @param channel
	 * @param opid
	 */
	private boolean statConnper(Date startDate, Date endDate, Date dayYMD,
			Integer appId, String ssid, String channel, Integer opid,Integer prvid) {
			logger.info(String.format("ConnPer stat is start:%s, %s, %s", startDate, endDate, dayYMD));
			
			logger.info(String.format("ssid=%s,channel=%s,opid=%s,prvid=%s", ssid,channel,opid,prvid));
			DBObject totalquery = new BasicDBObject();
			totalquery.put("prvid", prvid);
			totalquery.put("etime", new BasicDBObject("$gte", startDate)
					.append("$lt", endDate));
			totalquery.put("ssid", ssid);
			totalquery.put("uflag",new BasicDBObject("$gt",0));
			List totallist = mongoDBService.getCollection(
					MongoDBService.CONNDATAKEY + channel)
					.distinct("uid", totalquery);
			if(null == totallist || totallist.size()==0){
				logger.info("ConnPer stat is end:size=0");
				return false;
			}
			DBObject query = new BasicDBObject();
			query.put("etime", new BasicDBObject("$gte", startDate).append(
					"$lt", endDate));
			query.put("ssid", ssid);
			query.put("uflag", WftConnSession.UFLAG_SUCCESS);
			query.put("prvid", prvid);
			List succlist = mongoDBService.getCollection(
					MongoDBService.CONNDATAKEY + channel)
					.distinct("uid", query);
			WftTotalStat wftTotalStat = new WftTotalStat();
			wftTotalStat.setPersuc(succlist.size());
			wftTotalStat.setPerfail(totallist.size() - succlist.size());
			wftTotalStat.setDairy(dayYMD);
			wftTotalStat.setSsid(ssid);
			wftTotalStat.setOpid(opid);
			wftTotalStat.setChannel(channel);
			wftTotalStat.setPrvid(prvid);
			wftTotalStatMapper.insert(wftTotalStat);
			logger.info("ConnPer stat is end");
			return true;
	}

	/**
	 * 链接次数统计
	 * @param startDate
	 * @param endDate
	 * @param dayYMD
	 * @param appId
	 * @param ssid
	 * @param channel
	 * @param opid
	 */
	private void statConncnt(Date startDate, Date endDate, Date dayYMD,
			Integer appId, String ssid, String channel, Integer opid,Integer prvid) {
			logger.info("ConnCnt stat is start");
			String tableName = MongoDBService.CONNDATAKEY
					+ channel;
			Query succQuery = new Query();
			succQuery.addCriteria(Criteria.where("etime").gte(startDate).lt(
					endDate));
			succQuery.addCriteria(Criteria.where("prvid").is(prvid));
			succQuery.addCriteria(Criteria.where("ssid").is(ssid));
			succQuery.addCriteria(Criteria.where("uflag").is(
					WftConnSession.UFLAG_SUCCESS));
			int succSize = mongoDBService.count(succQuery, tableName);
			Query failQuery = new Query();
			failQuery.addCriteria(Criteria.where("prvid").is(prvid));
			failQuery.addCriteria(Criteria.where("etime").gte(startDate)
					.lt(endDate));
			failQuery.addCriteria(Criteria.where("ssid").is(ssid));
			failQuery.addCriteria(Criteria.where("uflag").is(
					WftConnSession.UFLAG_FAIL));
			int failSize = mongoDBService.count(failQuery, tableName);
			WftTotalStat wftTotalStat = new WftTotalStat();
			wftTotalStat.setDairy(dayYMD);
			wftTotalStat.setChannel(channel);
			wftTotalStat.setConnsuc(succSize);
			wftTotalStat.setConnfail(failSize);
			wftTotalStat.setOpid(opid);
			wftTotalStat.setSsid(ssid);
			wftTotalStat.setPrvid(prvid);
			wftTotalStatMapper.insert(wftTotalStat);
			logger.info("ConnCnt stat is end");
	}

	
	/**
	 * 计算sdk打开人数
	 * */
//	private void statSdkUseper(Date startDate, Date endDate, Date dayYMD,
//			Integer appId, String ssid, String channel, Integer opid) {
//		logger.info("Stat SDK Useper");
//		try {
//			Query query = new Query();
//			query.addCriteria(Criteria.where("logintime").gte(startDate).lt(
//					endDate));
//			List<WftLoginData> loginlist = mongoDBService.find(query,
//					WftLoginData.class, MongoDBService.LOGINDATAKEY + channel);
//			DBCollection dbc = mongoDBService
//					.getCollection(MongoDBService.LOGINDATAKEY + channel);
//			DBObject query1 = new BasicDBObject();
//			query1.put("logintime", new BasicDBObject("$gte", startDate)
//					.append("$lt", endDate));
//			List list = dbc.distinct("uid", query1);
//			WftOpenStat total = new WftOpenStat();
//			total.setDairy(dayYMD);
//			total.setChannel(wftChannel.getCode());
//			total.setSdkcnt(loginlist.size());
//			total.setSdkper(list.size());
//			wftOpenStatMapper.insert(total);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.info("Stat SDK Useper Error!!");
//		}
//	}

	

	public void setMongoDBService(MongoDBService mongoDBService) {
		this.mongoDBService = mongoDBService;
	}

	public void setWftApTypeMapper(WftApTypeMapper wftApTypeMapper) {
		this.wftApTypeMapper = wftApTypeMapper;
	}

	public void setUserMapper(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	public void setWftMacStatMapper(WftMacStatMapper wftMacStatMapper) {
		this.wftMacStatMapper = wftMacStatMapper;
	}

	public void setWftTotalStatMapper(WftTotalStatMapper wftTotalStatMapper) {
		this.wftTotalStatMapper = wftTotalStatMapper;
	}

	public void setWftProvinceMapper(WftProvinceMapper wftProvinceMapper) {
		this.wftProvinceMapper = wftProvinceMapper;
	}

	public void setWftChannelMapper(WftChannelMapper wftChannelMapper) {
		this.wftChannelMapper = wftChannelMapper;
	}

	public static void main(String[] args) {
		Date date = new Date();
		Date startDate = DateUtil.getNextNumDay(date, -1);
		Date endDate = DateUtil.getNextNumDay(startDate, 0);
		String dayYMD=DateUtil.formateDate(startDate, "yyyy-MM-dd");
		System.out.println(dayYMD);
	}

	public void setWftOpenPlatFormStatMapper(
			WftOpenPlatFormStatMapper wftOpenPlatFormStatMapper) {
		this.wftOpenPlatFormStatMapper = wftOpenPlatFormStatMapper;
	}
	
}
