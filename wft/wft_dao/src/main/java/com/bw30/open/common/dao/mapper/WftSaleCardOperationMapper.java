package com.bw30.open.common.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.bw30.open.common.dao.mapper.common.GenericMapper;
import com.bw30.open.common.model.sale.WftSaleCardOperation;
import com.bw30.open.wft.common.Pager;

public interface WftSaleCardOperationMapper extends GenericMapper<WftSaleCardOperation>{

	int countByParam(@Param("paramMap")Map<String, Object> paramMap,@Param("channel_id") Integer id);

	List<WftSaleCardOperation> pageFindByParam(@Param("channel_id")Integer id,@Param("paramMap") Map<String, Object> paramMap,@Param("pager") Pager pager);

	List<WftSaleCardOperation> findByParam(@Param("paramMap")Map<String, Object> paramMap,
			@Param("channel_id")Integer id);


}
