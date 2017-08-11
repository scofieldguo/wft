package com.bw30.open.cardpool.service;

import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 远程下线
 * 
 * @author Jack
 * 
 *         2014年9月10日
 */
public class RemoteLogoutThread extends Thread {
	private static final Logger LOG = Logger
			.getLogger(RemoteLogoutThread.class);

	private String url;

	private String channel;
	private String csid;
	private String uid;
	private String logoutUrl;
	private String csidqq;
	private Integer flag;
	private String cno;
	private String pwd;

	private OfflineCallbackService offlineCallbackService;
	private WftConnSessionService wftConnSessionService;

	public RemoteLogoutThread(String url, String channel, String csid,
			String uid, String logoutUrl, String csidqq, Integer flag, String cno, String pwd, 
			OfflineCallbackService offlineCallbackService, WftConnSessionService wftConnSessionService) {
		this.url = url;
		this.channel = channel;
		this.csid = csid;
		this.uid = uid;
		this.logoutUrl = logoutUrl;
		this.csidqq = csidqq;
		this.flag = flag;
		this.cno = cno;
		this.pwd = pwd;

		this.offlineCallbackService = offlineCallbackService;
		this.wftConnSessionService = wftConnSessionService;
	}

	public void run() {
		try {
			long time = System.currentTimeMillis();
			LOG.info(String
					.format("remote logout thread:channel=%s, csid=%s, logoutUrl=%s, csidqq=%s, flag=%s",
							this.channel, this.csid, this.logoutUrl,
							this.csidqq, this.flag));
			boolean isok = false;
			try {
				if(null != this.logoutUrl && 0 < this.logoutUrl.length()){
					//FIXME 客户端下线成功或失败，服务器均作下线（因为腾讯可能会先发下线成功，再发下线失败），
//					下线接口会对下线失败的查在线状态，如果不在线则认为下线成功，否则认为下线失败
					
//					if(2 == this.flag){//客户端下线成功，服务器无需作下线
//						isok = true;
//					}else{
						HttpClient client = new DefaultHttpClient();
						HttpParams params = client.getParams();
						HttpConnectionParams.setConnectionTimeout(params, 10000);
//						Account=13410958030&Password=LS4ShAty
						StringBuffer sb = new StringBuffer(this.url).append("?");
						sb.append("Account=").append(this.cno)
							.append("&Password=").append(this.pwd)
							.append("&URL=").append(URLEncoder.encode(this.logoutUrl, "UTF-8"));
						HttpGet httpGet = new HttpGet(sb.toString());
						HttpResponse response = client.execute(httpGet);
						int statusCode = response.getStatusLine().getStatusCode();
						LOG.info(String
								.format("remote logout thread:channel=%s, csid=%s, csidqq=%s, httpcode=%s",
										this.channel, this.csid, this.csidqq,
										statusCode));
						if (HttpStatus.SC_OK == statusCode) {
							String str = EntityUtils.toString(response.getEntity(),
									"UTF-8");
							httpGet.abort();
							LOG.info(String
									.format("remote logout thread:channel=%s, csid=%s, csidqq=%s, response=%s",
											this.channel, this.csid, this.csidqq, str));
							if (null != str && 0 < str.length()) {
								JSONObject obj = JSON.parseObject(str);
								String code = obj.getString("code");
								if ("0".equals(code)) {//下线成功
									LOG.info(String
											.format("remote logout thread:channel=%s, csid=%s, csidqq=%s, ok",
													this.channel, this.csid,
													this.csidqq));
									isok = true;
								}else{//下线失败
									LOG.warn(String
											.format("remote logout thread:channel=%s, csid=%s, csidqq=%s, fail:%s",
													this.channel, this.csid, this.csidqq, code));
								}
							} else {//下线失败：没有返回结果
								LOG.warn(String
										.format("remote logout thread:channel=%s, csid=%s, csidqq=%s, fail：no data response",
												this.channel, this.csid, this.csidqq));
							}
						}
//					}
				}else{//下线地址为空，不作下线，直接停止计费
					LOG.warn(
							String.format(
									"remote logout thread:channel=%s, csid=%s, csidqq=5s, logoutUrl is null, end bill ",
									this.channel, this.csid, this.csidqq));
					isok = true;
				}
				
			} catch (Exception e) {
				LOG.info(
						String.format(
								"remote logout thread:channel=%s, csid=%s, csidqq=5s, fail",
								this.channel, this.csid, this.csidqq), e);
			}

			// call back for qq
			this.offlineCallbackService.offlineResult(this.uid, this.csidqq,
					isok ? 1 : 0);

			// update connSession, freeback card
			//下线请求会重发，而且是多线程，需要加同步控制
			if(isok){//下线成功
				this.wftConnSessionService.closeSessionAndFreeCard(this.channel, this.csid, this.flag);
			}

			LOG.info(this.toString() + " run time: "
					+ (System.currentTimeMillis() - time) + "ms");
		} catch (Exception e) {
			LOG.error(this.toString() + " run error:", e);
		}

	}
	
	public String toString() {
		return new StringBuffer("remote logout thread[channel=")
				.append(this.channel).append(", csid=").append(this.csid)
				.append(", csidqq=").append(this.csidqq)
				.append(", flag=").append(this.flag).append("] ")
				.toString();
	}

}
