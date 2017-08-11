package com.bw30.open.wftAdm.service.operate;

import java.util.List;

import com.bw30.open.common.model.stat.WftOpenTopCardUse;
import com.bw30.open.wft.common.Pager;

public interface IWftOpenTopCardUseService {

	List<WftOpenTopCardUse> find(String start, String channel, Pager pager,
			String hour);

	Integer getChargeTopValue(String label, String daytime, String string,String channel);

	Integer getTopValue(String label, String daytime, String string,String channel,Integer flag);

	List<WftOpenTopCardUse> getlast20(String dairy, String channel, Integer limit);

}
