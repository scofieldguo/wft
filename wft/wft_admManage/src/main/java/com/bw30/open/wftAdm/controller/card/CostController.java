package com.bw30.open.wftAdm.controller.card;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bw30.open.wftAdm.controller.BaseController;

@Controller
public class CostController extends BaseController{
	
	@RequestMapping("costList.do")
	public ModelAndView list(ModelMap viewMap, HttpServletRequest req) {
		String begTime = this.getStringParam("begTime", req, null);
		String endTime = this.getStringParam("endTime", req, null);
		
		//TODO
		
		viewMap.addAttribute("begTime", begTime);
		viewMap.addAttribute("endTime", endTime);
		return new ModelAndView("card/costReport", viewMap);
	}
	
	@RequestMapping("costExport.do")
	public void export(ModelMap viewMap, HttpServletRequest req, HttpServletResponse resp) {
		String begTime = this.getStringParam("begTime", req, null);
		String endTime = this.getStringParam("endTime", req, null);
		
		//TODO
		
		String jsonStr = "{\"flag\":1}";
		this.returnJson(jsonStr, resp);
	}
	
}
