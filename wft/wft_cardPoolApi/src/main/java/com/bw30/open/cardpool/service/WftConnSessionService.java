package com.bw30.open.cardpool.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.bw30.open.common.dao.mapper.WftCardActiveMapper;
import com.bw30.open.common.dao.mapper.WftChannelMapper;
import com.bw30.open.common.dao.mapper.WftConnSessionMapper;
import com.bw30.open.common.model.WftCardActive;
import com.bw30.open.common.model.WftChannel;
import com.bw30.open.common.model.WftConnSession;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.wft.common.PwdEncrypt;
import com.bw30.open.wft.common.cardpool.rmi.bean.CardRecord;
import com.bw30.open.wft.common.cardpool.rmi.service.ICardPoolService;
import com.bw30.open.wft.mongo.MongoDBService;

public class WftConnSessionService {
	private static final Logger LOG = Logger
			.getLogger(WftConnSessionService.class);
	private static final SimpleDateFormat SDF_YMD_HMS = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat SDF_YMD = new SimpleDateFormat(
			"yyyy-MM-dd");

	@Resource
	private WftChannelMapper wftChannelMaper;
	@Resource
	private WftConnSessionMapper wftConnSessionMapper;
	@Resource
	private WftCardActiveMapper wftCardActiveMapper;
	@Resource
	private MongoDBService mongoDBService;
	@Resource
	private ICardPoolService cardPoolService;

	public void setWftChannelMaper(WftChannelMapper wftChannelMaper) {
		this.wftChannelMaper = wftChannelMaper;
	}

	public void setWftConnSessionMapper(
			WftConnSessionMapper wftConnSessionMapper) {
		this.wftConnSessionMapper = wftConnSessionMapper;
	}

	public void setWftCardActiveMapper(WftCardActiveMapper wftCardActiveMapper) {
		this.wftCardActiveMapper = wftCardActiveMapper;
	}

	public void setMongoDBService(MongoDBService mongoDBService) {
		this.mongoDBService = mongoDBService;
	}

	public void setCardPoolService(ICardPoolService cardPoolService) {
		this.cardPoolService = cardPoolService;
	}

	public WftConnSession getConnSessionById(String csid, String channel) {
		return this.wftConnSessionMapper.findById(channel, csid);
	}

	public List<WftConnSession> findByUid(String channel, String uid) {
		return this.wftConnSessionMapper.findByUid(uid, channel);
	}

	public List<WftConnSession> findByCsidqq(String channel, String csidqq) {
		return this.wftConnSessionMapper.findByCsidqq(csidqq, channel);
	}

	public WftConnSession findByCsidqqAndUid(String channel, String csidqq,
			String uid) {
		List<WftConnSession> csList = this.wftConnSessionMapper
				.findByCsidqqAndUid(csidqq, uid, channel);
		if (null != csList && 0 < csList.size()) {
			return csList.get(0);
		}
		return null;
	}

	public void updateConnSession(String channel, Map<String, Object> paramMap) {
		this.wftConnSessionMapper.updateByParam(paramMap, channel);
	}

	public void deleteConnSession(String channel, String csid) {
		this.wftConnSessionMapper.delete(channel, csid);
	}

	/**
	 * 查询用户成功使用清单
	 * 
	 * @param userId
	 * @param channel
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<WftConnSession> findUserRecord(String userId, String channel,
			Date startDate, Date endDate) {
		Query query = new Query(Criteria.where("uid").is(userId));
		query.addCriteria(Criteria.where("uflag").is(
				WftConnSession.UFLAG_SUCCESS));
		query.addCriteria(Criteria.where("status").is(
				WftConnSession.STATUS_CLOSE));
		query.addCriteria(Criteria.where("bstime").gte(startDate).lt(endDate));
		query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "bstime")));
		return this.mongoDBService.find(query, WftConnSession.class,
				MongoDBService.CONNDATAKEY + channel);
	}

	public synchronized void closeSessionAndFreeCard(String channel,
			String csid, Integer flag) throws ParseException {
		Date now = new Date();
		WftConnSession cs = this.getConnSessionById(csid, channel);
		if (null == cs) {// 下线请求会重发，而且是多线程，连接会话操作会不同步
			return;
		}
		Date bstime = cs.getBstime();
		if (null == bstime) {
			bstime = cs.getStime();
			cs.setBstime(cs.getStime());
			cs.setUflag(WftConnSession.UFLAG_SUCCESS);
		}
		Long time = now.getTime() - bstime.getTime();
		time = time / 1000;// 秒

		WftChannel wftChannel = null;
		try{
			wftChannel = this.wftChannelMaper.findById(channel);
		}catch(Exception e){
			
		}
		
		WftCardActive card = this.wftCardActiveMapper.findById(channel,
				cs.getCid());
		String pwd = card.getPwd();
		pwd = PwdEncrypt.decryptCardPwd(pwd);

		// 与运营商对账
//		CardRecord cr = this.getRecordFromOp(csid, channel, card.getOpid(),
//				card.getNo(), pwd, now, now, bstime);
//		if (null != cr) {
//			cs.setMflag(WftConnSession.CONN_RECORD_FALG_YES);
//			cs.setBstimeop(SDF_YMD_HMS.parse(cr.getStarttime()));
//			cs.setBetimeop(SDF_YMD_HMS.parse(cr.getEndtime()));
//			cs.setUtvalueop(cr.getTimelen());
//		} else {
//			cs.setMflag(WftConnSession.CONN_RECORD_FALG_ERROR);
//		}
		
		cs.setMflag(WftConnSession.CONN_RECORD_FALG_ERROR);
		
		// 保存卡使用记录到mongodb
		cs.setLstatus(WftConnSession.LSTATUS_OK);
		cs.setStatus(WftConnSession.STATUS_CLOSE);
		cs.setUstatus(2 == flag ? WftConnSession.USTATUS_CLOSED_NORMAL
				: WftConnSession.USTATUS_CLOSED_FORCE);
		cs.setBetime(now);
		cs.setEtime(now);
		cs.setUtvalue(time.intValue());
		
		if(null == wftChannel || time < wftChannel.getCmccinterval()){//计费时长 < 取卡间隔
			cs.setChannel(channel);
			this.wftConnSessionMapper.update(cs);
		} else {
			// 释放卡
			this.wftCardActiveMapper.freebackCard(channel, cs.getCid(),
					time.intValue());
			this.mongoDBService.save(cs, MongoDBService.CONNDATAKEY + channel);
			// 删除mysql
			this.deleteConnSession(channel, csid);
		}

	}

	private CardRecord getRecordFromOp(String csid, String channel, Integer op,
			String cno, String pwd, Date startDate, Date endDate, Date bstime) {
		LOG.info(String.format("match conn record[csid=%s, op=%s, no=%s]",
				csid, op, cno));
		try {
			String sday = SDF_YMD.format(startDate);
			String eday = SDF_YMD.format(endDate);

			if (WftOperator.OP_ID_CMCC == op) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(endDate);
				calendar.add(Calendar.DAY_OF_MONTH, 1);
				eday = SDF_YMD.format(calendar.getTime());
			}

			List<CardRecord> list = this.cardPoolService.queryRecord(channel,
					op, cno, pwd, sday, eday);
			if (null != list && list.size() > 0) {
				Long len = 0L;
				int i = 1;
				i = (WftOperator.OP_ID_CMCC == op ? 2 : 1);
				for (CardRecord r : list) {
					Date bsTimeOp = SDF_YMD_HMS.parse(r.getStarttime());
					len = bsTimeOp.getTime() - bstime.getTime();// 毫秒
					len /= 1000;// 秒
					LOG.info(String
							.format("match conn record[csid=%s, op=%s, no=%s]: timelen=%s second",
									csid, op, cno, len));
					if (-1 * i * 60 <= len && len <= i * 60) {
						LOG.info(String
								.format("match conn record[csid=%s, op=%s, no=%s]: match ok, timelen=%s second",
										csid, op, cno, len));
						return r;
					}
				}
				LOG.info(String.format(
						"match conn record[csid=%s, op=%s, no=%s]: match fail",
						csid, op, cno));
			} else {
				LOG.info(String
						.format("match conn record[csid=%s, op=%s, no=%s]: no op record",
								csid, op, cno));
			}
			return null;
		} catch (Exception e) {
			LOG.error("query record api error", e);
			return null;
		}

	}

}
