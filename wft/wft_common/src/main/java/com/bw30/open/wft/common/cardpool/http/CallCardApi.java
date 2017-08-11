package com.bw30.open.wft.common.cardpool.http;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.alibaba.fastjson.JSON;
import com.bw30.open.wft.common.cardpool.AesDecrypter;
import com.bw30.open.wft.common.cardpool.RsaEncrypter;
import com.bw30.open.wft.common.cardpool.proto.Req;
import com.bw30.open.wft.common.cardpool.proto.req.OpenCardReq;
import com.bw30.open.wft.common.cardpool.proto.req.QueryBalanceReq;
import com.bw30.open.wft.common.cardpool.proto.req.QueryOnlineReq;
import com.bw30.open.wft.common.cardpool.proto.req.QueryRecordReq;
import com.bw30.open.wft.common.cardpool.proto.req.QueryValidityReq;
import com.bw30.open.wft.common.cardpool.proto.req.RechargeReq;
import com.bw30.open.wft.common.cardpool.proto.req.UpdatePwdReq;
import com.bw30.open.wft.common.cardpool.proto.resp.OpenCardResp;
import com.bw30.open.wft.common.cardpool.proto.resp.QueryBalanceResp;
import com.bw30.open.wft.common.cardpool.proto.resp.QueryOnlineResp;
import com.bw30.open.wft.common.cardpool.proto.resp.QueryRecordResp;
import com.bw30.open.wft.common.cardpool.proto.resp.QueryValidityResp;
import com.bw30.open.wft.common.cardpool.proto.resp.RechargeResp;
import com.bw30.open.wft.common.cardpool.proto.resp.UpdatePwdResp;

/**
 * 卡池接口调用
 * 
 * @author Jack
 * @date 2014年7月21日 下午4:01:50
 */
public class CallCardApi {
	private static final SimpleDateFormat SDF_YMD_HMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private String URL_OPENCARD;//开卡接口地址
	private String URL_RECHARGE;//充值接口
	private String URL_QUREY_VALIDITY;//有效期查询接口
	private String URL_UPDATE_PWD;//修改密码接口
	private String URL_QUERY_BALANCE;//剩余时长查询接口
	private String URL_QUERY_ONLINE;//在线查询接口
	private String URL_QUEYR_RECORD;//卡使用记录查询接口
	
	private RsaEncrypter rsaEncrypter;
	private AesDecrypter aesDecrypter;
	
	/**
	 * 
	 * @param url 接口请求地址
	 * @param rsaPublicKeyPath rsa公钥路径
	 * @param aesKey aes密钥
	 * 
	 * @throws Exception
	 */
	public CallCardApi(String url, String rsaPublicKeyPath, String aesKey) throws Exception{
		url = url.endsWith("/") ? url : url + "/";
		this.URL_OPENCARD = url + "openCard.do";
		this.URL_RECHARGE = url + "recharge.do";
		this.URL_QUREY_VALIDITY = url + "queryValidity.do";
		this.URL_UPDATE_PWD = url + "updatePwd.do";
		this.URL_QUERY_BALANCE = url + "queryBalance.do";
		this.URL_QUERY_ONLINE = url + "queryOnline.do";
		this.URL_QUEYR_RECORD = url + "queryRecord.do";
		
		this.rsaEncrypter = new RsaEncrypter();
		this.rsaEncrypter.setPublicKeyPath(rsaPublicKeyPath);
		this.rsaEncrypter.initPublicKey();
		
		this.aesDecrypter = new AesDecrypter();
		this.aesDecrypter.setKey(aesKey);
		this.aesDecrypter.init();
	}

	/**
	 * 开卡接口
	 * 
	 * @param url
	 * @param partner
	 * @param op
	 * @param ctype
	 * @param orderid
	 * @param standby
	 * @return
	 * @throws Exception
	 */
	public OpenCardResp callOpenCard(String partner, Integer op, String ctype, String orderid, String standby) throws Exception {
		OpenCardReq ocr = new OpenCardReq();
		ocr.setPartner(partner);
		ocr.setOp(op);
		ocr.setCtype(ctype);
		ocr.setOrderid(orderid);
		ocr.setTimestamp(SDF_YMD_HMS.format(new Date()));
		ocr.setStandby(standby);
		
		return this.callApi(ocr, this.URL_OPENCARD, OpenCardResp.class);
	}
	
	/**
	 * 充值接口
	 * 
	 * @param url
	 * @param partner
	 * @param op
	 * @param cno
	 * @param ctype
	 * @param orderid
	 * @param standby
	 * @return
	 * @throws Exception
	 */
	public RechargeResp callRecharge(String partner, Integer op, String cno, String ctype, String orderid, String standby) throws Exception{
		RechargeReq rr = new RechargeReq();
		rr.setPartner(partner);
		rr.setOp(op);
		rr.setCno(cno);
		rr.setCtype(ctype);
		rr.setOrderid(orderid);
		rr.setStandby(standby);
		
		return this.callApi(rr, this.URL_RECHARGE, RechargeResp.class);
	}
	
	/**
	 * 有效期查询接口
	 * 
	 * @param url
	 * @param partner
	 * @param op
	 * @param cno
	 * @param pwd
	 * @param standby
	 * @return
	 * @throws Exception
	 */
	public QueryValidityResp callQueryValidity(String partner, Integer op, String cno, String pwd, String standby) throws Exception{
		QueryValidityReq qvr = new QueryValidityReq();
		qvr.setPartner(partner);
		qvr.setOp(op);
		qvr.setCno(cno);
		qvr.setPwd(pwd);
		qvr.setStandby(standby);
		
		return this.callApi(qvr, this.URL_QUREY_VALIDITY, QueryValidityResp.class);
	}
	
	/**
	 * 修改密码接口
	 * @param url
	 * @param partner
	 * @param op
	 * @param cno
	 * @param pwd
	 * @param newpwd
	 * @param standby
	 * @return
	 * @throws Exception
	 */
	public UpdatePwdResp callUpdatePwd(String partner, Integer op, String cno, String pwd, String newpwd, String standby) throws Exception{
		UpdatePwdReq upr = new UpdatePwdReq();
		upr.setPartner(partner);
		upr.setOp(op);
		upr.setCno(cno);
		upr.setPwd(pwd);
		upr.setNewpwd(newpwd);
		upr.setStandby(standby);
		
		return this.callApi(upr, this.URL_UPDATE_PWD, UpdatePwdResp.class);
	}
	
	/**
	 * 剩余时长查询接口
	 * @param url
	 * @param partner
	 * @param op
	 * @param cno
	 * @param pwd
	 * @param standby
	 * @return
	 * @throws Exception
	 */
	public QueryBalanceResp callQueryBalance(String partner, Integer op, String cno, String pwd, String standby) throws Exception{
		QueryBalanceReq qbr = new QueryBalanceReq();
		qbr.setPartner(partner);
		qbr.setOp(op);
		qbr.setCno(cno);
		qbr.setPwd(pwd);
		qbr.setStandby(standby);
		
		return this.callApi(qbr, this.URL_QUERY_BALANCE, QueryBalanceResp.class);
	}
	
	/**
	 * 在线查询接口
	 * 
	 * @param url
	 * @param partner
	 * @param op
	 * @param cno
	 * @param pwd
	 * @param standby
	 * @return
	 * @throws Exception
	 */
	public QueryOnlineResp callQueryOnline(String partner, Integer op, String cno, String pwd, String standby) throws Exception{
		QueryOnlineReq qor = new QueryOnlineReq();
		qor.setPartner(partner);
		qor.setOp(op);
		qor.setCno(cno);
		qor.setPwd(pwd);
		qor.setStandby(standby);
		
		return this.callApi(qor, this.URL_QUERY_ONLINE, QueryOnlineResp.class);
	}
	
	/**
	 * 卡使用记录查询接口
	 * 
	 * @param url
	 * @param partner
	 * @param op
	 * @param cno
	 * @param pwd
	 * @param startDate
	 * @param endDate
	 * @param standby
	 * @return
	 * @throws Exception
	 */
	public QueryRecordResp callQueryRecord(String partner, Integer op, String cno, String pwd, String startDate, String endDate, String standby) throws Exception{
		QueryRecordReq qrr = new QueryRecordReq();
		qrr.setPartner(partner);
		qrr.setOp(op);
		qrr.setCno(cno);
		qrr.setPwd(pwd);
		qrr.setStartdate(startDate);
		qrr.setEnddate(endDate);
		qrr.setStandby(standby);
		
		return this.callApi(qrr, this.URL_QUEYR_RECORD, QueryRecordResp.class);
	}
	
	/**
	 * 调用接口
	 * 
	 * @param req
	 * @param url
	 * @return
	 * @throws Exception
	 */
	private <T> T callApi(Req req, String url, Class<T> clz) throws Exception{
		String jsonStr = JSON.toJSONString(req);
		System.out.println("[" + url + "] req data:" + jsonStr);
		byte[] reqData = rsaEncrypter.encrypt(jsonStr.getBytes("UTF-8"));
		System.out.println("req data size:" + reqData.length);
		
		ByteArrayEntity body = new ByteArrayEntity(reqData);
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		post.setEntity(body);
		HttpResponse res = client.execute(post);
		System.out.println("[" + url + "] resp http code :"
				+ res.getStatusLine().getStatusCode());
		if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			InputStream response = res.getEntity().getContent();
			ByteArrayOutputStream bais = new ByteArrayOutputStream();
			int len = 0;
			byte[] buffer = new byte[4096];
			while ((len = response.read(buffer)) > 0) {
				bais.write(buffer, 0, len);
			}
			byte[] respData = bais.toByteArray();
			response.close();
			bais.close();
			System.out.println("[" + url + "]" + respData.length);
			
			respData = aesDecrypter.decrypt(respData);
			String respStr = new String(respData, "UTF-8");
			System.out.println("[" + url + "] resp :" + respStr);
			if(null == respStr || respStr.length() == 0){
				return null;
			}
			return JSON.parseObject(respStr, clz);
		}
		
		return null;
	}
	
}
