package com.bw30.open.cardpool.service.cmcc;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * cmcc卡使用记录
 * @author Jack
 *
 * 2014年8月12日
 */
@XStreamAlias("WlanGetUsageRes")
public class WlanGetUsageRes extends ResBean{
	@XStreamAlias("UsageDetailList")
	private List<UsageDetail> usageDetailList;
	
	@XStreamAlias("VerifyCode")
	private String verifyCode;
	
	public List<UsageDetail> getUsageDetailList() {
		return usageDetailList;
	}

	public void setUsageDetailList(List<UsageDetail> usageDetailList) {
		this.usageDetailList = usageDetailList;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	@XStreamAlias("UsageDetail")
	public static class UsageDetail{
		@XStreamAlias("StartTime")
		private String startTime;
		
		@XStreamAlias("StopTime")
		private String stopTime;
		
		@XStreamAlias("ResUsage")
		private Integer resUsage;
		
		@XStreamAlias("TotalFlow")
		private Integer totalFlow;
		
		@XStreamAlias("PkgType")
		private Integer pkgType;
		
		@XStreamAlias("AuthenType")
		private Integer authenType;
		
		public String getStartTime() {
			return startTime;
		}
		public void setStartTime(String startTime) {
			this.startTime = startTime;
		}
		public String getStopTime() {
			return stopTime;
		}
		public void setStopTime(String stopTime) {
			this.stopTime = stopTime;
		}
		public Integer getResUsage() {
			return resUsage;
		}
		public void setResUsage(Integer resUsage) {
			this.resUsage = resUsage;
		}
		public Integer getTotalFlow() {
			return totalFlow;
		}
		public void setTotalFlow(Integer totalFlow) {
			this.totalFlow = totalFlow;
		}
		public Integer getPkgType() {
			return pkgType;
		}
		public void setPkgType(Integer pkgType) {
			this.pkgType = pkgType;
		}
		public Integer getAuthenType() {
			return authenType;
		}
		public void setAuthenType(Integer authenType) {
			this.authenType = authenType;
		}
	}
	
}
