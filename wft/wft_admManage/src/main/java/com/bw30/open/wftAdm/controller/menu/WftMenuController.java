package com.bw30.open.wftAdm.controller.menu;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bw30.open.common.model.login.AdminMenu;
import com.bw30.open.wftAdm.controller.BaseController;
import com.bw30.open.wftAdm.service.IAdminMenuService;

@Controller
public class WftMenuController extends BaseController {

	@Resource
	private IAdminMenuService adminMenuService;

	@RequestMapping("menuIndex.do")
	public ModelAndView menuIndex(HttpServletRequest request, ModelMap view) {
		List<AdminMenu> menuList = adminMenuService.getMenuList();
		view.put("menuList", menuList);
		return new ModelAndView("menu/index", view);
	}

	@RequestMapping("getSubMenu.do")
	public ModelAndView getSubMenu(HttpServletRequest request, ModelMap view) {
		Integer id = this.getIntegerParam("id", request, 1);
		List<AdminMenu> menuList = adminMenuService.getSubMenuByParentId(id);
		view.put("menuList", menuList);
		return new ModelAndView("menu/edit", view);
	}

	@RequestMapping("menuSave.do")
	public ModelAndView menuSave(HttpServletRequest request, ModelMap view) {
		String menuList = this.getStringParam("subMenu", request, null);
		return new ModelAndView("", view);
	}

	@RequestMapping("updateMenu.do")
	public ModelAndView update(HttpServletRequest request, ModelMap view) {
		Integer id = this.getIntegerParam("id", request, null);
		Integer paId = this.getIntegerParam("paId", request, null);
		String name = this.getStringParam("name", request, null);
		String url = this.getStringParam("url", request, null);
		AdminMenu menu = new AdminMenu();
		menu.setId(id);
		menu.setUrl(url);
		menu.setName(name);
		adminMenuService.updateMenu(menu);
		List<AdminMenu> menuList = adminMenuService.getMenuList();
		view.put("menuList", menuList);
		view.put("menuId", paId);
		return new ModelAndView("menu/index", view);
	}

	@RequestMapping("deleteMenu.do")
	public ModelAndView deleteMenu(HttpServletRequest request, ModelMap view) {
		Integer id = this.getIntegerParam("id", request, null);
		Integer paId = this.getIntegerParam("paId", request, null);
		adminMenuService.deleteMenuById(id);
		List<AdminMenu> menuList = adminMenuService.getMenuList();
		view.put("menuList", menuList);
		view.put("menuId", paId);
		return new ModelAndView("menu/index", view);
	}
	
	@RequestMapping("addMenu.do")
	public ModelAndView addMenu(HttpServletRequest request, ModelMap view){
		String name = this.getStringParam("name", request, null);
		String url = this.getStringParam("url", request, null);
		Integer paId = this.getIntegerParam("parent_id", request, null);
		AdminMenu menu = new AdminMenu();
		menu.setUrl(url);
		menu.setName(name);
		menu.setParent_id(paId);
		adminMenuService.addMenu(menu);
		List<AdminMenu> menuList = adminMenuService.getMenuList();
		view.put("menuList", menuList);
		return new ModelAndView("menu/index", view);
	}

	public void setAdminMenuService(IAdminMenuService adminMenuService) {
		this.adminMenuService = adminMenuService;
	}

}
