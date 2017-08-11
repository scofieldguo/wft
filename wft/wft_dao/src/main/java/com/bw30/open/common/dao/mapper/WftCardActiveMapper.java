package com.bw30.open.common.dao.mapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.bw30.open.common.dao.mapper.common.GenericMapper;
import com.bw30.open.common.model.CardCacheBean;
import com.bw30.open.common.model.WftCardActive;

public interface WftCardActiveMapper extends GenericMapper<WftCardActive> {
	public WftCardActive findById(@Param("channel") String channel,
			@Param("id") Integer id);

	public void delete(@Param("channel") String channel,
			@Param("id") Integer id);
	
	void updateStop(@Param("channel")String channel);
	/**
	 * 查询
	 * 
	 * @param channel
	 *            渠道代码
	 * @param opid
	 *            运营商id
	 * @param no
	 *            卡号
	 * @return
	 */
	public List<WftCardActive> findCardByNo(@Param("channel") String channel,
			@Param("opid") Integer opid, @Param("no") String no);

	/**
	 * 电信取卡：当天已经与用户绑定的卡
	 * 
	 * @param uid
	 * @param day
	 *            yyyy-MM-dd
	 * @param opid
	 *            运营商id
	 * @return
	 */
	public List<WftCardActive> findCardByUidAndDay(
			@Param("channel") String channel, @Param("uid") String uid,
			@Param("day") String day, @Param("opid") Integer opid);

	/**
	 * 电信取卡：卡可分配，且未绑定用户
	 * 
	 * @param opid
	 * @param count
	 *            取卡数目
	 * @return
	 */
	public List<WftCardActive> findCardForCTCC(
			@Param("channel") String channel, @Param("opid") Integer opid,
			@Param("count") Integer count);

	/**
	 * 获取用户分配的卡， 按分配时间倒序
	 * 
	 * @param channel
	 * @param opid
	 * @param uid
	 * @return
	 */
	public List<WftCardActive> findCardByUid(@Param("channel") String channel,
			@Param("opid") Integer opid, @Param("uid") String uid);

	/**
	 * 取卡
	 * 
	 * @return
	 */
	public List<WftCardActive> getCardForOperator(
			@Param("params") Map<String, Object> params);

	/**
	 * 获取需要充值的电信卡：状态为可用，剩余时长小于tvalue
	 * 
	 * @param channel
	 *            渠道code
	 * @param tvalue
	 *            剩余时长
	 * @return
	 */
	public List<WftCardActive> findCtccCardForRecharge(
			@Param("channel") String channel, @Param("tvalue") Integer tvalue);

	/**
	 * 批量调入卡池
	 * 
	 */
	public void batchInsert(@Param("params") Map<String, Object> params);

	/**
	 * 获取可分分配的卡到卡缓存池中
	 * 
	 * @param params
	 * @return
	 */
	public LinkedList<CardCacheBean> findByParamForCardCache(
			@Param("params") Map<String, Object> params);

	/**
	 * 卡池初始化
	 * 
	 * @param params
	 * @return
	 */
	public LinkedList<CardCacheBean> findByParamForInitCardCache(
			@Param("params") Map<String, Object> params);
	/**
	 * 释放卡
	 * 
	 * @param channel
	 * @param id
	 * @param time
	 *            使用时长，秒
	 */
	public void freebackCard(@Param("channel") String channel,
			@Param("id") Integer id, @Param("time") Integer time);

	/**
	 * @param cstatus
	 * @param channel
	 * @param ssid
	 * 
	 * */
	public Integer countCardUse(@Param("cstatus") int cstatus,
			@Param("channel") String channel, @Param("ssid") String ssid);

	/**
	 * 
	 * @param opid
	 *            运营商id
	 * @param cstatus
	 *            卡状态
	 * @return
	 */
	public List<WftCardActive> findByOpidAndCstatus(
			@Param("channel") String channel, @Param("opid") Integer opid,
			@Param("cstatus") Integer cstatus);
	
	
	/**
	 * 
	 * @param opid
	 *            运营商id
	 * @param cstatus
	 *            卡状态
	 * @return
	 */
	public List<WftCardActive> findCmccStopCardBeforeMinute(
			@Param("channel") String channel, @Param("opid") Integer opid,
			@Param("cstatus") Integer cstatus, @Param("minute")Integer minute);

	/**
	 * 查看过期卡数
	 * */
	public Integer countCardOverTime(@Param("channel") String code,
			@Param("ssid") String ssid);

	/**
	 * 查看使用时间是两天前的卡，并且状态不是停卡，有效期在有效范围内
	 * @param channel
	 * @return
	 */
	public List<WftCardActive> findByUtimeAndCstatus(@Param("channel") String channel,@Param("opid")Integer opid, @Param("hour") Integer hour);
	
	public List<WftCardActive> findByVetime(@Param("channel") String channel);
}
