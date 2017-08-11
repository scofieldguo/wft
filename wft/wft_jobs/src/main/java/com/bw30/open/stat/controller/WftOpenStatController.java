package com.bw30.open.stat.controller;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bw30.open.stat.service.IWftAcessCardStatService;
import com.bw30.open.stat.service.IWftOpenStatService;
import com.bw30.open.stat.service.IWftTopCardUseSituationService;
import com.bw30.open.stat.service.impl.WftTotalStatServiceImpl;
import com.bw30.open.wft.common.DateUtil;


@Controller
public class WftOpenStatController {

	
	@Resource
	private IWftOpenStatService wftOpenStatService;
	@Resource
	private IWftAcessCardStatService wftAcessCardStatService;
	@Resource
	private IWftTopCardUseSituationService wftTopCardUseSituationService;
	@Resource
	private WftTotalStatServiceImpl wftTotalStatService;
	
	@RequestMapping("stat.do")
	public ModelAndView stat(HttpServletRequest request,ModelMap view){
		Date date = new Date();
		if(request.getParameter("date")!=null){
			try {
				date=DateUtil.strToDate(request.getParameter("date"));
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("");
			}
		}
		Date startDate = DateUtil.getNextNumDay(date, 0);
		Date endDate = DateUtil.getNextNumDay(startDate, 1);
		String dayYMD=DateUtil.formateDate(startDate, "yyyy-MM-dd");
		wftOpenStatService.statChannelData(startDate,endDate,dayYMD);
		return new ModelAndView("",view);
	}
	
	@RequestMapping("totalStat.do")
	public ModelAndView totalStat(HttpServletRequest request,ModelMap view){
		Date date = new Date();
		if(request.getParameter("date")!=null){
			try {
				date=DateUtil.strToDate(request.getParameter("date"));
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("");
			}
		}
		wftTotalStatService.statAppData(date);
		return new ModelAndView("",view);
	}

	public void stat(){
		Date date = new Date();
		Date startDate = DateUtil.getNextNumDay(date, -1);
		Date endDate = DateUtil.getNextNumDay(startDate, 1);
		String dayYMD=DateUtil.formateDate(startDate, "yyyy-MM-dd");
		wftOpenStatService.statChannelData(startDate,endDate,dayYMD);
	}
	

	public void statCardUse(){
		Date startHour=DateUtil.getBeforeHourDate();
		Date endHour=DateUtil.getAfterHourDate();
		String dayYMD=DateUtil.formateDate(startHour,"yyyy-MM-dd");
		String hour=DateUtil.getHour(startHour);
		wftAcessCardStatService.statCardUse(dayYMD,hour,startHour,endHour);
	}
	
	public  void statTopCardUseSituation(){
		Date startMinute=DateUtil.getBeforeMinuteDate();
		Date endMinute=DateUtil.getAfterMinuteDate();
		String dayYMD=DateUtil.formateDate(startMinute,"yyyy-MM-dd");
		String hour=DateUtil.getHour(startMinute);
		String minute=DateUtil.getMinute(startMinute);
		wftTopCardUseSituationService.stat(dayYMD,hour,minute,startMinute,endMinute);
		
	}
	
	
	public void setWftOpenStatService(IWftOpenStatService wftOpenStatService) {
		this.wftOpenStatService = wftOpenStatService;
	}

	public void setWftAcessCardStatService(
			IWftAcessCardStatService wftAcessCardStatService) {
		this.wftAcessCardStatService = wftAcessCardStatService;
	}

	public void setWftTopCardUseSituationService(
			IWftTopCardUseSituationService wftTopCardUseSituationService) {
		this.wftTopCardUseSituationService = wftTopCardUseSituationService;
	}

	public void setWftTotalStatService(WftTotalStatServiceImpl wftTotalStatService) {
		this.wftTotalStatService = wftTotalStatService;
	}
	public static void main(String[] args) {
		Date date = new Date();
		Date startDate = DateUtil.getNextNumDay(date, -1);
		Date endDate = DateUtil.getNextNumDay(startDate, 1);
		String dayYMD=DateUtil.formateDate(startDate, "yyyy-MM-dd");
		System.out.println(endDate);
	}
}
