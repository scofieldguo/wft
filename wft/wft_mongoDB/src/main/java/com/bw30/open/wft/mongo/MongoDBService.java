package com.bw30.open.wft.mongo;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.bw30.open.wft.common.Pager;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MapReduceOutput;

public class MongoDBService {
	public static final String LOGINDATAKEY ="loginRecord"; // 登录记录表
	public static final String CONNDATAKEY = "connRecord"; // 连接记录表
	
	/**
	 * 渠道开卡订单表：渠道请求开放平台开卡，<br/>
	 * 需加渠道号
	 */
	public static final String KEY_CARDOPEN_CHANNEL = "cardOpenRecord";
	/**
	 * 渠道卡充值订单表 ：渠道请求开放平台为卡充值，<br/>
	 * 需加渠道号
	 */
	public static final String KEY_CARDRECHARGE_CHANNEL = "cardRechargeRecord";
	
	/**
	 * 开放平台开卡订单表：开放平台请求运营商开卡，<br/>
	 * 需加运营商简称
	 */
	public static final String KEY_CARDOPEN_OP= "cardOpenRecord";
	/**
	 * 开放平台卡充值订单表：开放平台请求运营商对卡充值，<br/>
	 * 需加运营商简称
	 */
	public static final String KEY_CARDRECHARGE_OP="cardRechargeRecord";
	
	/**
	 * qq通知
	 */
	public static final String KEY_QQNOTIFY = "qqNotify";
	
	/**
	 * 取卡失败记录，
	 * 需加渠道号
	 */
	public static final String KEY_NO_CARD = "noCardRecord";
	
	@Resource
	private MongoTemplate mongoTemplate;

	/**
	 * 举例
	 * */
	
	public void Example(){
//		Query query =new Query();
//		query.addCriteria((Criteria.where("telePhone").is("15810574973")));
//		query.addCriteria(Criteria.where("appId").is(11111));
//		List<Object> list=this.find(query, Object.class, "tbName");
	}
	
	/**
	 * @param clazz 类别
	 * @return  返回同一类所有对象
	 * */
	public <T> List<T> findAll(Class<T> clazz) {
		return mongoTemplate.find(new Query(), clazz);
	}
	
	/**
	 * 插入对象
	 * @param t 对象
	 * @param tbName 表名
	 * **/
	public <T> void save(T t,String tbName){
		mongoTemplate.insert(t,tbName);
	}
	
	/**
	 * 插入同一类型list
	 * @param list 对象list
	 * @param tbName 表名
	 * */
	public<T> void save(List<T> list,String tbName){
		this.createCollection(tbName);
		mongoTemplate.insert(list, tbName);
	}
	
	/**
	 * 根据主键查询
	 * @param <T>
	 * @param clazz
	 * @param id
	 * @param tbName
	 * @return
	 */
	public <T> T findById(Class<T> clazz, Object id, String tbName){
		return this.mongoTemplate.findById(id, clazz, tbName);
	}

	/**
	 * 根据查找条件返回list
	 * @param query 条件对象
	 * @param clazz 要查找的对象类型
	 * @param tbName 表名
	 * @return 返回的对象list
	 * */
	public <T> List<T> find(Query query, Class<T> clazz,String tbName){
		return mongoTemplate.find(query, clazz, tbName);
	}
	
	/**
	 * @param query 条件对象
	 * @param paramMap 要更新的字段以及对应的值
	 * @param tbName 表名
	 * */
	public void update(Query query,Map<String ,Object> paramMap,String tbName){
		Update update=new Update();
		  for(Map.Entry<String, Object> map: paramMap.entrySet()){
			  String key = map.getKey().toString();
			  Object value=map.getValue();
			  update.set(key, value);
		  }
		mongoTemplate.updateMulti(query, update, tbName);
		
	}
	/**
	 * 创建表
	 *@param tbName 表名 
	 * */
	
	public void createCollection(String tbName) {
		if (!mongoTemplate.collectionExists(tbName)) {
			mongoTemplate.createCollection(tbName);
		}
		mongoTemplate.getCollection(tbName);
	}

	
	public List<DBObject> mapReduce(String tbName,String map ,String reduce,String finalize,DBObject query){
		DBObject command = new BasicDBObject();
		command.put("mapreduce", tbName);
		command.put("map", map);
		command.put("reduce", reduce);
		if(query !=null ){
		command.put("query", query);
		}
		if(finalize !=null){
		command.put("finalize", finalize);
		}
		command.put("out", "{inline:1}");
		command.put("verbose", true);
		DBCollection conn = mongoTemplate.getCollection(tbName);
		MapReduceOutput out =  conn.mapReduce(command);
		Iterator<DBObject> a1 = out.results().iterator();
		List<DBObject> objList =  new ArrayList<DBObject>();
		while (a1.hasNext()) {
			DBObject object =a1.next();
			objList.add(object);
		}
		return objList;
	}
	
	public DBCollection getCollection(String tbName){
		if (!mongoTemplate.collectionExists(tbName)) {
			mongoTemplate.createCollection(tbName);
		}
		return mongoTemplate.getCollection(tbName);
	}
	/**
	 * 删除表
	 * @param tbName 表名
	 * */
	public void dropCollection(String tbName) {
		if (mongoTemplate.collectionExists(tbName)) {
			mongoTemplate.dropCollection(tbName);
		}
	}
	
	/**
	 * 统计记录数
	 * @param query
	 * @param tbName
	 * @return
	 */
	public Integer count(Query query, String tbName){
		Long c = this.mongoTemplate.count(query, tbName);
		return null != c ? c.intValue() : null;
	}
	
	/**
	 * 
	 * @param <T>
	 * @param query
	 * @param clazz
	 * @param pager
	 * @param tbName
	 * @return
	 */
	public <T> List<T> findBySSL(Query query ,Class<T> clazz,String tbName){
		return this.mongoTemplate.find(query, clazz, tbName);
	}
	
	/**
	 * 分页查询
	 * 
	 * @param <T>
	 * @param query
	 * @param clazz
	 * @param pager
	 * @param tbName
	 * @return
	 */
	public <T> List<T> findForPage(Query query, Class<T> clazz, Pager pager, String tbName){
		query = null != query ? query : new Query();
		if(null != pager){
			query.skip(pager.getSkipNo());
			query.limit(pager.getPageSize());
		}
		return this.mongoTemplate.find(query, clazz, tbName);
	}
	
	
	/**
	 * 分页查询
	 * 
	 * @param <T>
	 * @param query
	 * @param clazz
	 * @param pager
	 * @param tbName
	 * @return
	 */
	public <T> List<T> findByDBObjectPage(Query query, Class<T> clazz, Pager pager, String tbName){
		DBCollection connection = this.getCollection(tbName);
		DBCursor cursor =connection.find(query.getQueryObject()).sort(query.getSortObject()).skip(pager.getSkipNo()).limit(pager.getPageSize());
		List<T> list = new ArrayList<T>();
		Field[] fields = clazz.getDeclaredFields();
		while (cursor.hasNext()) {
			if (fields != null) {
				DBObject bo = cursor.next();
				T obj = null;
				try {
					obj = clazz.newInstance();
				} catch (InstantiationException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				}
				if (obj != null) {
					for (int i = 0; i < fields.length; i++) {
						try {
							fields[i].setAccessible(true);
							if (Modifier.isFinal(fields[i].getModifiers())) {
								continue;
							} else {
								fields[i].set(obj,
										bo.get(fields[i].getName()));
							}
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					}
					list.add(obj);
				}
			}
		
		}
		if (cursor != null) {
			cursor.close();
		}
		return list;
	}

	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}

	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

}
