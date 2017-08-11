package com.bw30.open.wftAdm.service.user;

import java.util.List;

import com.bw30.open.common.model.OpenPlatformAccount;
import com.bw30.open.common.model.OpenPlatformChargeRecord;
import com.bw30.open.common.model.OpenPlatformNotice;
import com.bw30.open.common.model.OpenPlatformUser;
import com.bw30.open.common.model.stat.WftConsumeStat;
import com.bw30.open.common.model.stat.WftTotalStat;
import com.bw30.open.wft.common.Pager;

public interface IWftOpenUserService {

	List<OpenPlatformUser> findAllUser(Pager pager);

	void charge(Integer id, Integer chargeHours, Integer chargeCost);

	int countAllUser();

	int countRecordByUid(Integer id);

	List<OpenPlatformChargeRecord> pageFindRecordByUid(Pager pager, Integer id);

	boolean channelExist(Integer channel);

	void addDefaultChannel(Integer channel, String string);

	void sendNoticeToUser(OpenPlatformNotice oPlatformNotice);

	WftConsumeStat getYesterDayUseTime(Integer channel, String startDate);

	OpenPlatformAccount getAcccount(Integer channel);

	OpenPlatformUser getUserById(Integer id);

}
