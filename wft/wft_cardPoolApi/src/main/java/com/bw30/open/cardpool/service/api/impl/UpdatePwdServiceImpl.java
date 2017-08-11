package com.bw30.open.cardpool.service.api.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.bw30.open.cardpool.service.CardService;
import com.bw30.open.cardpool.service.api.ASuperHandle;
import com.bw30.open.cardpool.service.api.IUpdatePwdService;
import com.bw30.open.common.model.WftCardActive;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.wft.common.cardpool.proto.Resp;
import com.bw30.open.wft.common.cardpool.proto.req.UpdatePwdReq;
import com.bw30.open.wft.common.cardpool.proto.resp.UpdatePwdResp;
import com.bw30.open.wft.common.cardpool.rmi.service.ICardPoolService;

public class UpdatePwdServiceImpl extends
		ASuperHandle<UpdatePwdReq, UpdatePwdResp> implements IUpdatePwdService {
	private static final Logger LOG = Logger
			.getLogger(UpdatePwdServiceImpl.class);

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
	protected UpdatePwdResp doHandle(UpdatePwdReq t1) throws Exception {
		UpdatePwdResp upr = new UpdatePwdResp();
		upr.setStandby(t1.getStandby());

		// 检查参数是否合法
		if (!this.checkParam(t1)) {
			upr.setResult(Resp.RESULT_ERROR_PARAM);
			return upr;
		}

		// 检查op，只支持 电信
		if (!this.checkOp(t1.getOp())) {
			upr.setResult(Resp.RESULT_ERROR_OP);
			return upr;
		}

		// 检查卡号、卡密是否正确
		WftCardActive card = this.cardService.findCard(t1.getPartner(),
				t1.getOp(), t1.getCno());
		if (null == card) {
			LOG.warn(String.format("no card: partner=%s, op=%s, cno=%s",
					t1.getPartner(), t1.getOp(), t1.getCno()));
			upr.setResult(Resp.RESULT_ERROR_CNO);
			return upr;
		}
		if (!t1.getPwd().equals(card.getPwd())) {
			LOG.warn("card pwd error");
			upr.setResult(Resp.RESULT_ERROR_PWD);
			return upr;
		}

		// 修改密码
		if (!this.cardPoolService.updatePwd(t1.getPartner(), card.getId(),
				t1.getOp(), t1.getCno(), t1.getPwd(), t1.getNewpwd())) {
			LOG.error(String.format("updatePwd error:cid=%s, cno=%s",
					card.getId(), t1.getCno()));
			upr.setResult(Resp.RESULT_ERROR_OTHER);
			return upr;
		}

		this.cardService.updatePwd(t1.getPartner(), card.getId(),
				t1.getNewpwd());

		upr.setResult(Resp.RESULT_OK);
		return upr;

	}

	/**
	 * 检查参数是否合法
	 * 
	 * @param t1
	 * @return
	 */
	private boolean checkParam(UpdatePwdReq t1) {
		if (this.isEmpty(t1.getCno()) || this.isEmpty(t1.getPwd())
				|| this.isEmpty(t1.getNewpwd())
				|| this.isEmpty(t1.getTimestamp())) {
			return false;
		}

		if (!t1.getNewpwd().matches("^\\d{8}$")) {
			LOG.warn("newpwd is invalid");
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
