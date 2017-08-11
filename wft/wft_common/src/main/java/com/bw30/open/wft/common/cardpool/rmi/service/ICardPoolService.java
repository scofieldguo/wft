package com.bw30.open.wft.common.cardpool.rmi.service;

import java.util.Date;
import java.util.List;

import com.bw30.open.wft.common.cardpool.rmi.bean.CardBean;
import com.bw30.open.wft.common.cardpool.rmi.bean.CardPoolBean;
import com.bw30.open.wft.common.cardpool.rmi.bean.CardRecord;
import com.bw30.open.wft.common.cardpool.rmi.bean.OnlineBean;
import com.bw30.open.wft.common.cardpool.rmi.bean.RechargeBean;
import com.bw30.open.wft.common.cardpool.rmi.bean.RecordBean;

/**
 * 卡池服务，提供：开卡、充值、修改卡密、查询剩余时长、查询有效期、查询在线状态、查询卡使用记录等功能
 * 
 * @author Jack
 * 
 *         2014年8月6日
 */
public interface ICardPoolService {
	/**
	 * 合作方开卡<br/>
	 * 策略：合作方请求开卡时，即向运营商请求开卡
	 * 
	 * @param partner
	 *            合作方标识
	 * @param op
	 *            卡类型：1移动，2电信
	 * @param ctype
	 *            卡品
	 * @param tradesn
	 *            订单号
	 * @return
	 * @throws Exception
	 */
	public CardBean openCard(String partner, Integer op, String ctype,
			String tradesn) throws Exception;

	/**
	 * 充值
	 * 
	 * @param partner
	 *            合作方标识
	 * @param cid
	 *            卡在开放平台中的id
	 * @param cno
	 *            卡号
	 * @param op
	 *            卡类型
	 * @param ctype
	 *            卡品
	 * @param tradesn
	 *            订单号
	 * @return
	 * @throws Exception
	 */
	public RechargeBean recharge(String partner, Integer cid, String cno,
			Integer op, String ctype, String tradesn) throws Exception;

	/**
	 * 查询卡的剩余时长
	 * 
	 * @param partner
	 *            合作方标识
	 * @param cid
	 *            卡在开放平台中的id
	 * @param op
	 *            卡类型
	 * @param cno
	 *            卡号
	 * @param pwd
	 *            卡密
	 * @return
	 * @throws Exception
	 */
	public Integer getBalance(String partner, Integer cid, Integer op,
			String cno, String pwd) throws Exception;

	/**
	 * 修改卡密码
	 * 
	 * @param partner
	 *            合作方标识
	 * @param cid
	 *            卡在开放平台中的id
	 * @param op
	 *            卡类型
	 * @param cno
	 *            卡号
	 * @param pwd
	 *            卡密
	 * @param newPwd
	 *            新密码
	 * @return
	 * @throws Exception
	 */
	public boolean updatePwd(String partner, Integer cid, Integer op,
			String cno, String pwd, String newPwd) throws Exception;

	/**
	 * 在线查询<br/>
	 * 电信卡不需要传ip、dtime，<br/>
	 * 移动卡必须传ip或dtime（没取到ip时可传dtime）
	 * 
	 * @param op
	 *            卡类型
	 * @param cno
	 *            卡号
	 * @param pwd
	 *            卡密
	 * @param ip
	 *            用户wifi接入ip，移动在线查询时需要根据ip进行比对
	 * @param dtime
	 *            分卡时间
	 * 
	 * @throws Exception
	 */
	public OnlineBean queryOnline(Integer op, String cno, String pwd,
			String ip, Date dtime) throws Exception;

	/**
	 * 卡使用记录查询
	 * 
	 * @param partner
	 *            合作方标识
	 * @param op
	 *            卡类型
	 * @param cno
	 *            卡号
	 * @param pwd
	 *            卡密
	 * @param startDate
	 *            开始时间，yyyy-MM-dd
	 * @param endDate
	 *            结束时间，yyyy-MM-dd
	 * @return
	 * @throws Exception
	 */
	public List<CardRecord> queryRecord(String partner, Integer op, String cno,
			String pwd, String startDate, String endDate) throws Exception;

	/**
	 * 查移动卡使用记录
	 * 
	 * @param cno
	 * @param pwd
	 * @param startDate
	 *            yyyyMMdd HHmmss
	 * @param endDate
	 *            yyyyMMdd HHmmss
	 * @return
	 * @throws Exception
	 */
	public RecordBean queryCmccRecord(String cno, String pwd, String startDate,
			String endDate) throws Exception;
	
	/**
	 * 查询卡池信息
	 * @param op
	 * @return
	 * @throws Exception
	 */
	public CardPoolBean queryPoolInfo(Integer op) throws Exception;

}
