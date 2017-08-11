package com.bw30.open.cardpool.service.api.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.bw30.open.cardpool.service.CardService;
import com.bw30.open.cardpool.service.api.ASuperHandle;
import com.bw30.open.cardpool.service.api.IQueryRecordService;
import com.bw30.open.common.model.WftCardActive;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.wft.common.cardpool.proto.Resp;
import com.bw30.open.wft.common.cardpool.proto.req.QueryRecordReq;
import com.bw30.open.wft.common.cardpool.proto.resp.QueryRecordResp;
import com.bw30.open.wft.common.cardpool.rmi.bean.CardRecord;
import com.bw30.open.wft.common.cardpool.rmi.service.ICardPoolService;

public class QueryRecordServiceImpl extends
		ASuperHandle<QueryRecordReq, QueryRecordResp> implements
		IQueryRecordService {
	private static final Logger LOG = Logger
			.getLogger(QueryRecordServiceImpl.class);

	private static final SimpleDateFormat SDF_YMD = new SimpleDateFormat(
			"yyyy-MM-dd");

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
	protected QueryRecordResp doHandle(QueryRecordReq t1) throws Exception {
		QueryRecordResp qrr = new QueryRecordResp();
		qrr.setStandby(t1.getStandby());

		if (!this.checkParam(t1)) {
			qrr.setResult(Resp.RESULT_ERROR_PARAM);
			return qrr;
		}

		// 检查op，支持 电信、移动
		if (!this.checkOp(t1.getOp())) {
			qrr.setResult(Resp.RESULT_ERROR_OP);
			return qrr;
		}

		// 检查卡号、卡密
		WftCardActive card = this.cardService.findCard(t1.getPartner(),
				t1.getOp(), t1.getCno());
		if (null == card) {
			LOG.warn(String.format("no card: partner=%s, op=%s, cno=%s",
					t1.getPartner(), t1.getOp(), t1.getCno()));
			qrr.setResult(Resp.RESULT_ERROR_CNO);
			return qrr;
		}
		if (!card.getPwd().equals(t1.getPwd())) {
			qrr.setResult(Resp.RESULT_ERROR_PWD);
			return qrr;
		}

		// 查询卡使用记录
		List<CardRecord> rList = this.cardPoolService.queryRecord(
				t1.getPartner(), t1.getOp(), t1.getCno(), t1.getPwd(),
				t1.getStartdate(), t1.getEnddate());
		if (null != rList && 0 < rList.size()) {
			List<QueryRecordResp.Record> recordList = new ArrayList<QueryRecordResp.Record>(
					rList.size());
			for (CardRecord r : rList) {
				QueryRecordResp.Record record = new QueryRecordResp.Record();
				record.setStarttime(r.getStarttime());
				record.setEndtime(r.getEndtime());
				record.setTimelen(r.getTimelen());
				recordList.add(record);
			}
			qrr.setRecordlist(recordList);
		}
		qrr.setResult(Resp.RESULT_OK);
		qrr.setCno(t1.getCno());
		return qrr;
	}

	private boolean checkParam(QueryRecordReq t1) {
		if (this.isEmpty(t1.getCno()) || this.isEmpty(t1.getPwd())
				|| this.isEmpty(t1.getStartdate())
				|| this.isEmpty(t1.getEnddate())
				|| this.isEmpty(t1.getTimestamp())) {
			return false;
		}

		try {
			Date startDate = SDF_YMD.parse(t1.getStartdate());
			Date endDate = SDF_YMD.parse(t1.getEnddate());
			if (startDate.after(endDate)) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	/**
	 * 检查op，支持电信、移动
	 * 
	 * @param op
	 * @return
	 */
	private boolean checkOp(Integer op) {
		if (null == op
				|| (WftOperator.OP_ID_CTCC != op && WftOperator.OP_ID_CMCC != op)) {
			return false;
		}
		return true;
	}

}
