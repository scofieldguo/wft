package com.bw30.open.wftAdm.controller.user;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bw30.open.common.model.OpenPlatformAccount;
import com.bw30.open.common.model.OpenPlatformChargeRecord;
import com.bw30.open.common.model.OpenPlatformNotice;
import com.bw30.open.common.model.OpenPlatformUser;
import com.bw30.open.common.model.stat.WftConsumeStat;
import com.bw30.open.common.model.stat.WftOpenStat;
import com.bw30.open.common.model.stat.WftTotalStat;
import com.bw30.open.wft.common.DateUtil;
import com.bw30.open.wftAdm.controller.BaseController;
import com.bw30.open.wftAdm.service.sms.MailService;
import com.bw30.open.wftAdm.service.user.IWftOpenUserService;
import com.bw30.open.wftAdm.util.Content;

@Controller
public class WftOpenUserController extends BaseController{
		@Resource
		private IWftOpenUserService wftOpenUserService;
		@Resource
		private MailService mailService;
		@RequestMapping("userManage.do")
		public ModelAndView userManage(HttpServletRequest req,ModelMap view){
			Integer pageId=this.getIntegerParam("pageId", req, 1);
			pager.setPageIndex(pageId);
			pager.setRecCount(wftOpenUserService.countAllUser());
			List<OpenPlatformUser> userList = wftOpenUserService.findAllUser(pager);
			for (OpenPlatformUser openPlatformUser : userList) {
				OpenPlatformAccount account =openPlatformUser.getOpenPlatformAccount();
				BigDecimal bd_balace=new BigDecimal(account.getBuytime()).subtract(new BigDecimal(account.getUsetime()));
				String balace_hour=bd_balace.divide(new BigDecimal(3600),2,BigDecimal.ROUND_HALF_UP).toString();
				account.setRetime(balace_hour);
				String mails=Content.readConfig(openPlatformUser.getCompany());
				if("".equals(mails)){
					openPlatformUser.setSend_mails(openPlatformUser.getMail());
				}else{
					openPlatformUser.setSend_mails(mails);
					}
				}
			view.put("userList", userList);
			view.put("pager", pager);
			return new ModelAndView("user/userManage",view);
		}
		
		@RequestMapping("charge.do")
		public ModelAndView charge(HttpServletRequest req,ModelMap view){
			Integer pageId=this.getIntegerParam("pageId", req, 1);
			Integer id = this.getIntegerParam("id", req, null);
			Integer chargeHours = this.getIntegerParam("chargeHours", req, 0);
			Integer chargeCost = this.getIntegerParam("chargeCost", req, 0);
			Integer sendNotice= this.getIntegerParam("sendNotice", req, 0);
			Integer sendMail=this.getIntegerParam("sendMail", req, 0);
			String mails=this.getStringParam("mail", req,null);
			wftOpenUserService.charge(id, chargeHours, chargeCost);//充值
			String now=DateUtil.formateDate(new Date(),"yyyy-MM-dd");
			OpenPlatformUser user=wftOpenUserService.getUserById(id);
			OpenPlatformAccount account=wftOpenUserService.getAcccount(id);
			BigDecimal bd_balace=new BigDecimal(account.getBuytime()).subtract(new BigDecimal(account.getUsetime()));
			String balace_hour=bd_balace.divide(new BigDecimal(3600),2,BigDecimal.ROUND_HALF_UP).toString();
			if(sendNotice==1){
				StringBuilder sb = new StringBuilder();
				String dates[]=now.split("-");
				sb.append(dates[0]).append("年");
				sb.append(dates[1]).append("月").append(dates[2]).append("日");
				String content= "您好：您于"+sb+"已充值："+chargeHours+"小时，截止"+sb+"，"+user.getCompany()+"卡池剩余时长："+balace_hour+"小时。合作愉快！";
				sendNotice(content,id);
			}
			if(sendMail==1){
				String mail[]=mails.trim().split(";");
				for (int i = 0; i < mail.length; i++) {
					mailService.sendchargeMailToCustorme(mail[i].trim(),chargeHours,now,user,balace_hour);
				}
				Content.writeConfig(user.getCompany(), mails);
			}
			pager.setPageIndex(pageId);
			view.put("pager", pager);
			return new ModelAndView("redirect:/userManage.do",view);
		}
		
		@RequestMapping("chargeRecord.do")
		public ModelAndView chargeRecord(HttpServletRequest req,ModelMap view){
			Integer pageId=this.getIntegerParam("pageId", req, 1);
			Integer id = this.getIntegerParam("id", req, null);
			pager.setPageIndex(pageId);
			pager.setRecCount(wftOpenUserService.countRecordByUid(id));
			List<OpenPlatformChargeRecord> chargeRecordList = wftOpenUserService.pageFindRecordByUid(pager, id);
			view.put("chargeRecordList", chargeRecordList);
			view.put("pager", pager);
			view.put("uid", id);
			return new ModelAndView("user/chargeRecord",view);
		}
		
		@RequestMapping("addDefaultChannel.do")
		public void addDefaultChannel(HttpServletRequest req,HttpServletResponse response){
			Integer channel = this.getIntegerParam("channel", req, null);
			String name = this.getStringParam("name", req, null);
			String result = "exist";
			if(!wftOpenUserService.channelExist(channel)){
				wftOpenUserService.addDefaultChannel(channel, name);//不存在，创建默认channel信息
				result = "ok";
			}	
			try {
				response.getWriter().print(result);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@RequestMapping("sendWarn.do")
		public ModelAndView sendWarn(HttpServletRequest request,ModelMap view){
			Integer id = this.getIntegerParam("id", request, null);
			Integer sendMail=this.getIntegerParam("sendMail", request, 0);
			Integer notice=this.getIntegerParam("notice", request, 0);
			String mails=this.getStringParam("mail", request,null);
			String startDate=DateUtil.formateDate( DateUtil.getNextNumDay(new Date(), -1), "yyyy-MM-dd");
			WftConsumeStat totalStat= wftOpenUserService.getYesterDayUseTime(id,startDate);
			OpenPlatformAccount account=wftOpenUserService.getAcccount(id);
			OpenPlatformUser user=wftOpenUserService.getUserById(id);
			BigDecimal bd_balace=new BigDecimal(account.getBuytime()).subtract(new BigDecimal(account.getUsetime()));
			String balace_hour=bd_balace.divide(new BigDecimal(3600),2,BigDecimal.ROUND_HALF_UP).toString();
			String now=DateUtil.formateDate(new Date(),"yyyy-MM-dd");
			String yesteUseTime = "";
			if(totalStat!=null){
			yesteUseTime= BigDecimal.valueOf( totalStat.getTvalue()).divide(new BigDecimal(3600),2,BigDecimal.ROUND_HALF_UP).toString();
			}
			if(notice==1){
				String now_Date=constructDate(now);
				String start_Date=constructDate(startDate);
				String content= "您好:"+start_Date+"消耗："+yesteUseTime+"小时。截止"+now_Date+"，"+user.getCompany()+"卡池还剩约"+balace_hour+"小时，为免影响后续服务，请尽快处理好采购一应事宜！";
				sendNotice(content,id);
			}
			if(sendMail==1){
				String mail[]=mails.split(";");
				for (int i = 0; i < mail.length; i++) {
					mailService.sendWarnMailToCustorme(mail[i],balace_hour,now,startDate,yesteUseTime,user);
				}
				Content.writeConfig(user.getCompany(), mails);
			}
			return  new ModelAndView("redirect:/userManage.do",view);
		}
		
		private void sendNotice(String message,Integer userid){
			OpenPlatformNotice oPlatformNotice =new OpenPlatformNotice();
			oPlatformNotice.setUserid(userid);
			oPlatformNotice.setMessage(message);
			oPlatformNotice.setIntime(new Date());
			oPlatformNotice.setStatus(OpenPlatformNotice.UN_READ);
			wftOpenUserService.sendNoticeToUser(oPlatformNotice);
		}
		
		public void setWftOpenUserService(IWftOpenUserService wftOpenUserService) {
			this.wftOpenUserService = wftOpenUserService;
		}

		public void setMailService(MailService mailService) {
			this.mailService = mailService;
		}
		private String constructDate(String date){
			StringBuilder sb = new StringBuilder();
			String dates[]=date.split("-");
			sb.append(dates[0]).append("年");
			sb.append(dates[1]).append("月").append(dates[2]).append("日");
			return sb.toString();
		}
		
}
