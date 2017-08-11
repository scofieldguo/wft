package com.bw30.open.wftAdm.service.card;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.multipart.MultipartFile;

import com.bw30.open.common.dao.mapper.WftCardActiveMapper;
import com.bw30.open.common.dao.mapper.WftCardStoreMapper;
import com.bw30.open.common.model.WftCardStore;

public class CardDeleteService {

	@Resource
	private WftCardActiveMapper wftCardActiveMapper;
	@Resource
	private WftCardStoreMapper wftCardStoreMapper;

	public void setWftCardActiveMapper(WftCardActiveMapper wftCardActiveMapper) {
		this.wftCardActiveMapper = wftCardActiveMapper;
	}

	public void setWftCardStoreMapper(WftCardStoreMapper wftCardStoreMapper) {
		this.wftCardStoreMapper = wftCardStoreMapper;
	}

	public WftCardStore getCardByNo(String no) {
		List<WftCardStore> cardList = this.wftCardStoreMapper.findByNo(no);
		if (null != cardList && 0 < cardList.size()) {
			return cardList.get(0);
		}
		return null;
	}

	public List<String> deleteCard(MultipartFile txtFile, List<String> list)
			throws Exception {
		boolean flag = true;
		BufferedReader br = new BufferedReader(new InputStreamReader(txtFile
				.getInputStream()));
		String line = null;
		String no = null;
		WftCardStore cardStore = null;
		while (null != (line = br.readLine())) {
			no = line.trim();
			cardStore = this.getCardByNo(no);
			if (null == cardStore) {
				continue;
			} else {
				Integer id = cardStore.getId();
				String channel = cardStore.getChannel();
				try {
					wftCardActiveMapper.delete(channel, id);
					wftCardStoreMapper.delete(id);
				} catch (Exception e) {
					flag = false;
					// TODO: handle exception
				} finally {
					if (flag == false) {
						list.add(no);
						flag = true;
						continue;
					}
				}
			}
		}
		br.close();
		return list;
	}
}
