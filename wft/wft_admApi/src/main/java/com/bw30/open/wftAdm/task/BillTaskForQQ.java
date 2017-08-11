package com.bw30.open.wftAdm.task;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.bw30.open.common.model.WftConnSession;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.wft.mongo.MongoDBService;

/**
 * 定时任务：生成qq天账单
 * 
 * @author Jack
 * 
 *         2014年9月11日
 */
public class BillTaskForQQ {
	private static final Logger LOG = Logger.getLogger(BillTaskForQQ.class);
	private static final SimpleDateFormat SDF_YMD_HMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat SDF_YMD = new SimpleDateFormat("yyyyMMdd");
	
	@Resource
	private MongoDBService mongoDBService;
	
	private String billFilePath;

	public void setMongoDBService(MongoDBService mongoDBService) {
		this.mongoDBService = mongoDBService;
	}

	public void setBillFilePath(String billFilePath) {
		this.billFilePath = billFilePath;
	}


	public void bill() {
		LOG.info("bill task for qq start......");
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date edate = calendar.getTime();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		Date sdate = calendar.getTime();
		
		
		String channel = "10001";//腾讯
		String filePath = this.billFilePath + "qqdata/";
		
		//电信账单
		List<WftConnSession> csList = this.findRecord(channel, WftOperator.OP_SSID_CTCC, sdate, edate);
		filePath += ("BEIWEI_ChinaNet_" + SDF_YMD.format(sdate) + ".txt");
		if(null != csList && 0 < csList.size()){
			this.billForCtcc(channel, csList, filePath);
		}
		
		//移动账单
		filePath = this.billFilePath + "qqdata/";
		filePath += ("BEIWEI_CMCC_" + SDF_YMD.format(sdate) + ".txt");
		csList = this.findRecord(channel, WftOperator.OP_SSID_CMCC, sdate, edate);
		if(null != csList && 0 < csList.size()){
			this.billForCmcc(channel, csList, filePath);
		}
		
		LOG.info("bill task for qq end");
	}
	
	/**
	 * 生成电信账单文件，<br/>
	 * 以北纬平台记录的时间为准
	 * 
	 * @param channel
	 * @param csList
	 * @param filePath
	 */
	private void billForCtcc(String channel, List<WftConnSession> csList, String filePath){
		LOG.info("create ctcc bill:channel=" + channel + ", size=" + csList.size());
		try{
			List<BillRecord> records = new ArrayList<BillRecord>(csList.size());
			for(WftConnSession cs : csList){
				BillRecord record = new BillRecord();
				record.setUid(cs.getUid());
				if(WftConnSession.CONN_RECORD_FALG_YES == cs.getMflag()){//对账成功，则按运营商时长算
					record.setBstime(cs.getBstimeop());
					record.setBetime(cs.getBetimeop());
					record.setTimelen(cs.getUtvalueop());
					records.add(record);
				}
//				else{//对账失败，则按北纬平台记录算
//					record.setBstime(cs.getBstime());
//					record.setBetime(cs.getBetime());
//					record.setTimelen(cs.getUtvalue());
//				}
//				records.add(record);
			}
			this.outToFile(records, filePath);
		}catch(Exception e){
			LOG.error("create ctcc bill error:channel=" + channel, e);
		}
	}
	
	/**
	 * 生成移动账单文件，<br/>
	 * 与运营商对账成功的以运营商为准，对账失败的以合作方记录的时间为准
	 * 
	 * @param channel
	 * @param csList
	 * @param filePath
	 */
	private void billForCmcc(String channel, List<WftConnSession> csList, String filePath){
		LOG.info("create cmcc bill:channel=" + channel + ", size=" + csList.size());
		try{
			List<BillRecord> records = new ArrayList<BillRecord>(csList.size());
			for(WftConnSession cs : csList){
				BillRecord record = new BillRecord();
				record.setUid(cs.getUid());
				if(WftConnSession.CONN_RECORD_FALG_YES == cs.getMflag()){
					record.setBstime(cs.getBstimeop());
					record.setBetime(cs.getBetimeop());
					record.setTimelen(cs.getUtvalueop());
					records.add(record);
				}
//				else{
//					record.setBstime(cs.getBstime());
//					record.setBetime(cs.getBetime());
//					record.setTimelen(cs.getUtvalue());
//				}
//				records.add(record);
			}
			this.outToFile(records, filePath);
		}catch(Exception e){
			LOG.error("create cmcc bill error:channel=" + channel, e);
		}
	}

	private List<WftConnSession> findRecord(String channel, String ssid, Date startDate,
			Date endDate) {
		Query query = new Query(Criteria.where("uflag").is(
				WftConnSession.UFLAG_SUCCESS));
		query.addCriteria(Criteria.where("ssid").regex(ssid + ".*", "i"));
		query.addCriteria(Criteria.where("status").is(
				WftConnSession.STATUS_CLOSE));
		query.addCriteria(Criteria.where("bstime").gte(startDate).lt(endDate));
		query.with(new Sort(new Sort.Order(Sort.Direction.ASC, "bstime")));
		return this.mongoDBService.find(query, WftConnSession.class,
				MongoDBService.CONNDATAKEY + channel);
	}
	
	/**
	 * 写入账单文件
	 * 
	 * @param records
	 * @param filePath
	 */
	private void outToFile(List<BillRecord> records, String filePath) throws Exception{
		File file = new File(filePath);
		if(file.exists()){
			file.delete();
		}
		PrintWriter pw = new PrintWriter(new FileWriter(filePath));
		for(BillRecord record : records){
			pw.println(record.toString());
		}
		pw.flush();
		pw.close();
	}

	/**
	 * 账单记录
	 * @author Jack
	 *
	 * 2014年9月11日
	 */
	static class BillRecord {
		private String uid;
		private Date bstime;
		private Date betime;
		private Integer timelen;

		public String getUid() {
			return uid;
		}

		public void setUid(String uid) {
			this.uid = uid;
		}

		public Date getBstime() {
			return bstime;
		}

		public void setBstime(Date bstime) {
			this.bstime = bstime;
		}

		public Date getBetime() {
			return betime;
		}

		public void setBetime(Date betime) {
			this.betime = betime;
		}

		public Integer getTimelen() {
			return timelen;
		}

		public void setTimelen(Integer timelen) {
			this.timelen = timelen;
		}

		@Override
		public String toString() {
			return new StringBuffer(this.uid).append("|").append(SDF_YMD_HMS.format(this.bstime))
					.append("|").append(SDF_YMD_HMS.format(this.betime)).append("|")
					.append(this.timelen).append("|").toString();
		}
		
	}

}
