package com.bw30.openplatform.action;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.util.ServletContextAware;

import com.bw30.open.common.model.OpenPlatformAccount;
import com.bw30.open.common.model.OpenPlatformNotice;
import com.bw30.open.common.model.OpenPlatformUser;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.common.model.stat.WftTotalStat;
import com.bw30.open.wft.common.DateUtil;
import com.bw30.open.wft.common.Pager;
import com.bw30.openplatform.service.IOpenPlayFormAccountService;
import com.bw30.openplatform.service.IOpenPlayFormNoticeService;
import com.bw30.openplatform.service.IStatService;
import com.bw30.openplatform.util.ServletUtil;
import com.opensymphony.xwork2.ActionSupport;

public abstract class BaseAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware, SessionAware,
		ServletContextAware {

	private static final long serialVersionUID = 610218842485200240L;

	private HttpServletRequest request;
	private HttpServletResponse response;
	private Map<String, Object> session;
	private ServletContext servletContext;

	protected Pager pager = new Pager();

	public void setServletRequest(HttpServletRequest req) {
		request = req;
	}

	public void setServletResponse(HttpServletResponse res) {
		response = res;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public Pager getPager() {
		return pager;
	}

	public void setPager(Pager pager) {
		this.pager = pager;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	DecimalFormat df= new DecimalFormat("0.00");
	public IOpenPlayFormAccountService openPlayFormAccountService;
	public IStatService statService;
	public IOpenPlayFormNoticeService openPlayFormNoticeService;
	public OpenPlatformUser user;
	public OpenPlatformAccount useraccount;
	public String balace_hour;
	public Integer imgflag;
	public String balace; //鍓╀綑鏃堕暱
	public Integer flag_detail=0;
	public Integer unnoticecount;
	public WftTotalStat wftTotalStat;
	
	public void common(){
		user = (OpenPlatformUser) ServletUtil.getSessionObject("userData");
		Date endD = DateUtil.getNextNumDay(new Date(), -1);
		Date starD = DateUtil.getNextNumDay(endD, -6);
		String startDate_7 = DateUtil.formateDate(starD, "yyyy-MM-dd");
		String endDate_7 = DateUtil.formateDate(endD, "yyyy-MM-dd");
		List<Integer> useList=statService.getUsedTime(startDate_7, endDate_7, user.getChannelcode(), WftOperator.OP_ID_CTCC);
		BigDecimal recent7=new BigDecimal(0);
		for(int i :useList){
			recent7 =recent7.add(BigDecimal.valueOf(i));
		}
		BigDecimal x=recent7.divide(new BigDecimal(7),BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(10));
		useraccount = openPlayFormAccountService.getAccountByUserId(user
				.getId());
		BigDecimal bd_balace=new BigDecimal(useraccount.getBuytime()).subtract(new BigDecimal(useraccount.getUsetime()));
		balace_hour=bd_balace.divide(new BigDecimal(3600),2,BigDecimal.ROUND_HALF_UP).toString();
		if(Float.valueOf(balace_hour)>=10000 || Float.valueOf(balace_hour)<=-10000 ){
			balace=df.format((Float.valueOf(balace_hour)/10000))+"w";
			flag_detail=1;
		}else{
			balace=df.format((Float.valueOf(balace_hour)));
		}
		if(bd_balace.compareTo(x)==1){
			imgflag=0;
		}else{
			imgflag=1;
		}
		unnoticecount=openPlayFormNoticeService.findNotice(user.getId(),OpenPlatformNotice.UN_READ,null).size();
		String lastday=DateUtil.formateDate(DateUtil.getNextNumDay(new Date(), -1),"yyyy-MM-dd");
		wftTotalStat=statService.getLastDayStat(lastday, lastday,
				user.getChannelcode(), WftOperator.OP_ID_CTCC);
	}

	public OpenPlatformUser getUser() {
		return user;
	}

	public void setUser(OpenPlatformUser user) {
		this.user = user;
	}

	public OpenPlatformAccount getUseraccount() {
		return useraccount;
	}

	public void setUseraccount(OpenPlatformAccount useraccount) {
		this.useraccount = useraccount;
	}

	public String getBalace_hour() {
		return balace_hour;
	}

	public void setBalace_hour(String balace_hour) {
		this.balace_hour = balace_hour;
	}

	public Integer getImgflag() {
		return imgflag;
	}

	public void setImgflag(Integer imgflag) {
		this.imgflag = imgflag;
	}

	public String getBalace() {
		return balace;
	}

	public void setBalace(String balace) {
		this.balace = balace;
	}

	public Integer getFlag_detail() {
		return flag_detail;
	}

	public void setFlag_detail(Integer flag_detail) {
		this.flag_detail = flag_detail;
	}

	public Integer getUnnoticecount() {
		return unnoticecount;
	}

	public void setUnnoticecount(Integer unnoticecount) {
		this.unnoticecount = unnoticecount;
	}

	public WftTotalStat getWftTotalStat() {
		return wftTotalStat;
	}

	public void setWftTotalStat(WftTotalStat wftTotalStat) {
		this.wftTotalStat = wftTotalStat;
	}

	public void setOpenPlayFormAccountService(IOpenPlayFormAccountService openPlayFormAccountService) {
		this.openPlayFormAccountService = openPlayFormAccountService;
	}


	public void setStatService(IStatService statService) {
		this.statService = statService;
	}

	public void setOpenPlayFormNoticeService(IOpenPlayFormNoticeService openPlayFormNoticeService) {
		this.openPlayFormNoticeService = openPlayFormNoticeService;
	}
	
	
	
}
