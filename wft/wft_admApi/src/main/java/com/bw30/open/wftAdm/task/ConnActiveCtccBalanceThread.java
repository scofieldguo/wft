package com.bw30.open.wftAdm.task;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import com.bw30.open.common.model.WftCardActive;
import com.bw30.open.common.model.WftConnSession;
import com.bw30.open.wftAdm.service.CardApiService;
import com.bw30.open.wftAdm.service.CardService;
import com.bw30.open.wftAdm.service.WftConnSessionService;

/**
 * 处理ctcc计费中的会话： 查询ctcc卡余额查询接口，根据卡使用前后的余额计算使用时长。
 * 电信时长卡有3小时下线机制，但因设备原因也会存在下线失败的情况，此处忽略这种情况， 若计费时长大于3小时也认为卡已下线。
 * 
 * @author Jack
 * 
 *         2014年10月29日
 */
public class ConnActiveCtccBalanceThread extends Thread {
	private static final Logger LOG = Logger
			.getLogger(ConnActiveCtccBalanceThread.class);

	private CardApiService cardApiService;
	private CardService cardService;
	private WftConnSessionService wftConnSessionService;
	private Integer rechargeThresold;
	private String rechargeCtype;

	public ConnActiveCtccBalanceThread(CardApiService cardApiService,
			CardService cardService,
			WftConnSessionService wftConnSessionService,
			Integer rechargeThresold, String rechargeCtype) {
		this.cardApiService = cardApiService;
		this.cardService = cardService;
		this.wftConnSessionService = wftConnSessionService;
		this.rechargeThresold = rechargeThresold;
		this.rechargeCtype = rechargeCtype;
	}

	public void run() {
		LOG.info(this.getName() + " ctcc active session balance thread start");
		long time = System.currentTimeMillis();

		WftConnSession connSession = ConnActiveCtccTask.getConnSession();

		while (null != connSession) {
			try {
				this.doActiveSessionForCtcc(connSession);
			} catch (Exception e) {
				LOG.error(
						String.format(
								"%s ctcc active session balance thread run error: channel=%s, csid=%s",
								this.getName(), connSession.getChannel(),
								connSession.getCsid()), e);
			}

			connSession = ConnActiveCtccTask.getConnSession();
		}

		ConnActiveCtccTask.subThreadNum();
		LOG.info(this.getName() + " ctcc active session balance thread end: "
				+ (System.currentTimeMillis() - time) + "ms");
	}

	private void doActiveSessionForCtcc(WftConnSession connSession)
			throws Exception {
		String channel = connSession.getChannel();
		String csid = connSession.getCsid();
		Integer cid = connSession.getCid();

		WftCardActive card = this.cardService.findCardById(channel, cid);
		if (null == card) {
			LOG.error(String.format(
					"%s no card find: channel=%s, csid=%s, cid=%s",
					this.getName(), channel, csid, cid));
			this.wftConnSessionService.deleteConnSession(channel, csid);
			return;
		}

		// 查询卡的剩余时长
		Integer balance = this.cardApiService.getBalance(channel, cid,
				card.getOpid(), card.getNo(), card.getPwd());
		if (null == balance) {// 获取剩余时长失败，不释放卡，继续等待轮询
			LOG.error(String.format(
					"%s query card balance error:channel=%s, no=%s",
					this.getName(), channel, card.getNo()));
			return;
		}

		LOG.info(String.format(
				"%s query card balance success:no=%s, balance=%s",
				this.getName(), card.getNo(), balance));
		// 运营商计费时长 = 分卡时卡时长 - 使用完成后卡剩余时长
		Integer tvalue = card.getTvalue();
		int utvalueop = tvalue - balance;// 秒

		// bw平台计费时长
		Date now = new Date();
		Date bstime = connSession.getBstime();
		Long utvalue = now.getTime() - bstime.getTime();
		utvalue /= 1000;// 秒

		/*
		 * 电信卡计费结束后才扣费，若卡余额与使用前相等，则认为卡仍在计费中；
		 * 若计费时长大于3小时，则认为卡已下线（电信时长卡有3小时下线机制，但因设备原因也会存在下线失败的情况，此处忽略这种情况）
		 */
		if (0 == utvalueop && utvalue < 3 * 60 * 60) {
			return;
		}

		// 释放卡
		WftCardActive ca = new WftCardActive();
		ca.setId(cid);
		
		if (-60 > balance) {// 卡余额 < -300s，停卡
			LOG.info(String.format(
					"stop card: use timeout:channel=%s, cno=%s, tvalue=%s",
					channel, card.getNo(), balance));
			ca.setCstatus(WftCardActive.CSTATUS_STOP);
			ca.setStoptime(now);
		} else {
			if (WftCardActive.CSTATUS_STOP != card.getCstatus()) {
				ca.setCstatus(WftCardActive.CSTATUS_AVAILABLE);
//					if (balance < this.rechargeThresold) { // 充值
//						this.cardApiService.recharge(channel, card.getId(),
//								card.getNo(), card.getOpid(), this.rechargeCtype,
//								null);
//					}
			}else{
				ca.setCstatus(WftCardActive.CSTATUS_STOP);
			}
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
		
		ca.setUid(connSession.getUid());
		this.cardService.update(channel, ca);

		// 关闭并删除会话
		connSession.setBetime(now);
		connSession.setUtvalue(utvalue.intValue());// 秒
		connSession.setEtime(now);
		connSession.setStatus(WftConnSession.STATUS_CLOSE);
		connSession.setUstatus(WftConnSession.USTATUS_CLOSED_FORCE);

		// 计算修正时长：卡使用过程中透支的时长，秒
		if (0 <= balance) {
			connSession.setMvalue(0);
		} else {
			connSession.setMvalue(balance);
		}

		if (utvalueop <= 0) { // 卡在线且计费时长大于3小时或者卡时长校验错误, 认为对账失败，不计算在对账清单里
			LOG.warn(String.format(
					"%s utvalueop<=0:no=%s, tvalue=%s, balance=%s",
					this.getName(), card.getNo(), tvalue, balance));
			connSession.setMflag(WftConnSession.CONN_RECORD_FALG_ERROR);
		} else { // 对账成功
			// 计算计费结束时间 = 计费开始时间 + 计费时长
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(bstime);
			calendar.add(Calendar.SECOND, utvalueop);
			Date betimeop = calendar.getTime();

			connSession.setUtvalueop(utvalueop);
			connSession.setBstimeop(bstime);
			connSession.setBetimeop(betimeop);
			connSession.setMflag(WftConnSession.CONN_RECORD_FALG_YES);
		}

		this.wftConnSessionService.insertConnMongodb(connSession);// 把关闭的链接会话写入mongodb
		this.wftConnSessionService.deleteConnSession(channel, csid);// 删除链接会话
	}

}
