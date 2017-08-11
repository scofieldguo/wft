package com.bw30.open.wftAdm.service.card;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.multipart.MultipartFile;

import com.bw30.open.cardCache.proto.Resp;
import com.bw30.open.cardCache.proto.resp.RemoveCardResp;
import com.bw30.open.cardCache.rmi.service.ICardCacheRmiService;
import com.bw30.open.common.dao.mapper.WftCardActiveMapper;
import com.bw30.open.common.dao.mapper.WftCardStoreMapper;
import com.bw30.open.common.model.WftCardActive;
import com.bw30.open.common.model.WftCardStore;
import com.bw30.open.wft.common.PwdEncrypt;
import com.bw30.open.wftAdm.controller.card.CardUpdateModel;

public class CardUpdateService {
	@Resource
	private WftCardStoreMapper wftCardStoreMapper;
	@Resource
	private WftCardActiveMapper wftCardActiveMapper;

	@Resource
	private ICardCacheRmiService cardCacheRmiService;

	public WftCardStore getCardByNo(String no) {
		List<WftCardStore> cardList = this.wftCardStoreMapper.findByNo(no);
		if (null != cardList && 0 < cardList.size()) {
			return cardList.get(0);
		}
		return null;
	}

	/**
	 * 停用卡：移除缓存中的卡，修改卡池中卡有效期（相当于停卡）
	 * 
	 * @param txtFile
	 * @param list
	 * @throws IOException
	 */
	public boolean stopCard(MultipartFile txtFile, List<CardUpdateModel> list)
			throws IOException {
		boolean flag = true;
		BufferedReader br = new BufferedReader(new InputStreamReader(
				txtFile.getInputStream()));
		String line = null;
		String no = null;
		WftCardStore cardStore = null;
		CardUpdateModel model = null;
		while (null != (line = br.readLine())) {
			model = new CardUpdateModel();

			no = line.trim();
			model.setNo(no);
			cardStore = this.getCardByNo(no);
			if (null == cardStore) {
				flag = false;
				model.setMsg("失败：卡号不存在");
				list.add(model);
				continue;
			}
			model.setId(cardStore.getId());
			model.setChannel(cardStore.getChannel());

			// 从缓存中移除卡
			RemoveCardResp res = this.cardCacheRmiService.removeCard(
					cardStore.getChannel(), 0, 0, cardStore.getOpid(),
					cardStore.getId());
			if (Resp.SUCCESS == res.getResult() || Resp.FAIL == res.getResult()) {
				try {
					WftCardActive cardActive = new WftCardActive();
					cardActive.setId(cardStore.getId());
					cardActive.setChannel(cardStore.getChannel());
					cardActive.setCstatus(WftCardActive.CSTATUS_STOP);
					cardActive.setVetime(new Date());
					cardActive.setStoptime(new Date());
					cardActive.setCache(WftCardActive.OUT_CACHE);
					this.wftCardActiveMapper.update(cardActive);
					
//					WftCardStore cs = new WftCardStore();
//					cs.setId(cardStore.getId());
//					cs.setCstatus(WftCardStore.CARD_CSTATUS_STOPPED);
//					cs.setStoptime(new Date());
//					this.wftCardStoreMapper.update(cs);
				} catch (Exception e) {
					flag = false;
					model.setMsg("失败：停卡错误");
				}
			} else {
				flag = false;
				model.setMsg("失败：从缓存中移除卡错误");
			}
			list.add(model);

		}
		br.close();
		return flag;
	}
	
	/**
	 * 修改卡密码：移除缓存中的卡，修改卡库及卡池中的卡密码
	 * 
	 * @param txtFile
	 * @param list
	 * @return 
	 * @throws IOException
	 */
	public boolean updatePwd(MultipartFile txtFile, List<CardUpdateModel> list) throws IOException{
		boolean flag = true;
		BufferedReader br = new BufferedReader(new InputStreamReader(
				txtFile.getInputStream()));
		String line = null;
		String no = null;
		String pwd = null;
		WftCardStore cardStore = null;
		CardUpdateModel model = null;
		String[] strs = null;
		while (null != (line = br.readLine())) {
			model = new CardUpdateModel();

			strs = line.split(",");
			if(2 != strs.length){
				flag = false;
				model.setMsg("失败：数据格式错误[" + line + "]");
				list.add(model);
				continue;
			}
			
			no = strs[0].trim();
			pwd = strs[1].trim();
			model.setNo(no);
			model.setPwd(pwd);
			cardStore = this.getCardByNo(no);
			if (null == cardStore) {
				flag = false;
				model.setMsg("失败：卡号不存在");
				list.add(model);
				continue;
			}
			model.setId(cardStore.getId());
			model.setChannel(cardStore.getChannel());

			// 从缓存中移除卡
			RemoveCardResp res = this.cardCacheRmiService.removeCard(
					cardStore.getChannel(), 0, 0, cardStore.getOpid(),
					cardStore.getId());
			if (Resp.SUCCESS == res.getResult() || Resp.FAIL == res.getResult()) {
				// 修改卡库及卡池中卡密码
				try {
					WftCardActive cardActive = new WftCardActive();
					cardActive.setId(cardStore.getId());
					cardActive.setChannel(cardStore.getChannel());
					pwd = PwdEncrypt.encryptCardPwd(pwd);
					cardActive.setPwd(pwd);
					cardActive.setCache(WftCardActive.OUT_CACHE);
					this.wftCardActiveMapper.update(cardActive);
					
					WftCardStore cs = new WftCardStore();
					cs.setId(cardStore.getId());
					cs.setPwd(pwd);
					cs.setPctime(new Date());
					this.wftCardStoreMapper.update(cs);
					
				} catch (Exception e) {
					flag = false;
					model.setMsg("失败：修改卡库及卡池中卡密码错误");
				}
			} else {
				flag = false;
				model.setMsg("失败：从缓存中移除卡错误");
			}
			list.add(model);
		}
		br.close();
		return flag;
	}

	public void setWftCardStoreMapper(WftCardStoreMapper wftCardStoreMapper) {
		this.wftCardStoreMapper = wftCardStoreMapper;
	}

	public void setWftCardActiveMapper(WftCardActiveMapper wftCardActiveMapper) {
		this.wftCardActiveMapper = wftCardActiveMapper;
	}

	public void setCardCacheRmiService(ICardCacheRmiService cardCacheRmiService) {
		this.cardCacheRmiService = cardCacheRmiService;
	}

}
