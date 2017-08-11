package com.bw30.open.wftAdm.controller.operator;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bw30.open.wftAdm.service.operate.IIndexService;

@Controller
public class WftOpenIndexController {

	@Resource
	private IIndexService indexService;
	
	@RequestMapping("index.do")
	public ModelAndView login(ModelMap viewMap, HttpServletRequest request) {
		return new ModelAndView("menu/welcome", viewMap);
	}
	
	@RequestMapping("superleft.do")
	public ModelAndView Left(HttpServletRequest request,ModelMap view){
		return new ModelAndView("superleft",view);
	}
}
