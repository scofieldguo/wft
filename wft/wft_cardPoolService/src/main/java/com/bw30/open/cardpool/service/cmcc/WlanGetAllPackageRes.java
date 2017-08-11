package com.bw30.open.cardpool.service.cmcc;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * cmcc剩余时长及流量
 * @author Jack
 *
 * 2014年8月12日
 */
@XStreamAlias("WlanGetAllPackageRes")
public class WlanGetAllPackageRes extends ResBean{
	@XStreamAlias("CurFlowPkg")
	private List<PackageInfo> curFlowPkg;
	
	@XStreamAlias("NextFlowPkg")
	private List<PackageInfo> nextFlowPkg;
	
	@XStreamAlias("CurTimePkg")
	private List<PackageInfo> curTimePkg;
	
	@XStreamAlias("NextTimePkg")
	private List<PackageInfo> nextTimePkg;
	
	@XStreamAlias("VerifyCode")
	private String verifyCode;
	
	public List<PackageInfo> getCurFlowPkg() {
		return curFlowPkg;
	}

	public void setCurFlowPkg(List<PackageInfo> curFlowPkg) {
		this.curFlowPkg = curFlowPkg;
	}

	public List<PackageInfo> getNextFlowPkg() {
		return nextFlowPkg;
	}

	public void setNextFlowPkg(List<PackageInfo> nextFlowPkg) {
		this.nextFlowPkg = nextFlowPkg;
	}

	public List<PackageInfo> getCurTimePkg() {
		return curTimePkg;
	}

	public void setCurTimePkg(List<PackageInfo> curTimePkg) {
		this.curTimePkg = curTimePkg;
	}

	public List<PackageInfo> getNextTimePkg() {
		return nextTimePkg;
	}

	public void setNextTimePkg(List<PackageInfo> nextTimePkg) {
		this.nextTimePkg = nextTimePkg;
	}
	
	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	@XStreamAlias("PackageInfo")
	public static class PackageInfo{
		@XStreamAlias("EffDate")
		private String effDate;
		
		@XStreamAlias("ExpDate")
		private String expDate;
		
		@XStreamAlias("PkgType")
		private Integer pkgType;
		
		@XStreamAlias("PkgCode")
		private String pkgCode;
		
		@XStreamAlias("PkgName")
		private String pkgName;
		
		@XStreamAlias("PkgDesc")
		private String pkgDesc;
		
		@XStreamAlias("PkgPrice")
		private Float pkgPrice;//元
		
		@XStreamAlias("PkgFlow")
		private long pkgFlow;
		
		@XStreamAlias("PkgTimelong")
		private long pkgTimelong;
		
		@XStreamAlias("PkgUnit")
		private Integer pkgUnit;
		
		@XStreamAlias("PkgSMSOrder")
		private String pkgSMSOrder;
		
		@XStreamAlias("PkgFreeRes")
		private String pkgFreeRes;

		public String getEffDate() {
			return effDate;
		}

		public void setEffDate(String effDate) {
			this.effDate = effDate;
		}

		public String getExpDate() {
			return expDate;
		}

		public void setExpDate(String expDate) {
			this.expDate = expDate;
		}

		public Integer getPkgType() {
			return pkgType;
		}

		public void setPkgType(Integer pkgType) {
			this.pkgType = pkgType;
		}

		public String getPkgCode() {
			return pkgCode;
		}

		public void setPkgCode(String pkgCode) {
			this.pkgCode = pkgCode;
		}

		public String getPkgName() {
			return pkgName;
		}

		public void setPkgName(String pkgName) {
			this.pkgName = pkgName;
		}

		public String getPkgDesc() {
			return pkgDesc;
		}

		public void setPkgDesc(String pkgDesc) {
			this.pkgDesc = pkgDesc;
		}

		public Float getPkgPrice() {
			return pkgPrice;
		}

		public void setPkgPrice(Float pkgPrice) {
			this.pkgPrice = pkgPrice;
		}

		public long getPkgFlow() {
			return pkgFlow;
		}

		public void setPkgFlow(long pkgFlow) {
			this.pkgFlow = pkgFlow;
		}

		public long getPkgTimelong() {
			return pkgTimelong;
		}

		public void setPkgTimelong(long pkgTimelong) {
			this.pkgTimelong = pkgTimelong;
		}

		public Integer getPkgUnit() {
			return pkgUnit;
		}

		public void setPkgUnit(Integer pkgUnit) {
			this.pkgUnit = pkgUnit;
		}

		public String getPkgSMSOrder() {
			return pkgSMSOrder;
		}

		public void setPkgSMSOrder(String pkgSMSOrder) {
			this.pkgSMSOrder = pkgSMSOrder;
		}

		public String getPkgFreeRes() {
			return pkgFreeRes;
		}

		public void setPkgFreeRes(String pkgFreeRes) {
			this.pkgFreeRes = pkgFreeRes;
		}
		
	}

}
