package com.bw30.open.cardpool.service.api.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.bw30.open.cardpool.service.CardService;
import com.bw30.open.cardpool.service.api.ASuperHandle;
import com.bw30.open.cardpool.service.api.IOpenCardService;
import com.bw30.open.common.dao.mapper.WftCardTypeMapper;
import com.bw30.open.common.model.WftCardActive;
import com.bw30.open.common.model.WftCardType;
import com.bw30.open.common.model.WftChannel;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.wft.common.PwdEncrypt;
import com.bw30.open.wft.common.cardpool.proto.Resp;
import com.bw30.open.wft.common.cardpool.proto.req.OpenCardReq;
import com.bw30.open.wft.common.cardpool.proto.resp.OpenCardResp;
import com.bw30.open.wft.common.cardpool.rmi.bean.CardBean;
import com.bw30.open.wft.common.cardpool.rmi.service.ICardPoolService;

public class OpenCardServiceImpl extends
		ASuperHandle<OpenCardReq, OpenCardResp> implements IOpenCardService {
	private static final Logger LOG = Logger
			.getLogger(OpenCardServiceImpl.class);

	@Resource
	private ICardPoolService cardPoolService;
	@Resource
	private WftCardTypeMapper wftCardTypeMapper;
	@Resource
	private CardService cardService;

	public void setCardPoolService(ICardPoolService cardPoolService) {
		this.cardPoolService = cardPoolService;
	}

	public void setWftCardTypeMapper(WftCardTypeMapper wftCardTypeMapper) {
		this.wftCardTypeMapper = wftCardTypeMapper;
	}

	public void setCardService(CardService cardService) {
		this.cardService = cardService;
	}

	@Override
	protected OpenCardResp doHandle(OpenCardReq t1) throws Exception {
		OpenCardResp ocr = new OpenCardResp();
		ocr.setStandby(t1.getStandby());

		// 检查参数是否合法
		if (!this.checkParam(t1)) {
			ocr.setResult(Resp.RESULT_ERROR_PARAM);
			return ocr;
		}

		// 检查op，当前只支持 电信开卡
		if (!this.checkOp(t1.getOp())) {
			ocr.setResult(Resp.RESULT_ERROR_OP);
			return ocr;
		}

		// 检查卡品是否正确
		WftCardType cardType = this.checkCtype(t1.getOp(), t1.getCtype());
		if (null == cardType) {
			LOG.warn(String.format("no ctype", t1.getOp(), t1.getCtype()));
			ocr.setResult(Resp.RESULT_ERROR_CTYPE);
			return ocr;
		}

		WftChannel channel = this.getPartner(t1.getPartner());
		Integer maxNum = channel.getMaxnum();
		Integer cnum = channel.getCnum();
		if (0 <= maxNum && maxNum <= cnum) {
			LOG.warn("open card error: out of card size");
			ocr.setResult(Resp.RESULT_ERROR_OUT_CARDSIZE);
			return ocr;
		}

		// 开卡
		CardBean card = this.cardPoolService.openCard(t1.getPartner(),
				t1.getOp(), cardType.getCodeop(), t1.getOrderid());
		if (null == card) {
			LOG.error(String.format(
					"open card error: partner=%s, op=%s, ctype=%s, orderid=%s",
					t1.getPartner(), t1.getOp(), t1.getCtype(), t1.getOrderid()));
			ocr.setResult(Resp.RESULT_ERROR_OTHER);
			return ocr;
		}

		// 将卡保存到相应渠道的卡池中
		this.saveCardForPartner(t1.getPartner(), card);
		// 更新渠道的开卡数目
		this.wftChannelMapper.updateCnum(1, t1.getPartner());

		ocr.setResult(Resp.RESULT_OK);
		ocr.setCno(card.getNo());
		ocr.setPwd(card.getPwd());
		ocr.setBalance(card.getBalance());
		ocr.setValidity(SDF_YMD_HMS.format(card.getValidity()));
		return ocr;
	}

	/**
	 * 检查参数是否合法
	 * 
	 * @param t1
	 * @return
	 */
	private boolean checkParam(OpenCardReq t1) {
		if (this.isEmpty(t1.getCtype()) || this.isEmpty(t1.getOrderid())
				|| this.isEmpty(t1.getTimestamp())) {
			return false;
		}

		return true;
	}

	/**
	 * 检查op，当前只支持 电信开卡
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

	// 保存卡到渠道对应的卡池中
	private void saveCardForPartner(String partner, CardBean cb) {
		WftCardActive ca = new WftCardActive();
		ca.setId(cb.getId());
		ca.setOpid(cb.getOpid());
		ca.setSsid(cb.getSsid());
		ca.setPrvid(cb.getPrvId());
		ca.setCtype(0);// 类型：0
		ca.setNo(cb.getNo());
		ca.setPwd(PwdEncrypt.encryptCardPwd(cb.getPwd()));// 卡密加密
		ca.setUstatus(0);
		ca.setCstatus(WftCardActive.CSTATUS_AVAILABLE);
		ca.setBvalue(cb.getBalance());
		ca.setTvalue(cb.getBalance());
		ca.setVbtime(new Date());
		ca.setVetime(cb.getValidity());
		ca.setIntime(new Date());
		ca.setUtvalue(0);
		ca.setUcount(0);
		ca.setCache(WftCardActive.OUT_CACHE);
		this.cardService.saveCardActive(partner, ca);
	}

}
