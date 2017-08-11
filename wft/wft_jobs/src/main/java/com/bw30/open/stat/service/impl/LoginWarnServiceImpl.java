package com.bw30.open.stat.service.impl;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bw30.open.common.dao.mapper.WftChannelMapper;
import com.bw30.open.common.dao.mapper.WftOperatorMapper;
import com.bw30.open.common.dao.mapper.WftTenMinLoginStatMapper;
import com.bw30.open.common.model.WftChannel;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.common.model.stat.WftTenMinLoginStat;
import com.bw30.open.stat.util.SendWarn;
import com.bw30.open.wft.common.DateUtil;

public class LoginWarnServiceImpl {
	
	private Integer ctccConnSucNum; // 腾讯电信连接成功率
	private Integer cmccConnSucNum; // 腾讯移动连接成功率
	private Integer connCntNum;
	@Resource
	private WftChannelMapper wftChannelMapper;
	@Resource
	private WftOperatorMapper wftOperatorMapper;
	@Resource
    private WftTenMinLoginStatMapper wftTenMinLoginStatMapper;
	private Map<Integer, String> opMap = new HashMap<Integer, String>(); 
//	private SendWarn sendWarn;
	
	
	
	/**
	 * 告警处理开始
	 */
	public void doWarn(){
		String message="";
		System.out.println("warn is start");
		// 初始化运营商Map
		getAllOperator();
		Integer min = 0;
		Date beforeHalfDate = DateUtil.getBeforeNMinuteDate(10);
	    Date dairy = getShotDate(beforeHalfDate);
		Integer hour = Integer.parseInt(DateUtil.getHour(beforeHalfDate));
		Integer minute = Integer.parseInt(DateUtil.getMinute(beforeHalfDate));
		if(0<= minute && minute<10){
			min=0;
		}else if(10<=minute && minute<20){
			min=1;
		}else if(20<= minute && minute <30){
			min=2;
		}else if(30<= minute && minute <40){
			min=3;
		}else if(40<= minute && minute <50){
			min=4;
		}else{
			min=5;
		}
		List<WftChannel> list = wftChannelMapper.findAll();
		for(WftChannel wftChannel:list){
			if(wftChannel.getStatus()==1){
				System.out.println("channel="+wftChannel.getCode()+"dairy="+dairy+"hour="+hour+"min="+min);
				List<WftTenMinLoginStat> loginList = getLoginWarnStatList(Integer.parseInt(wftChannel.getCode()), dairy, hour, min);
				if(loginList == null){
					message = "服务器异常！";
					sendWarn(message);
				}else{
					doErrorMessage(loginList,wftChannel.getName());
				}
			}
				
		}
		System.out.println("warn is end");
	}
	
	/**
	 * 获取运营商Map
	 */
	private void getAllOperator(){
		List<WftOperator> opList =wftOperatorMapper.findAll();
		for(WftOperator wftOperator : opList){
			opMap.put(wftOperator.getId(), wftOperator.getName());
		}
	}
	
	/**
	 * 获取告警统计记录
	 * @param channel
	 * @param dairy
	 * @param hour
	 * @param minute
	 * @return
	 */
	public List<WftTenMinLoginStat> getLoginWarnStatList(Integer channel, Date dairy , Integer hour,Integer minute){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("channel", channel);
		paramMap.put("dairy", dairy);
		paramMap.put("hour", hour);
		paramMap.put("min", minute);
		List<WftTenMinLoginStat> list = wftTenMinLoginStatMapper.findByParam(paramMap);
		System.out.println("warn list size="+list.size());
		if(list !=null && list.size()>0){
			return list;
		}
		return null;
	}
	
	/**
	 * 告警信息处理
	 * @param loginList
	 * @param name
	 */
	private void doErrorMessage(List<WftTenMinLoginStat> loginList,String name){
		for(WftTenMinLoginStat wftTenMinLoginStat :loginList){
			Integer opid = wftTenMinLoginStat.getOpid();
			Integer connCnt = wftTenMinLoginStat.getConncnt();
			Integer connSuc = wftTenMinLoginStat.getConnsuc();
			Integer noCard = wftTenMinLoginStat.getNocard();
			String opName = opMap.get(opid);
			setLoginMessage(opName,opid,connSuc,connCnt,name);
			setNoCardMessage(opName, noCard, name);
		}
	}
	
	/**
	 * 设置登录成功率告警信息
	 * @param opName
	 * @param connSuc
	 * @param connCnt
	 * @param name
	 */
	private void setLoginMessage(String opName , Integer opId ,Integer connSuc,Integer connCnt,String name){
		Integer connSucNum =0;
		if(connCnt.intValue() >= connCntNum.intValue()){
			if(opId == 1){
				connSucNum = cmccConnSucNum;
			}else if(opId == 2){
				connSucNum = ctccConnSucNum;
			}
			if(LoginWarnServiceImpl.getDouble(connSuc, connCnt)*100 < connSucNum){
				String message=name+opName+"成功率异常！成功率："+LoginWarnServiceImpl.getNumberFormat(LoginWarnServiceImpl.getDouble(connSuc, connCnt));
				System.out.println("message====="+message);
				//sendWarn(message);
			}
		}
	}
	
	/**
	 * 设置取卡失败告警信息
	 * @param opName
	 * @param noCard
	 * @param name
	 */
	private void setNoCardMessage(String opName,Integer noCard,String name){
		if(noCard > 0){
			String message=name+opName+"取卡失败！次数："+noCard;
			System.out.println("message====="+message);
			sendWarn(message);
		}
	}
	
	public static void main(String[] args) {
		   //这里的数后面加“D”是表明它是Double类型，否则相除的话取整，无法正常使用
			System.out.println(getDouble(13, 20));
		
		   double percent = 50.5D / 150D;
		   //输出一下，确认你的小数无误
		   System.out.println("小数：" + percent);
		   //获取格式化对象
		   NumberFormat nt = NumberFormat.getPercentInstance();
		   //设置百分数精确度2即保留两位小数
		   nt.setMinimumFractionDigits(2);
		   //最后格式化并输出
		   System.out.println("百分数：" + nt.format(percent));
		  
		}
	
	private static String getNumberFormat(double percent){
		  NumberFormat nt = NumberFormat.getPercentInstance();
		   //设置百分数精确度2即保留两位小数
		   nt.setMinimumFractionDigits(2);
		   //最后格式化并输出
		   return nt.format(percent);
	}
	
	private static double getDouble(Integer sucNum ,Integer connNum){
		double percent = Double.parseDouble(String.valueOf(sucNum)) / Double.parseDouble(String.valueOf(connNum));
		return percent;
	}
	
	private Date getShotDate(Date date){
		 Calendar c = Calendar.getInstance();
		 c.setTime(date);
         c.set(Calendar.HOUR_OF_DAY, 0);
         c.set(Calendar.MINUTE, 0);
         c.set(Calendar.SECOND, 0);
         c.set(Calendar.MILLISECOND, 0);
         return c.getTime();
	}
	
	/**
	 * 发送告警信息
	 * @param message
	 */
	private void sendWarn(String message){
	//	sendWarn.sendWarn("test"+message);
//		IMonitorNotify notify = null; 
//		notify=MonitorSystemNotifierFactory.getInstance("connWarnTest");
//		notify.startUp("http://218.106.247.240:8800/ms/trap.do");
//		notify.notice(NotifyType.CUSTOM_STATUS_NOTIFY,NotifyLevel.NOTIFY_ERROR_LEVEL,message);
	}

	public Integer getCtccConnSucNum() {
		return ctccConnSucNum;
	}

	public void setCtccConnSucNum(Integer ctccConnSucNum) {
		this.ctccConnSucNum = ctccConnSucNum;
	}

	public Integer getCmccConnSucNum() {
		return cmccConnSucNum;
	}

	public void setCmccConnSucNum(Integer cmccConnSucNum) {
		this.cmccConnSucNum = cmccConnSucNum;
	}

	public void setWftChannelMapper(WftChannelMapper wftChannelMapper) {
		this.wftChannelMapper = wftChannelMapper;
	}

	public void setConnCntNum(Integer connCntNum) {
		this.connCntNum = connCntNum;
	}

	public void setWftOperatorMapper(WftOperatorMapper wftOperatorMapper) {
		this.wftOperatorMapper = wftOperatorMapper;
	}

	public void setWftTenMinLoginStatMapper(
			WftTenMinLoginStatMapper wftTenMinLoginStatMapper) {
		this.wftTenMinLoginStatMapper = wftTenMinLoginStatMapper;
	}

//	public void setSendWarn(SendWarn sendWarn) {
//		this.sendWarn = sendWarn;
//	}
//	
}
