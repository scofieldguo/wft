package com.bw30.open.wftAdm.task;

import java.net.URLEncoder;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bw30.open.common.model.WftCardActive;
import com.bw30.open.common.model.WftConnSession;
import com.bw30.open.wft.common.HttpClientUtil;
import com.bw30.open.wftAdm.service.CardService;
import com.bw30.open.wftAdm.service.WftConnSessionService;

public class ConnActiveCmccThread extends Thread{
	private static final Logger LOG = Logger.getLogger(ConnActiveCmccThread.class);

	private String remotelogoutUrl;
	
	private CardService cardService;
	
	private WftConnSessionService wftConnSessionService;
	
	private static HttpClient httpClient = HttpClientUtil.getClient(50000, 10000);

	public ConnActiveCmccThread(WftConnSessionService wftConnSessionService, CardService cardService,String remotelogoutUrl) {
		this.cardService = cardService;
		this.wftConnSessionService = wftConnSessionService;
		this.remotelogoutUrl = remotelogoutUrl;
	}

	public void run() {
		try {
			LOG.info(getName() + " 线程运行开始!");
			WftConnSession connSession = ConnActiveCmccTask.getConnSession();
			while (null !=connSession) {
			long time1 = System.currentTimeMillis();
			Long time = 0L;
			WftCardActive card = this.cardService.findCardById(connSession.getChannel(),
					connSession.getCid());
			Date nowDate = new Date();
			// 关闭会话
			if (WftConnSession.STATUS_ALIVE == connSession.getStatus()) {// 已计费
				LOG.info(String
						.format("close invalid conn session:billing, channel=%s, csid=%s, cid=%s",
								connSession.getChannel(), connSession.getCsid(), connSession.getCid()));
				time = ((nowDate.getTime() - connSession.getBstime().getTime()) / 1000);
				connSession.setStatus(WftConnSession.STATUS_CLOSE);
				connSession.setUstatus(WftConnSession.USTATUS_CLOSED_FORCE);
				connSession.setUflag(WftConnSession.UFLAG_SUCCESS);
				connSession.setBetime(nowDate);
				connSession.setEtime(nowDate);
				connSession.setUtvalue(time.intValue());
				connSession.setMflag(WftConnSession.CONN_RECORD_FALG_ERROR);

				// 下线， 移动下线接口性能太差，暂时不做下线
//				String logouturl = connSession.getLogouturl();
//				if (connSession.getSsid().equalsIgnoreCase(WftOperator.OP_SSID_CMCC)
//						&& null != logouturl) {
//					if (this.remoteLogout(connSession.getChannel(), connSession.getCsid(), card.getNo(),
//							card.getPwd(), logouturl)) {
//						connSession.setLstatus(WftConnSession.LSTATUS_OK);
//					}
//				}

			} else if (WftConnSession.STATUS_NEW == connSession.getStatus()) {// 未计费
				LOG.info(String
						.format("close invalid conn session:unbilling, channel=%s, csid=%s, cid=%s",
								connSession.getChannel(), connSession.getCsid(), connSession.getCid()));
				connSession.setEtime(nowDate);
				connSession.setUflag(WftConnSession.UFLAG_FAIL);
				connSession.setStatus(WftConnSession.STATUS_CLOSE);
				connSession.setUstatus(WftConnSession.USTATUS_CLOSED_FORCE);
				connSession.setMflag(WftConnSession.CONN_RECORD_FLAG_NO);
			}

			// 释放卡
			if (null != card) {
				if(connSession.getUid().equals(card.getUid())){ // 如果卡最后使用的用户和当前会话的用户是同一个用户释放卡
					this.cardService.updateCardInfoAndFreeCard(card, connSession.getChannel(),
							time.intValue(),connSession.getUid());
				}
			}
			this.wftConnSessionService.insertConnMongodb(connSession);

			this.wftConnSessionService.deleteConnSession(connSession.getChannel(), connSession.getCsid());

			LOG.info(toString(connSession) + " run time: "
					+ (System.currentTimeMillis() - time1) + "ms");
			
			connSession = ConnActiveCmccTask.getConnSession();
			}
			
		} catch (Exception e) {
			LOG.error(this.toString() + " run cmcc thread error:", e);
		}
		ConnActiveCmccTask.subThreadNum();
		LOG.info(getName() + " 线程运行结束!");

	}
	
	/**
	 * 远程下线
	 * 
	 * @param channel
	 * @param csid
	 * @param cno
	 * @param pwd
	 * @param logoutUrl
	 */
	public boolean remoteLogout(String channel, String csid, String cno,
			String pwd, String logoutUrl) {
		try {
			StringBuffer sb = new StringBuffer(this.remotelogoutUrl)
					.append("?");
			sb.append("Account=").append(cno).append("&Password=").append(pwd)
					.append("&URL=")
					.append(URLEncoder.encode(logoutUrl, "UTF-8"));
			HttpGet httpGet = new HttpGet(sb.toString());
			HttpResponse response = httpClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			LOG.info(String
					.format("remote logout for invalid cmcc session:channel=%s, csid=%s, httpcode=%s",
							channel, csid, statusCode));
			if (HttpStatus.SC_OK == statusCode) {
				String str = EntityUtils
						.toString(response.getEntity(), "UTF-8");
				httpGet.abort();
				LOG.info(String
						.format("remote logout for invalid cmcc session:channel=%s, csid=%s, response=%s",
								channel, csid, str));
				if (null != str && 0 < str.length()) {
					JSONObject obj = JSON.parseObject(str);
					String code = obj.getString("code");
					if ("0".equals(code)) {// 下线成功
						LOG.info(String
								.format("remote logout for invalid cmcc session:channel=%s, csid=%s, ok",
										channel, csid));
						return true;
					}
					// 下线失败
					LOG.warn(String
							.format("remote logout for invalid cmcc session:channel=%s, csid=%s, fail:%s",
									channel, csid, code));
					return false;
				}

				// 下线失败：没有返回结果
				LOG.warn(String
						.format("remote logout for invalid cmcc session:channel=%s, csid=%s, fail：no data response",
								channel, csid));
				return false;
			}

			return false;
		} catch (Exception e) {
			LOG.error(
					String.format(
							"remote logout for invalid cmcc session:channel=%s, csid=%s, fail：",
							channel, csid), e);
			return false;
		}

	}

	public String toString(WftConnSession connSession) {
		return new StringBuffer("ConnActiveThread[channel=")
				.append(connSession.getChannel()).append(", csid=")
				.append(connSession.getCsid()).append("] ").toString();
	}
	
	
	
}
