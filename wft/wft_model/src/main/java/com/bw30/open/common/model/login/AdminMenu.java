package com.bw30.open.common.model.login;

import java.util.List;

public class AdminMenu {

	private Integer id;
	private String name;
	private Integer parent_id;
	private String url;
	private Integer menu_sequence;
	private AdminMenu parentMenu;
	private List<AdminMenu> subMenuList;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getMenu_sequence() {
		return menu_sequence;
	}
	public void setMenu_sequence(Integer menu_sequence) {
		this.menu_sequence = menu_sequence;
	}
	public AdminMenu getParentMenu() {
		return parentMenu;
	}
	public void setParentMenu(AdminMenu parentMenu) {
		this.parentMenu = parentMenu;
	}
	public Integer getParent_id() {
		return parent_id;
	}
	public void setParent_id(Integer parent_id) {
		this.parent_id = parent_id;
	}
	public List<AdminMenu> getSubMenuList() {
		return subMenuList;
	}
	public void setSubMenuList(List<AdminMenu> subMenuList) {
		this.subMenuList = subMenuList;
	}
	
	
}
