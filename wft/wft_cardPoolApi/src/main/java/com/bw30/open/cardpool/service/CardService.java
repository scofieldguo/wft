package com.bw30.open.cardpool.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.bw30.open.common.dao.mapper.WftCardActiveMapper;
import com.bw30.open.common.dao.mapper.WftCardStoreMapper;
import com.bw30.open.common.model.WftCardActive;
import com.bw30.open.common.model.WftCardStore;
import com.bw30.open.wft.common.PwdEncrypt;

public class CardService {
	@Resource
	private WftCardActiveMapper wftCardActiveMapper;
	@Resource
	private WftCardStoreMapper wftCardStoreMapper;
	
	public void setWftCardActiveMapper(WftCardActiveMapper wftCardActiveMapper) {
		this.wftCardActiveMapper = wftCardActiveMapper;
	}

	public void setWftCardStoreMapper(WftCardStoreMapper wftCardStoreMapper) {
		this.wftCardStoreMapper = wftCardStoreMapper;
	}


	/**
	 * 插入卡池
	 * @param channel
	 * @param card
	 */
	public void saveCardActive(String channel, WftCardActive card){
		card.setChannel(channel);
		this.wftCardActiveMapper.insert(card);
		
		WftCardStore cs = new WftCardStore();
		cs.setId(card.getId());
		cs.setInplace(1);
		cs.setIntime(new Date());
		cs.setChannel(channel);
		this.wftCardStoreMapper.update(cs);
	}
	
	/**
	 * 更新卡池
	 * 
	 * @param channel
	 * @param card
	 */
	public void updateCardActive(String channel, WftCardActive card){
		card.setChannel(channel);
		this.wftCardActiveMapper.update(card);
	}
	
	/**
	 * 根据卡号查询卡， 卡密已解密
	 * 
	 * @param partner
	 * @param opid
	 * @param cno
	 * @return
	 */
	public WftCardActive findCard(String partner, Integer opid, String cno){
		List<WftCardActive> cList = this.wftCardActiveMapper.findCardByNo(partner, opid, cno);
		if (null != cList && 0 < cList.size()) {
			WftCardActive ca = cList.get(0);
			ca.setPwd(PwdEncrypt.decryptCardPwd(ca.getPwd()));
			return ca;
		}
		return null;
	}
	
	/**
	 * 修改卡密码
	 * 
	 * @param partner
	 * @param cid
	 * @param pwd
	 */
	public void updatePwd(String partner, Integer cid, String pwd){
		WftCardActive ca = new WftCardActive();
		ca.setId(cid);
		ca.setPwd(PwdEncrypt.encryptCardPwd(pwd));
		this.updateCardActive(partner, ca);
	}
	
	
}
