package com.bw30.open.wftAdm.controller.operator;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bw30.open.common.model.WftOperator;
import com.bw30.open.common.model.stat.WftCardUseTempModel;
import com.bw30.open.wft.common.cardpool.rmi.bean.CardPoolBean;
import com.bw30.open.wft.common.cardpool.rmi.service.ICardPoolService;
import com.bw30.open.wftAdm.service.operate.IWftNowCardUseService;

@Controller
public class WftNowCardUserContoller {

	@Resource
	private IWftNowCardUseService wftNowCardUseService;
	@Resource
	private ICardPoolService cardPoolService;
	@RequestMapping("nowCardUse.do")
	public ModelAndView cardUse(HttpServletRequest req,ModelMap view){
		List<WftCardUseTempModel> uselist=wftNowCardUseService.getNowCardUse();
		String balance="";
		try {
			CardPoolBean cardPoolBean=cardPoolService.queryPoolInfo(WftOperator.OP_ID_CTCC);
			if(cardPoolBean!=null){
				balance=String.valueOf(cardPoolBean.getBalance());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		view.put("balance",balance);
		view.put("uselist", uselist);
		return new ModelAndView("stat/carduse",view);
	}
	public void setWftNowCardUseService(IWftNowCardUseService wftNowCardUseService) {
		this.wftNowCardUseService = wftNowCardUseService;
	}
	public void setCardPoolService(ICardPoolService cardPoolService) {
		this.cardPoolService = cardPoolService;
	}
}
