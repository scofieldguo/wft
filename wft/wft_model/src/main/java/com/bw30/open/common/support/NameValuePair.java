package com.bw30.open.common.support;

public class NameValuePair {

	private String name;
	private Object value;

	public static NameValuePair createNameValuePair(String name, Object value) {
		return new NameValuePair(name, value);
	}

	private NameValuePair(String name, Object value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public Object getValue() {
		return value;
	}

}
