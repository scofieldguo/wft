package com.bw30.openplatform.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.bw30.openplatform.global.Environment;

public class BaseFilter implements Filter {

	private FilterConfig config;

	public void destroy() {
		this.config = null;
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession(true);
		req.setCharacterEncoding("UTF-8");
		Map<String, String> map = Environment.getGlobalMap();
		for (String key : map.keySet()) {
			session.setAttribute(key, map.get(key));
		}
		chain.doFilter(req, response);
	}

	public void init(FilterConfig config) throws ServletException {
		setConfig(config);
	}

	public FilterConfig getConfig() {
		return config;
	}

	public void setConfig(FilterConfig config) {
		this.config = config;
	}

}
