package com.bw30.open.wftAdm.service;

import java.util.List;

import com.bw30.open.common.model.login.AdminMenu;

public interface IAdminMenuService {

	List<AdminMenu> getAdminMenuList();

	List<AdminMenu> getMenuList();

	List<AdminMenu> getSubMenuByParentId(Integer id);

	void updateMenu(AdminMenu menu);

	void deleteMenuById(Integer id);

	void addMenu(AdminMenu menu);

}
