package com.bw30.open.cardpool.service.cmcc;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * cmcc在线状态
 * @author Jack
 *
 * 2014年8月12日
 */
@XStreamAlias("WlanGetTerminalStatusRes")
public class WlanGetTerminalStatusRes extends ResBean{
	@XStreamAlias("TerminalNum")
	private Integer terminalNum;
	
	@XStreamAlias("TerminalList")
	private List<TerminalInfo> terminalList;
	
	@XStreamAlias("VerifyCode")
	private String verifyCode;
	
	public Integer getTerminalNum() {
		return terminalNum;
	}

	public void setTerminalNum(Integer terminalNum) {
		this.terminalNum = terminalNum;
	}

	public List<TerminalInfo> getTerminalList() {
		return terminalList;
	}

	public void setTerminalList(List<TerminalInfo> terminalList) {
		this.terminalList = terminalList;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}


	@XStreamAlias("TerminalInfo")
	public static class TerminalInfo{
		@XStreamAlias("wlanacip")
		private String wlanacip;
		
		@XStreamAlias("wlanuserip")
		private String wlanuserip;
		
		@XStreamAlias("wlanacssid")
		private String wlanacssid;
		
		@XStreamAlias("logintime")
		private String logintime;
		
		@XStreamAlias("logintype")
		private String logintype;
		
		@XStreamAlias("nasport")
		private String nasport;
		
		@XStreamAlias("sessionid")
		private String sessionid;
		
		@XStreamAlias("userdomain")
		private String userdomain;
		
		@XStreamAlias("imsi")
		private String imsi;

		public String getWlanacip() {
			return wlanacip;
		}

		public void setWlanacip(String wlanacip) {
			this.wlanacip = wlanacip;
		}

		public String getWlanuserip() {
			return wlanuserip;
		}

		public void setWlanuserip(String wlanuserip) {
			this.wlanuserip = wlanuserip;
		}

		public String getWlanacssid() {
			return wlanacssid;
		}

		public void setWlanacssid(String wlanacssid) {
			this.wlanacssid = wlanacssid;
		}

		public String getLogintime() {
			return logintime;
		}

		public void setLogintime(String logintime) {
			this.logintime = logintime;
		}

		public String getLogintype() {
			return logintype;
		}

		public void setLogintype(String logintype) {
			this.logintype = logintype;
		}

		public String getNasport() {
			return nasport;
		}

		public void setNasport(String nasport) {
			this.nasport = nasport;
		}

		public String getSessionid() {
			return sessionid;
		}

		public void setSessionid(String sessionid) {
			this.sessionid = sessionid;
		}

		public String getUserdomain() {
			return userdomain;
		}

		public void setUserdomain(String userdomain) {
			this.userdomain = userdomain;
		}

		public String getImsi() {
			return imsi;
		}

		public void setImsi(String imsi) {
			this.imsi = imsi;
		}
		
	}

}
