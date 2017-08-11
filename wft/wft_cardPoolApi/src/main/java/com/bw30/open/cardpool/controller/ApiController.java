package com.bw30.open.cardpool.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bw30.open.cardpool.service.api.IOpenCardService;
import com.bw30.open.cardpool.service.api.IQueryBalanceService;
import com.bw30.open.cardpool.service.api.IQueryOnlineService;
import com.bw30.open.cardpool.service.api.IQueryRecordService;
import com.bw30.open.cardpool.service.api.IQueryValidityService;
import com.bw30.open.cardpool.service.api.IRechargeService;
import com.bw30.open.cardpool.service.api.IUpdatePwdService;

/**
 * 提供给合作方的接口
 * 
 * @author Jack
 * @date 2014年7月10日 下午4:07:59
 */
@Controller
public class ApiController extends BaseController{
	@Resource
	private IOpenCardService openCardService;
	@Resource
	private IRechargeService rechargeService;
	@Resource
	private IQueryBalanceService queryBalanceService;
	@Resource
	private IQueryValidityService queryValidityService;
	@Resource
	private IUpdatePwdService updatePwdService;
	@Resource
	private IQueryRecordService queryRecordService;
	@Resource
	private IQueryOnlineService queryOnlineService;
	
	/**
	 * 开卡
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value="/openCard.do", method=RequestMethod.POST)
	public void openCard(HttpServletRequest req, HttpServletResponse resp){
		byte[] respData = this.openCardService.doParse(req);
		this.outToResponse(resp, respData);
	}
	
	/**
	 * 充值
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value="/recharge.do", method=RequestMethod.POST)
	public void recharge(HttpServletRequest req, HttpServletResponse resp){
		byte[] respData = this.rechargeService.doParse(req);
		this.outToResponse(resp, respData);
	}
	
	/**
	 * 查询卡剩余时长
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value="/queryBalance.do", method=RequestMethod.POST)
	public void queryBalance(HttpServletRequest req, HttpServletResponse resp){
		byte[] respData = this.queryBalanceService.doParse(req);
		this.outToResponse(resp, respData);
	}
	
	/**
	 * 查询卡有效期
	 * 
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value="/queryValidity.do", method=RequestMethod.POST)
	public void queryValidity(HttpServletRequest req, HttpServletResponse resp){
		byte[] respData = this.queryValidityService.doParse(req);
		this.outToResponse(resp, respData);
	}
	
	/**
	 * 查询卡有效期
	 * 
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value="/updatePwd.do", method=RequestMethod.POST)
	public void updatePwd(HttpServletRequest req, HttpServletResponse resp){
		byte[] respData = this.updatePwdService.doParse(req);
		this.outToResponse(resp, respData);
	}
	
	/**
	 * 卡使用记录查询
	 * 
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value="/queryRecord.do", method=RequestMethod.POST)
	public void queryRecord(HttpServletRequest req, HttpServletResponse resp){
		byte[] respData = this.queryRecordService.doParse(req);
		this.outToResponse(resp, respData);
	}
	
	/**
	 * 在线查询
	 * 
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value="/queryOnline.do", method=RequestMethod.POST)
	public void queryOnline(HttpServletRequest req, HttpServletResponse resp){
		byte[] respData = this.queryOnlineService.doParse(req);
		this.outToResponse(resp, respData);
	}

	public void setOpenCardService(IOpenCardService openCardService) {
		this.openCardService = openCardService;
	}

	public void setRechargeService(IRechargeService rechargeService) {
		this.rechargeService = rechargeService;
	}

	public void setQueryBalanceService(IQueryBalanceService queryBalanceService) {
		this.queryBalanceService = queryBalanceService;
	}

	public void setQueryValidityService(IQueryValidityService queryValidityService) {
		this.queryValidityService = queryValidityService;
	}

	public void setUpdatePwdService(IUpdatePwdService updatePwdService) {
		this.updatePwdService = updatePwdService;
	}

	public void setQueryRecordService(IQueryRecordService queryRecordService) {
		this.queryRecordService = queryRecordService;
	}

	public void setQueryOnlineService(IQueryOnlineService queryOnlineService) {
		this.queryOnlineService = queryOnlineService;
	}
	
}
