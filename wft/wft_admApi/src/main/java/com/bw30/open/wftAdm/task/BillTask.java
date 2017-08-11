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
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.bw30.open.common.dao.mapper.WftConsumeStatMapper;
import com.bw30.open.common.dao.mapper.WftOpenStatMapper;
import com.bw30.open.common.model.WftConnSession;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.wft.mongo.MongoDBService;

/**
 * 生成360使用详单
 * 
 * @author Jack
 * 
 *         2014年11月7日
 */
public class BillTask {
	private static final Logger LOG = Logger.getLogger(BillTask.class);
	private static final SimpleDateFormat SDF_YMD_HMS = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat SDF_YMD = new SimpleDateFormat(
			"yyyyMMdd");

	@Resource
	private WftOpenStatMapper wftOpenStatMapper;
	@Resource
	private WftConsumeStatMapper wftConsumeStatMapper;
	@Resource
	private MongoDBService mongoDBService;

	private List<String> channelList;
	private String billFilePath;

	public void setWftOpenStatMapper(WftOpenStatMapper wftOpenStatMapper) {
		this.wftOpenStatMapper = wftOpenStatMapper;
	}

	public void setWftConsumeStatMapper(
			WftConsumeStatMapper wftConsumeStatMapper) {
		this.wftConsumeStatMapper = wftConsumeStatMapper;
	}

	public void setMongoDBService(MongoDBService mongoDBService) {
		this.mongoDBService = mongoDBService;
	}

	public void setChannelList(List<String> channelList) {
		this.channelList = channelList;
	}

	public void setBillFilePath(String billFilePath) {
		this.billFilePath = billFilePath;
	}

	public void bill() {
		if (null != channelList && 0 < channelList.size()) {
			for (String channel : channelList) {
				bill(channel);
			}
		}
	}

	public void bill(String channel) {
		LOG.info("bill task " + channel + " start");
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			Date edate = calendar.getTime();
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			Date sdate = calendar.getTime();

			String day = SDF_YMD.format(sdate);
			String filePath = this.billFilePath + "bill_" + channel;
			File file = new File(filePath);
			if (!file.exists() || !file.isDirectory()) {
				file.mkdir();
			}

			// 电信账单
			List<WftConnSession> csList = this.findRecord(channel,
					WftOperator.OP_ID_CTCC, sdate, edate);
			filePath += ("/BEIWEI_ChinaNet_USER_" + day + ".txt");

			// 计算修正时长 = 平台对账时长 - 电信实际消耗时长
			/*WftOpenStat openstat = this.wftOpenStatMapper.findByCondition(
					new SimpleDateFormat("yyyy-MM-dd").format(sdate), channel);// 平台对账时长
			if (null == openstat) {
				LOG.warn("bill task " + channel + " fail: no openstat");
				return;
			}
			List<WftConsumeStat> list = this.wftConsumeStatMapper.findByCondition(
					new SimpleDateFormat("yyyy-MM-dd").format(sdate), channel, WftOperator.OP_ID_CTCC);// 电信时间消耗时长
			if (null == list || 0 == list.size()) {
				LOG.warn("bill task " + channel + " fail: no consumestat");
				return;
			}
			WftConsumeStat  consume = list.get(0);
			long cvalue = openstat.getCnutvalueop() - consume.getTvalue();// 时长修正值
*/
			long cvalue = 0;
			if (null != csList && 0 < csList.size()) {
				this.billForCtcc(channel, csList, filePath, cvalue);
			}

			LOG.info("bill task " + channel + " end");
		} catch (Exception e) {
			LOG.error("bill task " + channel + " error", e);
		}

	}

	/**
	 * 生成电信账单文件，<br/>
	 * 修正时长>0不需修正，<0则需要修正：为每条记录加时长，直到修正时长>0
	 * 
	 * @param channel
	 * @param csList
	 * @param filePath
	 * @param cvalue
	 *            时长修正值， >0不需修正，<0则需要修正
	 */
	private void billForCtcc(String channel, List<WftConnSession> csList,
			String filePath, long cvalue) {
		LOG.info("bill task " + channel + ": create ctcc bill:size="
				+ csList.size() + ", cvalue=" + cvalue);
		try {
			long cvaluePer = 0;// 秒
			if (0 > cvalue) {// 计算每条记录的修正时长 = cvalue/size + 120
				cvaluePer = (-1 * cvalue) / csList.size();
				cvaluePer += 120;
			}

			List<BillRecord> records = new ArrayList<BillRecord>(csList.size());
			Date betime = null;
			int timelen = 0;
			long time = 0L;
			long sum = 0L;
			for (WftConnSession cs : csList) {
				BillRecord record = new BillRecord();
				record.setUid(cs.getUid());
				record.setBstime(cs.getBstimeop());
				betime = cs.getBetimeop();
				timelen = cs.getUtvalueop();
				if (0 > cvalue) {
					time = betime.getTime() + cvaluePer * 1000;// ms
					betime = new Date(time);
					timelen += cvaluePer;
					cvalue += cvaluePer;
				}
				record.setBetime(betime);
				record.setTimelen(timelen);
				records.add(record);
				sum += timelen;
			}
			this.outToFile(records, filePath);
			LOG.info("bill task " + channel
					+ ": create ctcc bill success: size=" + csList.size()
					+ ", sum=" + sum);
		} catch (Exception e) {
			LOG.error("bill task " + channel + ": create ctcc bill error", e);
		}
	}

	private List<WftConnSession> findRecord(String channel, Integer opid,
			Date startDate, Date endDate) {
		Query query = new Query(Criteria.where("mflag").is(
				WftConnSession.CONN_RECORD_FALG_YES));
		query.addCriteria(Criteria.where("opid").is(opid));
		query.addCriteria(Criteria.where("etime").gte(startDate).lt(endDate));
//		query.with(new Sort(new Sort.Order(Sort.Direction.ASC, "bstime")));
		return this.mongoDBService.find(query, WftConnSession.class,
				MongoDBService.CONNDATAKEY + channel);
	}

	/**
	 * 写入账单文件
	 * 
	 * @param records
	 * @param filePath
	 */
	private void outToFile(List<BillRecord> records, String filePath)
			throws Exception {
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
		PrintWriter pw = new PrintWriter(new FileWriter(filePath));
		for (BillRecord record : records) {
			pw.println(record.toString());
		}
		pw.flush();
		pw.close();
	}

	/**
	 * 账单记录
	 * 
	 * @author Jack
	 * 
	 *         2014年9月11日
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
			return new StringBuffer(this.uid).append("|")
					.append(SDF_YMD_HMS.format(this.bstime)).append("|")
					.append(SDF_YMD_HMS.format(this.betime)).append("|")
					.append(this.timelen).append("|").toString();
		}

	}

}
