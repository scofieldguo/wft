package com.bw30.open.cardpool.service.api.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.bw30.open.cardpool.service.ConnSessionService;
import com.bw30.open.cardpool.service.api.ASuperHandle;
import com.bw30.open.cardpool.service.api.IQueryUserRecordService;
import com.bw30.open.cardpool.util.Md5Encrypt;
import com.bw30.open.common.model.WftChannel;
import com.bw30.open.common.model.WftConnSession;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.wft.common.cardpool.proto.Resp;
import com.bw30.open.wft.common.cardpool.proto.req.QueryUserRecordReq;
import com.bw30.open.wft.common.cardpool.proto.resp.QueryUserRecordResp;

public class QueryUserRecordServiceImpl extends
		ASuperHandle<QueryUserRecordReq, QueryUserRecordResp> implements
		IQueryUserRecordService {
	private static final SimpleDateFormat SDF_YMD = new SimpleDateFormat(
			"yyyy-MM-dd");

	@Resource
	private ConnSessionService connSessionService;

	public void setConnSessionService(ConnSessionService connSessionService) {
		this.connSessionService = connSessionService;
	}

	@Override
	protected QueryUserRecordResp doHandle(QueryUserRecordReq t1)
			throws Exception {
		QueryUserRecordResp qurr = new QueryUserRecordResp();
		qurr.setOpenid(t1.getOpenid());
		qurr.setStandby(t1.getStandby());

		if (!this.checkParam(t1)) {
			qurr.setResult(Resp.RESULT_ERROR_PARAM);
			return qurr;
		}
		
		if(!this.checkSign(t1)){
			qurr.setResult(Resp.RESULT_ERROR_PARAM);
			return qurr;
		}

		String openid = t1.getOpenid();
		Date startDate = SDF_YMD.parse(t1.getStartdate());
		Date endDate = SDF_YMD.parse(t1.getEnddate());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endDate);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		List<WftConnSession> csList = this.connSessionService.getRecord(openid,
				t1.getPartner(), startDate, calendar.getTime());
		qurr.setResult(Resp.RESULT_OK);
		if (null != csList && 0 < csList.size()) {
			List<QueryUserRecordResp.Record> rList = new ArrayList<QueryUserRecordResp.Record>(
					csList.size());
			for (WftConnSession cs : csList) {
				if(WftConnSession.CONN_RECORD_FALG_YES == cs.getMflag()){//已对账，给实际消耗时长
					QueryUserRecordResp.Record r = new QueryUserRecordResp.Record();
					r.setOpid(cs.getSsid().toUpperCase().contains(WftOperator.OP_SSID_CMCC) ? 1 : 2);
					r.setStarttime(SDF_YMD_HMS.format(cs.getBstimeop()));
					r.setEndtime(SDF_YMD_HMS.format(cs.getBetimeop()));
					r.setTimelen(cs.getUtvalueop());
					rList.add(r);
				}
//				else{//未对账，则给北纬平台记录时长
//					r.setStarttime(SDF_YMD_HMS.format(cs.getBstime()));
//					r.setEndtime(SDF_YMD_HMS.format(cs.getEtime()));
//					r.setTimelen(cs.getUtvalue());
//				}
//				
//				rList.add(r);
			}
			qurr.setDetaillist(rList);
		}
		return qurr;
	}

	private boolean checkParam(QueryUserRecordReq t1) {
		if (this.isEmpty(t1.getOpenid()) || this.isEmpty(t1.getStartdate())
				|| this.isEmpty(t1.getEnddate())) {
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
	
	private boolean checkSign(QueryUserRecordReq t1){
		String partner = t1.getPartner();
		if("10001".equals(partner)){
			return true;
		}
		
		String sign = t1.getSign();
		if(null == sign){
			return false;
		}
		WftChannel channel = this.getPartner(partner);
		StringBuffer sb = new StringBuffer("partner=").append(t1.getPartner())
								.append("&openid=").append(t1.getOpenid())
								.append("&startdate=").append(t1.getStartdate())
								.append("&enddate=").append(t1.getEnddate())
								.append("&timestamp=").append(t1.getTimestamp())
								.append("&key=").append(channel.getOpenkey());
		String mySign = Md5Encrypt.md5Enc(sb.toString(), "UTF-8");
		if(mySign.equalsIgnoreCase(sign)){
			return true;
		}
		return false;
	}

}
