package com.bw30.open.common.model;

import java.io.Serializable;
import java.util.Date;

public class WftChannel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -984234988900788334L;
	/**
	 * 状态：禁用
	 */
	public static final int STATUS_FORBIDEN = 0;
	/**
	 * 状态：激活
	 */
	public static final int STATUS_ENABLE = 1;
	
	public static final int ISBILL_YES = 0;//默认（对账）
	public static final int ISBILL_NO = 1;//不对账
	
	public static final int AUTH_TRUE=1;
	public static final int AUTH_FALSE=0;
	private String code;
    private String name;
    private Integer status;
    private String key;
    private String openkey;
    private String standby;
    private Integer cnum;
    private Integer maxnum;
    
    private Integer conntimeout;//连接热点超时阈值，秒
    private Integer rechargetime;//充值阈值，秒
    
    private Integer ctccnum;//电信开卡阈值：卡池中可用电信卡数小于该值时，开卡定时任务自动开卡
    private Integer cmccnum;//移动开卡阈值：卡池中可用移动卡数小于该值时，开卡定时任务自动开卡
    
    private Integer ctccbalance;//秒，电信充值阈值：卡池中可用电信卡剩余时长小于该值时，充值定时任务自动为卡充值；-1不充值
    private Integer cmccbalance;//秒， 移动充值阈值：卡池中可用移动卡剩余时长小于该值时，充值定时任务自动为卡充值；-1不充值
    
    private Date intime;
    private Integer auth; // 鉴权标识
    
    private String ctypeforopenctcc;//电信开卡卡品，运营商卡品
    private String ctypeforrechargectcc;//电信充值卡品，运营商卡品
    
    private Integer cmccinterval;//移动取卡时间间隔时间，秒
    private Integer isbill;//是否后台对账（对账进行账户扣款）0-对账、1-不对账
    
    public WftChannel(){
    	
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getStandby() {
		return standby;
	}

	public void setStandby(String standby) {
		this.standby = standby;
	}
	
	public Integer getCnum() {
		return cnum;
	}

	public void setCnum(Integer cnum) {
		this.cnum = cnum;
	}

	public Integer getMaxnum() {
		return maxnum;
	}

	public void setMaxnum(Integer maxnum) {
		this.maxnum = maxnum;
	}

	public Integer getConntimeout() {
		return conntimeout;
	}

	public void setConntimeout(Integer conntimeout) {
		this.conntimeout = conntimeout;
	}

	public Integer getRechargetime() {
		return rechargetime;
	}

	public void setRechargetime(Integer rechargetime) {
		this.rechargetime = rechargetime;
	}
	
	public Integer getCtccnum() {
		return ctccnum;
	}

	public void setCtccnum(Integer ctccnum) {
		this.ctccnum = ctccnum;
	}

	public Integer getCmccnum() {
		return cmccnum;
	}

	public void setCmccnum(Integer cmccnum) {
		this.cmccnum = cmccnum;
	}

	public Integer getCtccbalance() {
		return ctccbalance;
	}

	public void setCtccbalance(Integer ctccbalance) {
		this.ctccbalance = ctccbalance;
	}

	public Integer getCmccbalance() {
		return cmccbalance;
	}

	public void setCmccbalance(Integer cmccbalance) {
		this.cmccbalance = cmccbalance;
	}

	public Date getIntime() {
		return intime;
	}

	public void setIntime(Date intime) {
		this.intime = intime;
	}

	public Integer getAuth() {
		return auth;
	}

	public void setAuth(Integer auth) {
		this.auth = auth;
	}

	public String getCtypeforopenctcc() {
		return ctypeforopenctcc;
	}

	public void setCtypeforopenctcc(String ctypeforopenctcc) {
		this.ctypeforopenctcc = ctypeforopenctcc;
	}

	public String getCtypeforrechargectcc() {
		return ctypeforrechargectcc;
	}

	public void setCtypeforrechargectcc(String ctypeforrechargectcc) {
		this.ctypeforrechargectcc = ctypeforrechargectcc;
	}

	public Integer getCmccinterval() {
		return cmccinterval;
	}

	public void setCmccinterval(Integer cmccinterval) {
		this.cmccinterval = cmccinterval;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getOpenkey() {
		return openkey;
	}

	public void setOpenkey(String openkey) {
		this.openkey = openkey;
	}

	public Integer getIsbill() {
		return isbill;
	}

	public void setIsbill(Integer isbill) {
		this.isbill = isbill;
	}
	
}
