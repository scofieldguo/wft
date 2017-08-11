package com.bw30.open.wftAdm.util;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.MongoOptions;
import com.mongodb.ServerAddress;

/**
 * @Title: MongoDBUtil.java
 * @Package: com.bw30.ad.everyhour.test
 * @Description: mongodb工具类
 * @author: lilj@bw30.com
 * @date: 2011-6-13 下午05:30:08
 * @version: V1.0
 */
public class MongoDBUtil {
	static {
		init();
	}
	private static final SimpleDateFormat SDF_YMD_HMS = new SimpleDateFormat(
	"yyyy-MM-dd HH:mm:ss");
	/**
	 * mongodb的IP
	 */
	private static String MONGODB_HOST;
	/**
	 * mongodb的端口
	 */
	private static int MONGODB_PORT;
	/**
	 * 默认使用的数据库名
	 */
	public static String DEFAULT_DB;
	public static String OPERATE_TB; // 用户操作集合
	public static String PRIZE_TB; // 奖品操作集合
	private static Map<String, DB> DB_MAP = new HashMap<String, DB>();// db库
	private static Map<String, DBCollection> COLL_MAP = new HashMap<String, DBCollection>();// 连接

	private static Mongo mongo;

	private static void init() {
		MONGODB_HOST = "192.168.0.110";
//		OPERATE_TB = Config.getConfig("OperateTB");
//		PRIZE_TB = Config.getConfig("prizeTB");
//		try {
//			MONGODB_PORT = Integer.parseInt(Config.getConfig("mongodbPort"));
//		} catch (NumberFormatException e) {
			MONGODB_PORT = 27017;
//			e.printStackTrace();
//		}
		//DEFAULT_DB = "wftopenTest";
	}

	/**
	 * 获取mongo实例，暂时这样使用，有待改进
	 * 
	 * @return
	 */
	private synchronized Mongo getMongo() {
		if (mongo == null) {
			try {
				MongoOptions options = new MongoOptions();
				options.autoConnectRetry = true;
				options.connectionsPerHost = 10;
				options.maxWaitTime = 5000;
				options.socketTimeout = 0;
				options.connectTimeout = 15000;
				options.threadsAllowedToBlockForConnectionMultiplier = 10;
//				mongo = new Mongo(MONGODB_HOST, MONGODB_PORT);// 创建数据库连接
				mongo = new Mongo(new ServerAddress(MONGODB_HOST, MONGODB_PORT), options);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (MongoException e) {
				e.printStackTrace();
			}
		}
		return mongo;
	}

	/**
	 * 获取库
	 * 
	 * @param dbName
	 * @return
	 */
	private DB getDB(String dbName) {
		getMongo();
		DB db = DB_MAP.get(dbName);
		if (db == null || !db.isAuthenticated()) {
			db = mongo.getDB(dbName);
			DB_MAP.put(dbName, db);
		}
		return db;
	}

	/**
	 * 获取链接
	 * 
	 * @param dbName
	 * @param tbName
	 * @return
	 */
	private DBCollection getDBCollection(String dbName, String tbName) {
		DBCollection con = null;
		try {
			con = COLL_MAP.get(tbName);
			if (con == null) {
				con = getDB(dbName).getCollection(tbName);
				COLL_MAP.put(tbName, con);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}
	public void findAndUpdate(DBObject conditions,String db,String tbName){
		DBCollection col = getDBCollection(db, tbName);
		DBCursor ite =col.find(conditions);
		System.out.println("aaaaaaaaaaaaaa="+ite.size());
		while (ite.hasNext()) {
			DBObject object = ite.next();
			if(null == object.get("betimeop")){
				continue;
			}
			String betimeopS = object.get("betimeop").toString();
			String bstimeopS = object.get("bstimeop").toString();
			String csid = object.get("csid").toString();
			System.out.println(betimeopS);
			DBObject updateObj = new BasicDBObject();
			Date bstimeop = null;
			Date betimeop = null;
			try {
				if(betimeopS.contains("CST")){
					continue;
				}
				bstimeop = SDF_YMD_HMS.parse(bstimeopS);
			    betimeop = SDF_YMD_HMS.parse(betimeopS);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			updateObj.put("betimeop", betimeop);
			updateObj.put("bstimeop", bstimeop);
			DBObject allCondition = new BasicDBObject();
			DBObject whereObj = new BasicDBObject("csid",csid);
			allCondition.put("$set", updateObj);
			update(whereObj, allCondition, tbName,
					db, false, false);
		}
	}

	public static void main(String args[]){
		MongoDBUtil nDbUtil = new MongoDBUtil();
		DBObject cond = new BasicDBObject();
		nDbUtil.findAndUpdate(cond,"wftopenTest", "connRecord10001");
		/*DBObject cond = new BasicDBObject();
		cond.put("phone", "13847272727");
		BasicDBObject key = new BasicDBObject();
		key.put("age", true);
		BasicDBObject initial = new BasicDBObject("cnt", 0);
		initial.append("num", 0);
		String reduce = "function(doc,out){if(doc.name=='wuxb'){out.cnt++} else if(doc.name=='xugx'){out.num++}}";
		nDbUtil.group(cond, key, initial, reduce,UserOperateModel.class, "user","wuxb");*/
//		String prizeName="耳机";
//		BasicDBObject cond = new BasicDBObject();
//		if (prizeName != null && !"".equals(prizeName.trim())) {
//			Pattern pattern = Pattern.compile("^.*" + prizeName + ".*$",
//					Pattern.MULTILINE);
//			BasicDBObject regex = new BasicDBObject("$regex", pattern);
//			cond.put("prizeName", regex);
//		}
//		String map = "function(){if(this.func=='grant'){emit({prizeName:this.prizeName},{grant:1,prize:0});}else{emit({prizeName:this.prizeName},{grant:0,prize:1});}}";
//		String reduce="function(key,values){var prizeCnt=0;var grantCnt=0;for( var i=0;i<values.length;i++){grantCnt +=values[i].grant;prizeCnt +=values[i].prize;} return {grant:grantCnt,prize:prizeCnt};}";
//		String outputTarget="tempe";
		
		//List<PrizeRecordRst> list = nDbUtil.mapReduce(cond, map,reduce,outputTarget, PrizeRecordRst.class,"prizeRecord", "roulette");
		//System.out.println(list.size());
		//System.err.println(list.get(0).getKey().getPrizeName());
	}
	
	

	public DBCollection getCollection(String dbName, String tbname) {
		DBCollection col = getDBCollection(dbName, tbname);
		return col;
	}

//	public DBCollection getCollection(String tbname) {
//		return getCollection(DEFAULT_DB, tbname);
//	}

	public void update(DBObject q, DBObject o, String tbName,String dbName,boolean upsert, boolean multi){
		DBCollection col = getDBCollection(dbName, tbName);
		col.update(q, o,upsert,multi);
	}
}
