package com.bw30.open.wftAdm.controller.operator;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bw30.open.common.dao.mapper.WftChannelMapper;
import com.bw30.open.common.dao.mapper.WftOperatorMapper;
import com.bw30.open.common.model.WftChannel;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.common.model.stat.WftTotalStat;
import com.bw30.open.wft.common.DateUtil;
import com.bw30.open.wftAdm.controller.BaseController;
import com.bw30.open.wftAdm.service.impl.WftTotalStatServiceImpl;

@Controller
public class WftTotalStatController extends BaseController {

	@Resource
	private WftTotalStatServiceImpl wftTotalStatServiceImpl;
	@Resource
	private WftOperatorMapper wftOperatorMapper;
	@Resource
	private WftChannelMapper wftChannelMapper;
	
	
	@RequestMapping("totalStat.do")
	public ModelAndView totalStat(HttpServletRequest request,ModelMap view){
		Date nowDay = new Date();
		Date sDate = DateUtil.getNextNumDay(nowDay, -6);
		String startDate = DateUtil.formateDate(sDate, "yyyy-MM-dd");
		String endDate = DateUtil.formateDate(nowDay, "yyyy-MM-dd");
		List<WftOperator> opList = wftOperatorMapper.findAll();
		List<WftChannel> channelList = wftChannelMapper.findAll();
		view.put("opList", opList);
		view.put("channelList", channelList);
		view.put("startDate", startDate);
		view.put("endDate", endDate);
		return new ModelAndView("stat/totalstat",view);
	}
	
	@RequestMapping("findTotalStat.do")
	public ModelAndView findTotalStat(HttpServletRequest request,ModelMap view){
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String channel = request.getParameter("channel");
		Integer opid = getIntegerParam("opid", request,0);
		Integer pageId=this.getIntegerParam("pageId", request, 1);
		pager.setPageIndex(pageId);
		List<WftTotalStat> totalStatList = wftTotalStatServiceImpl.getWftTotalStats(startDate, endDate, channel, opid, pager);
		List<WftOperator> opList = wftOperatorMapper.findAll();
		List<WftChannel> channelList = wftChannelMapper.findAll();
		view.put("opid", opid);
		view.put("totalStatList", totalStatList);
		view.put("opList", opList);
		view.put("channelList", channelList);
		view.put("startDate", startDate);
		view.put("channel", channel);
		view.put("endDate", endDate);
		view.put("pager", pager);
		return new ModelAndView("stat/totalstat",view);
	}
	@RequestMapping("exportTotalExcel.do")
	public void exportTotalExcel(HttpServletRequest request,HttpServletResponse response){
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String channel = request.getParameter("channel");
		Integer opid = getIntegerParam("opid", request,0);
		wftTotalStatServiceImpl.exportExcel(startDate, endDate, channel, opid, response);
	}

	public void setWftTotalStatServiceImpl(
			WftTotalStatServiceImpl wftTotalStatServiceImpl) {
		this.wftTotalStatServiceImpl = wftTotalStatServiceImpl;
	}

	public void setWftOperatorMapper(WftOperatorMapper wftOperatorMapper) {
		this.wftOperatorMapper = wftOperatorMapper;
	}

	public void setWftChannelMapper(WftChannelMapper wftChannelMapper) {
		this.wftChannelMapper = wftChannelMapper;
	}

}
