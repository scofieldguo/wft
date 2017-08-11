package com.bw30.open.wftAdm.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.bw30.open.wft.common.Pager;

public class BaseController {
	private static final Logger LOG = Logger.getLogger(BaseController.class);
	
	protected static final SimpleDateFormat SDF_YMD = new SimpleDateFormat("yyyy-MM-dd");
	protected static final SimpleDateFormat SDF_YMD_HM = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	protected static final SimpleDateFormat SDF_YMD_HMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	protected Pager pager = new Pager();
	
	/**
	 * 返回错误页面
	 * 
	 * @param operate 操作描述
	 * @param errMsg 错误描述
	 * 
	 * @return
	 */
	public ModelAndView errorView(String operate, String errMsg){
		ModelMap viewMap = new ModelMap();
		viewMap.addAttribute("operate", operate);
		viewMap.addAttribute("errMsg", errMsg);
		return new ModelAndView("error", viewMap);
	}
	
	/**
	 * 获取整形参数值
	 * 
	 * @param param
	 * @param req
	 * @param defValue 默认值
	 * @return
	 */
	public Integer getIntegerParam(String param, HttpServletRequest req, Integer defValue){
		String v = req.getParameter(param);
		if(null != v){
			v = v.trim();
			try{
				return Integer.parseInt(v);
			}catch(NumberFormatException nfe){
//				LOG.warn(String.format("req param invalid: %s = %s", param, v));
				return defValue;
			}
		}
		return defValue;
	}
	
	/**
	 * 获取浮点参数值
	 * 
	 * @param param
	 * @param req
	 * @param defValue 默认值
	 * @return
	 */
	public Double getDoubleParam(String param, HttpServletRequest req, Double defValue){
		String v = req.getParameter(param);
		if(null != v){
			v = v.trim();
			try{
				return Double.parseDouble(v);
			}catch(NumberFormatException nfe){
//				LOG.warn(String.format("req param invalid: %s = %s", param, v));
				return defValue;
			}
		}
		return defValue;
	}
	
	/**
	 * 获取字符串参数值
	 * 
	 * @param param
	 * @param req
	 * @param defValue 默认值
	 * @return
	 */
	public String getStringParam(String param, HttpServletRequest req, String defValue){
		String v = req.getParameter(param);
		if(null != v && 0 < v.trim().length()){
			return v.trim();
		}else{
			return defValue;
		}
	}
	
	/**
	 * 获取日期参数值，yyyy-MM-dd
	 * 
	 * @param param
	 * @param req
	 * @return
	 */
	public Date getDateParamForYMD(String param, HttpServletRequest req){
		return this.getDateParam(param, req, SDF_YMD);
	}

	/**
	 * 获取日期参数值，yyyy-MM-dd HH:mm:ss
	 * 
	 * @param param
	 * @param req
	 * @return
	 */
	public Date getDateParamForYMDHMS(String param, HttpServletRequest req){
		return this.getDateParam(param, req, SDF_YMD_HMS);
	}
	
	/**
	 * 获取参数值数组
	 * 
	 * @param param
	 * @param req
	 * @return
	 */
	public String[] getParams(String param, HttpServletRequest req){
		return req.getParameterValues(param);
	}
	
	/**
	 * 获取时间类型的参数值
	 * 
	 * @param param
	 * @param req
	 * @param sdf 日期格式化
	 * @return
	 */
	protected Date getDateParam(String param, HttpServletRequest req, SimpleDateFormat sdf){
		String v = req.getParameter(param);
		if(null != v && 0 < v.trim().length()){
			v = v.trim();
			try{
				return sdf.parse(v);
			}catch(Exception e){
//				LOG.warn(String.format("req param invalid: %s=%s", param, v));
				return null;
			}
		}
		return null;
	}
	
	
	/**
	 * ajax返回json
	 * 
	 * @param jsonStr json串
	 * @param response
	 */
	public void returnJson(String jsonStr, HttpServletResponse response){
		try{
//			response.setCharacterEncoding("UTF-8");
			response.setContentType("applation/json;charset=utf-8");
			response.getWriter().print(jsonStr);
		}catch(Exception e){
			LOG.error("ajax return ERROR", e);
		}
	}
	
	/**
	 * ajax返回json
	 * 
	 * @param obj 可转换为json的实例
	 * @param response
	 */
	public void returnJson(Object obj, HttpServletResponse response){
		String jsonStr = null;
		try{
			jsonStr = JSON.toJSONString(obj);
		}catch(Exception e){
			LOG.error("SQL ERROR", e);
			jsonStr = "{\"flag\":0}";
		}
		
		try{
//			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().print(jsonStr);
		}catch(Exception e){
			LOG.error("ajax return ERROR", e);
		}
	}
	
	/**
	 * ajax返回text
	 * 
	 * @param text 字符串
	 * @param response
	 */
	public void returnText(String text, HttpServletResponse response){
		try{
//			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().print(text);
		}catch(Exception e){
			LOG.error("ajax return ERROR", e);
		}
	}

}
