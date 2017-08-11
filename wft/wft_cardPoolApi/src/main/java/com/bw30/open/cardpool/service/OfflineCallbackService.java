package com.bw30.open.cardpool.service;

import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.bw30.open.cardpool.service.qq.OfflineResultReq;
import com.bw30.open.cardpool.service.qq.OfflineResultResp;
import com.bw30.open.cardpool.service.qq.WiFiQueryCgiServerData;
import com.bw30.open.cardpool.service.qq.WiFiQueryCgiServerReq;
import com.bw30.open.cardpool.service.qq.WiFiQueryCgiServerResp;
import com.bw30.open.cardpool.util.Md5Encrypt;

/**
 * 下线结果回调qq
 * 
 * @author Jack
 *
 * 2014年9月10日
 */
public class OfflineCallbackService {
	private static final Logger LOG = Logger.getLogger("OFFLINE_RESULT");
	
	private String appid;
	private String url;
	private String md5Key;
	
	/**
	 * 下线结果回调
	 * 
	 * @param openid
	 * @param csid
	 * @param status
	 * @throws Exception
	 */
	public void offlineResult(String openid, String csid, Integer status){
		try{
			OfflineResultReq data = new OfflineResultReq();
			data.setAppid(this.appid);
			data.setOpenid(openid);
			data.setCsid(csid);
			data.setStatus(status);
			data.setSign(this.sign(openid, csid, status));
			
			WiFiQueryCgiServerReq req = new WiFiQueryCgiServerReq();
			req.setOfflineResult(data);
			String jsonStr = JSONObject.toJSONString(req);
			String getUrl = this.url + "?methods=" + URLEncoder.encode(jsonStr, "UTF-8");
			LOG.info(String.format("callback for offline: csid=%s, url=%s, methods=%s", csid, this.url, jsonStr));
			
			HttpClient client = new DefaultHttpClient();
			HttpParams params = client.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpGet httpGet = new HttpGet(getUrl);
			HttpResponse response = client.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			LOG.info(String.format("callback for offline: csid=%s, httpcode=%s", csid, statusCode));
			String str = EntityUtils.toString(response.getEntity());
			LOG.info(String.format("callback for offline: csid=%s, result=%s", csid, str));
			httpGet.abort();
			
			WiFiQueryCgiServerResp resp = JSONObject.parseObject(str, WiFiQueryCgiServerResp.class);
			if(0 == resp.getEcode()){
				WiFiQueryCgiServerData respData = resp.getData();
				OfflineResultResp orr = respData.getOfflineResult();
				if(0 == orr.getRetCode()){
					LOG.info(String.format("callback for offline: csid=%s, ok", csid));
					return;
				}
			}
			LOG.error(String.format("callback for offline: csid=%s, fail", csid));
		}catch(Exception e){
			LOG.error(String.format("callback for offline: csid=%s, fail", csid), e);
		}
		
	}
	
	private String sign(String openid, String csid, Integer status) throws Exception{
		StringBuffer content = new StringBuffer();
		content.append("appid=").append(this.appid)
				.append("&openid=").append(openid)
				.append("csid=").append(csid)
				.append("status=").append(status)
				.append("&key=").append(this.md5Key);
		
		return Md5Encrypt.md5Enc(content.toString(), "UTF-8");
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMd5Key() {
		return md5Key;
	}

	public void setMd5Key(String md5Key) {
		this.md5Key = md5Key;
	}
	
}
