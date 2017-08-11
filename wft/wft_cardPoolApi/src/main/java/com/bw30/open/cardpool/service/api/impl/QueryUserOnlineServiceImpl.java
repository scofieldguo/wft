package com.bw30.open.cardpool.service.api.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.bw30.open.cardpool.service.ConnSessionService;
import com.bw30.open.cardpool.service.api.ASuperHandle;
import com.bw30.open.cardpool.service.api.IQueryUserOnlineService;
import com.bw30.open.cardpool.util.Md5Encrypt;
import com.bw30.open.common.dao.mapper.WftCardActiveMapper;
import com.bw30.open.common.model.WftCardActive;
import com.bw30.open.common.model.WftChannel;
import com.bw30.open.common.model.WftConnSession;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.wft.common.cardpool.proto.Resp;
import com.bw30.open.wft.common.cardpool.proto.req.QueryUserOnlineReq;
import com.bw30.open.wft.common.cardpool.proto.resp.QueryUserOnlineResp;

public class QueryUserOnlineServiceImpl extends
		ASuperHandle<QueryUserOnlineReq, QueryUserOnlineResp> implements
		IQueryUserOnlineService {
	private static final Logger LOG = Logger
			.getLogger(QueryUserOnlineServiceImpl.class);

	@Resource
	private WftCardActiveMapper wftCardActiveMapper;
	@Resource
	private ConnSessionService connSessionService;

	public void setWftCardActiveMapper(WftCardActiveMapper wftCardActiveMapper) {
		this.wftCardActiveMapper = wftCardActiveMapper;
	}

	public void setConnSessionService(ConnSessionService connSessionService) {
		this.connSessionService = connSessionService;
	}

	@Override
	protected QueryUserOnlineResp doHandle(QueryUserOnlineReq t1)
			throws Exception {
		QueryUserOnlineResp quor = new QueryUserOnlineResp();
		quor.setOpenid(t1.getOpenid());
		quor.setStandby(t1.getStandby());

		if (!this.checkParam(t1)) {
			quor.setResult(Resp.RESULT_ERROR_PARAM);
			return quor;
		}

		if (!this.checkSign(t1)) {
			quor.setResult(Resp.RESULT_ERROR_PARAM);
			return quor;
		}

		quor.setResult(Resp.RESULT_OK);
		String openid = t1.getOpenid();
		WftConnSession cs = this.connSessionService.checkOnline(openid,
				t1.getPartner());
		if (null == cs) {
			LOG.info(String.format("user is offline: uid=%s", openid));
			quor.setStatus(1);
			return quor;
		}

		Date now = new Date();
		WftCardActive ca = this.wftCardActiveMapper.findById(t1.getPartner(),
				cs.getCid());
		if (null != ca) {
			Long timeLen = now.getTime() - cs.getBstime().getTime();
			timeLen /= 1000;// 秒
			if (0 >= (ca.getTvalue() - timeLen.intValue())) {
				LOG.info(String
						.format("user is offline: uid=%s, csid=%s, cardBalance=%s, useTime=%s",
								openid, cs.getCsid(), ca.getTvalue(), timeLen));
				quor.setStatus(1);
				return quor;
			}
		}

		quor.setStatus(0);
		quor.setOpid(cs.getSsid().toUpperCase()
				.contains(WftOperator.OP_SSID_CMCC) ? 1 : 2);
		return quor;

		// if(null != cs){
		// quor.setStatus(0);
		// quor.setOpid(cs.getSsid().toUpperCase().contains(WftOperator.OP_SSID_CMCC)
		// ? 1 : 2);
		// }else{
		// quor.setStatus(1);
		// }
		//
		// return quor;
	}

	private boolean checkParam(QueryUserOnlineReq t1) {
		if (this.isEmpty(t1.getOpenid())) {
			return false;
		}

		return true;
	}

	private boolean checkSign(QueryUserOnlineReq t1) {
		String partner = t1.getPartner();
		if ("10001".equals(partner)) {
			return true;
		}

		String sign = t1.getSign();
		if (null == sign) {
			return false;
		}
		WftChannel channel = this.getPartner(partner);
		StringBuffer sb = new StringBuffer("partner=").append(t1.getPartner())
				.append("&openid=").append(t1.getOpenid())
				.append("&timestamp=").append(t1.getTimestamp())
				.append("&key=").append(channel.getOpenkey());
		String mySign = Md5Encrypt.md5Enc(sb.toString(), "UTF-8");
		if (mySign.equalsIgnoreCase(sign)) {
			return true;
		}
		return false;
	}

}
