package com.bw30.open.wftAdm.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bw30.open.common.dao.mapper.AccountMapper;
import com.bw30.open.common.dao.mapper.WftChannelMapper;
import com.bw30.open.common.dao.mapper.WftConsumeStatMapper;
import com.bw30.open.common.model.WftChannel;
import com.bw30.open.common.model.WftConnSession;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.common.model.stat.WftConsumeStat;
import com.bw30.open.wft.common.DateUtil;
import com.bw30.open.wft.common.Pager;
import com.bw30.open.wft.mongo.MongoDBService;
import com.bw30.open.wftAdm.service.CardService;
import com.bw30.open.wftAdm.task.GrapCtccRecordTask;

@Controller
public class BillController {
	private static final Logger LOG = Logger.getLogger(BillController.class);
	private static final SimpleDateFormat SDF_YMD_HMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat SDF_YMD = new SimpleDateFormat("yyyyMMdd");
	@Resource
	private CardService cardService;
	@Resource
	private WftConsumeStatMapper wftConsumeStatMapper;
	@Resource
	private WftChannelMapper wftChannelMapper;
	@Resource
	private AccountMapper accountMapper;
	
	public void setCardService(CardService cardService) {
		this.cardService = cardService;
	}
	
	public void setWftConsumeStatMapper(WftConsumeStatMapper wftConsumeStatMapper) {
		this.wftConsumeStatMapper = wftConsumeStatMapper;
	}

	public void setWftChannelMapper(WftChannelMapper wftChannelMapper) {
		this.wftChannelMapper = wftChannelMapper;
	}

	public void setAccountMapper(AccountMapper accountMapper) {
		this.accountMapper = accountMapper;
	}
	@Resource
	private MongoDBService mongoDBService;
	
	private String billFilePath;

	public void setMongoDBService(MongoDBService mongoDBService) {
		this.mongoDBService = mongoDBService;
	}

	public void setBillFilePath(String billFilePath) {
		this.billFilePath = billFilePath;
	}
	
	@RequestMapping("toBill.do")
	public ModelAndView toBill(ModelMap viewMap, HttpServletRequest req) {
		Date yestoday = DateUtil.getNextNumDay(new Date(), -1);
		String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(yestoday);
		viewMap.put("day", dateStr);
		return new ModelAndView("bill", viewMap);
	}
	@RequestMapping("toBill360.do")
	public void bill360(HttpServletRequest request,HttpServletResponse response) throws Exception{
			String day = request.getParameter("day");
			LOG.info("bill task for 360 start......");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(DateUtil.strToDate(day));
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			Date edate = calendar.getTime();
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			Date sdate = calendar.getTime();
			
			
			String channel = "10003";//腾讯
			String filePath = "/opt/tomcat/" + "10003data/";
			Integer count = findRecordCount(channel, WftOperator.OP_SSID_CTCC, sdate, edate);
		
			Pager pager =  new Pager();
			pager.setPageSize(5000);
			pager.setRecCount(count);
			pager.getPageCount();
			LOG.info("bill task for 360 count......"+count+"pageSize="+pager.getPageCount());
			boolean flag =true;
			filePath += ("BEIWEI_ChinaNet_" + SDF_YMD.format(sdate) + ".txt");
			for(int i=0;i<=pager.getPageCount();i++ ){
				pager.setPageIndex(i+1);
				List<WftConnSession> csList = this.findRecord(channel, WftOperator.OP_SSID_CTCC, sdate, edate,pager);
				if(null != csList && 0 < csList.size()){
					this.billForCtcc(channel, csList, filePath,flag);
				}
				flag = false;
			}
			//电信账单
			
			
			LOG.info("bill task for 360 end");
		}
		
		/**
		 * 生成电信账单文件，<br/>
		 * 以北纬平台记录的时间为准
		 * 
		 * @param channel
		 * @param csList
		 * @param filePath
		 */
		private void billForCtcc(String channel, List<WftConnSession> csList, String filePath,boolean flag){
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
//					else{//对账失败，则按北纬平台记录算
//						record.setBstime(cs.getBstime());
//						record.setBetime(cs.getBetime());
//						record.setTimelen(cs.getUtvalue());
//					}
//					records.add(record);
				}
				this.outToFile(records, filePath,flag);
			}catch(Exception e){
				LOG.error("create ctcc bill error:channel=" + channel, e);
			}
		}
		

		private List<WftConnSession> findRecord(String channel, String ssid, Date startDate,
				Date endDate,Pager pager) {
			Query query = new Query(Criteria.where("uflag").is(
					WftConnSession.UFLAG_SUCCESS));
			query.addCriteria(Criteria.where("ssid").regex(ssid + ".*", "i"));
			query.addCriteria(Criteria.where("status").is(
					WftConnSession.STATUS_CLOSE));
			query.addCriteria(Criteria.where("bstime").gte(startDate).lt(endDate));
			LOG.info("bill task for 360 skipNo===="+pager.getSkipNo()+"limit=" + pager.getPageSize());
			//query.with(new Sort(new Sort.Order(Sort.Direction.ASC, "bstime")));
			return this.mongoDBService.findForPage(query, WftConnSession.class,pager,
					MongoDBService.CONNDATAKEY + channel);
		}
		
		private Integer findRecordCount(String channel, String ssid, Date startDate,
				Date endDate) {
			Query query = new Query(Criteria.where("uflag").is(
					WftConnSession.UFLAG_SUCCESS));
			query.addCriteria(Criteria.where("ssid").regex(ssid + ".*", "i"));
			query.addCriteria(Criteria.where("status").is(
					WftConnSession.STATUS_CLOSE));
			query.addCriteria(Criteria.where("bstime").gte(startDate).lt(endDate));
			//query.with(new Sort(new Sort.Order(Sort.Direction.ASC, "bstime")));
			return this.mongoDBService.count(query, MongoDBService.CONNDATAKEY + channel);
		}
		
		/**
		 * 写入账单文件
		 * 
		 * @param records
		 * @param filePath
		 */
		private void outToFile(List<BillRecord> records, String filePath, boolean flag) throws Exception{
			File file = new File(filePath);
			if(flag){
				if(file.exists()){
					file.delete();
				}
			}
			PrintWriter pw = new PrintWriter(new FileWriter(filePath, true));
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
		
	
	/**
	 * 生成ctcc卡账单文件：以卡为单位
	 * @param request day=yyyy-MM-dd
	 * @param response
	 */
	@RequestMapping("bill.do")
	public void bill(HttpServletRequest request,HttpServletResponse response) throws Exception{
		LOG.info("create bill file");
		String day = request.getParameter("day");
		LOG.info("create bill file:day="+ day);
		
		this.wftConsumeStatMapper.deleteByDairy(day);
		
		GrapCtccRecordTask gcrt = new GrapCtccRecordTask();
		gcrt.setCardService(this.cardService);
		gcrt.setAccountMapper(this.accountMapper);
		gcrt.setWftConsumeStatMapper(this.wftConsumeStatMapper);
		gcrt.setWftChannelMapper(this.wftChannelMapper);
		List<GrapCtccRecordTask.CtccRecord> crList = gcrt.parseFile("/opt/tomcat/ctccrecord/pool_BWTX_" + day + ".csv");
		
		//分渠道统计时长
		List<WftChannel> channelList = this.wftChannelMapper.findAll();
		if (null != channelList && 0 < channelList.size()) {
			String result = day + "<br/>";
			int ctccValue = 0;
			for (WftChannel channel : channelList) {
				//卡账单文件目录
				String filePath = "/opt/tomcat/bill_" + channel.getCode();
				filePath += ("/BEIWEI_ChinaNet_CARD_" + day.replace("-", "") + ".txt");
				
				WftConsumeStat consume = new WftConsumeStat();
				consume.setDairy(day);
				consume.setChannel(channel.getCode());
				consume.setChannelname(channel.getName());
				consume.setOpid(WftOperator.OP_ID_CTCC);
				if(0 < crList.size()){
					ctccValue = gcrt.statForChannel(channel, crList, filePath);
				}else{
					ctccValue = 0;
				}
				consume.setTvalue(ctccValue);
				consume.setIntime(new Date());
				
				LOG.info(String.format("grap ctcc record result: channel=%s, sum=%s s", channel.getCode(), ctccValue));
				this.wftConsumeStatMapper.insert(consume);
				result += "channel=" + channel.getCode() + ", sum=" + ctccValue + "<br/>";
			}
			
			response.getWriter().println(result);
		}
		
	}
	
}
