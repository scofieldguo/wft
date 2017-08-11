package com.bw30.open.cardpool.service.ctcc;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bw30.open.cardpool.service.CardPoolServiceImpl;
import com.bw30.open.wft.common.cardpool.DesDecrypter;
import com.bw30.open.wft.common.cardpool.RsaEncrypter;
import com.bw30.open.wft.common.cardpool.rmi.bean.CardBean;
import com.bw30.open.wft.common.cardpool.rmi.bean.CardPoolBean;
import com.bw30.open.wft.common.cardpool.rmi.bean.CardRecord;
import com.bw30.open.wft.common.cardpool.rmi.bean.OnlineBean;
import com.bw30.open.wft.common.cardpool.rmi.bean.RechargeBean;
import com.bw30.open.wft.common.cardpool.rmi.bean.RecordBean;
import com.bw30.open.wft.common.cardpool.rmi.bean.RespBean;
import com.bw30.wftMem.open.service.IMemcachService;

/**
 * 电信时长卡接口
 * 
 * @author Jack
 * @date 2014年7月15日 下午6:16:11
 */
public class CardCtccService {
	private static final Logger LOG = Logger.getLogger("CTCC");
	private static final SimpleDateFormat SDF_YMD_HMSS = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss.SSS");
	private static final String CHARSET = "UTF-8";
	
	private static final int CTCC_RESULT_OK = 200;
	private static final int CTCC_RESULT_INVALID_TOKEN = 7006;
	
	private static final BASE64Decoder base64Decoder = new BASE64Decoder();
	
	private String partner = null; //"BWTX";
	private String url = null; //"http://222.66.199.176:9280/wifiaaa/"; //"http://218.92.136.194:8080/wifiaaa/";
	
	private String URL_AUTHEN;
	private String URL_CREATECARD;
	private String URL_RECHARGE;
	private String URL_QUREY_CARD_LIFE;
	private String URL_UPDATE_CARD_PASSWORD;
	private String URL_GET_BALANCE;
	private String URL_QUERY_ONLINE;
	private String URL_QUEYR_OFFLINE;
	private String URL_QUERY_POOLINFO;
	
	@Resource
	private RsaEncrypter rsaEncrypterForCTCC;
	@Resource
	private DesDecrypter desDecrypterForCTCC;
	@Resource
	private IMemcachService memcachedService;
	
	public void setPartner(String partner) {
		this.partner = partner;
	}

	public void setUrl(String url) {
		this.url = url;
		
		URL_AUTHEN = this.url + "AuthenService";
		URL_CREATECARD = this.url + "CreateCardForThirdService";
		URL_RECHARGE = this.url + "RechargeCardForThirdService";
		URL_QUREY_CARD_LIFE = this.url + "QueryCardLifeForThirdService";
		URL_UPDATE_CARD_PASSWORD = this.url + "UpdateCardPasswordForThirdService";
		URL_GET_BALANCE = this.url + "GetBalanceForThirdService";
		URL_QUERY_ONLINE = this.url + "QueryOnlineForThirdService";
		URL_QUEYR_OFFLINE = this.url + "QueryOfflineForThirdService";
		URL_QUERY_POOLINFO = this.url + "NumberSpecForThirdService";
	}
	
	public void setRsaEncrypterForCTCC(RsaEncrypter rsaEncrypterForCTCC) {
		this.rsaEncrypterForCTCC = rsaEncrypterForCTCC;
	}

	public void setDesDecrypterForCTCC(DesDecrypter desDecrypterForCTCC) {
		this.desDecrypterForCTCC = desDecrypterForCTCC;
	}

	public void setMemcachedService(IMemcachService memcachedService) {
		this.memcachedService = memcachedService;
	}

	
	private static final String KEY_CTCC_TOKEN = "CTCC_TOKEN";
	/**
	 * 从memcached中取ctcc的token
	 * 
	 * @return
	 * @throws Exception
	 */
	private String getToken() throws Exception{
		String token=null;
		if(this.memcachedService.isExistKey(KEY_CTCC_TOKEN)){
		Object obj = this.memcachedService.getValue(KEY_CTCC_TOKEN);
			token =  (String)obj;
		}else{
			token = this.auth();
			this.memcachedService.setValue(KEY_CTCC_TOKEN, token);
		}
		
		return token;
	}
	
	/**
	 * 更新memcached中ctcc的token
	 * 
	 * @throws Exception
	 */
	private void updateToken() throws Exception{
		String token = this.auth();
		this.memcachedService.setValue(KEY_CTCC_TOKEN, token);
	}

	/**
	 * 鉴权接口，获取有效Token，24小时有效
	 * 
	 */
	private String auth() throws Exception {
		JSONObject json = new JSONObject();
		json.put("partner", partner);
		json.put("timeStamp", SDF_YMD_HMSS.format(new Date()));
		
		String resStr = callApi(URL_AUTHEN, json.toJSONString());
		if(null == resStr){
			return null;
		}
		try{
			json = JSON.parseObject(resStr);
		}catch(Exception e){
			LOG.error("parse auth json error:" + resStr, e);
			throw new Exception("parse auth json error:" + resStr, e);
		}
		
		return json.getString("token");
	}

	/**
	 * 开卡
	 * 
	 * @param phoneNumber
	 * @param cardType
	 * @param orderID
	 * @throws Exception
	 */
	public CardBean createCard(String phoneNumber,
			int cardType, String orderID) throws Exception {
		JSONObject json = new JSONObject();
		json.put("partner", partner);
		json.put("token", this.getToken());
		json.put("timestamp", SDF_YMD_HMSS.format(new Date()));
		json.put("phoneNumber", phoneNumber);
		json.put("cardType", cardType);
		json.put("orderID", orderID);
		json.put("standby", "");
		
		CardBean cb = new CardBean();
		cb.setResult(RespBean.RESULT_OK);
		String resStr = callApi(URL_CREATECARD, json.toJSONString());
		if(null == resStr){
			cb.setResult(RespBean.RESULT_FAIL);
			return cb;
		}
		try{
			json = JSON.parseObject(resStr);
		}catch(Exception e){
			LOG.error("parse createCard json error:" + resStr, e);
			throw new Exception("parse createCard json error:" + resStr, e);
		}
		Integer result = json.getInteger("result");
		switch(result){
		case CTCC_RESULT_OK:
			cb.setNo(json.getString("cardNumber"));
			cb.setPwd(json.getString("password"));
			cb.setCardType(cardType);
			cb.setBalance(json.getIntValue("balance"));
			cb.setValidity(json.getDate("validity"));
			cb.setPrvId(15);//电信时长卡，省份为：上海
			return cb;
		case CTCC_RESULT_INVALID_TOKEN:
			this.updateToken();
			return this.createCard(phoneNumber, cardType, CardPoolServiceImpl.genOrderId(CardPoolServiceImpl.PREFIX_ORDER_OPEN));
		default:
			cb.setResult("" + result);
			return cb;
		}
	}

	/**
	 * 充值接口
	 * 
	 * @param phoneNumber
	 * @param cardNumber
	 * @param cardType
	 * @param orderID
	 * @throws Exception
	 */
	public RechargeBean recharge(String phoneNumber,
			String cardNumber, int cardType, String orderID) throws Exception {
		JSONObject json = new JSONObject();
		json.put("partner", partner);
		json.put("token", this.getToken());
		json.put("timestamp", SDF_YMD_HMSS.format(new Date()));
		json.put("phoneNumber", phoneNumber);
		json.put("cardNumber", cardNumber);
		json.put("cardType", cardType);
		json.put("orderID", orderID);
		json.put("standby", "");

		RechargeBean rb = new RechargeBean();
		rb.setResult(RespBean.RESULT_OK);
		String resStr = callApi(URL_RECHARGE, json.toJSONString());
		if(null == resStr){
			rb.setResult(RespBean.RESULT_FAIL);
			return rb;
		}
		try{
			json = JSON.parseObject(resStr);
		}catch(Exception e){
			LOG.error("parse recharge json error:" + resStr, e);
			throw new Exception("parse recharge json error:" + resStr, e);
		}
		Integer result = json.getInteger("result");
		switch(result){
		case CTCC_RESULT_OK:
			rb.setCno(cardNumber);
			rb.setBalance(json.getIntValue("balance"));
			rb.setOldbalance(json.getIntValue("oldbalance"));
			rb.setNewbalance(json.getIntValue("nowbalance"));
			rb.setValidity(json.getDate("validity"));
			
			return rb;
		case CTCC_RESULT_INVALID_TOKEN:
			this.updateToken();
			return this.recharge(phoneNumber, cardNumber, cardType, CardPoolServiceImpl.genOrderId(CardPoolServiceImpl.PREFIX_ORDER_RECHARGE));
		default:
			rb.setResult("" + result);
			return rb;
		}
	}

	/**
	 * 卡有效期查询
	 * 
	 * @param phoneNumber
	 * @param cardNumber
	 * @param pwd
	 * @param orderId
	 * @throws Exception
	 */
	public void queryCardLife(String phoneNumber,
			String cardNumber, String pwd, String orderID) throws Exception {
		JSONObject json = new JSONObject();
		json.put("partner", partner);
		json.put("token", this.getToken());
		json.put("timestamp", SDF_YMD_HMSS.format(new Date()));
		json.put("phoneNumber", phoneNumber);
		json.put("cardNumber", cardNumber);
		json.put("password", pwd);
		json.put("orderID", orderID);
		json.put("standby", "");
		
		String resStr = callApi(URL_QUREY_CARD_LIFE, json.toJSONString());
		if(null == resStr){
			return;
		}
		try{
			json = JSON.parseObject(resStr);
		}catch(Exception e){
			LOG.error("parse queryCardLift json error:" + resStr, e);
			throw new Exception("parse queryCardLife json error:" + resStr, e);
		}
		Integer result = json.getInteger("result");
		switch(result){
		case CTCC_RESULT_OK:
			break;
		case CTCC_RESULT_INVALID_TOKEN:
			this.updateToken();
			this.queryCardLife(phoneNumber, cardNumber, pwd, orderID);
			break;
		}
		
	}

	/**
	 * 修改卡密码
	 * 
	 * @param phoneNumber
	 * @param cardNumber
	 * @param oldPwd
	 * @param newPwd
	 * @param orderId
	 * @throws Exception
	 */
	public boolean updateCardPassword(String phoneNumber,
			String cardNumber, String oldPwd, String newPwd, String orderID)
			throws Exception {
		JSONObject json = new JSONObject();
		json.put("partner", partner);
		json.put("token", this.getToken());
		json.put("timestamp", SDF_YMD_HMSS.format(new Date()));
		json.put("phoneNumber", phoneNumber);
		json.put("cardNumber", cardNumber);
		json.put("orderID", orderID);
		json.put("password", oldPwd);
		json.put("newpassword", newPwd);
		json.put("standby", "");

		String resStr = callApi(URL_UPDATE_CARD_PASSWORD, json.toJSONString());
		if(null == resStr){
			return false;
		}
		try{
			json = JSON.parseObject(resStr);
		}catch(Exception e){
			LOG.error("parse updatePwd json error:" + resStr, e);
			throw new Exception("parse updatePwd json error:" + resStr, e);
		}
		Integer result = json.getInteger("result");
		switch(result){
		case CTCC_RESULT_OK:
			return true;
		case CTCC_RESULT_INVALID_TOKEN:
			this.updateToken();
			return this.updateCardPassword(phoneNumber, cardNumber, oldPwd, newPwd, CardPoolServiceImpl.genOrderId(CardPoolServiceImpl.PREFIX_ORDER_PWD));
		default:
			return false;
		}
	}

	/**
	 * 获取卡的剩余时长
	 * 
	 * @param cardNumber
	 * @param pwd
	 * @throws Exception
	 */
	public String getBalance(String cardNumber, String pwd,String orderID)
			throws Exception {
		JSONObject json = new JSONObject();
		json.put("partner", partner);
		json.put("token", this.getToken());
		json.put("timestamp", SDF_YMD_HMSS.format(new Date()));
		json.put("cardNumber", cardNumber);
		json.put("orderID", orderID);
		json.put("password", pwd);
		json.put("standby", "");

		String resStr = callApi(URL_GET_BALANCE, json.toJSONString());
		if(null == resStr){
			return null;
		}
		try{
			json = JSON.parseObject(resStr);
		}catch(Exception e){
			LOG.error("parse getBalance json error:" + resStr, e);
			throw new Exception("parse getBalance json error:" + resStr, e);
		}
		Integer result = json.getInteger("result");
		switch(result){
		case CTCC_RESULT_OK:
			return resStr;
		case CTCC_RESULT_INVALID_TOKEN:
			this.updateToken();
			return this.getBalance(cardNumber, pwd,CardPoolServiceImpl.genOrderId(CardPoolServiceImpl.PREFIX_ORDER_BALANCE));
		default:
			return null;
		}
	}

	/**
	 * 在线查询，
	 * 返回null表示不在线
	 * 
	 * @param cardNumber
	 * @param pwd
	 * @param orderID
	 * @throws Exception
	 */
	public OnlineBean queryOnline(String cardNumber, String pwd,
			String orderID) throws Exception {
		JSONObject json = new JSONObject();
		json.put("partner", partner);
		json.put("token", this.getToken());
		json.put("timestamp", SDF_YMD_HMSS.format(new Date()));
		json.put("cardNumber", cardNumber);
		json.put("password", pwd);
		json.put("orderID", orderID);
		json.put("standby", "");

		OnlineBean ob = new OnlineBean();
		ob.setResult(RespBean.RESULT_OK);
		String resStr = callApi(URL_QUERY_ONLINE, json.toJSONString());
		if(null == resStr){
			throw new Exception("queryOnline error: no data response");
//			ob.setResult(RespBean.RESULT_FAIL);
//			return ob;
		}
		
		try{
			json = JSON.parseObject(resStr);
		}catch(Exception e){
			LOG.error("parse queryOnline json error:" + resStr, e);
			throw new Exception("parse queryOnline json error:" + resStr, e);
		}
		Integer result = json.getInteger("result");
		switch(result){
		case CTCC_RESULT_OK:
			ob.setRealm(json.getString("realm"));
			ob.setStarttime(json.getString("starttime"));
			ob.setBindattr(json.getString("bindattr"));
			ob.setMack(json.getString("mack"));
			ob.setNasip(json.getString("nasip"));
			ob.setNasport(json.getString("nasport"));
			ob.setNasporttype(json.getString("nasporttype"));
			ob.setSessionid(json.getString("sessionid"));
			ob.setFramedip(json.getString("framedip"));
			ob.setRoamflag(json.getString("roamflag"));
			return ob;
		case CTCC_RESULT_INVALID_TOKEN:
			this.updateToken();
			return this.queryOnline(cardNumber, pwd, CardPoolServiceImpl.genOrderId(CardPoolServiceImpl.PREFIX_ORDER_ONLINE));
		default:
			ob.setResult("" + result);
			return ob;
		}
	}

	/**
	 * 清单查询
	 * 
	 * @param cardNumber
	 * @param pwd
	 * @param startDate 开始时间，yyyy-MM-dd
	 * @param endDate 结束时间，yyyy-MM-dd
	 * @param orderID
	 * @throws Exception
	 */
	public RecordBean queryOffline(String cardNumber,
			String pwd, String startDate, String endDate, String orderID)
			throws Exception {
		JSONObject json = new JSONObject();
		json.put("partner", partner);
		json.put("token", this.getToken());
		json.put("timestamp", SDF_YMD_HMSS.format(new Date()));
		json.put("cardNumber", cardNumber);
		json.put("password", pwd);
		json.put("orderID", orderID);
		json.put("startdate", startDate);
		json.put("enddate", endDate);
		json.put("standby", "");

		RecordBean rb = new RecordBean();
		rb.setCno(cardNumber);
		rb.setResult(RespBean.RESULT_OK);
		
		String resStr = callApi(URL_QUEYR_OFFLINE, json.toJSONString());
		if(null == resStr){
			rb.setResult(RespBean.RESULT_FAIL);
			return rb;
		}
		try{
			json = JSON.parseObject(resStr);
		}catch(Exception e){
			LOG.error("parse queryOffline json error:" + resStr, e);
			throw new Exception("parse queryOffline json error:" + resStr, e);
		}
		Integer result = json.getInteger("result");
		switch(result){
		case CTCC_RESULT_OK:
			String details = json.getString("details");
			if(null != details && 0 < details.trim().length()){
				JSONArray array = JSON.parseArray(details);
				List<CardRecord> rList = new ArrayList<CardRecord>(array.size());
				for(Object obj : array){
					json = (JSONObject) obj;
					CardRecord r = new CardRecord();
					r.setCno(cardNumber);
					r.setStarttime(json.getString("starttime"));
					r.setEndtime(json.getString("stoptime"));
					r.setTimelen(json.getIntValue("timelen"));
					r.setMack(json.getString("mack"));
					r.setNasip(json.getString("nasip"));
					r.setNasport(json.getString("nasport"));
					r.setNasporttype(json.getString("nasporttype"));
					r.setSessionid(json.getString("sessionid"));
					r.setFramedip(json.getString("framedip"));
					rList.add(r);
				}
				rb.setRecordList(rList);
			}
			return rb;
		case CTCC_RESULT_INVALID_TOKEN:
			this.updateToken();
			return this.queryOffline(cardNumber, pwd, startDate, endDate, CardPoolServiceImpl.genOrderId(CardPoolServiceImpl.PREFIX_ORDER_RECORD));
		default:
			rb.setResult("" + result);
			return rb;
		}
	}
	
	public CardPoolBean queryPoolInfo(String orderId) throws Exception{
		JSONObject json = new JSONObject();
		json.put("partner", this.partner);
		json.put("token", this.getToken());
		json.put("timestamp", SDF_YMD_HMSS.format(new Date()));
		json.put("orderID", orderId);
		json.put("standby", "");

		CardPoolBean cpb = new CardPoolBean();
		cpb.setResult(RespBean.RESULT_OK);
		
		String resStr = callApi(URL_QUERY_POOLINFO, json.toJSONString());
		if(null == resStr){
			cpb.setResult(RespBean.RESULT_FAIL);
			return cpb;
		}
		try{
			json = JSON.parseObject(resStr);
		}catch(Exception e){
			LOG.error("parse queryPoolInfo json error:" + resStr, e);
			throw new Exception("parse queryPoolInfo json error:" + resStr, e);
		}
		Integer result = json.getInteger("result");
		switch(result){
		case CTCC_RESULT_OK:
			Long balance = Long.parseLong(json.getString("balance"));
			Long consume = Long.parseLong(json.getString("rechargebalance"));
			cpb.setTotal(balance);
			cpb.setConsume(consume);
			cpb.setBalance(balance - consume);
			cpb.setNum(json.getIntValue("newcardcount"));
			return cpb;
		case CTCC_RESULT_INVALID_TOKEN:
			this.updateToken();
			return this.queryPoolInfo(CardPoolServiceImpl.genOrderId(CardPoolServiceImpl.PREFIX_ORDER_INFO));
		default:
			cpb.setResult("" + result);
			return cpb;
		}
	}

	/**
	 * 生成流水订单号
	 * 仅用于测试接口
	 * @return
	 */
//	private String genOrder() {
//		return new StringBuffer("BW").append(
//				new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()))
//				.append(new Random().nextInt(100))
//				.toString();
//	}

	/**
	 * 调用电信时长卡api接口
	 * @param url
	 * @param jsonStr
	 * @return
	 * @throws Exception
	 */
	private String callApi(String url, String jsonStr) throws Exception {
		LOG.info("req to [" + url + "] :" + jsonStr);
		System.out.println("req to [" + url + "] :" + jsonStr);
		// RSA加密
		byte[] rsaString = null;
		try{
			rsaString = this.rsaEncrypterForCTCC.encrypt(jsonStr.getBytes(CHARSET));
		}catch(Exception e){
			LOG.error("rsa encrypt for ctcc error", e);
			return null;
		}
		ByteArrayEntity body = new ByteArrayEntity(rsaString);
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		try{
			post.setEntity(body);
			HttpResponse res = client.execute(post);
			LOG.info("[" + url + "] resp http code :"
				+ res.getStatusLine().getStatusCode());
			System.out.println("[" + url + "] resp code :"
				+ res.getStatusLine().getStatusCode());
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				InputStream response = res.getEntity().getContent();
				ByteArrayOutputStream bais = new ByteArrayOutputStream();
			int len = 0;
			byte[] buffer = new byte[4096];
			while ((len = response.read(buffer)) > 0) {
				bais.write(buffer, 0, len);
			}
			byte[] data = bais.toByteArray();
			String ret = new String(data, CHARSET);
			LOG.info("[" + url + "] resp :" + ret);
			System.out.println("[" + url + "] resp :" + ret);
			response.close();
			if(null == ret || 0 == ret.length()){
				return null;
			}
			// DES解密
			try{
				ret = new String(this.desDecrypterForCTCC.decrypt(base64Decoder.decodeBuffer(ret)), CHARSET);
			}catch(Exception e){
				LOG.error("des decrypt error:" + ret, e);
				return null;
			}
			
			LOG.info("[" + url + "] resp decrypt :" + ret);
			System.out.println("[" + url + "] resp decrypt :" + ret);
			return ret;
		}
		}catch (Exception e) {
			LOG.error("call Api error:", e);
		}finally{
			post.abort();
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		CardCtccService ccs = new CardCtccService();
		ccs.setPartner("BWTX");
		
		RsaEncrypter re = new RsaEncrypter();
		re.setPublicKeyPath("D:/public.key");;
		re.initPublicKey();
		ccs.setRsaEncrypterForCTCC(re);
		
		DesDecrypter dd = new DesDecrypter();
		dd.setKey("6d35a27781e245d0ada1462a");
		dd.init();
		ccs.setDesDecrypterForCTCC(dd);
		
		ccs.setUrl("http://222.66.199.176:9280/wifiaaa/");
//		String authResult = auth();
//		JSONObject authRes = JSONObject.parseObject(authResult);
//		String token = (String) authRes.get("token");
//		System.out.println("token:" + token);
		
//		ccs.createCard(null, 20, ccs.genOrder());
//		
//		ccs.recharge(null, "W50100000009", 20, ccs.genOrder());
//		
//		ccs.queryCardLife(null, "W50100000009", "18159466", ccs.genOrder());
//		
//		ccs.updateCardPassword(null, "W50100000009", "18159467", "18159466", ccs.genOrder());
//		
//		ccs.getBalance("W50100000009", "18159466");
		
//		ccs.queryOnline("W50100000009", "18159466", ccs.genOrder());

		ccs.queryOffline("W50100000094", "65382595", "2014-06-15", "2014-07-23", CardPoolServiceImpl.genOrderId(CardPoolServiceImpl.PREFIX_ORDER_INFO));
		
//		String str = "{\"cardNumber\":\"W50100001424\",\"orderID\":\"2014082510200205565\",\"partner\":\"BWTX\",\"password\":\"17957774\",\"standby\":\"\",\"timestamp\":\"2014-08-2510:20:02.056\",\"token\":\"5406b5633422\"}";
//		for(int i = 0; i < 100; i++){
//			System.out.println(i + ": " + str.getBytes(CHARSET).length);
//			re.encrypt(str.getBytes(CHARSET));
//		}
//		System.out.println("SUCCESS");
		
	}
	
}
