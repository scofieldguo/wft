package com.bw30.open.wftAdm.task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import com.bw30.open.common.model.WftCardActive;
import com.bw30.open.common.model.WftConnSession;
import com.bw30.open.wft.common.cardpool.rmi.bean.CardRecord;
import com.bw30.open.wftAdm.service.CardApiService;
import com.bw30.open.wftAdm.service.CardService;
import com.bw30.open.wftAdm.service.WftConnSessionService;

/**
 * 处理超时的会话，查询卡的在线状态： 若卡在线，则写入计费时间、修改计费状态； 若卡不在线，则认为连接失败，关闭会话并释放卡。
 * 
 * @author Jack
 * 
 *         2014年8月15日
 */
public class ConnTimeoutCtccThread extends Thread {
	private static final Logger LOG = Logger.getLogger(ConnTimeoutCtccThread.class);

	private CardApiService cardApiService;
	private CardService cardService;
	private WftConnSessionService wftConnSessionService;

	public ConnTimeoutCtccThread(CardApiService cardApiService, CardService cardService,
			WftConnSessionService wftConnSessionService) {
		this.cardApiService = cardApiService;
		this.cardService = cardService;
		this.wftConnSessionService = wftConnSessionService;
	}

	public void run() {
		LOG.info(this.getName() + " ConnTimeoutThread start");
		long time = System.currentTimeMillis();
		
		try {
			WftConnSession connSession = ConnTimeoutCtccTask.getConnSession();
			
			while(null != connSession){
				String channel = connSession.getChannel();
				String csid = connSession.getCsid();

				WftCardActive card = this.cardService.findCardById(channel,
						connSession.getCid());
				if (null == card) {
					LOG.error(String.format(
							"no card find: channel=%s, cid=%s", channel,
							connSession.getCid()));
				} else {
					LOG.info(String
							.format("check online for connSession timeout:channel=%s, csid=%s, opid=%s, cno=%s",
									channel, csid, card.getOpid(), card.getNo()));
					
					Date bstime = this.cardApiService.queryOnline(card.getOpid(), card.getNo(),
							card.getPwd(), null, connSession.getStime());
					if (null != bstime) {// 在线，写入计费时间及状态
						LOG.info(String
								.format("connSession timeout but online, so update it for billing: channel=%s, csid=%s",
										channel, csid));
						WftConnSession cs = new WftConnSession();
						cs.setChannel(channel);
						cs.setCsid(csid);
						cs.setStatus(WftConnSession.STATUS_ALIVE);
						cs.setUstatus(WftConnSession.USTATUS_LOGIN_SUCCESS);
						cs.setBstime(bstime);
						cs.setUflag(WftConnSession.UFLAG_SUCCESS);
						this.wftConnSessionService.updateConnSession(channel, cs);
					} else {// 不在线，比对使用记录
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(connSession.getStime());
						calendar.add(Calendar.DAY_OF_MONTH, 1);
//						CardRecord record = this.wftConnSessionService.getRecordFromOp(csid, 
//								channel, card.getOpid(), card.getNo(), card.getPwd(), connSession.getStime(),
//								calendar.getTime(), connSession.getStime());
//						if (null != record) {
//							LOG.info(String
//									.format("connSession timeout and offline, but find record in operator: channel=%s, csid=%s",
//											channel, csid));
//							
//							int utvalue = record.getTimelen();
//							int tvalue = card.getTvalue();
//							tvalue -= utvalue;//卡剩余时长
//							if(tvalue < 0){
//								connSession.setMvalue(tvalue);
//							}else{
//								connSession.setMvalue(0);
//							}
//							
//							// 释放卡
//							if(WftCardActive.CSTATUS_STOP != card.getCstatus()){
//								this.cardService.freebackAvailable(card.getId(),
//										WftCardActive.CSTATUS_AVAILABLE, channel ,connSession.getUid(), tvalue);
//							}
//							
//							Date bstimeop = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(record.getStarttime());
//							Date betimeop = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(record.getEndtime());
//							connSession.setBstime(bstimeop);
//							connSession.setBetime(betimeop);
//							connSession.setUtvalue(utvalue);
//							connSession.setBstimeop(bstimeop);
//							connSession.setBetimeop(betimeop);
//							connSession.setUtvalueop(utvalue);
//							connSession.setUflag(WftConnSession.UFLAG_SUCCESS);
//							connSession.setMflag(WftConnSession.CONN_RECORD_FALG_YES);
//							connSession
//									.setUhourop(utvalue % 3600 != 0 ? (utvalue / 3600 + 1)
//											: utvalue / 3600);
//						} else {
							LOG.info(String
									.format("connSession timeout and offline, so close it and freeback card: channel=%s, csid=%s",
											channel, csid));
							// 释放卡
							this.cardService.freebackAvailable(card.getId(),
									WftCardActive.CSTATUS_AVAILABLE, channel ,connSession.getUid(), card.getTvalue(),null,null);
							
							connSession.setUflag(WftConnSession.UFLAG_FAIL);
							connSession.setMvalue(0);
//						}

						// 关闭链接会话
						this.wftConnSessionService.colseTimeOutConnSession(connSession,
								new Date());
					}
				}
				
				connSession = ConnTimeoutCtccTask.getConnSession();
				
			}
			
		} catch (Exception e) {
			LOG.error(this.getName() + " ConnTimeoutThread run error:", e);
		}
		
		ConnTimeoutCtccTask.subThreadNum();
		LOG.info(this.getName() + " ConnTimeoutThread end: "
				+ (System.currentTimeMillis() - time) + "ms");

	}

}
