package com.bw30.openplatform.service;

import java.util.List;

import com.bw30.open.common.model.OpenPlatformNotice;
import com.bw30.open.wft.common.Pager;

public interface IOpenPlayFormNoticeService {

	List<OpenPlatformNotice> findNotice(Integer id, Integer unRead, Pager pager);

	void updateNotice(Integer id);

	void delete(OpenPlatformNotice notice);

}
