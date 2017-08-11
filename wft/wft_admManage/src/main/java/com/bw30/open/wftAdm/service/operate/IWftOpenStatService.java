package com.bw30.open.wftAdm.service.operate;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.bw30.open.common.model.WftChannel;
import com.bw30.open.common.model.stat.WftOpenStat;
import com.bw30.open.wft.common.Pager;

public interface IWftOpenStatService {

	List<WftOpenStat> findOpenStat(String start, String end, Pager pager);

	void exportExcel(String start, String end, HttpServletResponse res);

	List<WftOpenStat> findOpenStatByChannel(String start, String end,
			Pager pager, String channel);

	void exportChannelExcel(String start, String end, HttpServletResponse res,
			String channel);

	List<WftChannel> findChannel();

	void exportUnoteExcel(Date start, Date end, Integer opid, String channel, String note, HttpServletResponse res);

}
