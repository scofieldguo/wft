package com.bw30.open.cardpool.service.api.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.bw30.open.cardpool.service.CardService;
import com.bw30.open.cardpool.service.api.ASuperHandle;
import com.bw30.open.cardpool.service.api.IQueryOnlineService;
import com.bw30.open.common.model.WftCardActive;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.wft.common.cardpool.proto.Resp;
import com.bw30.open.wft.common.cardpool.proto.req.QueryOnlineReq;
import com.bw30.open.wft.common.cardpool.proto.resp.QueryOnlineResp;
import com.bw30.open.wft.common.cardpool.rmi.bean.OnlineBean;
import com.bw30.open.wft.common.cardpool.rmi.bean.RespBean;
import com.bw30.open.wft.common.cardpool.rmi.service.ICardPoolService;

public class QueryOnlineServiceImpl extends
		ASuperHandle<QueryOnlineReq, QueryOnlineResp> implements
		IQueryOnlineService {
	private static final Logger LOG = Logger
			.getLogger(QueryOnlineServiceImpl.class);

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
	protected QueryOnlineResp doHandle(QueryOnlineReq t1) throws Exception {
		QueryOnlineResp qor = new QueryOnlineResp();
		qor.setStandby(t1.getStandby());

		if (!this.checkParam(t1)) {
			qor.setResult(Resp.RESULT_ERROR_PARAM);
			return qor;
		}
		
		//检查op，支持 电信、移动
		if(!this.checkOp(t1.getOp())){
			qor.setResult(Resp.RESULT_ERROR_OP);
			return qor;
		}

		// 检查卡号、卡密
		WftCardActive card = this.cardService.findCard(t1.getPartner(),
				t1.getOp(), t1.getCno());
		if (null == card) {
			LOG.warn(String.format("no card: partner=%s, op=%s, cno=%s",
					t1.getPartner(), t1.getOp(), t1.getCno()));
			qor.setResult(Resp.RESULT_ERROR_CNO);
			return qor;
		}
		if (!card.getPwd().equals(t1.getPwd())) {
			qor.setResult(Resp.RESULT_ERROR_PWD);
			return qor;
		}

		// 查询卡的在线状态
		OnlineBean ob = this.cardPoolService.queryOnline(t1.getOp(),
				t1.getCno(), t1.getPwd(), t1.getIp(), null);

		if (null == ob || RespBean.RESULT_FAIL.equals(ob.getResult())) {
			LOG.error(String.format("quern online error: op=%s, cno=%s",
					t1.getOp(), t1.getCno()));
			qor.setResult(Resp.RESULT_ERROR_OTHER);
			return qor;
		}

		if (RespBean.RESULT_OK.equals(ob.getResult())) {
			qor.setResult(Resp.RESULT_OK);
			qor.setCno(t1.getCno());
			qor.setStarttime(ob.getStarttime());
			qor.setMack(ob.getMack());
			qor.setNasip(ob.getNasip());
			qor.setNasport(ob.getNasport());
			qor.setSessionid(ob.getSessionid());
			qor.setFramedip(ob.getFramedip());
			return qor;
		}

		LOG.info(String.format("card is offline: no=%s, result=%s",
				t1.getCno(), ob.getResult()));
		qor.setResult(Resp.RESULT_ERROR_OFFLINE);
		return qor;
	}

	private boolean checkParam(QueryOnlineReq t1) {
		if (this.isEmpty(t1.getCno()) || this.isEmpty(t1.getPwd())
				|| this.isEmpty(t1.getIp())) {
			return false;
		}

		return true;
	}
	
	/**
	 * 检查op，支持电信、移动
	 * @param op
	 * @return
	 */
	private boolean checkOp(Integer op){
		if(null == op || (WftOperator.OP_ID_CTCC != op && WftOperator.OP_ID_CMCC != op)){
			return false; 
		}
		return true;
	}

}
