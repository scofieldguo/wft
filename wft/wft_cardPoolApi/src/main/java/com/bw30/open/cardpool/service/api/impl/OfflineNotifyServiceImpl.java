package com.bw30.open.cardpool.service.api.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.bw30.open.cardpool.service.ConnSessionService;
import com.bw30.open.cardpool.service.api.ASuperHandle;
import com.bw30.open.cardpool.service.api.IOfflineNotifyService;
import com.bw30.open.cardpool.util.Md5Encrypt;
import com.bw30.open.wft.common.cardpool.proto.Resp;
import com.bw30.open.wft.common.cardpool.proto.req.OfflineNotifyReq;
import com.bw30.open.wft.common.cardpool.proto.resp.OfflineNotifyResp;

/**
 * 下线通知
 * 
 * @author Jack
 *
 * 2014年9月8日
 */
public class OfflineNotifyServiceImpl extends
		ASuperHandle<OfflineNotifyReq, OfflineNotifyResp> implements
		IOfflineNotifyService {
	private static final Logger LOG = Logger.getLogger("OFFLINE_NOTIFY");

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
	protected OfflineNotifyResp doHandle(OfflineNotifyReq t1) throws Exception {
		LOG.info("req:" + JSON.toJSONString(t1));
		OfflineNotifyResp onr = new OfflineNotifyResp();
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

		Date betimeqq = SDF_YMD_HMS.parse(t1.getBetime());
		this.connSessionService.updateForOffline(t1.getOpenid(),
				t1.getPartner(), t1.getCsid(), betimeqq, t1.getFlag());
		LOG.info("resp:" + JSON.toJSONString(onr));
		onr.setResult(Resp.RESULT_OK);
		return onr;
	}

	private boolean checkParam(OfflineNotifyReq t1) {
		if (this.isEmpty(t1.getOpenid()) || this.isEmpty(t1.getBetime())) {
			return false;
		}

		try {
			SDF_YMD_HMS.parse(t1.getBetime());
		} catch (Exception e) {
			return false;
		}

		return true;
	}
	
	private boolean checkSign(OfflineNotifyReq t1){
		String sign = t1.getSign();
		if(null == sign){//兼容老接口
			return true;
		}
		
		StringBuffer sb = new StringBuffer("partner=").append(t1.getPartner())
								.append("&openid=").append(t1.getOpenid())
								.append("&csid=").append(t1.getCsid())
								.append("&flag=").append(t1.getFlag())
								.append("&betime=").append(t1.getBetime())
								.append("&key=").append(this.md5key);
		String mySign = Md5Encrypt.md5Enc(sb.toString(), "UTF-8");
		if(mySign.equalsIgnoreCase(sign)){
			return true;
		}
		return false;
	}

}
