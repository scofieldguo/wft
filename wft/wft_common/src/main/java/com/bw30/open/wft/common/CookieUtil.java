package com.bw30.open.wft.common;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

	private final static Integer MAX_AGE = 60 * 60;// 默认Cookie的生命周期为1小时
	private final static String PATH = "/";// 默认同一服务器中，所有的WEB应用共享Cookie

	/**
	 * 获取Cookie数组
	 * 
	 * @param request
	 * @return
	 */
	public static Cookie[] getCookieArray(HttpServletRequest request) {
		Cookie[] cookieArray = request.getCookies();
		return cookieArray;
	}

	/**
	 * 获取Cookie Map
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, Cookie> getCookieMap(HttpServletRequest request) {
		Map<String, Cookie> cookieMap = null;
		Cookie[] cookieArray = getCookieArray(request);
		if (cookieArray != null) {
			cookieMap = new HashMap<String, Cookie>();
			for (Cookie cookie : cookieArray) {
				String cookieName = cookie.getName();
				cookieMap.put(cookieName, cookie);
			}
		}
		return cookieMap;
	}

	/**
	 * 根据Cookie名称获取Cookie
	 * 
	 * @param request
	 * @param cookieName
	 *            Cookie的key
	 * @return
	 */
	public static Cookie getCookie(HttpServletRequest request, String cookieName) {
		Cookie cookie = null;
		Map<String, Cookie> cookieMap = getCookieMap(request);
		if (cookieMap != null && cookieMap.containsKey(cookieName))
			cookie = cookieMap.get(cookieName);
		return cookie;
	}

	/**
	 * 构造Cookie（工厂方法）
	 * 
	 * @param cookieName
	 *            Cookie的key
	 * @param cookieValue
	 *            Cookie的value
	 * @return
	 */
	public static Cookie getCookie(String cookieName, String cookieValue) {
		Cookie cookie = new Cookie(cookieName, cookieValue);
		cookie.setMaxAge(MAX_AGE);
		cookie.setPath(PATH);
		return cookie;
	}

	/**
	 * 添加Cookie到响应中
	 * 
	 * @param cookie
	 * @param response
	 * @return
	 */
	public static boolean addCookie(Cookie cookie, HttpServletResponse response) {
		if (cookie == null)
			return false;
		response.addCookie(cookie);
		return true;
	}

	/**
	 * 添加Cookie到响应中
	 * 
	 * @param cookieName
	 *            Cookie的key
	 * @param cookieValue
	 *            Cookie的value
	 * @param response
	 * @return
	 */
	public static boolean addCookie(String cookieName, String cookieValue,
			HttpServletResponse response) {
		Cookie cookie = getCookie(cookieName, cookieValue);
		response.addCookie(cookie);
		return true;
	}

	/**
	 * 移除Cookie
	 * 
	 * @param cookieName
	 * @param response
	 * @return
	 */
	public static boolean removeCookie(String cookieName,
			HttpServletResponse response) {
		if (cookieName != null && !"".equals(cookieName)) {
			Cookie cookie = new Cookie(cookieName, null);
			cookie.setMaxAge(0);
			cookie.setPath(PATH);
			response.addCookie(cookie);
			return true;
		}
		return false;
	}

}
