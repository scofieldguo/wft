package com.bw30.open.cardpool.controller;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class BaseController {
	private static final Logger LOG = Logger.getLogger(BaseController.class);
	
	/**
	 * 请求响应
	 * 
	 * @param resp
	 * @param resObject 响应数据
	 */
	protected void outToResponse(HttpServletResponse resp, byte[] respData){
		try{
			resp.setContentType("text/html;charset=UTF-8");
			resp.setHeader("Pragma", "No-cache");
			resp.setDateHeader("Expires", 0);
			resp.setHeader("Cache-Control", "no-cache");
//			resp.setCharacterEncoding("UTF-8");
			ServletOutputStream sos = resp.getOutputStream();
			respData = (null != respData ? respData : new byte[0]);
			sos.write(respData);
			sos.flush();
		}catch(IOException ioe){
			LOG.error("io exception", ioe);
		}
	}
	
}

