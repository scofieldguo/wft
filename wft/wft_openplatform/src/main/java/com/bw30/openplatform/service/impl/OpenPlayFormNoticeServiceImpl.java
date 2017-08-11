package com.bw30.openplatform.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bw30.open.common.dao.mapper.NoticeMapper;
import com.bw30.open.common.model.OpenPlatformNotice;
import com.bw30.open.wft.common.Pager;
import com.bw30.openplatform.service.IOpenPlayFormNoticeService;

public class OpenPlayFormNoticeServiceImpl implements IOpenPlayFormNoticeService{
	private NoticeMapper noticeMapper ;
	public List<OpenPlatformNotice> findNotice(Integer id, Integer status,Pager pager) {
		Map<String, Object> paramMap=new HashMap<String, Object>();
		paramMap.put("id", id);
		paramMap.put("status", status);
		if(pager!=null){
		pager.setRecCount(noticeMapper.countByParam(paramMap));
		}
		return noticeMapper.findNotice(id,status,pager);
	}
	public void setNoticeMapper(NoticeMapper noticeMapper) {
		this.noticeMapper = noticeMapper;
	}
	public void updateNotice(Integer userid) {
		noticeMapper.updateByUserId(userid);
		
	}
	public void delete(OpenPlatformNotice notice) {
		noticeMapper.delete(notice.getId());
	}

}
