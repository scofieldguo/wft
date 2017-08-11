package com.bw30.open.wftAdm.service.operate;

import java.util.List;

import com.bw30.open.common.model.WftChannel;
import com.bw30.open.common.model.stat.WftOpenAcessCardStat;
import com.bw30.open.wft.common.Pager;

public interface IWftOpenAcessCardStatService {

	List<WftOpenAcessCardStat> findAcessCardStat(String start, Pager pager, String channel);

	List<WftChannel> getAllChannel();

}
