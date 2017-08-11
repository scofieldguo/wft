package com.bw30.open.wftAdm.controller.card;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bw30.open.common.dao.mapper.WftSaleCardRecordMapper;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.common.model.sale.WftSaleCardRecord;
import com.bw30.open.wft.common.DateUtil;
import com.bw30.open.wft.common.SessionUtil;
import com.bw30.open.wft.common.cardpool.rmi.bean.CardBean;
import com.bw30.open.wft.common.cardpool.rmi.bean.RechargeBean;
import com.bw30.open.wft.common.cardpool.rmi.service.ICardPoolService;
import com.bw30.open.wftAdm.controller.BaseController;
import com.bw30.open.wftAdm.util.CheckMobile;

@Controller
public class OpenCardController extends BaseController {
	@Resource
	private ICardPoolService cardPoolService;
	
	@Resource
	private WftSaleCardRecordMapper wftSaleCardRecordMapper;
	
	@ResponseBody
	@RequestMapping("openCard.do")
	public WftSaleCardRecord openCard(HttpServletResponse resp,
		@RequestParam(value="tmall_order",required=true)String tmall_order,
		@RequestParam(value="tmall_name",required=false)String tmall_name,
		@RequestParam(value="codeop",required=true)String codeop){
		WftSaleCardRecord cardRecord = null;
		try{
			CardBean card = cardPoolService.openCard("10015",2,codeop, tmall_order);
			if(card!=null){
				cardRecord = new WftSaleCardRecord();
				cardRecord.setCno(card.getNo());
				cardRecord.setPwd(card.getPwd());
				cardRecord.setOrder_id(tmall_order);
				cardRecord.setShop_name(tmall_name);
				cardRecord.setCard_type(Integer.valueOf(codeop));
				cardRecord.setOpen_time(new Date());
				cardRecord.setEnd_time(card.getValidity());
				cardRecord.setDuration(card.getBalance());
				cardRecord.setValidity(DateUtil.formateDate(card.getValidity(), "yyyy-MM-dd HH:mm:ss"));
				wftSaleCardRecordMapper.insert(cardRecord);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return cardRecord;
	}
	@ResponseBody
	@RequestMapping("recharge.do")
	public WftSaleCardRecord recharge(ModelMap viewMap,HttpServletRequest request,
		@RequestParam(value="pageId",required=false)Integer pageIndex,
		@RequestParam(value="card_no",required=false)String cardNo,
		@RequestParam(value="codeop",required=false)String codeop){
		WftSaleCardRecord cardRecord = null;
		if(pageIndex==null){
			pageIndex = 1;
		}
		try{
			String orderId= SessionUtil.getSessionId();
			RechargeBean card = cardPoolService.recharge("10015", null, cardNo, WftOperator.OP_ID_CTCC, codeop, orderId);
			if (card != null) {
				cardRecord = new WftSaleCardRecord();
				cardRecord.setCno(cardNo);
				cardRecord.setEnd_time(card.getValidity());
				cardRecord.setDuration(card.getNewbalance());
				cardRecord.setValidity(DateUtil.formateDate(card.getValidity(), "yyyy-MM-dd HH:mm:ss"));
				wftSaleCardRecordMapper.update(cardRecord);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return cardRecord;
	}
	
	@RequestMapping("to_recharge.do")
	public ModelAndView toRecharge(ModelMap viewMap,HttpServletRequest request,
		@RequestParam(value="pageId",required=false)Integer pageIndex,
		@RequestParam(value="card_no",required=false)String cardNo){
		if(pageIndex==null){
			pageIndex = 1;
		}
		viewMap.put("pageId", pageIndex);
		viewMap.put("cardNo", cardNo);
		System.out.println("cardNo=================="+cardNo);
		return new ModelAndView("wap/recharge",viewMap);
	}
	
	@RequestMapping("records.do")
	public ModelAndView records(ModelMap viewMap,HttpServletRequest request,
		@RequestParam(value="pageId",required=false)Integer pageIndex,
		@RequestParam(value="startDate",required=false)String startDate,
		@RequestParam(value="endDate",required=false)String endDate,
		@RequestParam(value="card_no",required=false)String cardNo){
		if(pageIndex==null){
			pageIndex = 1;
		}
//		if(startDate==null||"".equals(startDate)||endDate==null||"".equals(endDate)){
//			startDate = DateUtil.formateDate(DateUtil.getNextNumDay(new Date(), -7),"yyyy-MM-dd");
//			endDate = DateUtil.formateDate(new Date(),"yyyy-MM-dd");
//		}
		
		pager.setPageIndex(pageIndex);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("cardNo", cardNo);
		System.out.println("cardNo========================"+cardNo);
		paramMap.put("startDate", startDate);
		paramMap.put("endDate", endDate);
		pager.setRecCount(wftSaleCardRecordMapper.countByParam(paramMap));
		List<WftSaleCardRecord> records = wftSaleCardRecordMapper.pageFindByParam(paramMap, pager);
		viewMap.put("records", records);
		viewMap.put("cardNo",cardNo);
		viewMap.put("pager", pager);
		viewMap.put("startDate", startDate);
		viewMap.put("endDate", endDate);
		if(CheckMobile.check(request.getHeader("USER-AGENT").toLowerCase())){
			return new ModelAndView("wap/record",viewMap);
		}
		return new ModelAndView("card/cardTmallSales",viewMap);
	}

	public void setCardPoolService(ICardPoolService cardPoolService) {
		this.cardPoolService = cardPoolService;
	}

	public void setWftSaleCardRecordMapper(
			WftSaleCardRecordMapper wftSaleCardRecordMapper) {
		this.wftSaleCardRecordMapper = wftSaleCardRecordMapper;
	}
	
}
