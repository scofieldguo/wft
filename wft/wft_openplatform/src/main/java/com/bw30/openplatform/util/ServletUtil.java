package com.bw30.openplatform.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;

public class ServletUtil {

	public static void setActionContextObject(String key, Object obj) {
		ActionContext actionContext = ServletActionContext.getContext();
		actionContext.put(key, obj);
	}

	public static void setSessionObject(String key, Object obj) {
		getSession().setAttribute(key, obj);
	}

	public static Object getSessionObject(String key) {
		return getSession().getAttribute(key);
	}

	public static HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}

	public static HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	private static HttpSession getSession() {
		HttpServletRequest request = getRequest();
		HttpSession session = request.getSession();
		return session;
	}
}
