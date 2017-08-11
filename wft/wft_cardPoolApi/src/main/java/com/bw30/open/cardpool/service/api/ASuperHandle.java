package com.bw30.open.cardpool.service.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.bw30.open.common.dao.mapper.WftChannelMapper;
import com.bw30.open.common.model.WftChannel;
import com.bw30.open.wft.common.cardpool.AesEncrypter;
import com.bw30.open.wft.common.cardpool.RsaDecrypter;
import com.bw30.open.wft.common.cardpool.proto.Req;
import com.bw30.open.wft.common.cardpool.proto.Resp;

public abstract class ASuperHandle<T1 extends Req, T2 extends Resp> implements ISuperHandle {
	private static final Logger LOG =  Logger.getLogger(ASuperHandle.class);
	
	protected static final SimpleDateFormat SDF_YMD_HMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static final String CHARSET = "UTF-8";
	
	@Resource
	private RsaDecrypter rsaDecrypterForPartner;
	@Resource
	private AesEncrypter aesEncrypterForPartner;
	
	@Resource
	protected WftChannelMapper wftChannelMapper;
	
	/**
	 * 业务处理逻辑
	 * @param t1
	 * @return
	 */
	protected abstract T2 doHandle(T1 t1) throws Exception;
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public byte[] doParse(HttpServletRequest req) {
		String method = this.getMethod(req);
		
		Class<T1> clz = getTClass(0);
		T1 t1 = doBody(req, clz);
		if(null == t1){
			return null;
		}
		
		Resp resp = null;
		String aesKey = null;
		try{
			//检查partner
			String partnerCode = t1.getPartner();
			if(this.isEmpty(partnerCode)){
				return null;
			}
			WftChannel partner = this.getPartner(partnerCode);
			if(null == partner){
				LOG.warn(method + ": no partner[" + partnerCode + "]");
				return null;
			}
			
			if(WftChannel.STATUS_ENABLE != partner.getStatus()){
				LOG.warn(method + ": partner[" + partnerCode + "] is forbidden");
				return null;
			}
			
			aesKey = partner.getOpenkey();
			
			//ip鉴权
			String ip = this.getIp(req);
			if(!this.authIp(partnerCode, ip)){
				LOG.warn(method + ": ip[" + ip + "] invalid for partner[" + partnerCode + "]");
				resp = new Resp();
				resp.setResult(Resp.RESULT_ERROR_IP);
				return this.parseData(method, resp, aesKey);
			}
			
			//业务逻辑处理
			T2 t2 = doHandle(t1);
			
			return this.parseData(method, t2, aesKey);
		} catch (Exception e) {
			LOG.error(method + ": error", e);
			resp = new Resp();
			resp.setResult(Resp.RESULT_ERROR_OTHER);
			return this.parseData(method, resp, aesKey);
		}
	}

	/**
	 * 解析请求数据
	 * 
	 * @param req
	 * @param clazz
	 * @return
	 */
	private T1 doBody(HttpServletRequest req, Class<T1> clazz) {
		String method = this.getMethod(req);
		byte[] reqData = null;
		try{
			ServletInputStream sis = req.getInputStream();
//			InputStreamReader reader = new InputStreamReader(sis, CHARSET);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] data = new byte[1024];
			int len = sis.read(data);
			while (len != -1) {
				baos.write(data, 0, len);
				len = sis.read(data);
			}
			reqData = baos.toByteArray();
			LOG.info(method + " req data size:" + reqData.length);
			baos.close();
			sis.close();
		}catch(IOException ioe){
			LOG.error(method + ": read data error", ioe);
			return null;
		}
		
		if(0 == reqData.length){
			LOG.warn(method + ": no data read");
			return null;
		}
		
		String reqStr = null;
		try{
			if(method.equalsIgnoreCase("queryUserOnline.do") || 
					method.equalsIgnoreCase("queryUserRecord.do") || 
					method.equalsIgnoreCase("onlineNotify.do") || 
					method.equalsIgnoreCase("offlineNotify.do")){
				reqStr = new String(reqData, CHARSET);
			}else{//需要解密
				//rsa decrypte
				reqStr = new String(this.rsaDecrypterForPartner.decrypt(reqData), CHARSET);
			}
			LOG.info(method + " req data:" + reqStr);
			return JSON.parseObject(reqStr, clazz);
		} catch(Exception e){
			LOG.error(method + ": parse req data error", e);
			return null;
		}
		
	}

	@SuppressWarnings("rawtypes")
	private Class getTClass(int i) {
		Type genType = getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		Class cla = (Class) params[i];
		return cla;
	}

	
	
	/**
	 * 加密及编码响应数据
	 * 
	 * @param method
	 * @param obj 响应数据
	 * @param aesKey
	 * @return
	 */
	private byte[] parseData(String method, Object obj, String aesKey){
		try{
			String respData = JSON.toJSONString(obj);
			LOG.info(method + " resp data:" + respData);
			if(method.equalsIgnoreCase("queryUserOnline.do") || 
					method.equalsIgnoreCase("queryUserRecord.do") ||
					method.equalsIgnoreCase("onlineNotify.do") || 
					method.equalsIgnoreCase("offlineNotify.do")){//不加密
				return respData.getBytes(CHARSET);
			}else{//aes encrypt
				return this.aesEncrypterForPartner.encrypt(respData.getBytes(CHARSET), aesKey);
			}
		}catch(Exception e){
			LOG.info(method + ": aes encrypt or base64 encode resp data error", e);
			return null;
		}
	}
	
	/**
	 * 检查合作方是否存在
	 * 
	 * @param partner
	 * @return ture存在，false不存在
	 */
	public WftChannel getPartner(String partnerCode){
		if(null == partnerCode){
			return null;
		}
		WftChannel partner = this.wftChannelMapper.findById(partnerCode);
		return partner;
	}
	
	/**
	 * ip鉴权
	 * 
	 * @param partner
	 * @param ip
	 * @return true ip合法，false ip非法
	 */
	private boolean authIp(String partner, String ip){
		//TODO ip auth
		
		return true;
	}
	
	/**
	 * 判断是否为空
	 * @param str
	 * @return
	 */
	protected boolean isEmpty(String str){
		if(null == str || 0 == str.trim().length()){
			return true;
		}
		return false;
	}

	private String getMethod(HttpServletRequest req){
		String reqUri = req.getRequestURI();
		return reqUri.substring(reqUri.lastIndexOf("/") + 1);
	}
	
	private String getIp(HttpServletRequest req){
		return req.getRemoteAddr();
	}

	public void setRsaDecrypterForPartner(RsaDecrypter rsaDecrypterForPartner) {
		this.rsaDecrypterForPartner = rsaDecrypterForPartner;
	}

	public void setAesEncrypterForPartner(AesEncrypter aesEncrypterForPartner) {
		this.aesEncrypterForPartner = aesEncrypterForPartner;
	}

	public void setWftChannelMapper(WftChannelMapper wftChannelMapper) {
		this.wftChannelMapper = wftChannelMapper;
	}

}
