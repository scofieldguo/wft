package com.bw30.open.cardpool.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.bw30.open.common.model.WftCardActive;
import com.bw30.open.common.model.WftConnSession;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.wft.common.TaskThreadPool;
import com.bw30.open.wft.mongo.MongoDBService;

public class ConnSessionService {
	private static final Logger LOG = Logger
			.getLogger(ConnSessionService.class);

	@Resource
	private CardService cardService;
	@Resource
	private OfflineCallbackService offlineCallbackService;
	@Resource
	private WftConnSessionService wftConnSessionService;
	@Resource
	private MongoDBService mongoDBService;

	private String url;

	private TaskThreadPool remoteLogoutThreadPool;

	public void setCardService(CardService cardService) {
		this.cardService = cardService;
	}

	public void setOfflineCallbackService(
			OfflineCallbackService offlineCallbackService) {
		this.offlineCallbackService = offlineCallbackService;
	}

	public void setWftConnSessionService(
			WftConnSessionService wftConnSessionService) {
		this.wftConnSessionService = wftConnSessionService;
	}

	public void setMongoDBService(MongoDBService mongoDBService) {
		this.mongoDBService = mongoDBService;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setRemoteLogoutThreadPool(TaskThreadPool remoteLogoutThreadPool) {
		this.remoteLogoutThreadPool = remoteLogoutThreadPool;
	}

	/**
	 * 查询用户是否在线，开始计费才认为在线
	 * 
	 * @param userId
	 * @param channel
	 * @return null不在线
	 */
	public WftConnSession checkOnline(String userId, String channel) {
		List<WftConnSession> csList = this.wftConnSessionService.findByUid(
				channel, userId);
		if (null != csList && 0 < csList.size()) {
			for (WftConnSession cs : csList) {
				if (WftConnSession.STATUS_ALIVE == cs.getStatus()) {
					return cs;
				}
			}
		}
		return null;
	}

	/**
	 * 查询用户的使用记录，成功且结束计费的卡使用记录，<br/>
	 * 按计费开始时间查询
	 * 
	 * @param userId
	 * @param channel
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<WftConnSession> getRecord(String userId, String channel,
			Date startDate, Date endDate) {
		return this.wftConnSessionService.findUserRecord(userId, channel,
				startDate, endDate);
	}

	/**
	 * 更新合作方上线时间
	 * 
	 * @param uid
	 * @param channel
	 * @param csid
	 * @param bstimeqq
	 */
	public void updateForOnline(String uid, String channel, String csidqq,
			Date bstimeqq) {
		// WftConnSession cs = this.wftConnSessionMapper.findById(channel,
		// csid);
		// Map<String, Object> paramMap = new HashMap<String, Object>();
		// if(null != cs){
		// paramMap.put("bstimeqq", bstimeqq);
		// paramMap.put("csid", csid);
		// this.wftConnSessionMapper.updateByParam(paramMap, channel);
		// return;
		// }
		//
		// paramMap.clear();
		// paramMap.put("bstimeqq", bstimeqq);
		// Query query = new Query(Criteria.where("csid").is(csid));
		// this.mongoDBService.update(query, paramMap,
		// MongoDBService.CONNDATAKEY + channel);

		// 当前csid不是真实值，只能根据uid查, 取最近的一次连接
		List<WftConnSession> csList = this.wftConnSessionService.findByUid(
				channel, uid);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		WftConnSession cs = null;
		if (null != csList && 0 < csList.size()) {
			cs = csList.get(0);
			if (null == cs.getBstime()) {// 客户端计费请求602未上来，或者后台上线请求在602之前上来时
				LOG.warn(String
						.format("no wifi602 but onlineNotify come, update bstime and uflag:csid=%s, csidqq=%s",
								cs.getCsid(), csidqq));
				paramMap.put("bstime", cs.getStime());
				paramMap.put("uflag", WftConnSession.UFLAG_SUCCESS);
				paramMap.put("status", WftConnSession.STATUS_ALIVE);
				paramMap.put("ustatus", WftConnSession.USTATUS_LOGIN_SUCCESS);
			}
			paramMap.put("bstimeqq", bstimeqq);
			paramMap.put("csidqq", csidqq);
			paramMap.put("csid", cs.getCsid());
			this.wftConnSessionService.updateConnSession(channel, paramMap);
		} else {
			LOG.warn(String.format(
					"online notify error: uid=%s, csidqq=%s, no connsession",
					uid, csidqq));
		}

//		try {
//			if (null != cs) {
//				this.saveQqNotify(uid, cs.getCsid(), csidqq, cs.getSsid(),
//						bstimeqq);
//			} else {
//				this.saveQqNotify(uid, null, csidqq, null, bstimeqq);
//			}
//		} catch (Exception e) {
//			LOG.error("save qq notify error");
//		}

		// 只查询在线的会话
		// Query query = new Query(Criteria.where("uid").is(uid));
		// Integer size = this.mongoDBService.count(query,
		// MongoDBService.CONNDATAKEY + channel);
		// if (0 >= size) {
		// return;
		// }
		// Pager pager = new Pager();
		// pager.setRecCount(size);
		// pager.setPageIndex(1);
		// pager.setPageSize(1);
		// query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "stime")));
		// csList = this.mongoDBService.findForPage(query, WftConnSession.class,
		// pager, MongoDBService.CONNDATAKEY + channel);
		// if (null == csList || 0 == csList.size()) {
		// return;
		// }
		// paramMap.clear();
		// paramMap.put("bstimeqq", bstimeqq);
		// this.mongoDBService.update(
		// new Query(Criteria.where("csid").is(csList.get(0).getCsid())),
		// paramMap, MongoDBService.CONNDATAKEY + channel);

	}

//	private void saveQqNotify(String uid, String csid, String csidqq,
//			String ssid, Date bstime) {
//		QqNotify notify = new QqNotify();
//		notify.setUid(uid);
//		notify.setCsid(csid);
//		notify.setCsidqq(csidqq);
//		notify.setSsid(ssid);
//		notify.setBstime(bstime);
//		this.mongoDBService.save(notify, MongoDBService.KEY_QQNOTIFY);
//	}

//	private void updateQqNotify(String uid, String csidqq, Integer flag,
//			Date betime) {
//		Query query = new Query(Criteria.where("uid").is(uid));
//		query.addCriteria(Criteria.where("csidqq").is(csidqq));
//		Map<String, Object> paramMap = new HashMap<String, Object>();
//		paramMap.put("betime", betime);
//		paramMap.put("flag", flag);
//		this.mongoDBService
//				.update(query, paramMap, MongoDBService.KEY_QQNOTIFY);
//	}

	/**
	 * 更新合作方下线时间
	 * 
	 * @param uid
	 * @param channel
	 * @param csid
	 * @param betimeqq
	 */
	public void updateForOffline(String uid, String channel, String csidqq,
			Date betimeqq, Integer flag) {
		// WftConnSession cs = this.wftConnSessionMapper.findById(channel,
		// csid);
		// Map<String, Object> paramMap = new HashMap<String, Object>();
		// if(null != cs){
		// paramMap.put("betimeqq", betimeqq);
		// paramMap.put("csid", csid);
		// this.wftConnSessionMapper.updateByParam(paramMap, channel);
		// return;
		// }
		//
		// paramMap.clear();
		// paramMap.put("betimeqq", betimeqq);
		// Query query = new Query(Criteria.where("csid").is(csid));
		// this.mongoDBService.update(query, paramMap,
		// MongoDBService.CONNDATAKEY + channel);

		// 当前csid不是真实值，只能根据uid查
		// this.wftConnSessionMapper.findByUid(uid, channel);

//		try {
//			this.updateQqNotify(uid, csidqq, flag, betimeqq);
//		} catch (Exception e) {
//			LOG.error("update qq notify error");
//		}

		WftConnSession cs = this.wftConnSessionService.findByCsidqqAndUid(
				channel, csidqq, uid);
		if (null != cs) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("flagqq", flag);
			paramMap.put("betimeqq", betimeqq);
			paramMap.put("csid", cs.getCsid());
			this.wftConnSessionService.updateConnSession(channel, paramMap);
			if (cs.getSsid().toUpperCase().contains(WftOperator.OP_SSID_CMCC)) {
				this.remoteLogout(cs, channel, flag);
				return;
			}
			LOG.warn(String
					.format("offline notify: uid=%s, csidqq=%s, ctcc conn session, callback to qq offline success",
							uid, csidqq));
		} else {
			// 没找到连接会话，不作下线，直接通知下线成功
			LOG.warn(String
					.format("offline notify error: uid=%s, csidqq=%s, no connsession, callback to qq offline success",
							uid, csidqq));
		}

		this.offlineCallbackService.offlineResult(uid, csidqq, 1);

		// Query query = new Query(Criteria.where("uid").is(uid));
		// Integer size = this.mongoDBService.count(query,
		// MongoDBService.CONNDATAKEY + channel);
		// if (0 >= size) {
		// return;
		// }
		// Pager pager = new Pager();
		// pager.setRecCount(size);
		// pager.setPageIndex(1);
		// pager.setPageSize(1);
		// query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "stime")));
		// csList = this.mongoDBService.findForPage(query, WftConnSession.class,
		// pager, MongoDBService.CONNDATAKEY + channel);
		// if (null == csList || 0 == csList.size()) {
		// return;
		// }
		// paramMap.clear();
		// paramMap.put("betimeqq", betimeqq);
		// this.mongoDBService.update(
		// new Query(Criteria.where("csid").is(csList.get(0).getCsid())),
		// paramMap, MongoDBService.CONNDATAKEY + channel);
		// this.remoteLogout(csList.get(0), channel);
	}

	/**
	 * 远程下线，只针对移动
	 * 
	 * @param cs
	 */
	private void remoteLogout(WftConnSession cs, String channel, Integer flag) {
		String ssid = cs.getSsid();
		String logoutUrl = cs.getLogouturl();
		WftCardActive card = this.cardService.findCard(channel,
				WftOperator.OP_ID_CMCC, cs.getCno());
		if (null == card) {
			LOG.warn(String.format("no card:channel=%s, csid=%s, cid=%s",
					channel, cs.getCsid(), cs.getCid()));
			return;
		}
		if (ssid.toUpperCase().contains(WftOperator.OP_SSID_CMCC)) {// 移动下线
			this.remoteLogoutThreadPool.addTask(new RemoteLogoutThread(
					this.url, channel, cs.getCsid(), cs.getUid(), logoutUrl, cs
							.getCsidqq(), flag, card.getNo(), card.getPwd(),
					this.offlineCallbackService, this.wftConnSessionService));
		}
	}

}
