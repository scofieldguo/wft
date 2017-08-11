package com.bw30.open.wftAdm.service.impl;

import java.util.List;

import com.bw30.open.common.dao.mapper.WftAdminMenuMapper;
import com.bw30.open.common.model.login.AdminMenu;
import com.bw30.open.wftAdm.service.IAdminMenuService;

public class AdminMenuServiceImpl implements IAdminMenuService{

	private WftAdminMenuMapper wftAdminMenuMapper;
	@Override
	public List<AdminMenu> getAdminMenuList() {
		return wftAdminMenuMapper.getAdminMenuList();
	}

	@Override
	public List<AdminMenu> getMenuList() {
		return wftAdminMenuMapper.getMenuList();
	}


	@Override
	public List<AdminMenu> getSubMenuByParentId(Integer id) {
		return wftAdminMenuMapper.selectSubMenuByParentId(id);
	}

	@Override
	public void updateMenu(AdminMenu menu) {
		wftAdminMenuMapper.update(menu);
	}

	@Override
	public void deleteMenuById(Integer id) {
		wftAdminMenuMapper.delete(id);
	}
	@Override
	public void addMenu(AdminMenu menu) {
		wftAdminMenuMapper.insert(menu);
	}
	public void setWftAdminMenuMapper(WftAdminMenuMapper wftAdminMenuMapper) {
		this.wftAdminMenuMapper = wftAdminMenuMapper;
	}


}
