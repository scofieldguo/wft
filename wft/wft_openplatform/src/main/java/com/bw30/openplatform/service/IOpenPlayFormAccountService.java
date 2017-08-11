package com.bw30.openplatform.service;

import com.bw30.open.common.model.OpenPlatformAccount;


public interface IOpenPlayFormAccountService {

	OpenPlatformAccount getAccountByUserId(Integer id);

	void updateAccount(OpenPlatformAccount useraccount);

}
