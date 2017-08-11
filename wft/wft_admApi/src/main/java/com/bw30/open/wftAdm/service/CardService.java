package com.bw30.open.wftAdm.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.bw30.cmcc.wlan.service.operator.ICmccWlanService;
import com.bw30.cmcc.wlan.service.operator.model.HandleResult;
import com.bw30.open.common.dao.mapper.WftCardActiveMapper;
import com.bw30.open.common.dao.mapper.WftCardStoreMapper;
import com.bw30.open.common.model.WftCardActive;
import com.bw30.open.common.model.WftCardStore;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.wft.common.PwdEncrypt;

public class CardService {
	private static final Logger LOG = Logger.getLogger(CardService.class);

	@Resource
	private WftCardStoreMapper wftCardStoreMapper;
	@Resource
	private WftCardActiveMapper wftCardActiveMapper;
	@Resource
	private BaseDataService baseDataService;
	@Resource
	private CardApiService cardApiService;
	@Resource
	private ICmccWlanService cmccWlanService;

	public void setCmccWlanService(ICmccWlanService cmccWlanService) {
		this.cmccWlanService = cmccWlanService;
	}

	public void setWftCardStoreMapper(WftCardStoreMapper wftCardStoreMapper) {
		this.wftCardStoreMapper = wftCardStoreMapper;
	}

	public void setWftCardActiveMapper(WftCardActiveMapper wftCardActiveMapper) {
		this.wftCardActiveMapper = wftCardActiveMapper;
	}

	public void setBaseDataService(BaseDataService baseDataService) {
		this.baseDataService = baseDataService;
	}

	public void setCardApiService(CardApiService cardApiService) {
		this.cardApiService = cardApiService;
	}

	/**
	 * 
	 * @param channel
	 * @param opid
	 *            运营商id
	 * @param cstatus
	 *            卡状态
	 * @return
	 */
	public List<WftCardActive> findByOpidAndCstatus(String channel,
			Integer opid, Integer cstatus) {
		return this.wftCardActiveMapper.findByOpidAndCstatus(channel, opid,
				cstatus);
	}

	/**
	 * 回收已分配出去的卡
	 * 
	 * @param c
	 * @param ccid
	 * @throws Exception
	 */
	public void freebackAvailable(Integer cid, Integer status, String channel,
			String uid, Integer tvalue, String cno, Integer opid) {
		WftCardActive ca = new WftCardActive();
		LOG.info(String.format(
				"cmcc password reload startTimout opid=%s ,no=%s", opid, cno));
		String pwd = "";
		try {
			if (cno != null && opid != null) {
				if (WftOperator.OP_ID_CMCC == opid.intValue()) {
					LOG
							.info(String
									.format(
											"cmcc password reload response timeout no=%s pwd=%s",
											cno, pwd));
					pwd = cmccWlanService.reloadPassword(cno);
					LOG
							.info(String
									.format(
											"cmcc password reload response timeout no=%s pwd=%s",
											cno, pwd));
					if (pwd != null && !pwd.equals("")) {
						pwd = PwdEncrypt.encryptCardPwd(pwd);
						ca.setPwd(pwd);
					}
				}
			}
		} catch (Exception e) {
			LOG.error("cmcc password reload response timeout no=" + cno
					+ "pwd=" + pwd, e);
			// TODO: handle exception
		} finally {
			ca.setId(cid);
			if (null != tvalue) {
				ca.setTvalue(tvalue);
			}
			ca.setUid(uid); // 按用户释放卡
			ca.setCstatus(status);
			this.update(channel, ca);
		}
	}

	/**
	 * 更新卡信息并释放卡
	 * 
	 * @param nowDate
	 * @param cid
	 * @param channel
	 * @param bstime
	 */
	public void updateCardInfoAndFreeCard(WftCardActive card, String channel,
			Integer utime, String uid) {
		WftCardActive ca = new WftCardActive();
		ca.setId(card.getId());

		Integer tvalue = card.getTvalue() - utime;
		Integer utvalue = 0;
		if (null == card.getUtvalue()) {
			utvalue = utime;
		} else {
			utvalue = card.getUtvalue() + utime;
		}
	
		// if(WftOperator.OP_ID_CTCC == card.getOpid()){
		// LOG.info("check ctcc card balance:no=" + card.getNo());
		// try{
		// String pwd = card.getPwd();
		// if(pwd.length() > 10){//需要解密
		// pwd = PwdEncrypt.decryptCardPwd(pwd);
		// }
		// Integer balance = this.cardApiService.getBalance(channel,
		// card.getId(), WftOperator.OP_ID_CTCC, card.getNo(), pwd);
		// if(null == balance){
		// LOG.warn("check ctcc card balance fail:no=" + card.getNo());
		// }else{
		// tvalue = balance;
		// LOG.info("check ctcc card balance success:no=" + card.getNo() +
		// ", tvalue=" + tvalue);
		// }
		// }catch(Exception e){
		// LOG.error("check ctcc card balance error:no=" + card.getNo(), e);
		// }
		// }

		ca.setTvalue(tvalue);
		// if("10003".equals(channel)){ // 360渠道剩余时间小于等于0停卡
		// if(0 < tvalue){//-300
		// if(WftCardActive.CSTATUS_STOP != card.getCstatus()){
		// ca.setCstatus(WftCardActive.CSTATUS_AVAILABLE);
		// }
		// }else{
		// LOG.info(String.format("stop card: offline:channel=%s, cno=%s, tvalue=%s",
		// channel, card.getNo(), tvalue));
		// ca.setCstatus(WftCardActive.CSTATUS_STOP);
		// ca.setStoptime(new Date());
		// }
		// }else{
		if (WftOperator.OP_ID_CMCC == card.getOpid()) {
			LOG.info(String.format(
					"cmcc stop startActive opid=%s ,no=%s", card
							.getOpid(), card.getNo()));
			HandleResult handleResult =null;
			try{
			handleResult = cmccWlanService.pauseMobile(card
					.getNo());
			
			
			if (handleResult != null && handleResult.getTranFlag() != null
					&& "T1".equals(handleResult.getTranFlag())) {
				ca.setCstatus(WftCardActive.CSTATUS_STOP);
				ca.setStoptime(new Date());
				LOG.info(String.format(
						"cmcc stop cardActive success opid=%s ,no=%s", card
								.getOpid(), card.getNo()));
			}else{
				LOG.info(String.format(
						"cmcc stop cardActive error opid=%s ,no=%s", card
								.getOpid(), card.getNo()));
				ca.setCstatus(WftCardActive.CSTATUS_AVAILABLE);
			}
			ca.setUtvalue(utvalue);
			ca.setUid(uid);
			update(channel, ca);
			}catch (Exception e) {
				ca.setCstatus(WftCardActive.CSTATUS_AVAILABLE);
				ca.setUtvalue(utvalue);
				ca.setUid(uid);
				update(channel, ca);
				// TODO: handle exception
				LOG.info("cmcc stop cardActive error", e);
			}
		}else{
			if (-60 <= tvalue) {// -300
				if (WftCardActive.CSTATUS_STOP != card.getCstatus()) {
					ca.setCstatus(WftCardActive.CSTATUS_AVAILABLE);
				} else {
					ca.setCstatus(WftCardActive.CSTATUS_STOP);
				}
			} else {
				LOG.info(String.format(
						"stop card: offline:channel=%s, cno=%s, tvalue=%s",
						channel, card.getNo(), tvalue));
				ca.setCstatus(WftCardActive.CSTATUS_STOP);
				ca.setStoptime(new Date());
			}
			
			/**
			 * 平安WiFi专用
			 * 剩余时长小于0直接停卡
			 * */
			if(("10009".equals(channel) || "10010".equals(channel)) && tvalue<=0){
				LOG.info(String.format(
						"[平安] stop card: offline:channel=%s, cno=%s, tvalue=%s",
						channel, card.getNo(), tvalue));
				ca.setCstatus(WftCardActive.CSTATUS_STOP);
				ca.setStoptime(new Date());
			}
			ca.setUtvalue(utvalue);
			ca.setUid(uid);
			update(channel, ca);
		}
		
		// }
		
	}

	/**
	 * 根据id查询合作方的卡，卡密码已解密
	 * 
	 * @param channel
	 *            渠道code
	 * @param id
	 *            卡id
	 * @return
	 */
	public WftCardActive findCardById(String channel, Integer id) {
		WftCardActive card = this.wftCardActiveMapper.findById(channel, id);
		if (null != card) {
			card.setPwd(PwdEncrypt.decryptCardPwd(card.getPwd()));
			return card;
		}
		return null;
	}

	public WftCardStore getCardByNo(String no) {
		List<WftCardStore> cardList = this.wftCardStoreMapper.findByNo(no);
		if (null != cardList && 0 < cardList.size()) {
			return cardList.get(0);
		}
		return null;
	}

	/**
	 * 更新卡数据
	 * 
	 * @param channel
	 * @param card
	 */
	public void update(String channel, WftCardActive card) {
		// card.setChannel(channel);
		// this.wftCardActiveMapper.update(card);

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", card.getId());
		paramMap.put("tvalue", card.getTvalue());
		paramMap.put("utvalue", card.getUtvalue());
		paramMap.put("uid", card.getUid());
		paramMap.put("cstatus", card.getCstatus());
		paramMap.put("channel", channel);
		paramMap.put("stoptime", card.getStoptime());
		paramMap.put("pwd", card.getPwd());
		wftCardActiveMapper.updateByParam(paramMap, channel);
	}

	/**
	 * 添加卡到卡库
	 * 
	 * @param csList
	 * @param isEnable
	 *            true则同时调入卡池
	 * @param channel
	 *            渠道code
	 * @param prtime
	 *            预启用时间
	 */
	public void addCardsToStore(List<WftCardStore> csList, boolean isEnable,
			String channel, Date prtime) {
		if (null == csList || 0 == csList.size()) {
			return;
		}

		if (isEnable) {// 加卡到卡库，并调入卡池
			for (WftCardStore cs : csList) {
				cs.setAtime(new Date());
				cs.setCstatus(WftCardStore.CARD_CSTATUS_AVAILABLE);
				cs.setInplace(1);
				cs.setIntime(new Date());
				cs.setChannel(channel);
				this.wftCardStoreMapper.insert(cs);// 需要id，所以不能批量插入
			}
			this.batchInsertCardActive(channel, csList);
		} else {// 加卡到卡库，并设置预启用时间
			for (WftCardStore cs : csList) {
				cs.setCstatus(WftCardStore.CARD_CSTATUS_ENABLE);
				cs.setPrtime(prtime);
				cs.setChannel(channel);
				cs.setAtime(new Date());
			}
			this.wftCardStoreMapper.batchInsert(csList);
		}

	}

	private void batchInsertCardActive(String channel, List<WftCardStore> csList) {
		List<WftCardActive> caList = new ArrayList<WftCardActive>(csList.size());
		for (WftCardStore cs : csList) {
			WftCardActive ca = new WftCardActive();
			ca.setId(cs.getId());
			ca.setOpid(cs.getOpid());
			ca.setSsid(cs.getSsid());
			ca.setPrvid(cs.getPrvid());
			ca.setCtype(cs.getCtype());
			ca.setNo(cs.getNo());
			ca.setPwd(cs.getPwd());
			ca.setVbtime(cs.getVbtime());
			ca.setVetime(cs.getVetime());
			ca.setBvalue(cs.getBvalue());
			ca.setTvalue(cs.getBvalue());
			ca.setUstatus(cs.getUstatus());
			ca.setCstatus(WftCardStore.CARD_CSTATUS_AVAILABLE);
			ca.setIntime(new Date());
			ca.setUtvalue(0);
			ca.setUcount(0);
			ca.setUscount(0);
			ca.setCache(WftCardActive.OUT_CACHE);
			caList.add(ca);
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("channel", channel);
		params.put("list", caList);
		this.wftCardActiveMapper.batchInsert(params);
	}

	/**
	 * 获取并启用到预启用时间的卡，调入卡池
	 * 
	 * @param status
	 * @param now
	 */
	public void checkForEnableCard(Date now) {
		List<WftCardStore> csList = this.wftCardStoreMapper.getCardForEnable(
				WftCardStore.CARD_CSTATUS_ENABLE, now);

		if (null == csList || csList.size() == 0) {
			LOG.info("no card to change status to avaliable");
			return;
		}

		// 卡状态为启用，并且到预启用时间了，状态改为可用
		LOG.info(String.format("change card status to abaliable size:%s",
				csList.size()));
		for (WftCardStore cs : csList) {
			LOG
					.info(String
							.format(
									"change card[id=%s, no=%s, cstatus=%s, prtime=%s] to avaliable[cstatus=%s], and take it into cardactive",
									cs.getId(), cs.getNo(), cs.getCstatus(), cs
											.getPrtime(),
									WftCardStore.CARD_CSTATUS_AVAILABLE));
			try {
				this.enableCard(cs);
			} catch (Exception e) {
				LOG.error("enableCard[" + cs.getId() + "] error, so rollback",
						e);
				continue;
			}
		}

	}

	/**
	 * 启用卡并调入卡池，<br/>
	 * 加事务处理
	 * 
	 * @param cs
	 */
	public void enableCard(WftCardStore cs) throws Exception {
		WftCardActive ca = new WftCardActive();
		ca.setId(cs.getId());
		ca.setOpid(cs.getOpid());
		ca.setSsid(cs.getSsid());
		ca.setPrvid(cs.getPrvid());
		ca.setCtype(cs.getCtype());
		ca.setNo(cs.getNo());
		ca.setPwd(cs.getPwd());
		ca.setVbtime(cs.getVbtime());
		ca.setVetime(cs.getVetime());
		ca.setBvalue(cs.getBvalue());
		ca.setTvalue(cs.getBvalue());
		ca.setUstatus(cs.getUstatus());
		ca.setCstatus(WftCardStore.CARD_CSTATUS_AVAILABLE);
		ca.setIntime(new Date());
		ca.setUtvalue(0);
		ca.setUcount(0);
		ca.setUscount(0);
		ca.setChannel(cs.getChannel());
		ca.setCache(WftCardActive.OUT_CACHE);
		this.wftCardActiveMapper.insert(ca);

		cs.setCstatus(WftCardStore.CARD_CSTATUS_AVAILABLE);
		cs.setInplace(1);
		cs.setIntime(new Date());
		this.wftCardStoreMapper.update(cs);
	}

}
