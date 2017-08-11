package com.bw30.open.common.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.bw30.open.common.dao.mapper.common.GenericMapper;
import com.bw30.open.common.model.stat.WftOpenPlatFormStat;
import com.bw30.open.wft.common.Pager;

public interface WftOpenPlatFormStatMapper extends
		GenericMapper<WftOpenPlatFormStat> {

	List<WftOpenPlatFormStat> pageFindByParamGroupByOp(
			@Param("paramMap") Map<String, Object> paramMap,
			@Param("pager") Pager pager);

	List<WftOpenPlatFormStat> findByParamGroupByOp(
			@Param("paramMap") Map<String, Object> paramMap);

	List<WftOpenPlatFormStat> getIntervalStat(@Param("startday")String startday,@Param("endday")String endday,
			@Param("channelcode")String channelcode,@Param("opIdCtcc") int opIdCtcc);

//	List<WftOpenStat> findWithParam(@Param("paramMap")Map<String, Object> paramMap);
//
//	int countByChannel(@Param("start")String start,@Param("end") String end,@Param("channel") String channel);
//
//	List<WftOpenStat> findPagerByChannel(@Param("start")String start,@Param("end") String end,@Param("pager") Pager pager,
//			@Param("channel")String channel);
//
//	List<WftOpenStat> findByChannel(@Param("start")String start,@Param("end") String end,@Param("channel") String channel);
//
//	public  WftOpenStat findByCondition(@Param("dairy")String dairy,@Param("channel")String channel);
	Integer countByParamGroupByOp(
			@Param("paramMap") Map<String, Object> paramMap);

	List<WftOpenPlatFormStat> pageFindMacByParamGroupByOp(
			@Param("paramMap") Map<String, Object> paramMap,
			@Param("pager") Pager pager);

	List<WftOpenPlatFormStat> findMacByParamGroupByOp(
			@Param("paramMap") Map<String, Object> paramMap);

	Integer countMacByParamGroupByOp(
			@Param("paramMap") Map<String, Object> paramMap);

	List<WftOpenPlatFormStat> findByParamGroupByPrv(
			@Param("paramMap") Map<String, Object> paramMap);

	WftOpenPlatFormStat getLastDayStat(
			@Param("paramMap") Map<String, Object> paramMap);

	// List<WftOpenStat> findWithParam(@Param("paramMap")Map<String, Object>
	// paramMap);
	//
	// int countByChannel(@Param("start")String start,@Param("end") String
	// end,@Param("channel") String channel);
	//
	// List<WftOpenStat> findPagerByChannel(@Param("start")String
	// start,@Param("end") String end,@Param("pager") Pager pager,
	// @Param("channel")String channel);
	//
	// List<WftOpenStat> findByChannel(@Param("start")String start,@Param("end")
	// String end,@Param("channel") String channel);
	//
	// public WftOpenStat findByCondition(@Param("dairy")String
	// dairy,@Param("channel")String channel);

	public WftOpenPlatFormStat findByOpidDairyChannel(
			@Param("opid") Integer opid, @Param("dairy") String dairy,
			@Param("channel") String channel);
}
