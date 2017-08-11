package com.bw30.open.wft.common.gpsparser;

import com.alibaba.fastjson.JSON;
import com.bw30.open.wft.common.HttpClientUtil;
import com.bw30.open.wft.common.gpsparser.model.Total;

/**
 * 根据经纬度获取省市信息
 *
 */
public class GpsParser {
	private String url;
	private String ak; //key，用百度账号申请时获取
	
	/**
	 * 根据经纬度获取省市区详细信息
	 * 
	 * @param latitude 纬度
	 * @param longitude 经度
	 * 
	 * @return 省市区详细信息
	 */
	public String parseAddr(double latitude, double longitude) {
		try{
			String addr = this.parse(latitude, longitude);
			if(null != addr){
				Total total=JSON.parseObject(addr, Total.class);
				if(0 == total.getStatus()){
					return total.getResult().getFormatted_address();
				}
			}
			return null;
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * 根据经纬度获取省份
	 * 
	 * @param latitude 纬度
	 * @param longitude 经度
	 * 
	 * @return 省份
	 */
	public String parseProvince(double latitude, double longitude){
		try{
			String addr = this.parse(latitude, longitude);
			if(null != addr){
				Total total=JSON.parseObject(addr, Total.class);
				if(0 == total.getStatus()){
					return total.getResult().getAddressComponent().getProvince();
				}
			}
			return null;
		}catch(Exception e){
			return null;
		}
	}
	
	private String parse(double latitude, double longitude){
		StringBuffer sb = new StringBuffer(this.url);
		sb.append("?ak=").append(this.ak)
			.append("&location=").append(latitude).append(",").append(longitude)
			.append("&output=json&pois=0");
		return HttpClientUtil.get(sb.toString());
	}
	
	public void setUrl(String url) {
		this.url = url;
	}

	public void setAk(String ak) {
		this.ak = ak;
	}

	public static final void main(String[] args) {
		GpsParser gpsParser = new GpsParser();
		gpsParser.setUrl("http://api.map.baidu.com/geocoder/v2/");
		gpsParser.setAk("kDe32pGQ7CvOn6mun54oW9yA");
		
		String addr = gpsParser.parseAddr(22.53836564, 114.02681090);
		System.out.println(addr);
		
		String pro = gpsParser.parseProvince(22.53836564, 114.02681090);
		System.out.println(pro);
	}

	
}
