package com.bw30.open.wftAdm.service.operate;

import java.util.Date;
import java.util.List;

import com.bw30.open.common.model.WftChannel;
import com.bw30.open.common.model.versionstat.WftOpenVersionStat;

public interface IWftOpenVersionStatService {

	void stat(Date startDate, Date endDate, String dayYMD, String version,
			String channel);

	List<WftChannel> findAllChannel();

	List<WftOpenVersionStat> getVersionStat(String dairy, String version, String channel);


}
