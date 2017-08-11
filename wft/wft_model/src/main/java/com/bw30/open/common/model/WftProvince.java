package com.bw30.open.common.model;

import java.io.Serializable;

public class WftProvince implements Serializable {
	private static final long serialVersionUID = -6254317500008655946L;
	private Integer id;
	private String name;
	private String sname;

	public WftProvince() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

}