package com.bw30.open.common.dao.mapper.common;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.bw30.open.wft.common.Pager;

public interface GenericMapper<E> {

	public Integer count();

//	public List<E> pageFindAll(@Param("pager") Pager pager);

	public Integer countByParam(@Param("paramMap") Map<String, Object> paramMap);

//	public List<E> pageFindByParam(
//			@Param("paramMap") Map<String, Object> paramMap,
//			@Param("pager") Pager pager);

	public List<E> findByParam(@Param("paramMap") Map<String, Object> paramMap) ;
	
	public List<E> findByParam(@Param("paramMap") Map<String, Object> paramMap,@Param("channel")String channel);
	public List<E> pageFindByParam(@Param("paramMap") Map<String, Object> paramMap,@Param("pager")Pager pager);

	public List<E> findByIdChannel(@Param("id") Integer id, @Param("channel")String channel);

	public List<E> findListByClz(E e);
	
	public Integer countByClz(E e);
	
	public List<E> findAll();

	public E findById(@Param("id") Serializable id);

	public void insert(E e);

	public void delete(@Param("id") Serializable id);

	public void deleteByIds(@Param("ids") Serializable[] ids);

	public void logicDelete(@Param("id") Serializable id,
			@Param("status") Serializable status);

	public void update(E e);
	
	public void updateByParam(@Param("paramMap") Map<String, Object> paramMap,@Param("channel")String channel);
	
	public void updateByParam(@Param("setParamMap") Map<String, Object> setParamMap,
			@Param("paramMap") Map<String, Object> paramMap);

}
