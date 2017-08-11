package com.bw30.open.wftAdm.service.operate;

import java.util.List;

import com.bw30.open.common.model.stat.WftCardUseTempModel;

public interface IWftNowCardUseService {
	List<WftCardUseTempModel> getNowCardUse();
	List<WftCardUseTempModel> getNowCardUseBySsid(String ssid);
}
