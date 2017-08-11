package com.bw30.open.cardCache.rmi.service;

import java.util.LinkedList;

import com.bw30.open.cardCache.proto.resp.GetCardResp;
import com.bw30.open.cardCache.proto.resp.ImportCardResp;
import com.bw30.open.cardCache.proto.resp.RemoveCardResp;
import com.bw30.open.common.model.CardCacheBean;

public interface ICardCacheRmiService {
	
	public static final Integer PARAMS_ERROR=9001; // 参数错误
	public static final Integer SUCCESS=1;// 成功
	public static final Integer FAIL = 0; // 失败
	public static final Integer SERVICE_ERROR=103; // 服务器异常
	public static final Integer GETCARD_ERROR=-1; // 取卡失败

	/**
	 * 从缓存池中获取卡
	 * @param getCardReq
	 * @return
	 */
	public GetCardResp getCard(String channel, Integer opid,Integer type,Integer prvid);
	
	
	/**
	 * 从缓存池中获取卡
	 * @param getCardReq
	 * @return
	 */
	public GetCardResp getCardByUser(String channel, Integer opid, Integer type, Integer prvid, String uid);
	
	
	/**
	 * 从缓存池中移除卡
	 * @param removeCardReq
	 * @return
	 */
	public RemoveCardResp removeCard(String channel ,Integer opid, Integer type, Integer prvid ,Integer cid);
	
	/**
	 * 向缓存池中导入卡
	 * @param importCardReq
	 * @return
	 */
	public ImportCardResp importCard(String channel, Integer opid, Integer type, Integer prvid ,LinkedList<CardCacheBean> cardList);
}
