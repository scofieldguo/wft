package com.bw30.open.common.dao.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bw30.open.common.dao.mapper.common.GenericMapper;
import com.bw30.open.common.model.WftConnSession;

public interface WftConnSessionMapper extends GenericMapper<WftConnSession> {

	public WftConnSession findById(@Param("channel") String channel,
			@Param("id") String csid);

	/**
	 * 获取用户未关闭的会话
	 * 
	 * @param uid
	 * @return
	 */
	public List<WftConnSession> findUncloseSession(
			@Param("channel") String channel, @Param("uid") String uid);

	public void delete(@Param("channel") String channel,
			@Param("id") String csid);

	/**
	 * 查询未关闭且未计费，但超时(>2分钟)的连接会话
	 * 
	 * @param channel
	 * @param ssid
	 * 
	 * @return
	 */
	public List<WftConnSession> findTimeoutConnSession(
			@Param("channel") String channel, @Param("ssid") String ssid,
			@Param("minute") Integer minute, @Param("nowTime") Date now);

	/**
	 * 查询计费超过minute的连接会话
	 * 
	 * @param channel
	 * @param ssid
	 * @param minute
	 *           分钟
	 * @param now
	 * 
	 * @return
	 */
	public List<WftConnSession> findInvalidConnSession(
			@Param("channel") String channel, @Param("ssid") String ssid,
			@Param("minute") Integer minute, @Param("nowTime") Date now);

	/**
	 * 查询已计费且计费时间小于minute的会话
	 * 
	 * @param channel
	 * @param ssid
	 * @param minute
	 *            分钟
	 * @param nowDate
	 *            褰撳墠鏃堕棿
	 * @return
	 */
	public List<WftConnSession> findActiveSession(
			@Param("channel") String channel, @Param("ssid") String ssid,
			@Param("minute") Integer minute, @Param("nowDate") Date nowDate);

	/**
	 * 查询已关闭会话且计费时间大于minute的会话
	 * 
	 * @param channel
	 * @param ssid
	 * @param minute
	 * @param now
	 * @return
	 */
	public List<WftConnSession> findCloseConnSession(
			@Param("channel") String channel, @Param("ssid") String ssid,
			@Param("minute") Integer minute, @Param("nowDate") Date now);

	/**
	 * 查询用户的连接会话， 按stime倒序
	 * 
	 * @param uid
	 * @param channel
	 * @return
	 */
	public List<WftConnSession> findByUid(@Param("uid") String uid,
			@Param("channel") String channel);

	public List<WftConnSession> findByCsidqq(@Param("csidqq") String csidqq,
			@Param("channel") String channel);

	public List<WftConnSession> findByCsidqqAndUid(
			@Param("csidqq") String csidqq, @Param("uid") String uid,
			@Param("channel") String channel);

	public List<WftConnSession> findByCid(@Param("cid") Integer cid,
			@Param("status") Integer status, @Param("channel") String channel);

	public int countCardUseSituation(@Param("channel") String code,
			@Param("status") int statusAlive, @Param("opid") int opIdCmcc);

}
