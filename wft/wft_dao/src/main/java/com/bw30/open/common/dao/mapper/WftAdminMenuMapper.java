package com.bw30.open.common.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bw30.open.common.dao.mapper.common.GenericMapper;
import com.bw30.open.common.model.login.AdminMenu;

public interface WftAdminMenuMapper extends GenericMapper<AdminMenu>{

	List<AdminMenu> getAdminMenuList();

	List<AdminMenu> getMenuList();

	List<AdminMenu> selectSubMenuByParentId(@Param("id")Integer id);

}
