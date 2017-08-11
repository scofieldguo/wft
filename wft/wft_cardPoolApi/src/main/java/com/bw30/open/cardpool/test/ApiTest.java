package com.bw30.open.cardpool.test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.bw30.open.common.dao.mapper.WftChannelMapper;
import com.bw30.open.common.model.WftChannel;
import com.bw30.open.wft.common.cardpool.AesDecrypter;
import com.bw30.open.wft.common.cardpool.RsaEncrypter;
import com.bw30.open.wft.common.cardpool.proto.Req;
import com.bw30.open.wft.common.cardpool.proto.req.OpenCardReq;
import com.bw30.open.wft.common.cardpool.proto.req.QueryBalanceReq;
import com.bw30.open.wft.common.cardpool.proto.req.QueryOnlineReq;
import com.bw30.open.wft.common.cardpool.proto.req.QueryRecordReq;
import com.bw30.open.wft.common.cardpool.proto.req.QueryUserOnlineReq;
import com.bw30.open.wft.common.cardpool.proto.req.QueryUserRecordReq;
import com.bw30.open.wft.common.cardpool.proto.req.QueryValidityReq;
import com.bw30.open.wft.common.cardpool.proto.req.RechargeReq;
import com.bw30.open.wft.common.cardpool.proto.req.UpdatePwdReq;

/**
 * 接口测试
 * 
 * @author Jack
 * @date 2014年7月21日 下午4:01:50
 */
public class ApiTest {
	private static final Logger LOG = Logger.getLogger(ApiTest.class);
	
	private static final SimpleDateFormat SDF_YMD_HMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static RsaEncrypter rsaEncrypter;
	static{
		try{
			String path = ApiTest.class.getClassLoader().getResource("config.properties").getPath().replace("%20", " ");
			PropertiesConfiguration config = new PropertiesConfiguration();
			config.setEncoding("UTF-8");
			config.setFileName(path);  
			config.load(); 
			
			rsaEncrypter = new RsaEncrypter();
			rsaEncrypter.setPublicKeyPath(String.valueOf(config.getProperty("rsa.publickey.path")));
			rsaEncrypter.initPublicKey();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	@Resource
	private static WftChannelMapper wftChannelMapper;
	
	public void setWftChannelMapper(WftChannelMapper wftChannelMapper) {
		ApiTest.wftChannelMapper = wftChannelMapper;
	}
	
	public ApiTest(){
		
	}
	

	/**
	 * 测试开卡接口
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
	public String testOpenCard(String url, String partner, String op, String ctype, String orderid, String standby) throws Exception {
		LOG.info("Testing openCard ...");
		OpenCardReq ocr = new OpenCardReq();
		ocr.setPartner(partner);
		ocr.setOp(Integer.parseInt(op));
		ocr.setCtype(ctype);
		ocr.setOrderid(orderid);
		ocr.setTimestamp(SDF_YMD_HMS.format(new Date()));
		ocr.setStandby(standby);
		
		String result = this.callApi(ocr, url, partner);

		return result;
	}
	
	/**
	 * 测试充值接口
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
	public String testRecharge(String url, String partner, String op, String cno, String ctype, String orderid, String standby) throws Exception{
		LOG.info("Testing recharge ...");
		RechargeReq rr = new RechargeReq();
		rr.setPartner(partner);
		rr.setOp(Integer.parseInt(op));
		rr.setCno(cno);
		rr.setCtype(ctype);
		rr.setOrderid(orderid);
		rr.setStandby(standby);
		
		String result = this.callApi(rr, url, partner);
		return result;
	}
	
	/**
	 * 测试有效期查询接口
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
	public String testQueryValidity(String url, String partner, String op, String cno, String pwd, String standby) throws Exception{
		LOG.info("Testing queryValidity ...");
		QueryValidityReq qvr = new QueryValidityReq();
		qvr.setPartner(partner);
		qvr.setOp(Integer.parseInt(op));
		qvr.setCno(cno);
		qvr.setPwd(pwd);
		qvr.setStandby(standby);
		
		String result = this.callApi(qvr, url, partner);
		return result;
	}
	
	/**
	 * 测试修改密码接口
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
	public String testUpdatePwd(String url, String partner, String op, String cno, String pwd, String newpwd, String standby) throws Exception{
		LOG.info("Testing updatePwd ...");
		UpdatePwdReq upr = new UpdatePwdReq();
		upr.setPartner(partner);
		upr.setOp(Integer.parseInt(op));
		upr.setCno(cno);
		upr.setPwd(pwd);
		upr.setNewpwd(newpwd);
		upr.setStandby(standby);
		
		String result = this.callApi(upr, url, partner);
		return result;
	}
	
	/**
	 * 测试剩余时长查询接口
	 * @param url
	 * @param partner
	 * @param op
	 * @param cno
	 * @param pwd
	 * @param standby
	 * @return
	 * @throws Exception
	 */
	public String testQueryBalance(String url, String partner, String op, String cno, String pwd, String standby) throws Exception{
		LOG.info("Testing queryBalance ...");
		QueryBalanceReq qbr = new QueryBalanceReq();
		qbr.setPartner(partner);
		qbr.setOp(Integer.parseInt(op));
		qbr.setCno(cno);
		qbr.setPwd(pwd);
		qbr.setStandby(standby);
		
		String result = this.callApi(qbr, url, partner);
		return result;
	}
	
	/**
	 * 测试在线查询接口
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
	public String testQueryOnline(String url, String partner, String op, String cno, String pwd, String ip, String standby) throws Exception{
		LOG.info("Testing queryOnline ...");
		QueryOnlineReq qor = new QueryOnlineReq();
		qor.setPartner(partner);
		qor.setOp(Integer.parseInt(op));
		qor.setCno(cno);
		qor.setPwd(pwd);
		qor.setIp(ip);
		qor.setStandby(standby);
		
		String result = this.callApi(qor, url, partner);
		return result;
	}
	
	/**
	 * 测试卡使用记录查询接口
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
	public String testQueryRecord(String url, String partner, String op, String cno, String pwd, String startDate, String endDate, String standby) throws Exception{
		LOG.info("Testing queryRecord ...");
		QueryRecordReq qrr = new QueryRecordReq();
		qrr.setPartner(partner);
		qrr.setOp(Integer.parseInt(op));
		qrr.setCno(cno);
		qrr.setPwd(pwd);
		qrr.setStartdate(startDate);
		qrr.setEnddate(endDate);
		qrr.setStandby(standby);
		
		String result = this.callApi(qrr, url, partner);
		return result;
	}
	
	
	/**
	 * 测试用户在线状态查询接口
	 * 
	 * @param url
	 * @param partner
	 * @param openid
	 * @param standby
	 * @return
	 * @throws Exception
	 */
	public String testQueryUserOnline(String url, String partner, String openid, String standby) throws Exception{
		LOG.info("Testing queryUserOnline...");
		QueryUserOnlineReq quor = new QueryUserOnlineReq();
		quor.setPartner(partner);
		quor.setOpenid(openid);
		quor.setStandby(standby);
		
		String result = this.callApi(quor, url, partner);
		return result;
	}
	
	/**
	 * 测试用户使用记录查询接口
	 * 
	 * @param url
	 * @param partner
	 * @param openid
	 * @param startdate
	 * @param enddate
	 * @param standby
	 * @return
	 * @throws Exception
	 */
	public String testQueryUserRecord(String url, String partner, String openid, String startdate, String enddate, String standby) throws Exception{
		LOG.info("Testing queryUserRecord...");
		QueryUserRecordReq qurr = new QueryUserRecordReq();
		qurr.setPartner(partner);
		qurr.setOpenid(openid);
		qurr.setStartdate(startdate);
		qurr.setEnddate(enddate);
		qurr.setStandby(standby);
		
		String result = this.callApi(qurr, url, partner);
		return result;
	}
	
	
	/**
	 * 调用接口
	 * 
	 * @param req
	 * @param url
	 * @return
	 * @throws Exception
	 */
	private String callApi(Req req, String url, String partner) throws Exception{
		String jsonStr = JSON.toJSONString(req);
		LOG.info("[" + url + "] req data:" + jsonStr);
		byte[] reqData = null;
		if(url.contains("queryUserOnline") || url.contains("queryUserRecord")){
			reqData = jsonStr.getBytes("UTF-8");
		}else{//加密
			reqData = rsaEncrypter.encrypt(jsonStr.getBytes("UTF-8"));
		}
		System.out.println("req data size:" + reqData.length);
		
		ByteArrayEntity body = new ByteArrayEntity(reqData);
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		post.setEntity(body);
		HttpResponse res = client.execute(post);
		LOG.info("[" + url + "] resp http code :"
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
			if(null == respData || 0 == respData.length){
				return null;
			}
			
			WftChannel channel = wftChannelMapper.findById(partner);
			AesDecrypter aesDecrypter = new AesDecrypter();
			aesDecrypter.setKey(channel.getOpenkey());
			aesDecrypter.init();
			
			if(!url.contains("queryUserOnline") && !url.contains("queryUserRecord")){//解密
				respData = aesDecrypter.decrypt(respData);
			}
			
			String respStr = new String(respData, "UTF-8");
			LOG.info("[" + url + "] resp :" + respStr);
			return respStr;
		}
		
		return null;
	}
	
}
