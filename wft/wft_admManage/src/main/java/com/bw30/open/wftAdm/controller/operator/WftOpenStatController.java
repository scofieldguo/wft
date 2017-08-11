package com.bw30.open.wftAdm.controller.operator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bw30.open.common.model.WftChannel;
import com.bw30.open.common.model.stat.WftOpenStat;
import com.bw30.open.wft.common.DateUtil;
import com.bw30.open.wftAdm.controller.BaseController;
import com.bw30.open.wftAdm.service.operate.IWftOpenStatService;

@Controller
public class WftOpenStatController extends BaseController{

	@Resource
	private IWftOpenStatService wftOpenStatService;
	@RequestMapping("stat.do")
	public ModelAndView statOpen(HttpServletRequest req,ModelMap view){
		Integer pageId=this.getIntegerParam("pageId", req, 1);
		String start=this.getStringParam("start", req, null);
		String end=this.getStringParam("end", req, null);
		if(start==null || end ==null){
			start=DateUtil.formateDate(DateUtil.getNextNumDay(new Date(), -1),"yyyy-MM-dd");
			end=DateUtil.formateDate(DateUtil.getNextNumDay(new Date(), -1),"yyyy-MM-dd");
		}
		pager.setPageIndex(pageId);
		List<WftOpenStat> list=wftOpenStatService.findOpenStat(start,end,pager);
		view.put("list", list);
		view.put("pager", pager);
		view.put("start",start );
		view.put("end",end );
		return new ModelAndView("stat/today",view);
	}
	
	@RequestMapping("statExport.do")
	public void statExport(HttpServletRequest request,HttpServletResponse res){
		String start=this.getStringParam("start", request, null);
		String end=this.getStringParam("end", request, null);
		wftOpenStatService.exportExcel(start,end,res);
		
	}
	
	@RequestMapping("statChannel.do")
	public ModelAndView statChannel(HttpServletRequest request,ModelMap view){
		Integer pageId=this.getIntegerParam("pageId", request, 1);
		String start=this.getStringParam("start", request, null);
		String end=this.getStringParam("end", request, null);
		String channel=this.getStringParam("channel", request, null);
		if(start==null || end ==null){
			start=DateUtil.formateDate(DateUtil.getNextNumDay(new Date(), -1),"yyyy-MM-dd");
			end=DateUtil.formateDate(DateUtil.getNextNumDay(new Date(), -1),"yyyy-MM-dd");
		}
		pager.setPageIndex(pageId);
		List<WftOpenStat> list=wftOpenStatService.findOpenStatByChannel(start,end,pager,channel);
		view.put("list", list);
		view.put("pager", pager);
		view.put("start",start );
		view.put("end",end );
		view.put("channel",channel);
		return new ModelAndView("stat/todaychannel",view);
		
	}
	@RequestMapping("statChannelExport.do")
	public void statChannelExport(HttpServletRequest request,HttpServletResponse res){
		String start=this.getStringParam("start", request, null);
		String end=this.getStringParam("end", request, null);
		String channel=this.getStringParam("channel", request, null);
		wftOpenStatService.exportChannelExcel(start,end,res,channel);
		
	}
	
	@RequestMapping("unoteIndex.do")
	public ModelAndView unoteIndex(HttpServletRequest request,ModelMap view){
		List<WftChannel> channelList=wftOpenStatService.findChannel();
		List<Integer> hourList=new ArrayList<Integer>();
		for(int i=0;i<=23;i++){
			hourList.add(i);
		}
		view.put("hourList",hourList);
		view.put("channelList", channelList);
		return new ModelAndView("stat/unote",view);
	}
	
	@RequestMapping("exportUnote.do")
	public void exportUnoteExcel(HttpServletRequest request,HttpServletResponse res){
		String dairy=this.getStringParam("dairy", request, null);
		Integer hour=this.getIntegerParam("hour", request, null);
		String channel=this.getStringParam("channel", request, null);
		Integer opid = this.getIntegerParam("opid", request, 0);
		String note = this.getStringParam("note", request, null);
		Date start;
		Date end;
		if(hour==null || hour.equals("")){
			Date date=DateUtil.strToDate(dairy);
			start=DateUtil.getNextNumDay(date, 0);
			end=DateUtil.getNextNumDay(date, 1);
		}else{
			start=DateUtil.getHourStartDate(DateUtil.strToDate(dairy), hour);
			end=DateUtil.getHourStartDate(DateUtil.strToDate(dairy), hour+1);
		}
		wftOpenStatService.exportUnoteExcel(start,end,opid, channel,note,res);
	}
	public void setWftOpenStatService(IWftOpenStatService wftOpenStatService) {
		this.wftOpenStatService = wftOpenStatService;
	}
}
