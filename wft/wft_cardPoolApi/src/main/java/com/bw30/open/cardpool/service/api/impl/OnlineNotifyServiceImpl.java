package com.bw30.open.cardpool.service.api.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.bw30.open.cardpool.service.ConnSessionService;
import com.bw30.open.cardpool.service.api.ASuperHandle;
import com.bw30.open.cardpool.service.api.IOnlineNotifyService;
import com.bw30.open.cardpool.util.Md5Encrypt;
import com.bw30.open.wft.common.cardpool.proto.Resp;
import com.bw30.open.wft.common.cardpool.proto.req.OnlineNotifyReq;
import com.bw30.open.wft.common.cardpool.proto.resp.OnlineNotifyResp;

/**
 * 上线通知
 * 
 * @author Jack
 *
 * 2014年9月8日
 */
public class OnlineNotifyServiceImpl extends
		ASuperHandle<OnlineNotifyReq, OnlineNotifyResp> implements
		IOnlineNotifyService {
	private static final Logger LOG = Logger.getLogger("ONLINE_NOTIFY");

	@Resource
	private ConnSessionService connSessionService;
	
	private String md5key;

	public void setConnSessionService(ConnSessionService connSessionService) {
		this.connSessionService = connSessionService;
	}
	
	public void setMd5key(String md5key) {
		this.md5key = md5key;
	}

	@Override
	protected OnlineNotifyResp doHandle(OnlineNotifyReq t1) throws Exception {
		LOG.info("req:" + JSON.toJSONString(t1));
		OnlineNotifyResp onr = new OnlineNotifyResp();
		onr.setOpenid(t1.getOpenid());
		onr.setStandby(t1.getStandby());

		if (!this.checkParam(t1)) {
			onr.setResult(Resp.RESULT_ERROR_PARAM);
			LOG.info("resp:" + JSON.toJSONString(onr));
			return onr;
		}
		
		if(!this.checkSign(t1)){
			onr.setResult(Resp.RESULT_ERROR_PARAM);
			LOG.error("check sign error");
			LOG.info("resp:" + JSON.toJSONString(onr));
			return onr;
		}

		Date bstimeqq = SDF_YMD_HMS.parse(t1.getBstime());
		this.connSessionService.updateForOnline(t1.getOpenid(),
				t1.getPartner(), t1.getCsid(), bstimeqq);
		onr.setResult(Resp.RESULT_OK);
		LOG.info("resp:" + JSON.toJSONString(onr));
		return onr;
	}
	
	private boolean checkParam(OnlineNotifyReq t1) {
		if (this.isEmpty(t1.getOpenid()) || this.isEmpty(t1.getCsid()) 
				|| this.isEmpty(t1.getBstime())) {
			return false;
		}

		try {
			SDF_YMD_HMS.parse(t1.getBstime());
		} catch (Exception e) {
			return false;
		}

		return true;
	}
	
	private boolean checkSign(OnlineNotifyReq t1){
		String sign = t1.getSign();
		if(null == sign){//兼容老接口
			return true;
		}
		
		StringBuffer sb = new StringBuffer("partner=").append(t1.getPartner())
								.append("&openid=").append(t1.getOpenid())
								.append("&csid=").append(t1.getCsid())
								.append("&bstime=").append(t1.getBstime())
								.append("&key=").append(this.md5key);
		String mySign = Md5Encrypt.md5Enc(sb.toString(), "UTF-8");
		if(mySign.equalsIgnoreCase(sign)){
			return true;
		}
		return false;
	}

}
