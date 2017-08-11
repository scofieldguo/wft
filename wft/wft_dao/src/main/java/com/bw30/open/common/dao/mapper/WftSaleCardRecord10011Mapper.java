package com.bw30.open.common.dao.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.bw30.open.common.dao.mapper.common.GenericMapper;
import com.bw30.open.common.model.sale.WftSaleCardRecord;
import com.bw30.open.common.model.sale.WftSaleCardRecord10011;
import com.bw30.open.wft.common.Pager;

public interface WftSaleCardRecord10011Mapper extends GenericMapper<WftSaleCardRecord10011>{

	List<WftSaleCardRecord10011> findRecordByOrderId(@Param("channel_id")Integer id,@Param("order") String order);

	void update(@Param("record")WftSaleCardRecord10011 record,@Param("channel_id") Integer id);

	int countByParam(@Param("paramMap")Map<String, Object> paramMap,@Param("channel_id") Integer id);

	List<WftSaleCardRecord10011> pageFindByParam(@Param("paramMap")Map<String, Object> paramMap,
			@Param("channel_id")Integer id,@Param("pager") Pager pager);

	List<WftSaleCardRecord10011> findByParam(@Param("paramMap")HashMap<String, Object> paramMap,
			@Param("channel_id")Integer id);

	WftSaleCardRecord10011 findByCno(@Param("cno")String cno,@Param("channel_id") Integer id);
	
}
