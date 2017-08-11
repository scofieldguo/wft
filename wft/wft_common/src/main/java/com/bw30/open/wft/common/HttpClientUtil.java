package com.bw30.open.wft.common;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

public class HttpClientUtil {
	private static DefaultHttpClient httpClient;

	private static final Integer CONN_TIMEOUT = 2000;
	private static final Integer SO_TIMEOUT = 5000;

	/**
	 * @param url
	 * @return
	 */
	public static String get(String url) {
		DefaultHttpClient httpClient = getClient();
		HttpGet httpGet = null;
		try {
			httpGet = new HttpGet(url);
			HttpResponse response = httpClient.execute(httpGet);
			return getResult(response);
		} catch (Exception e) {
			e.getMessage();
		} finally {
			if (httpGet != null && !httpGet.isAborted()) {
				httpGet.abort();
			}
		}
		return null;
	}

	/**
	 * 创建HttpClient
	 * 
	 * @return
	 */
	private static DefaultHttpClient getClient() {
		if (httpClient == null) {
			synchronized (HttpClientUtil.class) {
				if (httpClient == null) {
					HttpParams params = new BasicHttpParams();
					HttpConnectionParams.setConnectionTimeout(params,
							CONN_TIMEOUT);// 连接超时
					HttpConnectionParams.setSoTimeout(params, SO_TIMEOUT);// 请求超时
					SchemeRegistry schReg = new SchemeRegistry();
					schReg.register(new Scheme("http", PlainSocketFactory
							.getSocketFactory(), 80));
					ClientConnectionManager conman = new ThreadSafeClientConnManager(
							params, schReg);
					httpClient = new DefaultHttpClient(conman, params);
				}
			}
		}
		return httpClient;
	}

	/**
	 * 创建httpClient
	 * 
	 * @param connTimeout 连接超时
	 * @param soTimeout 请求响应超时
	 * @return
	 */
	public static synchronized HttpClient getClient(Integer connTimeout,
			Integer soTimeout) {
		HttpClient client = null;
		HttpParams params = new BasicHttpParams();
		if(null != connTimeout){// 连接超时
			HttpConnectionParams.setConnectionTimeout(params, connTimeout);
		}
		if(null != soTimeout){// 请求超时
			HttpConnectionParams.setSoTimeout(params, soTimeout);
		}
		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		ClientConnectionManager conman = new ThreadSafeClientConnManager(
				params, schReg);
		client = new DefaultHttpClient(conman, params);
		return client;
	}

	private static String getResult(HttpResponse response)
			throws ParseException, IOException {
		if (response.getStatusLine().getStatusCode() == 200) {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				return EntityUtils.toString(entity, "UTF-8");
			}
		}
		return null;
	}

}
