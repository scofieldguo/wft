package com.bw30.openplatform.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSON;
import com.bw30.open.common.model.OpenPlatformApp;
import com.bw30.open.common.model.OpenPlatformAppType;
import com.bw30.open.common.model.OpenPlatformNotice;
import com.bw30.open.common.model.OpenPlatformUser;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.common.model.stat.WftConsumeStat;
import com.bw30.open.common.model.stat.WftOpenPlatFormStat;
import com.bw30.open.common.support.DynamicSqlParameter;
import com.bw30.open.wft.common.DateUtil;
import com.bw30.openplatform.service.IOpenPlayFormAppService;
import com.bw30.openplatform.service.IOpenPlayFormAppTypeService;
import com.bw30.openplatform.service.IUserService;
import com.bw30.openplatform.service.IWftOperatorService;
import com.bw30.openplatform.util.ServletUtil;

public class ManageAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OpenPlatformUser user;
	private OpenPlatformApp userapp;
	private List<OpenPlatformApp> userappList;
	private List<WftOperator> opList;
	private List<OpenPlatformAppType> apptypeList;

	private IOpenPlayFormAppService openPlayFormAppService;
	private List<WftOpenPlatFormStat> wftTotalStatList;
	private IOpenPlayFormAppTypeService openPlayFormAppTypeService;
	private IWftOperatorService wftOperatorService;
	private Integer appid;
	private String newpassword;
	private String oldpassword;
	private IUserService userService;
	private String opids;

	private Integer day;
	private Integer opid;
	private String startDate;
	private String endDate;
	private String mail;
	private List<OpenPlatformNotice> noticeList;
	private OpenPlatformNotice notice;
	private String usedTime;
	private Integer interday;
	DecimalFormat df= new DecimalFormat("0.00");
	public String account() {
		common();
		user = (OpenPlatformUser) ServletUtil.getSessionObject("userData");
		String firstDayOfMonth=DateUtil.formateDate(DateUtil.getFirstDayOfMonth(),"yyyy-MM-dd");
		String today=DateUtil.formateDate(new Date(),"yyyy-MM-dd");
		List<Integer> useList=statService.getUsedTime(firstDayOfMonth, today, user.getChannelcode(), WftOperator.OP_ID_CTCC);
		BigDecimal bd=new BigDecimal(0);
		for(int i :useList){
			bd =bd.add(BigDecimal.valueOf(i));
		}
		usedTime= bd.divide(new BigDecimal(3600),2,BigDecimal.ROUND_HALF_UP).toString();
		if(Float.valueOf(usedTime)>10000){
			usedTime=df.format(Float.valueOf(usedTime)/10000)+"w";
		}else{
			usedTime=df.format(Float.valueOf(usedTime));
		}
		return "accountUrl";
	}


	
	
	public String stat() {
		common();
		user = (OpenPlatformUser) ServletUtil.getSessionObject("userData");
		String opidStr = user.getOpids();
		opList = getOperatorList(opidStr);
		return "statUrl";
	}

	public String findData() {
		common();
		user = (OpenPlatformUser) ServletUtil.getSessionObject("userData");
		String opidStr = user.getOpids();
		opList = getOperatorList(opidStr);
		wftTotalStatList = statService.getWftTotalStats(startDate, endDate,
				user.getChannelcode(), opid, pager);
		return "statUrl";
	}

	public void exportExcel(){
		user = (OpenPlatformUser) ServletUtil.getSessionObject("userData");
		statService.exportExcel(startDate,endDate,user.getChannelcode(),opid,getResponse());
	}
	
	public void getDate(){
		PrintWriter out = null;
		try {
			out = getResponse().getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Date nowDate = new Date();
		if (null != day) {
			Date endD = DateUtil.getNextNumDay(nowDate, -1);
			Date starD = DateUtil.getNextNumDay(endD, -day);
			startDate = DateUtil.formateDate(starD, "yyyy-MM-dd");
			endDate = DateUtil.formateDate(endD, "yyyy-MM-dd");
		}
		out.print(startDate+","+endDate);
	}

	private List<WftOperator> getOperatorList(String opids){
		List<WftOperator> opList = new ArrayList<WftOperator>();
		if (null != opids && !"".endsWith(opids)){
			String opidArray[] = opids.trim().split(",");
			for (String opid : opidArray) {
				WftOperator operator = wftOperatorService.findById(Integer
						.parseInt(opid.trim()));
				opList.add(operator);
			}
		}
		return opList;
	}
	
	public String app() {
		user = (OpenPlatformUser) ServletUtil.getSessionObject("userData");
		userappList = openPlayFormAppService.getAppByUserId(user.getId());
		opList = wftOperatorService.findOperator(); // 鍙敤wifi杩愯惀鍟�
		apptypeList = openPlayFormAppTypeService
				.findAppType(OpenPlatformAppType.STATUS_ON);
		return "appUrl";
	}

	/*public String appcreat() {
		opList = wftOperatorService.findOperator(); // 鍙敤wifi杩愯惀鍟�
		apptypeList = openPlayFormAppTypeService
				.findAppType(OpenPlatformAppType.STATUS_ON);
	}*/

	public String creatApp() {
		user = (OpenPlatformUser) ServletUtil.getSessionObject("userData");
		userapp.setUid(user.getId());
		userapp.setOpenkey("ssss");
		userapp.setStatus(OpenPlatformApp.STATUS_TEST);
		userapp.setCtime(new Date());
		openPlayFormAppService.addUserApp(userapp);
		return "creatappSuccess";
	}

	public String updateapp() {
		openPlayFormAppService.updateApp(userapp);
		return "updateSuccess";
	}

	public void applyKey() {
		userapp = new OpenPlatformApp();
		userapp.setId(appid);
		userapp.setStatus(OpenPlatformApp.STATUS_EXAMINE);
		openPlayFormAppService.updateApp(userapp);
	}

	public String password() {
		common();
		user = (OpenPlatformUser) ServletUtil.getSessionObject("userData");
		return "passwordUrl";

	}

	public void changePassword() {
		user = (OpenPlatformUser) ServletUtil.getSessionObject("userData");
		String strs[] = {"password"};
		Object objs[] = {newpassword};
		DynamicSqlParameter params = new DynamicSqlParameter(strs, objs);
		userService.updateByMap(user.getId(), params);
		ServletUtil.setSessionObject("userData", null);
	}

	public void comparePassword(){
		PrintWriter out = null;
		try {
			out = ServletActionContext.getResponse().getWriter();
			user = (OpenPlatformUser) ServletUtil.getSessionObject("userData");
			if (oldpassword.equals(user.getPwd())) {
				out.print("1");
			} else {
				out.print("0");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public String user() {
		common();
		user = (OpenPlatformUser) ServletUtil.getSessionObject("userData");
		opList = wftOperatorService.findOperator();
		Map<String, String> map = new HashMap<String, String>();
		for (WftOperator operator : opList) {
			map.put(operator.getId().toString(), operator.getName());
		}
		StringBuffer sb = new StringBuffer();
		if (user.getOpids() != null) {
			String[] op = user.getOpids().split(",");
			if (op.length > 1) {
				for (int i = 0; i < op.length; i++) {
					if (map.get(op[i].trim()) != null) {
						if (i != op.length - 1) {
							sb.append(map.get(op[i].trim())).append(",");
						} else {
							sb.append(map.get(op[i].trim()));
						}
					}
				}
				opids=sb.toString();
			}else {
				opids=map.get(user.getOpids());
			}
			
		} 
		return "userinfo";
	}

	public String userInfo() {
		common();
		user = (OpenPlatformUser) ServletUtil.getSessionObject("userData");
		opList = wftOperatorService.findOperator();
		return "useredit";
	}

	public String changeInfo() {
		if (userService.updateUser(user)) {
			ServletUtil.setSessionObject("userData", user);
			return "user";// 鏇存柊涓汉淇℃伅鎴愬姛
		}
		return "fail";
	}
	
	public String macstat(){
		common();
		user = (OpenPlatformUser) ServletUtil.getSessionObject("userData");
		String opidStr = user.getOpids();
		opList = getOperatorList(opidStr);
		return "macUrl";
	}
	
	public String findMacData() {
		common();
		user = (OpenPlatformUser) ServletUtil.getSessionObject("userData");
		String opidStr = user.getOpids();
		opList = getOperatorList(opidStr);
		wftTotalStatList = statService.getWftMacStats(startDate, endDate,
				user.getChannelcode(), opid, pager);
		return "macUrl";
	}

	public void exportMacExcel(){
		user = (OpenPlatformUser) ServletUtil.getSessionObject("userData");
		String opidStr = user.getOpids();
		opList = getOperatorList(opidStr);
		statService.exportMacExcel(startDate, endDate,
				user.getChannelcode(), opid, getResponse());
	}
	
	public String notice(){
		common();
		noticeList=openPlayFormNoticeService.findNotice(user.getId(),null,pager);
		user = (OpenPlatformUser) ServletUtil.getSessionObject("userData");
		openPlayFormNoticeService.updateNotice(user.getId());
		return "notice";
	}
	
	public String noticeDelete(){
		common();
		openPlayFormNoticeService.delete(notice);
		noticeList=openPlayFormNoticeService.findNotice(user.getId(),null,pager);
		return "notice";
		
	}
	public void consumeTimeConnChart(){
		System.out.println("consume conn start====>");
		user = (OpenPlatformUser) ServletUtil.getSessionObject("userData");
		String startday=DateUtil.formateDate(DateUtil.getNextNumDay(new Date(), interday), "yyyy-MM-dd");
		String endday=DateUtil.formateDate(new Date(),"yyyy-MM-dd");
		List<WftOpenPlatFormStat> list=null;
		try {
			list=statService.getIntervalConnStat(startday, endday, user.getChannelcode(),WftOperator.OP_ID_CTCC);
		} catch (Exception e) {
			e.printStackTrace();
		}
		PrintWriter out = null;
		try {
			out = getResponse().getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.print(JSON.toJSONString(list));
		
		
	}
	
	public void consumeTimeChart(){
		System.out.println("consume time start====>");
		user = (OpenPlatformUser) ServletUtil.getSessionObject("userData");
		String startday=DateUtil.formateDate(DateUtil.getNextNumDay(new Date(), interday), "yyyy-MM-dd");
		String endday=DateUtil.formateDate(new Date(),"yyyy-MM-dd");
		List<WftConsumeStat> list=null;
		try {
			list=statService.getIntervalStat(startday, endday, user.getChannelcode(),WftOperator.OP_ID_CTCC);
			for (WftConsumeStat wftConsumeStat : list) {
				String hour_utvalueop=new BigDecimal(wftConsumeStat.getTvalue()).divide(new BigDecimal(3600),0,BigDecimal.ROUND_HALF_UP).toString();
				wftConsumeStat.setUtvaluel(Long.valueOf(hour_utvalueop));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		PrintWriter out = null;
		try {
			out = getResponse().getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.print(JSON.toJSONString(list));
	}
	
	public Integer getInterday() {
		return interday;
	}

	public void setInterday(Integer interday) {
		this.interday = interday;
	}


	public OpenPlatformApp getUserapp() {
		return userapp;
	}

	public void setUserapp(OpenPlatformApp userapp) {
		this.userapp = userapp;
	}

	public List<OpenPlatformApp> getUserappList() {
		return userappList;
	}

	public void setUserappList(List<OpenPlatformApp> userappList) {
		this.userappList = userappList;
	}


	public void setOpenPlayFormAppService(
			IOpenPlayFormAppService openPlayFormAppService) {
		this.openPlayFormAppService = openPlayFormAppService;
	}

	public List<WftOperator> getOpList() {
		return opList;
	}

	public void setOpList(List<WftOperator> opList) {
		this.opList = opList;
	}

	public void setWftOperatorService(IWftOperatorService wftOperatorService) {
		this.wftOperatorService = wftOperatorService;
	}

	public List<OpenPlatformAppType> getApptypeList() {
		return apptypeList;
	}

	public void setApptypeList(List<OpenPlatformAppType> apptypeList) {
		this.apptypeList = apptypeList;
	}

	public void setOpenPlayFormAppTypeService(
			IOpenPlayFormAppTypeService openPlayFormAppTypeService) {
		this.openPlayFormAppTypeService = openPlayFormAppTypeService;
	}

	public Integer getAppid() {
		return appid;
	}

	public void setAppid(Integer appid) {
		this.appid = appid;
	}

	public String getNewpassword() {
		return newpassword;
	}

	public void setNewpassword(String newpassword) {
		this.newpassword = newpassword;
	}

	public String getOldpassword() {
		return oldpassword;
	}

	public void setOldpassword(String oldpassword) {
		this.oldpassword = oldpassword;
	}
	public Integer getOpid() {
		return opid;
	}

	public void setOpid(Integer opid) {
		this.opid = opid;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public List<WftOpenPlatFormStat> getWftTotalStatList() {
		return wftTotalStatList;
	}

	public void setWftTotalStatList(List<WftOpenPlatFormStat> wftTotalStatList) {
		this.wftTotalStatList = wftTotalStatList;
	}


	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public String getOpids() {
		return opids;
	}

	public void setOpids(String opids) {
		this.opids = opids;
	}


	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public List<OpenPlatformNotice> getNoticeList() {
		return noticeList;
	}

	public void setNoticeList(List<OpenPlatformNotice> noticeList) {
		this.noticeList = noticeList;
	}


	public OpenPlatformNotice getNotice() {
		return notice;
	}

	public void setNotice(OpenPlatformNotice notice) {
		this.notice = notice;
	}


	public String getUsedTime() {
		return usedTime;
	}

	public void setUsedTime(String usedTime) {
		this.usedTime = usedTime;
	}





	
}
