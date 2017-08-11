package com.bw30.open.wftAdm.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bw30.open.common.model.WftConnSession;
import com.bw30.open.wft.common.DateUtil;
import com.bw30.open.wft.mongo.MongoDBService;

@Controller
public class StatController {

	@Resource
	private MongoDBService mongoDBService;
	private static final Logger logger = Logger.getLogger(StatController.class);

	@RequestMapping("teststat.do")
	public void test(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("1111111111");
		// String dbString= request.getParameter("db");
		// String tableString = request.getParameter("table");
		Date date = DateUtil.strToDate(request.getParameter("dairy"));
		Date startDate = DateUtil.getNextNumDay(date, -1);
		Date endDate = DateUtil.getNextNumDay(startDate, 1);
		// MongoDBUtil mongoDBUtil = new MongoDBUtil();
		// DBObject conditions = new BasicDBObject();
		// conditions.put("bstimeop", new BasicDBObject("$exists", true));
		// mongoDBUtil.findAndUpdate(conditions,dbString, tableString);
		// connSessionTask.colseTimeOutConn(new Date());
		//getUserCount(startDate, endDate, date);
		updateConnData(startDate, endDate, date);
		try {
			response.getWriter().println("11111");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void updateConnData(Date startDate, Date endDate, Date dairy){
		System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		Query query = new Query();
		query.addCriteria(Criteria.where("uflag").is(WftConnSession.UFLAG_SUCCESS));
		query.addCriteria(Criteria.where("ssid").regex("CMCC", "i"));
		query.addCriteria(Criteria.where("etime").gte(startDate)
				.lt(endDate));
		query.addCriteria(Criteria.where("utvalue").gt(9000));
		List<WftConnSession> list = mongoDBService.find(query, WftConnSession.class, MongoDBService.CONNDATAKEY
				+ "10001");
		System.out.println("aaaaaaaaaa="+list.size());
		for(WftConnSession wftConnSession : list){
			Integer utvalue = wftConnSession.getUtvalue();
			if(utvalue>9000){
				utvalue = utvalue/1000;
			}
			String csid = wftConnSession.getCsid();
			Query query1 = new Query();
			query1.addCriteria(Criteria.where("csid").is(csid));
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("utvalue", utvalue);
			mongoDBService.update(query, paramMap,  MongoDBService.CONNDATAKEY
					+ "10001");
		}
	}
	
	
//	private void getUserCount(Date startDate, Date endDate, Date dairy) {
//		List<String> channelList = new ArrayList<String>();
//		channelList.add("10001");
//		// channelList.add("10003");
//		List<String> ssidList = new ArrayList<String>();
//		ssidList.add("CMCC");
//		ssidList.add("ChinaNet");
//		try {
//			for (String ssid : ssidList) {
//				for (String wftChannel : channelList) {
//					logger.info(String.format("Stat Channel=%s uid Conn Start",
//							wftChannel));
//					DBCollection dbc = mongoDBService
//							.getCollection(MongoDBService.CONNDATAKEY
//									+ wftChannel);
//					String mapfun = "function(){emit({uid:this.uid},{count:1})}";
//					String reducefun = "function(key, values){"
//							+ "var count = 0;" + "for(var i in values) {"
//							+ "count+= values[i].count;" +
//
//							"}" + "return {count:count};" + "}";
//					DBObject command = new BasicDBObject();
//					DBObject query = new BasicDBObject();
//					query.put("ssid", ssid);
//					query.put("etime", new BasicDBObject("$gte", startDate)
//							.append("$lt", endDate));
//					command.put("mapreduce", MongoDBService.CONNDATAKEY
//							+ wftChannel);
//					command.put("map", mapfun);
//					command.put("reduce", reducefun);
//					command.put("query", query);
//					command.put("out", "{inline:1}");
//					command.put("keytemp", false);
//					command.put("verbose", true);
//					MapReduceOutput mapReduceOutput = dbc.mapReduce(command);
//					for (DBObject o : mapReduceOutput.results()) {
//						JSONObject json = JSON.parseObject(o.toString());
//						JSONObject json_id = JSON.parseObject(json.get("_id")
//								.toString());
//						String uid = json_id.getString("uid");
//						JSONObject json_value = JSON.parseObject(json.get(
//								"value").toString());
//						Integer count = json_value.getIntValue("count");
//						UserCountStat userCountStat = new UserCountStat();
//						userCountStat.setChannel(wftChannel);
//						userCountStat.setCount(count);
//						userCountStat.setUid(uid);
//						userCountStat.setSsid(ssid);
//						userCountStat.setDairy(dairy);
//						mongoDBService.save(userCountStat, "userCountStat"
//								+ wftChannel);
//					}
//					mongoDBService.dropCollection("{inline:1}");
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.info("Stat Mac Conn Error", e);
//		}
//		logger.info("Stat Mac Conn End");
//	}

	public static void main(String[] args) {
//		MongoDBUtil mongoDBUtil = new MongoDBUtil();
//		File file = new File("C:\\20141002\\adm\\session.log.2014-10-03.txt");
//		BufferedReader reader = null;
//		try {
//			System.out.println("以行为单位读取文件内容，一次读一整行：");
//			reader = new BufferedReader(new FileReader(file));
//			String tempString = null;
//			int line = 1;
//			// 一次读入一行，直到读入null为文件结束
//			while ((tempString = reader.readLine()) != null) {
//				// 显示行号
//				System.out.println("line " + line + ": " + tempString);
//				if (tempString.contains("CONNSESSION")) {
//					continue;
//				} else {
//					int index = tempString.indexOf(";");
//					String str = tempString.substring(index + 1);
//					WftConnSession wftConnSession = JSON.parseObject(str,
//							WftConnSession.class);
//					if ("10001".equals(wftConnSession.getChannel())) {
//						mongoDBUtil.insert(wftConnSession, "connRecord10001",
//								"wftopen");
//					}
//					if ("10003".equals(wftConnSession.getChannel())) {
//						mongoDBUtil.insert(wftConnSession, "connRecord10003",
//								"wftopen");
//					}
//				}
//				System.out.println("line " + line + ": " + tempString);
//				line++;
//			}
//			reader.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			if (reader != null) {
//				try {
//					reader.close();
//				} catch (IOException e1) {
//				}
//			}
//		}
		ConcurrentMap<String, Long> map =  new ConcurrentHashMap<String, Long>();
		map.put("1", 7l);
		map.put("2", 6l);
		map.put("3", 3l);
		
		map.remove("1");
		
		Iterator<Map.Entry<String, Long>> it = null;
		Map.Entry<String, Long> entry = null;
		it = map.entrySet().iterator();
		while (it.hasNext()) {
			entry = it.next();
			long vc = entry.getValue();
			System.out.println(vc);
		}
	}

	public MongoDBService getMongoDBService() {
		return mongoDBService;
	}

	public void setMongoDBService(MongoDBService mongoDBService) {
		this.mongoDBService = mongoDBService;
	}

}
