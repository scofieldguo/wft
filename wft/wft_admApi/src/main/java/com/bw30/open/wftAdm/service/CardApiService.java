package com.bw30.open.wftAdm.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.bw30.open.common.dao.mapper.WftCardActiveMapper;
import com.bw30.open.common.dao.mapper.WftCardStoreMapper;
import com.bw30.open.common.model.WftCardActive;
import com.bw30.open.common.model.WftCardStore;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.wft.common.PwdEncrypt;
import com.bw30.open.wft.common.cardpool.rmi.bean.CardBean;
import com.bw30.open.wft.common.cardpool.rmi.bean.CardRecord;
import com.bw30.open.wft.common.cardpool.rmi.bean.OnlineBean;
import com.bw30.open.wft.common.cardpool.rmi.bean.RechargeBean;
import com.bw30.open.wft.common.cardpool.rmi.bean.RespBean;
import com.bw30.open.wft.common.cardpool.rmi.service.ICardPoolService;

public class CardApiService {
	private static final Logger LOG = Logger.getLogger(CardApiService.class);
	
	@Resource
	private ICardPoolService cardPoolService;
	@Resource
	private WftCardActiveMapper wftCardActiveMapper;
	@Resource
	private WftCardStoreMapper wftCardStoreMapper;

	/**
	 * 查询在线状态，若在线则返回登陆时间，否则返回null
	 * 
	 * @param op
	 * @param cno
	 * @param pwd
	 * @param ip
	 * @return
	 */
	public Date queryOnline(Integer op, String cno, String pwd, String ip,
			Date dtime) throws Exception {
		LOG.info(String.format(
				"queryOnline req op=%s,cno=%s,pwd=%s,ip=%s,dtime=%s", op, cno,
				pwd, ip, dtime));
		OnlineBean onlineBean = cardPoolService.queryOnline(op, cno, pwd, ip,
				dtime);
		if (null == onlineBean) {
			LOG.error(String.format("quern online error: op=%s, cno=%s", op,
					cno));
			return null;
		}
		LOG.info(String.format("queryOnline resp OnlineBean=%s", JSON
				.toJSONString(onlineBean).toString()));
		if (RespBean.RESULT_OK.equals(onlineBean.getResult())) {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(onlineBean.getStarttime());
		}
		return null;
	}
	
	/**
	 * 查询电信卡是否在线
	 * 
	 * @param cno
	 * @param pwd
	 * @param ip
	 * @param dtime
	 * @return true在线， false不在线
	 */
	public boolean queryOnlineForCtcc(String cno, String pwd, String ip, Date dtime) {
		LOG.info(String.format(
				"queryOnlineForCtcc req: cno=%s,pwd=%s,ip=%s,dtime=%s", cno,
				pwd, ip, dtime));
		try{
			OnlineBean onlineBean = cardPoolService.queryOnline(WftOperator.OP_ID_CTCC, cno, pwd, ip,
					dtime);
			if (null == onlineBean) {
				LOG.error(String.format("quern online error: cno=%s", cno));
				return true;
			}
			LOG.info(String.format("queryOnlineForCtcc resp OnlineBean=%s", JSON
					.toJSONString(onlineBean).toString()));
			
			if("-1".equals(onlineBean.getResult())){//不在线
				return false;
			}
			
			return true;
		}catch(Exception e){
			LOG.error("query online error", e);
			return true;
		}
		
	}

	/**
	 * 查询卡剩余时长
	 * 
	 * @param partner
	 * @param cid
	 * @param op
	 * @param cno
	 * @param pwd
	 * @return 秒， null查询失败
	 */
	public Integer getBalance(String partner, Integer cid, Integer op,
			String cno, String pwd) {
		Integer balance = 0;
		LOG.info(String.format(
				"getBalance req channel=%s,cid=%s,op=%s,cno=%s,pwd=%s",
				partner, cid, op, cno, pwd));
		try {
			balance = cardPoolService.getBalance(partner, cid, op, cno, pwd);
			if (null == balance) {
				LOG.info("getBalance is error");
				return null;
			}
			LOG.info(String.format("getBalance resp balance=%s", balance));
			WftCardActive wftCardActive = new WftCardActive();
			wftCardActive.setChannel(partner);
			wftCardActive.setId(cid);
			wftCardActive.setTvalue(balance);
			wftCardActiveMapper.update(wftCardActive);
			return balance;
		} catch (Exception e) {
			LOG.error("getBalance error", e);
			return null;
		}
	}
	
	public String openCard(String partner, Integer op, String ctype,
			String tradesn) {
		String no = null;
		LOG.info(String.format(
				"open card req channel=%s,opid=%s,ctype=%s,tradesn=%s",
				partner, op, ctype, no));
		try {
			CardBean cb = cardPoolService.openCard(partner, op, ctype, tradesn);
			if (null == cb) {
				LOG.info("openCard is error");
				return null;
			}
			LOG.info(String.format("open card resp cardBean=%s", JSON
					.toJSONString(cb).toString()));
			WftCardActive ca = new WftCardActive();
			ca.setId(cb.getId());
			ca.setOpid(cb.getOpid());
			ca.setSsid(cb.getSsid());
			ca.setPrvid(cb.getPrvId());
			ca.setCtype(0);// 类型：0
			ca.setNo(cb.getNo());
			ca.setPwd(PwdEncrypt.encryptCardPwd(cb.getPwd()));// 卡密加密
			ca.setUstatus(0);
			ca.setCstatus(WftCardActive.CSTATUS_AVAILABLE);
			ca.setBvalue(cb.getBalance());
			ca.setTvalue(cb.getBalance());
			ca.setVbtime(new Date());
			ca.setVetime(cb.getValidity());
			ca.setIntime(new Date());
			ca.setUtvalue(0);
			ca.setUcount(0);
			ca.setCache(WftCardActive.OUT_CACHE);
			ca.setChannel(partner);
			wftCardActiveMapper.insert(ca);
			no = cb.getNo();
			
			WftCardStore cs = new WftCardStore();
			cs.setId(cb.getId());
			cs.setInplace(1);
			cs.setIntime(new Date());
			cs.setChannel(partner);
			this.wftCardStoreMapper.update(cs);
			
		} catch (Exception e) {
			LOG.error("openCard error", e);
			return null;
		}
		return no;
	}

	public Integer recharge(String partner, Integer cid, String cno,
			Integer op, String ctype, String tradesn) {
		LOG.info(String
				.format("recharge req channel=%s,cid=%s,cno=%s,op=%s,ctype=%s,tradesn=%s",
						partner, cid, cno, op, ctype, tradesn));
		try {
			RechargeBean rechargeBean = cardPoolService.recharge(partner, cid,
					cno, op, ctype, tradesn);
			if (null == rechargeBean) {
				LOG.info("recharge is error");
				return -1;
			}
			LOG.info(String.format("recharge resp rechargeBean=%s", JSON
					.toJSONString(rechargeBean).toString()));
			WftCardActive ca = new WftCardActive();
			ca.setChannel(partner);
			ca.setBvalue(rechargeBean.getBalance());
			ca.setTvalue(rechargeBean.getNewbalance());
			ca.setVetime(rechargeBean.getValidity());
			ca.setId(cid);
			wftCardActiveMapper.update(ca);
			
			return rechargeBean.getNewbalance();
		} catch (Exception e) {
			LOG.error("recharge error", e);
			return -1;
		}
		
	}

	public List<CardRecord> queryRecord(String partner, Integer op, String cno,
			String pwd, String startDate, String endDate) {
		List<CardRecord> list = new ArrayList<CardRecord>();
		LOG.info(String
				.format("queryRecord req channel=%s,op=%s,cno=%s,pwd=%s,startDate=%s,endDate=%s",
						partner, op, cno, pwd, startDate, endDate));
		try {
			list = cardPoolService.queryRecord(partner, op, cno, pwd,
					startDate, endDate);
			//LOG.info(String.format("queryRecord resp listSize %s", list.size()));
		} catch (Exception e) {
			LOG.error("queryRecord error:op=" + op, e);
			return null;
		}
		return list;
	}

	public void setCardPoolService(ICardPoolService cardPoolService) {
		this.cardPoolService = cardPoolService;
	}

	public void setWftCardActiveMapper(WftCardActiveMapper wftCardActiveMapper) {
		this.wftCardActiveMapper = wftCardActiveMapper;
	}

	public void setWftCardStoreMapper(WftCardStoreMapper wftCardStoreMapper) {
		this.wftCardStoreMapper = wftCardStoreMapper;
	}

}
