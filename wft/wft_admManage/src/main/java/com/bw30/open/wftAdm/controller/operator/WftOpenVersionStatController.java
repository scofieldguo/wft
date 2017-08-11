package com.bw30.open.wftAdm.controller.operator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bw30.open.common.model.WftChannel;
import com.bw30.open.common.model.versionstat.WftOpenVersionStat;
import com.bw30.open.wft.common.DateUtil;
import com.bw30.open.wftAdm.service.operate.IWftOpenVersionStatService;

@Controller
public class WftOpenVersionStatController {
	@Resource
	private IWftOpenVersionStatService wftOpenVersionStatService;

	@RequestMapping("computeVersion.do")
	public ModelAndView computeVersionData(HttpServletRequest request,ModelMap view){
		List<WftChannel> channelList=wftOpenVersionStatService.findAllChannel();
		view.put("channelList", channelList);
		return new ModelAndView("stat/version",view);
	}
	
	@RequestMapping("statVersion.do")
	public ModelAndView statVersion(HttpServletRequest request,ModelMap view){
		String dairy=request.getParameter("dairy");
		String version=request.getParameter("version");
		String channel=request.getParameter("channel");
		List<WftOpenVersionStat> list=new ArrayList<WftOpenVersionStat>();
		list=wftOpenVersionStatService.getVersionStat(dairy,version,channel);
		List<WftChannel> channelList=wftOpenVersionStatService.findAllChannel();
		if(list==null || list.size()==0){
			Date date=DateUtil.strToDate(dairy);
			Date startDate = DateUtil.getNextNumDay(date, 0);
			Date endDate = DateUtil.getNextNumDay(startDate, 1);
			String dayYMD=DateUtil.formateDate(startDate, "yyyy-MM-dd");
			wftOpenVersionStatService.stat(startDate,endDate,dayYMD,version,channel);
			list=wftOpenVersionStatService.getVersionStat(dairy,version,channel);
		}
		view.put("list", list);
		view.put("channelList", channelList);
		view.put("dairy", dairy);
		view.put("version", version);
		view.put("channel", channel);
		return new ModelAndView("stat/version",view);
	}

	public void setWftOpenVersionStatService(
			IWftOpenVersionStatService wftOpenVersionStatService) {
		this.wftOpenVersionStatService = wftOpenVersionStatService;
	}
	
	
}
