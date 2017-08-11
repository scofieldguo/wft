package com.bw30.openplatform.service.impl;

import com.bw30.open.common.dao.mapper.AccountMapper;
import com.bw30.open.common.model.OpenPlatformAccount;
import com.bw30.openplatform.service.IOpenPlayFormAccountService;

public class OpenPlayFormAccountServiceImpl implements IOpenPlayFormAccountService{

	
	private AccountMapper accountMapper;
	public OpenPlatformAccount getAccountByUserId(Integer id) {
		return accountMapper.getAccountByUserId(id);
	}
	public void setAccountMapper(AccountMapper accountMapper) {
		this.accountMapper = accountMapper;
	}
	public void updateAccount(OpenPlatformAccount useraccount) {
		accountMapper.update(useraccount);
		
	}

}
