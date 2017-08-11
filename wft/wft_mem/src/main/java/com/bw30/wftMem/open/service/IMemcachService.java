package com.bw30.wftMem.open.service;

public interface IMemcachService {
	/**
	 * �ж��Ƿ��KEY
	 * @param key
	 * @return
	 */
	public boolean isExistKey(String key);
	/**
	 * ���KEY��ȡ{K,V}
	 * @param key
	 * @return
	 */
	public Object getValue(String key);
	/**
	 * ѹ��{K,V}
	 * @param key
	 * @param o
	 * @return
	 */
	public boolean setValue(String key, Object o);
	/**
	 * ɾ��KEY
	 * @param key
	 * @return
	 */
	public boolean delValue(String key);
}
