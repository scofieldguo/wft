package com.bw30.open.common.dao.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.bw30.open.common.dao.mapper.common.GenericMapper;
import com.bw30.open.common.model.WftCardStore;

public interface WftCardStoreMapper extends GenericMapper<WftCardStore> {
	/**
	 * 获取卡号列表
	 * 
	 * @param opid null查所有
	 * @return
	 */
	public List<String> findAllNo(@Param("opid") Integer opid);
	
	public List<WftCardStore> findByNo(@Param("no") String no);

	/**
	 * 批量插入卡
	 * 
	 * @param list
	 */
	public void batchInsert(@Param("list") List<WftCardStore> list);

	/**
	 * 批量修改卡状态
	 * 
	 * @param map
	 */
	public void batchUpdateStatus(@Param("params") Map<String, Object> params);

	/**
	 * 获取卡库中到预启用时间的卡
	 * 
	 * @param cstatus
	 * @param prtime
	 * @return
	 */
	public List<WftCardStore> getCardForEnable(@Param("cstatus") int cstatus,
			@Param("prtime") Date prtime);
	
	/**
	 * 获取卡库或卡池中的卡
	 * @param inplace 0卡库，1卡池
	 * @param opid 
	 * @return
	 */
	public List<WftCardStore> getCardForActive(@Param("inplace") Integer inplace, @Param("opid") Integer opid);

}
