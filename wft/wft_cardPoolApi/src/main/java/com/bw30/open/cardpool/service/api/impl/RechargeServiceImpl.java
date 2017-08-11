package com.bw30.open.cardpool.service.api.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.bw30.open.cardpool.service.CardService;
import com.bw30.open.cardpool.service.api.ASuperHandle;
import com.bw30.open.cardpool.service.api.IRechargeService;
import com.bw30.open.common.dao.mapper.WftCardTypeMapper;
import com.bw30.open.common.model.WftCardActive;
import com.bw30.open.common.model.WftCardType;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.wft.common.cardpool.proto.Resp;
import com.bw30.open.wft.common.cardpool.proto.req.RechargeReq;
import com.bw30.open.wft.common.cardpool.proto.resp.RechargeResp;
import com.bw30.open.wft.common.cardpool.rmi.bean.RechargeBean;
import com.bw30.open.wft.common.cardpool.rmi.bean.RespBean;
import com.bw30.open.wft.common.cardpool.rmi.service.ICardPoolService;

public class RechargeServiceImpl extends
		ASuperHandle<RechargeReq, RechargeResp> implements IRechargeService {
	private static final Logger LOG = Logger
			.getLogger(RechargeServiceImpl.class);

	@Resource
	private WftCardTypeMapper wftCardTypeMapper;
	@Resource
	private CardService cardService;
	@Resource
	private ICardPoolService cardPoolService;

	public void setWftCardTypeMapper(WftCardTypeMapper wftCardTypeMapper) {
		this.wftCardTypeMapper = wftCardTypeMapper;
	}

	public void setCardService(CardService cardService) {
		this.cardService = cardService;
	}

	public void setCardPoolService(ICardPoolService cardPoolService) {
		this.cardPoolService = cardPoolService;
	}

	@Override
	protected RechargeResp doHandle(RechargeReq t1) throws Exception {
		RechargeResp rr = new RechargeResp();
		rr.setStandby(t1.getStandby());

		// 检查参数是否合法
		if (!this.checkParam(t1)) {
			rr.setResult(Resp.RESULT_ERROR_PARAM);
			return rr;
		}

		// 检查op，只支持 电信
		if (!this.checkOp(t1.getOp())) {
			rr.setResult(Resp.RESULT_ERROR_OP);
			return rr;
		}

		// 检查卡是否存在
		WftCardActive card = this.cardService.findCard(t1.getPartner(),
				t1.getOp(), t1.getCno());
		if (null == card) {
			LOG.warn(String.format("no card: partner=%s, op=%s, cno=%s",
					t1.getPartner(), t1.getOp(), t1.getCno()));
			rr.setResult(Resp.RESULT_ERROR_CNO);
			return rr;
		}

		// 检查卡品是否正确
		WftCardType cardType = this.checkCtype(t1.getOp(), t1.getCtype());
		if (null == cardType) {
			LOG.warn(String.format("no ctype", t1.getOp(), t1.getCtype()));
			rr.setResult(Resp.RESULT_ERROR_CTYPE);
			return rr;
		}

		// 充值
		RechargeBean rb = this.cardPoolService.recharge(t1.getPartner(),
				card.getId(), card.getNo(), t1.getOp(), cardType.getCodeop(),
				t1.getOrderid());

		if (null != rb && RespBean.RESULT_OK.equals(rb.getResult())) {
			this.updateTvalue(t1.getPartner(), card.getId(), rb);

			rr.setResult(Resp.RESULT_OK);
			rr.setTimestamp(SDF_YMD_HMS.format(new Date()));
			rr.setCno(t1.getCno());
			rr.setBalance(rb.getBalance());
			rr.setOldbalance(rb.getOldbalance());
			rr.setNewbalance(rb.getNewbalance());
			rr.setValidity(SDF_YMD_HMS.format(rb.getValidity()));
			return rr;
		}

		LOG.warn(String.format(
				"recharge error:partner=%s, op=%s, cno=%s, orderid=%s",
				t1.getPartner(), t1.getOp(), t1.getCno(), t1.getOrderid()));
		rr.setResult(Resp.RESULT_ERROR_OTHER);
		return rr;

	}

	/**
	 * 检查参数是否合法
	 * 
	 * @param t1
	 * @return
	 */
	private boolean checkParam(RechargeReq t1) {
		if (this.isEmpty(t1.getCno()) || this.isEmpty(t1.getCtype())
				|| this.isEmpty(t1.getOrderid())
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
	 * 检查卡品是否正确
	 * 
	 * @param op
	 * @param ctype
	 * @return
	 */
	private WftCardType checkCtype(Integer op, String ctype) {
		List<WftCardType> ccList = this.wftCardTypeMapper.findCardType(op,
				ctype);
		if (null != ccList && 0 < ccList.size()) {
			return ccList.get(0);
		}
		return null;
	}

	/**
	 * 更新充值后的时长及有效期
	 * 
	 * @param partner
	 * @param cid
	 * @param rb
	 */
	private void updateTvalue(String partner, Integer cid, RechargeBean rb) {
		WftCardActive ca = new WftCardActive();
		ca.setId(cid);
		ca.setBvalue(rb.getBalance());
		ca.setTvalue(rb.getNewbalance());
		ca.setVetime(rb.getValidity());
		this.cardService.updateCardActive(partner, ca);
	}

}
