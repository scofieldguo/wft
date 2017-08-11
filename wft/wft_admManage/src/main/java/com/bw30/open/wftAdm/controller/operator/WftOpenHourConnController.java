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
import com.bw30.open.common.model.stat.WftOpenHourSuccessStat;
import com.bw30.open.wft.common.DateUtil;
import com.bw30.open.wftAdm.controller.BaseController;
import com.bw30.open.wftAdm.service.IWftOpenChannelService;
import com.bw30.open.wftAdm.service.operate.IWftOpenHourService;

@Controller
public class WftOpenHourConnController extends BaseController{

	@Resource
	public IWftOpenHourService wftOpenHourService; 
	@Resource
	private IWftOpenChannelService wftOpenChannelService;
	
	@RequestMapping("hourConn.do")
	public ModelAndView hourConn(HttpServletRequest request,ModelMap view){
		String dairy=this.getStringParam("dairy",request , null);
		String channel=this.getStringParam("channel", request, null);
		Integer pageId=this.getIntegerParam("pageId", request, 1);
		pager.setPageIndex(pageId);
		if(dairy==null){
			dairy=DateUtil.formateDate(new Date(), "yyyy-MM-dd");
		}
		List<WftOpenHourSuccessStat> list=wftOpenHourService.findByParam(dairy,pager,channel);
		List<WftChannel> channelList=wftOpenChannelService.findAll();
		view.put("openlist", list);
		view.put("pager", pager);
		view.put("channel",channel);
		view.put("channelList", channelList);
		view.put("dairy", dairy);
		return new ModelAndView("stat/hour",view);
	}

	public void setWftOpenHourService(IWftOpenHourService wftOpenHourService) {
		this.wftOpenHourService = wftOpenHourService;
	}

	public void setWftOpenChannelService(
			IWftOpenChannelService wftOpenChannelService) {
		this.wftOpenChannelService = wftOpenChannelService;
	}
	
	
}
