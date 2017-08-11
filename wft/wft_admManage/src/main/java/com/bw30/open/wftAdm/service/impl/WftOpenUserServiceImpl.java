package com.bw30.open.wftAdm.service.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bw30.open.common.dao.mapper.AccountMapper;
import com.bw30.open.common.dao.mapper.ChargeRecordMapper;
import com.bw30.open.common.dao.mapper.NoticeMapper;
import com.bw30.open.common.dao.mapper.UserMapper;
import com.bw30.open.common.dao.mapper.WftChannelMapper;
import com.bw30.open.common.dao.mapper.WftConsumeStatMapper;
import com.bw30.open.common.dao.mapper.WftOpenStatMapper;
import com.bw30.open.common.dao.mapper.WftTotalStatMapper;
import com.bw30.open.common.model.OpenPlatformAccount;
import com.bw30.open.common.model.OpenPlatformChargeRecord;
import com.bw30.open.common.model.OpenPlatformNotice;
import com.bw30.open.common.model.OpenPlatformUser;
import com.bw30.open.common.model.WftChannel;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.common.model.stat.WftConsumeStat;
import com.bw30.open.common.model.stat.WftOpenStat;
import com.bw30.open.common.model.stat.WftTotalStat;
import com.bw30.open.wft.common.Pager;
import com.bw30.open.wftAdm.service.user.IWftOpenUserService;

public class WftOpenUserServiceImpl implements IWftOpenUserService {
	
	private UserMapper userMapper;
	private AccountMapper accountMapper;
	private ChargeRecordMapper chargeRecordMapper;
	private WftChannelMapper wftChannelMapper;
	private NoticeMapper noticeMapper;
	private WftOpenStatMapper wftOpenStatMapper;
	private WftTotalStatMapper wftTotalStatMapper;
	private WftConsumeStatMapper wftConsumeStatMapper;
	@Override
	public List<OpenPlatformUser> findAllUser(Pager pager) {
		return userMapper.findByPage(pager);
	}
	
	@Override
	public void charge(Integer id, Integer chargeHours, Integer chargeCost) {
		OpenPlatformAccount account = accountMapper.getAccountByUserId(id);
		BigInteger buytime = new BigInteger(account.getBuytime());
		BigInteger bigx = new BigInteger(chargeHours.toString());
		BigInteger bigy = new BigInteger("3600");
		BigInteger bigxy = bigx.multiply(bigy);
		buytime = buytime.add(bigxy);
		account.setBuytime(buytime.toString());
		accountMapper.update(account);		
		OpenPlatformChargeRecord openPlatformChargeRecord = new OpenPlatformChargeRecord();
		openPlatformChargeRecord.setDairy(new Date());
		openPlatformChargeRecord.setChargecost(chargeCost);
		openPlatformChargeRecord.setChargehours(chargeHours);
		openPlatformChargeRecord.setUserid(id);
		chargeRecordMapper.insert(openPlatformChargeRecord);
	}
	
	@Override
	public int countRecordByUid(Integer id) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userid", id);
		return chargeRecordMapper.countByParam(paramMap);
	}
	
	@Override
	public List<OpenPlatformChargeRecord> pageFindRecordByUid(Pager pager, Integer id) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userid", id);
		return chargeRecordMapper.pageFindByParam(paramMap, pager);
	}

	@Override
	public boolean channelExist(Integer channel) {
		if(wftChannelMapper.findById(channel) == null){
			return false;//不存在
		}
		return true;//存在
	}
	
	@Override
	public void addDefaultChannel(Integer channel, String name) {
		WftChannel wftchannel = new WftChannel();
		wftchannel.setName(name);
		wftchannel.setCode(channel.toString());
		wftchannel.setStatus(1);
		wftchannel.setCnum(0);
		wftchannel.setMaxnum(-1);
		wftchannel.setConntimeout(30);
		wftchannel.setRechargetime(-1);
		wftchannel.setCtccnum(0);
		wftchannel.setCmccnum(0);
		wftchannel.setCtccbalance(-1);
		wftchannel.setCmccbalance(-1);
		wftchannel.setAuth(0);//设定默认channel信息
		wftChannelMapper.insert(wftchannel);			
	}
	
	@Override
	public OpenPlatformUser getUserById(Integer id) {
		return userMapper.findById(id);
	}

	
	@Override
	public WftConsumeStat getYesterDayUseTime(Integer channel, String startDate) {
		if(wftConsumeStatMapper.findByCondition(startDate,channel.toString(),2)!=null){
			return wftConsumeStatMapper.findByCondition(startDate,channel.toString(),2).get(0);
			
		}
		return null;
	}

	public void setWftTotalStatMapper(WftTotalStatMapper wftTotalStatMapper) {
		this.wftTotalStatMapper = wftTotalStatMapper;
	}

	@Override
	public OpenPlatformAccount getAcccount(Integer channel) {
		return accountMapper.getAccountByUserId(channel);
	}

	
	@Override
	public void sendNoticeToUser(OpenPlatformNotice oPlatformNotice) {
		noticeMapper.insert(oPlatformNotice);
	}
	@Override
	public int countAllUser() {
		return userMapper.count();
	}
	
	public void setUserMapper(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	public void setAccountMapper(AccountMapper accountMapper) {
		this.accountMapper = accountMapper;
	}

	public void setChargeRecordMapper(ChargeRecordMapper chargeRecordMapper) {
		this.chargeRecordMapper = chargeRecordMapper;
	}

	public void setWftChannelMapper(WftChannelMapper wftChannelMapper) {
		this.wftChannelMapper = wftChannelMapper;
	}

	public void setNoticeMapper(NoticeMapper noticeMapper) {
		this.noticeMapper = noticeMapper;
	}

	public void setWftOpenStatMapper(WftOpenStatMapper wftOpenStatMapper) {
		this.wftOpenStatMapper = wftOpenStatMapper;
	}

	public void setWftConsumeStatMapper(WftConsumeStatMapper wftConsumeStatMapper) {
		this.wftConsumeStatMapper = wftConsumeStatMapper;
	}





}
