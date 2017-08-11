package com.bw30.open.cardpool.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bw30.open.cardpool.service.api.IOfflineNotifyService;
import com.bw30.open.cardpool.service.api.IOnlineNotifyService;
import com.bw30.open.cardpool.service.api.IQueryUserOnlineService;
import com.bw30.open.cardpool.service.api.IQueryUserRecordService;

@Controller
public class ApiUserController extends BaseController{
	@Resource
	private IQueryUserOnlineService queryUserOnlineService;
	@Resource
	private IQueryUserRecordService queryUserRecordService;
	@Resource
	private IOnlineNotifyService onlineNotifyService;
	@Resource
	private IOfflineNotifyService offlineNotifyService;
	
	/**
	 * 用户在线查询接口
	 * 
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value="/queryUserOnline.do", method=RequestMethod.POST)
	public void queryUserOnline(HttpServletRequest req, HttpServletResponse resp){
		byte[] respData = this.queryUserOnlineService.doParse(req);
		this.outToResponse(resp, respData);
	}
	
	/**
	 * 用户使用记录查询接口
	 * 
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value="/queryUserRecord.do", method=RequestMethod.POST)
	public void queryUserRecord(HttpServletRequest req, HttpServletResponse resp){
		byte[] respData = this.queryUserRecordService.doParse(req);
		this.outToResponse(resp, respData);
	}
	
	/**
	 * 上线通知接口
	 * 
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value="/onlineNotify.do", method=RequestMethod.POST)
	public void onlineNotify(HttpServletRequest req, HttpServletResponse resp){
		byte[] respData = this.onlineNotifyService.doParse(req);
		this.outToResponse(resp, respData);
	}
	
	/**
	 * 下线通知接口
	 * 
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value="/offlineNotify.do", method=RequestMethod.POST)
	public void offlineNotify(HttpServletRequest req, HttpServletResponse resp){
		byte[] respData = this.offlineNotifyService.doParse(req);
		this.outToResponse(resp, respData);
	}

	public void setQueryUserOnlineService(
			IQueryUserOnlineService queryUserOnlineService) {
		this.queryUserOnlineService = queryUserOnlineService;
	}

	public void setQueryUserRecordService(
			IQueryUserRecordService queryUserRecordService) {
		this.queryUserRecordService = queryUserRecordService;
	}

	public void setOnlineNotifyService(IOnlineNotifyService onlineNotifyService) {
		this.onlineNotifyService = onlineNotifyService;
	}

	public void setOfflineNotifyService(IOfflineNotifyService offlineNotifyService) {
		this.offlineNotifyService = offlineNotifyService;
	}
	
}
