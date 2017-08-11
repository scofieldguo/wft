package com.bw30.open.wftAdm.controller.operator;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bw30.open.common.dao.mapper.WftChannelMapper;
import com.bw30.open.common.dao.mapper.WftOperatorMapper;
import com.bw30.open.common.model.WftChannel;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.common.model.stat.WftTenMinLoginStat;
import com.bw30.open.wft.common.DateUtil;
import com.bw30.open.wftAdm.controller.BaseController;
import com.bw30.open.wftAdm.service.impl.WftHourStatServiceImpl;

@Controller
public class WftHourStatController extends BaseController {

	@Resource
	private WftHourStatServiceImpl wftHourStatService;
	@Resource
	private WftOperatorMapper wftOperatorMapper;
	@Resource
	private WftChannelMapper wftChannelMapper;

	@RequestMapping("hourStat.do")
	public ModelAndView hourStat(HttpServletRequest request, ModelMap view) {
		Date nowDay = new Date();
		Date sDate = DateUtil.getNextNumDay(nowDay, -6);
		String startDate = DateUtil.formateDate(sDate, "yyyy-MM-dd");
		String endDate = DateUtil.formateDate(nowDay, "yyyy-MM-dd");
		List<WftOperator> opList = wftOperatorMapper.findAll();
		List<WftChannel> channelList = wftChannelMapper.findAll();
		view.put("startDate", startDate);
		view.put("endDate", endDate);
		view.put("opList", opList);
		view.put("channelList", channelList);
		return new ModelAndView("stat/hourstat", view);
	}

	@RequestMapping("findHourStat.do")
	public ModelAndView findHourStat(HttpServletRequest request, ModelMap view) {
		List<WftOperator> opList = wftOperatorMapper.findAll();
		List<WftChannel> channelList = wftChannelMapper.findAll();
		String channel = getStringParam("channel", request, null);
		Integer opid = getIntegerParam("opid", request, 0);
		String startDate = getStringParam("startDate", request, "");
		String endDate = getStringParam("endDate", request, "");
		List<WftTenMinLoginStat> hourList = wftHourStatService.findEveryHourConnStat(startDate, endDate, opid, channel);
		view.put("opList", opList);
		view.put("hourList", hourList);
		view.put("channelList", channelList);
		view.put("startDate", startDate);
		view.put("endDate", endDate);
		view.put("opid", opid);
		view.put("channel", channel);
		return new ModelAndView("stat/hourstat", view);
	}

	@RequestMapping("findHourStatByDay.do")
	public ModelAndView findHourStatByDay(HttpServletRequest request, ModelMap view){
		List<WftOperator> opList = wftOperatorMapper.findAll();
		String channel = getStringParam("channel", request, null);
		Integer opid = getIntegerParam("opid", request, 0);
		String startDate = getStringParam("startDate", request, "");
		String endDate = getStringParam("endDate", request, "");
		Integer hour = getIntegerParam("hour", request, 0);
		List<WftTenMinLoginStat> hourList = wftHourStatService.findEveryHourConnStatByDay(startDate, endDate, opid, channel, hour);
		WftChannel channelUser = wftChannelMapper.findById(channel);
		view.put("hourList", hourList);
		view.put("opList", opList);
		view.put("startDate", startDate);
		view.put("endDate", endDate);
		view.put("opid", opid);
		view.put("channelUser", channelUser);
		return new ModelAndView("stat/hourOfDayStat", view);
	}
	
	
	public void setWftHourStatService(WftHourStatServiceImpl wftHourStatService) {
		this.wftHourStatService = wftHourStatService;
	}

	public void setWftOperatorMapper(WftOperatorMapper wftOperatorMapper) {
		this.wftOperatorMapper = wftOperatorMapper;
	}

	public void setWftChannelMapper(WftChannelMapper wftChannelMapper) {
		this.wftChannelMapper = wftChannelMapper;
	}

}
