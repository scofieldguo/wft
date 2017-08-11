package com.bw30.open.wftAdm.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bw30.open.common.model.login.AdminMenu;
import com.bw30.open.common.model.login.WftAdminUser;
import com.bw30.open.wftAdm.service.IAdminMenuService;
import com.bw30.open.wftAdm.service.IUserService;
import com.bw30.open.wftAdm.util.CheckMobile;
@Controller
public class LoginController extends BaseController{
	
	@Resource
	private IUserService userService;
	@Resource
	private IAdminMenuService adminMenuService;
	
	
	@RequestMapping("toLogin.do")
	public ModelAndView goLogin(HttpServletRequest request,ModelMap viewMap,
		@RequestParam(value="loginName",required=false)String loginName,
		@RequestParam(value="errMsg",required=false)String errMsg){
		viewMap.put("loginName", loginName);
		viewMap.put("errMsg", errMsg);
		if(CheckMobile.check(request.getHeader("USER-AGENT").toLowerCase())){
			return new ModelAndView("wap/login",viewMap);
		}
		return new ModelAndView("login/login",viewMap);
	}
	
	@RequestMapping("wapLogin.do")
	public void wapLogin(HttpServletRequest request,HttpServletResponse resp) {
		String loginName = this.getStringParam("loginName", request, null);
		String password = this.getStringParam("password", request, null);
		
		HttpSession session = request.getSession();
		WftAdminUser wu = this.userService.getUserinfo(loginName, password);
		if(wu != null){
			wu.setPassword(null);
			session.setAttribute("user", wu);
			returnJson("1",resp);
		} else{
			returnJson("0",resp);
		}
	}
	
	@RequestMapping("login.do")
	public ModelAndView login(ModelMap viewMap, HttpServletRequest request) {
		String loginName = this.getStringParam("loginName", request, null);
		String password = this.getStringParam("password", request, null);
		HttpSession session = request.getSession();
		WftAdminUser wu = this.userService.getUserinfo(loginName, password);
		if(wu != null){
			wu.setPassword(null);
			session.setAttribute("user", wu);
		} else {
			viewMap.put("loginName", loginName);
			viewMap.put("errMsg", "用户名或密码错误");
			return new ModelAndView("redirect:toLogin.do", viewMap);
		}
		return new ModelAndView("redirect:main.do");
	}
	
	@RequestMapping("main.do")
	public ModelAndView main(ModelMap viewMap, HttpServletRequest request){
		viewMap.put("menuUri", "left.do");
		if(CheckMobile.check(request.getHeader("USER-AGENT").toLowerCase())){
			return new ModelAndView("wap/index", viewMap);
		}
		return new ModelAndView("login/index", viewMap);
	}
	
	
	
	@RequestMapping("wapIndex.do")
	public ModelAndView wapIndex(HttpServletRequest request) {
		return new ModelAndView("wap/index");
	}
	
	@RequestMapping("destroy.do")
	public ModelAndView destory(HttpServletRequest request,ModelMap view){
		request.getSession().invalidate();
		String path = request.getScheme() + "://" + request.getServerName()
				+ ":" + request.getServerPort() + request.getContextPath();
		ModelAndView mav = new ModelAndView("redirect:" + path);
		return mav;
		
	}
	
	@RequestMapping("left.do")
	public ModelAndView left(HttpServletRequest request,ModelMap view){
		List<AdminMenu> menuList = adminMenuService.getAdminMenuList();
		view.put("menuList", menuList);
		return new ModelAndView("menu/left", view);
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}


	public void setAdminMenuService(IAdminMenuService adminMenuService) {
		this.adminMenuService = adminMenuService;
	}
	

}
