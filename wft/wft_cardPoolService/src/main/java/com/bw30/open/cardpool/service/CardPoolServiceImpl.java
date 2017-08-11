package com.bw30.open.cardpool.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bw30.open.cardpool.service.cmcc.CardCmccService;
import com.bw30.open.cardpool.service.ctcc.CardCtccService;
import com.bw30.open.common.dao.mapper.WftCardStoreMapper;
import com.bw30.open.common.model.WftCardActive;
import com.bw30.open.common.model.WftCardStore;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.wft.common.PwdEncrypt;
import com.bw30.open.wft.common.cardpool.rmi.bean.CardBean;
import com.bw30.open.wft.common.cardpool.rmi.bean.CardPoolBean;
import com.bw30.open.wft.common.cardpool.rmi.bean.CardRecord;
import com.bw30.open.wft.common.cardpool.rmi.bean.OnlineBean;
import com.bw30.open.wft.common.cardpool.rmi.bean.RechargeBean;
import com.bw30.open.wft.common.cardpool.rmi.bean.RecordBean;
import com.bw30.open.wft.common.cardpool.rmi.bean.RespBean;
import com.bw30.open.wft.common.cardpool.rmi.service.ICardPoolService;
import com.bw30.open.wft.mongo.MongoDBService;
import com.bw30.open.wft.mongo.model.CardOpenRecord;
import com.bw30.open.wft.mongo.model.CardRechargeRecord;

public class CardPoolServiceImpl implements ICardPoolService{
	private static final Logger LOG = Logger.getLogger(CardPoolServiceImpl.class);
	
	private static final SimpleDateFormat SDF_YMDHMS = new SimpleDateFormat("yyyyMMdd HHmmss");
	private static final SimpleDateFormat SDF_YMD = new SimpleDateFormat("yyyy-MM-dd");
	
	
	/**
	 * 开卡订单前缀
	 */
	public static final String PREFIX_ORDER_OPEN = "OPEN";
	
	/**
	 * 充值订单前缀
	 */
	public static final String PREFIX_ORDER_RECHARGE = "REC";
	
	public static final String PREFIX_ORDER_ONLINE="ONL";
	
	public static final String PREFIX_ORDER_RECORD="RED";
	
	public static final String PREFIX_ORDER_BALANCE="BAL";
	
	public static final String PREFIX_ORDER_PWD="PWD";
	
	public static final String PREFIX_ORDER_INFO="INF";
	
	@Resource
	private WftCardStoreMapper wftCardStoreMapper;
	@Resource
	private CardCtccService cardCtccService;
	@Resource
	private CardCmccService cardCmccService;
	@Resource
	private MongoDBService mongoDBService;
	
	public void setWftCardStoreMapper(WftCardStoreMapper wftCardStoreMapper) {
		this.wftCardStoreMapper = wftCardStoreMapper;
	}

	public void setCardCtccService(CardCtccService cardCtccService) {
		this.cardCtccService = cardCtccService;
	}

	public void setCardCmccService(CardCmccService cardCmccService) {
		this.cardCmccService = cardCmccService;
	}

	public void setMongoDBService(MongoDBService mongoDBService) {
		this.mongoDBService = mongoDBService;
	}

	/**
	 * 合作方开卡<br/>
	 * 策略：合作方请求开卡时，即向运营商请求开卡
	 * 
	 * @param partner
	 * @param op
	 * @param ctype
	 * @param tradesn
	 * @return
	 * @throws Exception
	 */
	public CardBean openCard(String partner, Integer op, String ctype, String tradesn) throws Exception {
		String orderid = genOrderId(PREFIX_ORDER_OPEN);
		CardOpenRecord order = new CardOpenRecord();
		order.setOrderid(orderid);
		order.setPartner(partner);
		order.setTradesn(tradesn);
		order.setOp(op);
		order.setCtype(ctype);
		order.setStime(new Date());
		
		switch(op){
		case WftOperator.OP_ID_CTCC:
			CardBean cb = this.cardCtccService.createCard(null, Integer.parseInt(ctype), orderid);
			if(RespBean.RESULT_OK == cb.getResult()){
				order.setStatus(CardOpenRecord.STATUS_OK);
				order.setCid(cb.getId());
				order.setCno(cb.getNo());
				order.setEtime(new Date());
				this.saveOpenOrder(order, partner, "CTCC");
				this.saveCardForOpen(op, WftOperator.OP_SSID_CTCC, partner, orderid, cb);
				return cb;
			}
			
			LOG.error("call ctcc error: openCard, code=" + cb.getResult());
			order.setStatus(CardOpenRecord.STATUS_FAIL);
			order.setError(cb.getResult());
			order.setEtime(new Date());
			
			this.saveOpenOrder(order, partner, "CTCC");
			return null;
		default:
			return null;
		}
		
	}
	
	//保存卡到卡库
	private void saveCardForOpen(Integer opid, String ssid, String partner, String orderid, CardBean cb){
		WftCardStore cs = new WftCardStore();
		cs.setOpid(opid);
		cs.setSsid(ssid);
		cs.setPrvid(cb.getPrvId());
		cs.setCtype(0);//类型：0
		cs.setNo(cb.getNo());
		cs.setPwd(PwdEncrypt.encryptCardPwd(cb.getPwd()));//卡密加密
		cs.setVbtime(new Date());
		cs.setVetime(cb.getValidity());
		cs.setBvalue(cb.getBalance());
		cs.setTvalue(cb.getBalance());
		cs.setUstatus(0);
		cs.setCstatus(WftCardActive.CSTATUS_AVAILABLE);
		cs.setInplace(0);
		cs.setAtime(new Date());
//		cs.setIntime(new Date());
		cs.setUtvalue(0);
		cs.setUcount(0);
		cs.setUscount(0);
		cs.setOrderid(orderid);
		cs.setChannel(partner);
		this.wftCardStoreMapper.insert(cs);
		
		cb.setId(cs.getId());
		cb.setOpid(WftOperator.OP_ID_CTCC);
		cb.setSsid(WftOperator.OP_SSID_CTCC);
	}
	
	
	//保存开卡订单：渠道订单、运营商订单
	private void saveOpenOrder(CardOpenRecord order, String partner, String opName){
		this.mongoDBService.save(order, MongoDBService.KEY_CARDOPEN_CHANNEL + partner);
		
		order.setPartner(null);
		order.setTradesn(null);
		order.setOp(null);
		this.mongoDBService.save(order, MongoDBService.KEY_CARDOPEN_OP + opName);
	}
	
	/**
	 * 充值
	 * 
	 * @param card
	 * @param op
	 * @param code
	 * @param orderid
	 * @return
	 * @throws Exception
	 */
	public RechargeBean recharge(String partner, Integer cid, String cno, Integer op, String code,
			String tradesn) throws Exception {
		String orderid = genOrderId(PREFIX_ORDER_RECHARGE);
		CardRechargeRecord order = new CardRechargeRecord();
		order.setOrderid(orderid);
		order.setPartner(partner);
		order.setTradesn(tradesn);
		order.setCid(cid);
		order.setCno(cno);
		order.setOp(op);
		order.setCtype(code);
		order.setStime(new Date());
		
		switch (op) {
		case WftOperator.OP_ID_CTCC:
			RechargeBean rb= this.cardCtccService.recharge(null, cno,
					Integer.parseInt(code), orderid);
			if (RespBean.RESULT_OK.equals(rb.getResult())) {
				//保存订单
				order.setStatus(CardRechargeRecord.STATUS_OK);
				order.setOldbalance(rb.getOldbalance());
				order.setNewbalance(rb.getNewbalance());
				order.setValidity(rb.getValidity());
				order.setEtime(new Date());
				this.saveRechargeOrder(order, partner, "CTCC");
				this.updateCardForRecharge(partner, cid, rb.getBalance(), rb.getNewbalance(), rb.getValidity());
				return rb;
			}
			
			LOG.error("call ctcc error: recharge, code=" + rb.getResult());
			order.setStatus(CardRechargeRecord.STATUS_FAIL);
			order.setError(rb.getResult());
			order.setEtime(new Date());
			this.saveRechargeOrder(order, partner, "CTCC");
			return rb;
		default:
			return null;
		}

	}
	
	//充值成功后修改卡库信息
	private void updateCardForRecharge(String channel, Integer cid, Integer balance, Integer tvalue, Date validity){
		WftCardStore cs = new WftCardStore();
		cs.setId(cid);
		cs.setBvalue(balance);
		cs.setTvalue(tvalue);
		cs.setVetime(validity);
		this.wftCardStoreMapper.update(cs);
	}
	
	//保存充值订单：渠道订单、运营商订单
	private void saveRechargeOrder(CardRechargeRecord order, String partner, String opName){
		this.mongoDBService.save(order, MongoDBService.KEY_CARDRECHARGE_CHANNEL + partner);
		
		order.setPartner(null);
		order.setTradesn(null);
		order.setOp(null);
		this.mongoDBService.save(order, MongoDBService.KEY_CARDRECHARGE_OP + opName);
	}
	
	/**
	 * 查询剩余时长
	 * 
	 * @param op
	 * @param cno
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	public Integer getBalance(String partner, Integer cid, Integer op, String cno, String pwd) throws Exception {
		switch (op) {
		case WftOperator.OP_ID_CTCC:
			String res = this.cardCtccService.getBalance(cno, pwd,genOrderId(PREFIX_ORDER_BALANCE));
			if (null != res) {
				JSONObject json = JSON.parseObject(res);
				return json.getIntValue("balance");
			}
			LOG.error("call ctcc error: getBalance");
			return null;
		case WftOperator.OP_ID_CMCC:
			Integer balance = this.cardCmccService.getBalance(cno, pwd);
			if(null != balance){
				return balance;
			}
			LOG.error("call cmcc error: getBalance");
			return null;
		default:
			return null;
		}
	}

	/**
	 * 修改密码
	 * 
	 * @param op
	 * @param cno
	 * @param pwd
	 * @param newPwd
	 * @return
	 * @throws Exception
	 */
	public boolean updatePwd(String partner, Integer cid, Integer op, String cno, String pwd,
			String newPwd) throws Exception {
		switch (op) {
		case WftOperator.OP_ID_CTCC:
			if (this.cardCtccService.updateCardPassword(null, cno, pwd, newPwd,
					genOrderId(PREFIX_ORDER_PWD))) {
				if(!"10016".equals(partner)){
					WftCardStore card = new WftCardStore();
					card.setId(cid);
					card.setPwd(PwdEncrypt.encryptCardPwd(newPwd));
					card.setPctime(new Date());
					this.wftCardStoreMapper.update(card);
				}
				return true;
			}
			LOG.error("call ctcc error: updatePwd");
			return false;
		default:
			return false;
		}

	}
	
	/**
	 * 在线查询
	 * 
	 * @param op
	 * @param cno
	 * @param pwd
	 * @param ip 用户wifi接入ip，移动在线查询时需要根据ip进行比对，若没取到ip，可传分卡时间dtime
	 * @param dtime 
	 * 
	 * @throws Exception
	 */
	public OnlineBean queryOnline(Integer op, String cno, String pwd, String ip, Date dtime) throws Exception{
		OnlineBean ob = null;
		switch(op){
		case WftOperator.OP_ID_CTCC:
			ob = this.cardCtccService.queryOnline(cno, pwd, genOrderId(PREFIX_ORDER_ONLINE));
			return ob;
		case WftOperator.OP_ID_CMCC:
			ob = this.cardCmccService.queryOnline(cno, pwd, ip, dtime);
			return ob;
		default:
			return null;
		}
	}
	
	/**
	 * 卡使用记录查询
	 * 
	 * @param op
	 * @param cno
	 * @param startDate yyyy-MM-dd
	 * @param endDate yyyy-MM-dd
	 */
	public List<CardRecord> queryRecord(String partner, Integer op, String cno, String pwd, String startDate, String endDate) throws Exception {
		RecordBean rb = null;
		switch(op){
		case WftOperator.OP_ID_CTCC:
			rb = this.cardCtccService.queryOffline(cno, pwd, startDate, endDate, genOrderId(PREFIX_ORDER_RECORD));
			if(null != rb && RespBean.RESULT_OK.equals(rb.getResult())){
				return rb.getRecordList();
			}
			LOG.error("call ctcc error: queryRecord:" + rb.getResult());
			return null;
		case WftOperator.OP_ID_CMCC:
			startDate = SDF_YMDHMS.format(SDF_YMD.parse(startDate));
			endDate = SDF_YMDHMS.format(SDF_YMD.parse(endDate));
			rb = this.cardCmccService.queryOffline(cno, pwd, startDate, endDate);
			if(null != rb && RespBean.RESULT_OK.equals(rb.getResult())){
				return rb.getRecordList();
			}
			LOG.error("call cmcc error: queryRecord:" + rb.getResult());
			return null;
		default:
			return null;
		}
	}
	
	/**
	 * 插移动卡使用记录
	 * 
	 * @param cno 
	 * @param pwd
	 * @param startDate yyyy-MM-dd HH:mm:ss
 	 * @param endDate yyyy-MM-dd HH:mm:ss
	 * @return
	 * @throws Exception
	 */
	public RecordBean queryCmccRecord(String cno, String pwd, String startDate, String endDate) throws Exception{
		return this.cardCmccService.queryOffline(cno, pwd, startDate, endDate);
	}
	

	/**
	 * 生成流水订单号
	 * @param prefix 订单前缀
	 * @return
	 */
	public static String genOrderId(String prefix) {
		prefix = null != prefix ? prefix : "";
		return new StringBuffer(prefix).append(getSessionId())
				.toString();
	}
	public static String getSessionId(){
		String uuid = UUID.randomUUID().toString();
		uuid = uuid.replaceAll("-", "");
		return uuid;
	}
	
	public static void main(String[] args) {
		System.out.println(genOrderId("123"));
	}
	
	@Override
	public CardPoolBean queryPoolInfo(Integer op) throws Exception {
		switch(op){
		case WftOperator.OP_ID_CTCC:
			return this.cardCtccService.queryPoolInfo(genOrderId(PREFIX_ORDER_INFO));
		}
		return null;
	}
	
}
