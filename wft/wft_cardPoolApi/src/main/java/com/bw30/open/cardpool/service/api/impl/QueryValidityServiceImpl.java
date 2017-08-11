package com.bw30.open.cardpool.service.api.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.bw30.open.cardpool.service.CardService;
import com.bw30.open.cardpool.service.api.ASuperHandle;
import com.bw30.open.cardpool.service.api.IQueryValidityService;
import com.bw30.open.common.model.WftCardActive;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.wft.common.cardpool.proto.Resp;
import com.bw30.open.wft.common.cardpool.proto.req.QueryValidityReq;
import com.bw30.open.wft.common.cardpool.proto.resp.QueryValidityResp;

public class QueryValidityServiceImpl extends
		ASuperHandle<QueryValidityReq, QueryValidityResp> implements
		IQueryValidityService {
	private static final Logger LOG = Logger
			.getLogger(QueryValidityServiceImpl.class);

	@Resource
	private CardService cardService;

	public void setCardService(CardService cardService) {
		this.cardService = cardService;
	}

	@Override
	protected QueryValidityResp doHandle(QueryValidityReq t1) throws Exception {
		QueryValidityResp qvr = new QueryValidityResp();
		qvr.setStandby(t1.getStandby());

		if (!this.checkParam(t1)) {
			qvr.setResult(Resp.RESULT_ERROR_PARAM);
			return qvr;
		}

		// 检查op，只支持 电信
		if (!this.checkOp(t1.getOp())) {
			qvr.setResult(Resp.RESULT_ERROR_OP);
			return qvr;
		}

		WftCardActive card = this.cardService.findCard(t1.getPartner(),
				t1.getOp(), t1.getCno());
		if (null == card) {
			LOG.warn(String.format("no card: partner=%s, op=%s, cno=%s",
					t1.getPartner(), t1.getOp(), t1.getCno()));
			qvr.setResult(Resp.RESULT_ERROR_CNO);
			return qvr;
		}
		if (!card.getPwd().equals(t1.getPwd())) {
			qvr.setResult(Resp.RESULT_ERROR_PWD);
			return qvr;
		}

		qvr.setResult(Resp.RESULT_OK);
		qvr.setTimestamp(SDF_YMD_HMS.format(new Date()));
		qvr.setCno(card.getNo());
		qvr.setEffectdate(SDF_YMD_HMS.format(card.getVbtime()));
		qvr.setExpiredate(SDF_YMD_HMS.format(card.getVetime()));
		return qvr;
	}

	private boolean checkParam(QueryValidityReq t1) {
		if (this.isEmpty(t1.getCno()) || this.isEmpty(t1.getPwd())
				|| this.isEmpty(t1.getTimestamp())) {
			return false;
		}

		return true;
	}

	/**
	 * 检查op，当前只支持 电信卡
	 * 
	 * @param op
	 * @return
	 */
	private boolean checkOp(Integer op) {
		if (null == op || (WftOperator.OP_ID_CTCC != op)) {
			return false;
		}
		return true;
	}

}
