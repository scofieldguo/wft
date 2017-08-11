package com.bw30.open.common.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bw30.open.common.dao.mapper.common.GenericMapper;
import com.bw30.open.common.model.BillCompare;

public interface BillCompareMapper extends GenericMapper<BillCompare> {
	/**
	 * 批量插入数据
	 * 
	 */
	public void batchInsert(@Param("list") List<BillCompare> list);
	
	/**
	 * 清空数据列表
	 */
	public void deleteAll();
}
