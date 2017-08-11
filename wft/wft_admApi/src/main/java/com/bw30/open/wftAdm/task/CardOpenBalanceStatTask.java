package com.bw30.open.wftAdm.task;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.bw30.open.common.dao.mapper.AccountMapper;
import com.bw30.open.common.dao.mapper.WftCardActiveMapper;
import com.bw30.open.common.dao.mapper.WftCardTypeMapper;
import com.bw30.open.common.dao.mapper.WftChannelMapper;
import com.bw30.open.common.model.OpenPlatformAccount;
import com.bw30.open.common.model.WftCardType;
import com.bw30.open.common.model.WftChannel;

/**
 * 针对平安（10009）
 * 检查前一天开的卡，累加之后更新open_platform_account
 * 中的usetime
 * */
public class CardOpenBalanceStatTask {
	private static final Logger LOG = Logger.getLogger(CardOpenBalanceStatTask.class);
	private static final SimpleDateFormat F = new SimpleDateFormat("yyyy-MM-dd");
	private static final String CHANNEL = "10009";
	@Resource
	private AccountMapper accountMapper;
	@Resource
	private WftCardActiveMapper wftCardActiveMapper;
	@Resource
	private WftCardTypeMapper wftCardTypeMapper;
	@Resource
	private WftChannelMapper wftChannelMapper;
	
	public void runTask() {
		LOG.info(String.format("[CardOpenBalanceStatTask]start"));
		Calendar c = Calendar.getInstance();
	    c.setTime(new Date());
	    c.add(Calendar.DATE, -1);
	    
	    Map<String, Object> params = new HashMap<String, Object>();
		params.put("opid", 2);
		params.put("intime", F.format(c.getTime()));
		params.put("channel",CHANNEL);
	    
	    int num = wftCardActiveMapper.countByParam(params);
	    LOG.info(String.format("[CardOpenBalanceStatTask]date:%s open card num:%d",F.format(c.getTime()),num));
	    OpenPlatformAccount account = this.accountMapper.getAccountByUserId(Integer.parseInt(CHANNEL));
	    if (null != account) {
			LOG.info(String.format("[CardOpenBalanceStatTask] old account balance:%s",account.getUsetime()));
		    WftChannel channel = wftChannelMapper.findById(CHANNEL);
		    WftCardType cardType = wftCardTypeMapper.findCardCodeop(channel.getCtypeforopenctcc());
		    if(cardType!=null){
		    	LOG.info(String.format("[CardOpenBalanceStatTask] card type %s value %d",channel.getCtypeforopenctcc(),cardType.getBalance()));
				String openCardBalance = String.valueOf(num*cardType.getBalance().intValue());
				BigInteger bi = new BigInteger(account.getUsetime());
				bi = bi.add(new BigInteger(openCardBalance));
				account = new OpenPlatformAccount();
				account.setId(Integer.parseInt(CHANNEL));
				account.setUsetime(bi.toString());
				this.accountMapper.update(account);
				LOG.info(String.format("[CardOpenBalanceStatTask] open card balance:%s",openCardBalance));
				LOG.info(String.format("[CardOpenBalanceStatTask] update account value:%s",bi.toString()));
		    }
	    }else{
	    	LOG.info(String.format("[CardOpenBalanceStatTask] old account null"));
	    }
	}

	public void setAccountMapper(AccountMapper accountMapper) {
		this.accountMapper = accountMapper;
	}

	public void setWftCardActiveMapper(WftCardActiveMapper wftCardActiveMapper) {
		this.wftCardActiveMapper = wftCardActiveMapper;
	}

	public void setWftCardTypeMapper(WftCardTypeMapper wftCardTypeMapper) {
		this.wftCardTypeMapper = wftCardTypeMapper;
	}

	public void setWftChannelMapper(WftChannelMapper wftChannelMapper) {
		this.wftChannelMapper = wftChannelMapper;
	}
	
}
