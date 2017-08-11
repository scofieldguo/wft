package com.bw30.open.stat.service.impl;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bw30.open.common.dao.mapper.WftChannelMapper;
import com.bw30.open.common.dao.mapper.WftEveryHalfHourWarnStatMapper;
import com.bw30.open.common.model.WftChannel;
import com.bw30.open.common.model.warn.WftEveryHalfHourWarnStat;
import com.bw30.open.stat.util.SendWarn;
import com.bw30.open.wft.common.DateUtil;

public class WarnServiceImpl {
	
	private Integer qqCTCCConnSucNum; // 腾讯电信连接成功率
	private Integer qqCMCCConnSucNum; // 腾讯移动连接成功率
	private Integer cmccConnCnt;
	@Resource
	private WftChannelMapper wftChannelMapper;
	@Resource
	private SendWarn sendWarn;
	
    private WftEveryHalfHourWarnStatMapper wftEveryHalfHourWarnStatMapper;
	
	public void doWarn(){
		String message="";
		System.out.println("warn is start");
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
				WftEveryHalfHourWarnStat wftEveryHalfHourWarnStat = getWftEveryHalfHourWarnStat(Integer.parseInt(wftChannel.getCode()), dairy, hour, min);
				if(wftEveryHalfHourWarnStat == null){
					message = "服务器异常！";
				//	sendWarn(message);
				}else{
					getErrorMessage(wftEveryHalfHourWarnStat,wftChannel.getName());
				}
			}
				
		}
		System.out.println("warn is end");
	}
	
	public WftEveryHalfHourWarnStat getWftEveryHalfHourWarnStat(Integer channel, Date dairy , Integer hour,Integer minute){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("channel", channel);
		paramMap.put("dairy", dairy);
		paramMap.put("hour", hour);
		paramMap.put("minute", minute);
		List<WftEveryHalfHourWarnStat> list = wftEveryHalfHourWarnStatMapper.findByParam(paramMap);
		System.out.println("warn list size="+list.size());
		if(list !=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	private String getErrorMessage(WftEveryHalfHourWarnStat wftEveryHalfHourWarnStat,String name){
		String message="";
		Integer ctccConnNum = wftEveryHalfHourWarnStat.getCtccConnNum();
		Integer ctccConnSuc = wftEveryHalfHourWarnStat.getCtccConnSuc();
		Integer ctccNoCard = wftEveryHalfHourWarnStat.getCtccNoCard();
		Integer cmccConnNum = wftEveryHalfHourWarnStat.getCmccConnNum();
		Integer cmccConnSuc = wftEveryHalfHourWarnStat.getCmccConnSuc();
		Integer cmccNoCard = wftEveryHalfHourWarnStat.getCmccNoCard();
		System.out.println("ctccConnNum="+ctccConnNum+"ctccConnSuc="+ctccConnSuc+"ctccNoCard="+ctccNoCard);
		System.out.println("cmccConnNum="+cmccConnNum+"cmccConnSuc="+cmccConnSuc+"cmccNoCard="+cmccNoCard);
		System.out.println("ctcc :"+WarnServiceImpl.getNumberFormat(WarnServiceImpl.getDouble(ctccConnSuc, ctccConnNum)));
		System.out.println("cmcc :"+WarnServiceImpl.getNumberFormat(WarnServiceImpl.getDouble(cmccConnSuc, cmccConnNum)));
		
		if(WarnServiceImpl.getDouble(ctccConnSuc, ctccConnNum)*100 < qqCTCCConnSucNum){
			message=name+"电信成功率异常！成功率："+WarnServiceImpl.getNumberFormat(WarnServiceImpl.getDouble(ctccConnSuc, ctccConnNum));
			System.out.println("message====="+message);
			//sendWarn(message);
		}
		if(cmccConnNum.intValue() >cmccConnCnt.intValue()){
			if(WarnServiceImpl.getDouble(cmccConnSuc, cmccConnNum)*100 < qqCMCCConnSucNum){
				message=name+"移动成功率异常！成功率："+WarnServiceImpl.getNumberFormat(WarnServiceImpl.getDouble(cmccConnSuc, cmccConnNum));
				System.out.println("message====="+message);
			//	sendWarn(message);
			}
		}
		if(ctccNoCard > 0){
			message=name+"电信取卡失败！次数："+ctccNoCard;
			System.out.println("message====="+message);
			//sendWarn(message);
		}
		if(cmccConnNum.intValue()> cmccConnCnt.intValue()){
			if(cmccNoCard > 0){
				message=name+"移动取卡失败！次数："+cmccNoCard;
				System.out.println("message====="+message);
				//sendWarn(message);
			}
		}
		
		return message;
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
	
//	private void sendWarn(String message){
//		sendWarn.sendWarn(message);
//	}

	public Integer getQqCTCCConnSucNum() {
		return qqCTCCConnSucNum;
	}

	public void setQqCTCCConnSucNum(Integer qqCTCCConnSucNum) {
		this.qqCTCCConnSucNum = qqCTCCConnSucNum;
	}

	public Integer getQqCMCCConnSucNum() {
		return qqCMCCConnSucNum;
	}

	public void setQqCMCCConnSucNum(Integer qqCMCCConnSucNum) {
		this.qqCMCCConnSucNum = qqCMCCConnSucNum;
	}

	public void setWftEveryHalfHourWarnStatMapper(
			WftEveryHalfHourWarnStatMapper wftEveryHalfHourWarnStatMapper) {
		this.wftEveryHalfHourWarnStatMapper = wftEveryHalfHourWarnStatMapper;
	}

	public void setWftChannelMapper(WftChannelMapper wftChannelMapper) {
		this.wftChannelMapper = wftChannelMapper;
	}

	public void setCmccConnCnt(Integer cmccConnCnt) {
		this.cmccConnCnt = cmccConnCnt;
	}

	public void setSendWarn(SendWarn sendWarn) {
		this.sendWarn = sendWarn;
	}
	
}
