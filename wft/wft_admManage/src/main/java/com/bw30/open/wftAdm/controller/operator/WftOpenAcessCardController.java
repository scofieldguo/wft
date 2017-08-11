package com.bw30.open.wftAdm.controller.operator;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bw30.open.common.model.WftChannel;
import com.bw30.open.common.model.stat.WftOpenAcessCardStat;
import com.bw30.open.wft.common.DateUtil;
import com.bw30.open.wftAdm.controller.BaseController;
import com.bw30.open.wftAdm.service.operate.IWftOpenAcessCardStatService;


@Controller
public class WftOpenAcessCardController extends BaseController{

	@Resource
	private IWftOpenAcessCardStatService wftOpenAcessCardStatService;
	@RequestMapping("acessIndex.do")
	public ModelAndView accessIndex(HttpServletRequest request,ModelMap view){
		List<WftChannel> channelList=wftOpenAcessCardStatService.getAllChannel();
		view.put("channelList", channelList);
		return new ModelAndView("stat/acesscard",view);
	}
	@RequestMapping("acessCard.do")
	public ModelAndView accessCard(HttpServletRequest request,ModelMap view){
		Integer pageId=this.getIntegerParam("pageId", request, 1);
		String start=this.getStringParam("start", request, null);
		String channel=this.getStringParam("channel", request, null);
		if(start==null){
			start=DateUtil.formateDate(DateUtil.getNextNumDay(new Date(), -1),"yyyy-MM-dd");
		}
		pager.setPageIndex(pageId);
		List<WftOpenAcessCardStat> list=wftOpenAcessCardStatService.findAcessCardStat(start,pager,channel);
		List<WftChannel> channelList=wftOpenAcessCardStatService.getAllChannel();
		view.put("channelList", channelList);
		view.put("list", list);
		view.put("pager", pager);
		view.put("start",start );
		view.put("channel",channel );
		return new ModelAndView("stat/acesscard",view);
	}
	
}
