package com.bw30.open.cardpool.service.api.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.bw30.open.cardpool.service.CardService;
import com.bw30.open.cardpool.service.api.ASuperHandle;
import com.bw30.open.cardpool.service.api.IQueryBalanceService;
import com.bw30.open.common.model.WftCardActive;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.wft.common.cardpool.proto.Resp;
import com.bw30.open.wft.common.cardpool.proto.req.QueryBalanceReq;
import com.bw30.open.wft.common.cardpool.proto.resp.QueryBalanceResp;
import com.bw30.open.wft.common.cardpool.rmi.service.ICardPoolService;

public class QueryBalanceServiceImpl extends
		ASuperHandle<QueryBalanceReq, QueryBalanceResp> implements
		IQueryBalanceService {
	private static final Logger LOG = Logger
			.getLogger(QueryBalanceServiceImpl.class);
	@Resource
	private CardService cardService;
	@Resource
	private ICardPoolService cardPoolService;

	public void setCardService(CardService cardService) {
		this.cardService = cardService;
	}

	public void setCardPoolService(ICardPoolService cardPoolService) {
		this.cardPoolService = cardPoolService;
	}

	@Override
	protected QueryBalanceResp doHandle(QueryBalanceReq t1) throws Exception {
		QueryBalanceResp qbr = new QueryBalanceResp();
		qbr.setStandby(t1.getStandby());

		// 检查参数
		if (!this.checkParam(t1)) {
			qbr.setResult(Resp.RESULT_ERROR_PARAM);
			return qbr;
		}

		// 检查op，当前只支持 电信开卡
		if (!this.checkOp(t1.getOp())) {
			qbr.setResult(Resp.RESULT_ERROR_OP);
			return qbr;
		}

		// 检查卡号、卡密
		WftCardActive card = this.cardService.findCard(t1.getPartner(),
				t1.getOp(), t1.getCno());
		if (null == card) {
			LOG.warn(String.format("no card: partner=%s, op=%s, cno=%s",
					t1.getPartner(), t1.getOp(), t1.getCno()));
			qbr.setResult(Resp.RESULT_ERROR_CNO);
			return qbr;
		}
		if (!card.getPwd().equals(t1.getPwd())) {
			qbr.setResult(Resp.RESULT_ERROR_PWD);
			return qbr;
		}

		// 查询剩余时长
		Integer balance = this.cardPoolService.getBalance(t1.getPartner(),
				card.getId(), t1.getOp(), t1.getCno(), t1.getPwd());
		if (null == balance) {
			LOG.error(String.format("query balance error: cid=%s, cno=%s",
					card.getId(), t1.getCno()));
			qbr.setResult(Resp.RESULT_ERROR_OTHER);
			return qbr;
		}

		// 更新渠道的卡剩余时长
		this.updateTvalue(t1.getPartner(), card.getId(), balance);

		qbr.setResult(Resp.RESULT_OK);
		qbr.setTimestamp(SDF_YMD_HMS.format(new Date()));
		qbr.setCno(card.getNo());
		qbr.setBalance(balance);
		return qbr;
	}

	private boolean checkParam(QueryBalanceReq t1) {
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

	/**
	 * 更新卡的剩余时长
	 * 
	 * @param partner
	 * @param cid
	 * @param balance
	 */
	private void updateTvalue(String partner, Integer cid, Integer balance) {
		WftCardActive card = new WftCardActive();
		card.setId(cid);
		card.setTvalue(balance);
		this.cardService.updateCardActive(partner, card);
	}

}
