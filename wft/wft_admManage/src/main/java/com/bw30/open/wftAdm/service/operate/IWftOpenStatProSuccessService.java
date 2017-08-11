package com.bw30.open.wftAdm.service.operate;

import java.util.List;

import com.bw30.open.common.model.WftOperator;
import com.bw30.open.common.model.WftProvince;
import com.bw30.open.common.model.stat.WftTotalStat;

public interface IWftOpenStatProSuccessService {
	List<WftTotalStat> findByProvice(String channel, String start,
			String end);

	List<WftOperator> findAllOperator();

	List<WftProvince> findAllWftProvince();

}
