package com.bw30.open.common.model.stat;

import java.text.DecimalFormat;

import org.apache.log4j.chainsaw.Main;

public class WftOpenStat {

	private String dairy;
	private String channel;
	private String channelName;
	private Long cmutvalue=0l;
	private Long cmutvalueop=0l;
	private Long cnutvalue=0l;
	private String cnutvaluestr;
	private Long cnutvalueop=0l;
	private String cnutvalueopstr;
	private Integer sdkcnt=0;
	private Integer sdkper=0;
	private Integer cmcnfailcnt=0;
	private Integer cmcnsucccnt=0;
	private String   cmcnrate;    
	private Integer cncnfailcnt=0;
	private Integer cncnsucccnt=0;
	private String   cncnrate;
	private Integer cncnfailper=0;
	private Integer cncnsuccper=0;
	private String   cncnperrate;
	private Integer cmcnfailper=0;
	private Integer cmcnsuccper=0;
	private String  cmcnperrate;
	private Integer cmaccesscardfail=0;
	private Integer cnaccesscardsucc=0;
	private Integer cmsuccmac=0;
	private Integer cmfailmac=0;
	private String   cmmacrate;
	private Integer cnsuccmac=0;
	private Integer cnfailmac=0;
	private String  cnmacrate;
	private Integer cmfaovthree=0;
	private Integer cnfaovthree=0;
	private WftConsumeStat wftConsumeStat;
	
	DecimalFormat df= new DecimalFormat("0.0");
	
	public Integer getCmsuccmac() {
		return cmsuccmac;
	}
	public void setCmsuccmac(Integer cmsuccmac) {
		this.cmsuccmac = cmsuccmac;
	}
	public Integer getCmfailmac() {
		return cmfailmac;
	}
	public void setCmfailmac(Integer cmfailmac) {
		this.cmfailmac = cmfailmac;
	}
	public Integer getCnsuccmac() {
		return cnsuccmac;
	}
	public void setCnsuccmac(Integer cnsuccmac) {
		this.cnsuccmac = cnsuccmac;
	}
	public Integer getCnfailmac() {
		return cnfailmac;
	}
	public void setCnfailmac(Integer cnfailmac) {
		this.cnfailmac = cnfailmac;
	}
	public Integer getCmfaovthree() {
		return cmfaovthree;
	}
	public void setCmfaovthree(Integer cmfaovthree) {
		this.cmfaovthree = cmfaovthree;
	}
	public Integer getCnfaovthree() {
		return cnfaovthree;
	}
	public void setCnfaovthree(Integer cnfaovthree) {
		this.cnfaovthree = cnfaovthree;
	}
	public Integer getCmaccesscardfail() {
		return cmaccesscardfail;
	}
	public void setCmaccesscardfail(Integer cmaccesscardfail) {
		this.cmaccesscardfail = cmaccesscardfail;
	}
	public Integer getCnaccesscardsucc() {
		return cnaccesscardsucc;
	}
	public void setCnaccesscardsucc(Integer cnaccesscardsucc) {
		this.cnaccesscardsucc = cnaccesscardsucc;
	}
	public Integer getCncnfailper() {
		return cncnfailper;
	}
	public void setCncnfailper(Integer cncnfailper) {
		this.cncnfailper = cncnfailper;
	}
	public Integer getCncnsuccper() {
		return cncnsuccper;
	}
	public void setCncnsuccper(Integer cncnsuccper) {
		this.cncnsuccper = cncnsuccper;
	}
	public Integer getCmcnfailper() {
		return cmcnfailper;
	}
	public void setCmcnfailper(Integer cmcnfailper) {
		this.cmcnfailper = cmcnfailper;
	}
	public Integer getCmcnsuccper() {
		return cmcnsuccper;
	}
	public void setCmcnsuccper(Integer cmcnsuccper) {
		this.cmcnsuccper = cmcnsuccper;
	}
	public Integer getSdkcnt() {
		return sdkcnt;
	}
	public void setSdkcnt(Integer sdkcnt) {
		this.sdkcnt = sdkcnt;
	}
	public Integer getSdkper() {
		return sdkper;
	}
	public void setSdkper(Integer sdkper) {
		this.sdkper = sdkper;
	}
	public String getDairy() {
		return dairy;
	}
	public void setDairy(String dairy) {
		this.dairy = dairy;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}

	public Long getCmutvalue() {
		return cmutvalue;
	}
	public void setCmutvalue(Long cmutvalue) {
		this.cmutvalue = cmutvalue;
	}
	public Long getCmutvalueop() {
		return cmutvalueop;
	}
	public void setCmutvalueop(Long cmutvalueop) {
		this.cmutvalueop = cmutvalueop;
	}
	public Long getCnutvalue() {
		return cnutvalue;
	}
	public void setCnutvalue(Long cnutvalue) {
		this.cnutvalue = cnutvalue;
	}
	public Long getCnutvalueop() {
		return cnutvalueop;
	}
	public void setCnutvalueop(Long cnutvalueop) {
		this.cnutvalueop = cnutvalueop;
	}
	public Integer getCmcnfailcnt() {
		return cmcnfailcnt;
	}
	public void setCmcnfailcnt(Integer cmcnfailcnt) {
		this.cmcnfailcnt = cmcnfailcnt;
	}
	public Integer getCmcnsucccnt() {
		return cmcnsucccnt;
	}
	public void setCmcnsucccnt(Integer cmcnsucccnt) {
		this.cmcnsucccnt = cmcnsucccnt;
	}
	public Integer getCncnfailcnt() {
		return cncnfailcnt;
	}
	public void setCncnfailcnt(Integer cncnfailcnt) {
		this.cncnfailcnt = cncnfailcnt;
	}
	public Integer getCncnsucccnt() {
		return cncnsucccnt;
	}
	public void setCncnsucccnt(Integer cncnsucccnt) {
		this.cncnsucccnt = cncnsucccnt;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public String getCmcnrate() {
		if((getCmcnsucccnt()+getCmcnfailcnt())==0 ){
			return "0.0";
		}
		return df.format(((float)getCmcnsucccnt()/(getCmcnsucccnt()+getCmcnfailcnt())*100));
	}
	public void setCmcnrate(String cmcnrate) {
		this.cmcnrate = cmcnrate;
	}
	public String getCncnrate() {
		if((getCncnsucccnt()+getCncnfailcnt())==0 ){
			return "0.0";
		}
		return df.format(((float)getCncnsucccnt()/(getCncnsucccnt()+getCncnfailcnt())*100));
	}
	public void setCncnrate(String cncnrate) {
		this.cncnrate = cncnrate;
	}
	public String getCncnperrate() {
		if((getCncnsuccper()+getCncnfailper())==0 ){
			return "0.0";
		}
		return df.format(((float)getCncnsuccper()/(getCncnsuccper()+getCncnfailper())*100));
	}
	public void setCncnperrate(String cncnperrate) {
		this.cncnperrate = cncnperrate;
	}
	public String getCmcnperrate() {
		if((getCmcnsuccper()+getCmcnfailper())==0 ){
			return "0.0";
		}
		return df.format(((float)getCmcnsuccper()/(getCmcnsuccper()+getCmcnfailper())*100));
	}
	public void setCmcnperrate(String cmcnperrate) {
		this.cmcnperrate = cmcnperrate;
	}
	public String getCmmacrate() {
		if((getCmsuccmac()+getCmfailmac())==0 ){
			return "0.0";
		}
		return df.format(((float)getCmsuccmac()/(getCmsuccmac()+getCmfailmac())*100));
	}
	public void setCmmacrate(String cmmacrate) {
		this.cmmacrate = cmmacrate;
	}
	public String getCnmacrate() {
		if((getCnsuccmac()+getCnfailmac())==0 ){
			return "0.0";
		}
		return df.format(((float)getCnsuccmac()/(getCnsuccmac()+getCnfailmac())*100));
	}
	public void setCnmacrate(String cnmacrate) {
		this.cnmacrate = cnmacrate;
	}
	public WftConsumeStat getWftConsumeStat() {
		return wftConsumeStat;
	}
	public void setWftConsumeStat(WftConsumeStat wftConsumeStat) {
		this.wftConsumeStat = wftConsumeStat;
	}
	public String getCnutvaluestr() {
		return cnutvaluestr;
	}
	public void setCnutvaluestr(String cnutvaluestr) {
		this.cnutvaluestr = cnutvaluestr;
	}
	public String getCnutvalueopstr() {
		return cnutvalueopstr;
	}
	public void setCnutvalueopstr(String cnutvalueopstr) {
		this.cnutvalueopstr = cnutvalueopstr;
	}
	
	
}
