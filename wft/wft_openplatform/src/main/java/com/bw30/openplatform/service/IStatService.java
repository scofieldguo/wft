package com.bw30.openplatform.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.bw30.open.common.model.stat.WftConsumeStat;
import com.bw30.open.common.model.stat.WftOpenPlatFormStat;
import com.bw30.open.common.model.stat.WftTotalStat;
import com.bw30.open.wft.common.Pager;

public interface IStatService {
	public List<WftOpenPlatFormStat> getWftTotalStats(String startDate,String endDate,String channel,Integer opId,Pager pager);
	
	public void exportExcel(String startDate,String endDate,String channel,Integer opId,HttpServletResponse res);
	
	public List<WftOpenPlatFormStat> getWftMacStats(String startDate,String endDate,String channel,Integer opId,Pager pager);
	
	public void exportMacExcel(String startDate,String endDate,String channel,Integer opId,HttpServletResponse res);

	public WftTotalStat getLastDayStat(String lastday, String lastday2,
			String channelcode, int opIdCtcc);

	public List<Integer> getUsedTime(String firstDayOfMonth, String today,
			String channelcode, int opIdCtcc);

	public List<WftConsumeStat> getIntervalStat(String startday, String endday,
			String channelcode, int opIdCtcc);

	public List<WftOpenPlatFormStat> getIntervalConnStat(String startday,
			String endday, String channelcode, int opIdCtcc);
}
