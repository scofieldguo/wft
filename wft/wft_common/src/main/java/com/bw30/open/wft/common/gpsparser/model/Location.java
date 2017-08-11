package com.bw30.open.wft.common.gpsparser.model;

public class Location {
	private Latitude location;
	private String formatted_address;
	private String business;
	private Address addressComponent;
	private Integer cityCode;
	public Latitude getLocation() {
		return location;
	}
	public void setLocation(Latitude location) {
		this.location = location;
	}
	public String getFormatted_address() {
		return formatted_address;
	}
	public void setFormatted_address(String formatted_address) {
		this.formatted_address = formatted_address;
	}
	public String getBusiness() {
		return business;
	}
	public void setBusiness(String business) {
		this.business = business;
	}
	public Address getAddressComponent() {
		return addressComponent;
	}
	public void setAddressComponent(Address addressComponent) {
		this.addressComponent = addressComponent;
	}
	public Integer getCityCode() {
		return cityCode;
	}
	public void setCityCode(Integer cityCode) {
		this.cityCode = cityCode;
	}
	
	
}
