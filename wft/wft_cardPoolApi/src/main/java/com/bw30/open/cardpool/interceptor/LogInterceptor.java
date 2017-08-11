package com.bw30.open.cardpool.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class LogInterceptor implements HandlerInterceptor{
	private static final Logger LOG = Logger.getLogger(LogInterceptor.class);
	
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		
	}

	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		
	}

	public boolean preHandle(HttpServletRequest req, HttpServletResponse arg1,
			Object arg2) throws Exception {
		String uri = req.getRequestURI();
		String ip = req.getRemoteAddr();
		LOG.info("uri:" + uri);
		LOG.info("ip:" + ip);
		return true;
	}

}
