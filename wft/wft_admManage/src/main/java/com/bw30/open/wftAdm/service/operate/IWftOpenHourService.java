package com.bw30.open.wftAdm.service.operate;

import java.util.List;

import com.bw30.open.common.model.stat.WftOpenHourSuccessStat;
import com.bw30.open.wft.common.Pager;

public interface IWftOpenHourService {

	List<WftOpenHourSuccessStat> findByParam(String dairy, Pager pager, String channel);

}
