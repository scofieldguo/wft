package com.bw30.openplatform.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.bw30.open.common.model.sale.WftSaleCardRecord10011;
import com.bw30.open.wft.common.cardpool.rmi.bean.CardBean;
import com.bw30.open.wft.common.cardpool.rmi.bean.RechargeBean;
import com.bw30.openplatform.util.HttpClientUtil;

public class WiFiCardService {
	private static final Logger logger = Logger.getLogger(WiFiCardService.class);
	private  String serverURL;

	
	public  WftSaleCardRecord10011 openCard(String codeop,String order_id,String channel,String openKey){
		try {
		Map<String, String> params = new HashMap<String, String>();
		params.put("codeop", codeop);
		params.put("order_id", order_id);
		params.put("channelId", channel);
		params.put("openkey", openKey);
		logger.info(String.format("openCard params:codeop=%s,order_id=%s,channelId=%s,openKey%s",codeop, order_id,channel,openKey));
		String result = HttpClientUtil.post(serverURL+"card.do",params);
		logger.info(String.format("openCard resut=%s", result));
		if(result!=null && !result.trim().equals("")){
			 WftSaleCardRecord10011 obj =JSONObject.parseObject(result,WftSaleCardRecord10011.class);
			return obj;
		}
		return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public  CardBean balance(String cno,String pwd,String channel,String openKey){
		Map<String, String> params = new HashMap<String, String>();
		params.put("cardNo", cno);
		params.put("pwd", pwd);
		params.put("channelId", channel);
		params.put("openkey", openKey);
		String result = HttpClientUtil.post(serverURL+"balance.do",params);
		logger.info(String.format("balance resut=%s", result));
		if(result!=null){
			CardBean obj =(CardBean)JSONObject.parseObject(result,CardBean.class);
			if(obj!=null){
				return obj;
			}
		}
		return null;
	}
	
	public  WftSaleCardRecord10011 recharge(String codeop,String cno,String channel,String openKey){
		try {
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("codeop",codeop);
		params.put("cardNo",cno);
		params.put("channelId", channel);
		params.put("openkey", openKey);
		String result = HttpClientUtil.post(serverURL+"reCharge.do",params);
		logger.info(String.format("openCard resut=%s", result));
		if(result!=null){
			WftSaleCardRecord10011 bean =(WftSaleCardRecord10011)JSONObject.parseObject(result,WftSaleCardRecord10011.class);
			if(bean!=null){
				return bean;
			}
		}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	public String getServerURL() {
		return serverURL;
	}

	public void setServerURL(String serverURL) {
		this.serverURL = serverURL;
	}
}
