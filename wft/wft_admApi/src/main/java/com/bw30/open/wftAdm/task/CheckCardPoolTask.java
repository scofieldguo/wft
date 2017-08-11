package com.bw30.open.wftAdm.task;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.bw30.open.common.dao.mapper.WftCardActiveMapper;
import com.bw30.open.common.dao.mapper.WftCardStoreMapper;
import com.bw30.open.common.model.WftCardActive;
import com.bw30.open.common.model.WftCardStore;
import com.bw30.open.common.model.WftOperator;

/**
 * 比对卡库与卡池：
 * dubbo超时导致电信开卡入卡库，但未调入卡池。
 * 将不在卡池中的电信卡调入相应渠道的卡池
 * 
 * @author Jack
 *
 * 2014年10月10日
 */
public class CheckCardPoolTask {
	private static final Logger LOG = Logger.getLogger(CheckCardPoolTask.class);
	
	@Resource
	private WftCardStoreMapper wftCardStoreMapper;
	@Resource
	private WftCardActiveMapper wftCardActiveMapper;
	
	public void check(){
		LOG.info("check cardpool task start");
		checkCtcc();
		LOG.info("check cardpool task end");
	}
	
	/**
	 * 检查电信卡池：将不在卡池中的卡调入相应渠道的卡池
	 */
	private void checkCtcc(){
		List<WftCardStore> csList = this.wftCardStoreMapper.getCardForActive(0, WftOperator.OP_ID_CTCC);
		if(null != csList && 0 < csList.size()){
			LOG.info("check cardpool task: size = " + csList.size());
			for(WftCardStore cs : csList){
				if(cs.getChannel().equals("10015")){
					continue;
				}
				try{
					WftCardActive ca = new WftCardActive();
					ca.setId(cs.getId());
					ca.setOpid(cs.getOpid());
					ca.setSsid(cs.getSsid());
					ca.setPrvid(cs.getPrvid());
					ca.setCtype(cs.getCtype());// 类型：0
					ca.setNo(cs.getNo());
					ca.setPwd(cs.getPwd());// 卡密加密
					ca.setUstatus(0);
					ca.setCstatus(WftCardActive.CSTATUS_AVAILABLE);
					ca.setBvalue(cs.getBvalue());
					ca.setTvalue(cs.getTvalue());
					ca.setVbtime(cs.getVbtime());
					ca.setVetime(cs.getVetime());
					ca.setIntime(new Date());
					ca.setUtvalue(0);
					ca.setUcount(0);
					ca.setCache(WftCardActive.OUT_CACHE);
					ca.setChannel(cs.getChannel());
					wftCardActiveMapper.insert(ca);
					
					updateStore(cs);
				}catch(Exception e){
					updateStore(cs);
					e.printStackTrace();
					LOG.error("check cardpool task error"+e);
					continue;
				}
			}
		}else{
			LOG.info("check cardpool task: size = 0 ");
		}
		
	}

	private void updateStore(WftCardStore cs) {
		WftCardStore cardstore = new WftCardStore();
		cardstore.setId(cs.getId());
		cardstore.setInplace(1);
		cardstore.setIntime(new Date());
		this.wftCardStoreMapper.update(cardstore);
	}

	public void setWftCardStoreMapper(WftCardStoreMapper wftCardStoreMapper) {
		this.wftCardStoreMapper = wftCardStoreMapper;
	}

	public void setWftCardActiveMapper(WftCardActiveMapper wftCardActiveMapper) {
		this.wftCardActiveMapper = wftCardActiveMapper;
	}
	
}
