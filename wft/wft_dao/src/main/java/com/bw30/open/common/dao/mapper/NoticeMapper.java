package com.bw30.open.common.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bw30.open.common.dao.mapper.common.GenericMapper;
import com.bw30.open.common.model.OpenPlatformNotice;
import com.bw30.open.wft.common.Pager;

public interface NoticeMapper extends GenericMapper<OpenPlatformNotice>{

	List<OpenPlatformNotice> findNotice(@Param("userid")Integer userid,@Param("status") Integer status,@Param("pager") Pager pager);

	void updateByUserId(@Param("userid")Integer userid);

}
