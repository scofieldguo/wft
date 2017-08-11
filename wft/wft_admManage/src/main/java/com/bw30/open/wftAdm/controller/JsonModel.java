package com.bw30.open.wftAdm.controller;

import java.io.Serializable;

public class JsonModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int page; // 页码
	private int records; // 每页显示记录
	private int total; // 总数
	private Object rows;// 显示元素对象

	public Object getRows() {
		return rows;
	}

	public void setRows(Object rows) {
		this.rows = rows;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRecords() {
		return records;
	}

	public void setRecords(int records) {
		this.records = records;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
	
}
