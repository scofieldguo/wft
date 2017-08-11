package com.bw30.openplatform.action;

public class HtmlAction extends BaseAction{
	/**
	 * 静态页面访问： 访问连接请满足格式 html_{dir_name}_{file_name}.action
	 */
	private static final long serialVersionUID = 1L;
	
	private String pageName;
	
	public String execute(){
		return SUCCESS;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
}
