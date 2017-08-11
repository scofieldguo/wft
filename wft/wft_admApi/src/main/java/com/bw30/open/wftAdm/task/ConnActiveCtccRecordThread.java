package com.bw30.open.wftAdm.task;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.bw30.open.common.model.WftCardActive;
import com.bw30.open.common.model.WftConnSession;
import com.bw30.open.wft.common.cardpool.rmi.bean.CardRecord;
import com.bw30.open.wftAdm.service.CardService;
import com.bw30.open.wftAdm.service.WftConnSessionService;

/**
 * 处理ctcc计费中的会话：查询ctcc的清单接口，若对账成功，则关闭会话并释放卡
 * 
 * @author Jack
 * 
 *         2014年10月29日
 */
public class ConnActiveCtccRecordThread extends Thread {
	public static final Logger LOG = Logger
			.getLogger(ConnActiveCtccRecordThread.class);

	private WftConnSessionService wftConnSessionService;
	private CardService cardService;

	public ConnActiveCtccRecordThread(WftConnSessionService wftConnSessionService,
			CardService cardService) {
		this.wftConnSessionService = wftConnSessionService;
		this.cardService = cardService;
	}

	public void run() {
		LOG.info(this.getName() + " ctcc active session record thread start");
		long time = System.currentTimeMillis();

		WftConnSession connSession = ConnActiveCtccTask.getConnSession();
		while (null != connSession) {
			try {
				this.doActiveSessionForCtcc(connSession);
			} catch (Exception e) {
				LOG.error(this.getName()
						+ " ctcc active session record thread run error:csid="
						+ connSession.getCsid(), e);
			}

			connSession = ConnActiveCtccTask.getConnSession();
		}

		ConnActiveCtccTask.subThreadNum();
		LOG.info(this.getName() + " ctcc active session record thread end: "
				+ (System.currentTimeMillis() - time) + "ms");

	}

	/**
	 * 查询ctcc的清单接口，若对账成功，则关闭会话并释放卡
	 * 
	 * @param cs
	 * @throws Exception
	 */
	private void doActiveSessionForCtcc(WftConnSession cs) throws Exception {
		String channel = cs.getChannel();
		String csid = cs.getCsid();
		Integer cid = cs.getCid();
		WftCardActive card = this.cardService.findCardById(channel, cid);
		if (null == card) {
			this.wftConnSessionService.deleteConnSession(channel, csid);// 删除链接会话
			return;
		}

		Date now = new Date();
		Date bstime = cs.getBstime();
		Long time = (now.getTime() - bstime.getTime()) / 1000;

		// 卡使用记录对账
		CardRecord record = this.wftConnSessionService.getRecordFromOp(csid,
				channel, card.getOpid(), card.getNo(), card.getPwd(), bstime,
				now, bstime);

		if (null != record) {// 对账成功，释放卡，关闭会话并移除
			// 释放卡
			this.cardService.updateCardInfoAndFreeCard(card, cs.getChannel(),
					record.getTimelen(), cs.getUid());

			// 关闭并移除会话
			cs.setBetime(now);

			cs.setUtvalue(time.intValue());
			cs.setEtime(now);
			cs.setStatus(WftConnSession.STATUS_CLOSE);
			cs.setUstatus(WftConnSession.USTATUS_CLOSED_FORCE);
			cs.setBstimeop(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.parse(record.getStarttime()));
			cs.setBetimeop(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.parse(record.getEndtime()));
			Integer utimeop = record.getTimelen();
			cs.setUtvalueop(utimeop);
			cs.setUhourop(utimeop % 3600 != 0 ? (utimeop / 3600 + 1)
					: utimeop / 3600);
			cs.setMflag(WftConnSession.CONN_RECORD_FALG_YES);

			Integer mvalue = card.getTvalue() - utimeop;
			if (0 <= mvalue) {
				cs.setMvalue(0);
			} else {
				cs.setMvalue(mvalue);
			}
			this.wftConnSessionService.insertConnMongodb(cs);// 把关闭的链接会话写入mongodb
			this.wftConnSessionService.deleteConnSession(channel, csid);// 删除链接会话
		}

	}

}
