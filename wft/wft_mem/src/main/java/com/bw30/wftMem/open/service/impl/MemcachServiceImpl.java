package com.bw30.wftMem.open.service.impl;

import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.XMemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

import com.bw30.wftMem.open.service.IMemcachService;

public class MemcachServiceImpl implements IMemcachService {

	private XMemcachedClient memcachedClient;

	@Override
	public boolean delValue(String key) {
		boolean result = false;
		try {
			result = getMemcachedClient().delete(key);
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (MemcachedException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public Object getValue(String key) {
		Object result = null;
		try {
			result = getMemcachedClient().get(key);
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (MemcachedException e) {
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return result;
	}

	@Override
	public boolean isExistKey(String key) {
		// TODO Auto-generated method stub
		boolean result = false;
		try {
			result = (getMemcachedClient().get(key) != null);
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (MemcachedException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public boolean setValue(String key, Object o) {
		// TODO Auto-generated method stub
		boolean result = false;
		if (o == null)
			return result;
		try {
			result = getMemcachedClient().set(key, 0, o);
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (MemcachedException e) {
			e.printStackTrace();
		}
		return result;
	}

	public void setMemcachedClient(XMemcachedClient memcachedClient) {
		this.memcachedClient = memcachedClient;
	}

	public XMemcachedClient getMemcachedClient() {
		if (memcachedClient != null) {
			memcachedClient.setOpTimeout(30000L);
		}
		return memcachedClient;
	}
}
