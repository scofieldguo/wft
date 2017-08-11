package com.bw30.openplatform.util;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpClientUtil {
    private static final String CHARSET = HTTP.UTF_8;
    private static final Integer MAX_TOTAL_CONNECTIONS = 100;//最大连接数
    private static final Integer MAX_ROUTE_CONNECTIONS = 100;//每个路由最大连接数
    private static final Integer CONNECT_TIMEOUT = 30000;//连接超时时间
    private static final Integer READ_TIMEOUT = 30000;//读取超时时间
    private static final Integer WAIT_TIMEOUT = 2000; //获取连接的最大等待时间
	public static HttpClient getThreadSaveHttpClient(){  
		HttpClient apiHttpClient = null;  
		try{
	        HttpParams params = new BasicHttpParams();  
	        //设置基本参数  
	        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);  
	        HttpProtocolParams.setContentCharset(params, CHARSET);  
	        HttpProtocolParams.setUseExpectContinue(params, false); 
	        ConnManagerParams.setTimeout(params, WAIT_TIMEOUT);
	        ConnManagerParams.setMaxTotalConnections(params, MAX_TOTAL_CONNECTIONS);
	        ConnPerRouteBean connPerRoute = new ConnPerRouteBean(MAX_ROUTE_CONNECTIONS);
	        ConnManagerParams.setMaxConnectionsPerRoute(params, connPerRoute);
	        HttpConnectionParams.setConnectionTimeout(params, CONNECT_TIMEOUT);
	        HttpConnectionParams.setSoTimeout(params, READ_TIMEOUT);
	        
	        SchemeRegistry schReg = new SchemeRegistry();  
	        schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));  
	        schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));  
	        //使用线程安全的连接管理来创建HttpClient  
	        ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg); 
            apiHttpClient = new DefaultHttpClient(conMgr, params);
		}catch (Exception e){
			apiHttpClient =  new DefaultHttpClient();
		}
        return apiHttpClient;  
    }
	
	public static String post(String server,String param){
		HttpPost httPost = null;
		try{
			HttpClient httpClient = HttpClientUtil.getThreadSaveHttpClient();
			httPost = new HttpPost(server);
			StringEntity entity = new StringEntity(param, "utf-8");
			httPost.setEntity(entity);
			HttpResponse httpResponse = httpClient.execute(httPost);
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				HttpEntity httpEntity = httpResponse.getEntity();
				if (httpEntity != null) {
					String resp = EntityUtils.toString(httpEntity);
					httPost.abort();
					return resp;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		} finally{
			if(httPost != null)
				httPost.abort();
		}
		return null;
	}
	
	public static String post(String server,Map<String, String> param){
		HttpPost httPost = null;
		try{
			HttpClient httpClient = HttpClientUtil.getThreadSaveHttpClient();
			httPost = new HttpPost(server);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			for(String key:param.keySet()){
				nvps.add(new BasicNameValuePair(key, param.get(key)));
			}
			httPost.setEntity(new UrlEncodedFormEntity(nvps,"UTF-8"));
			HttpResponse httpResponse = httpClient.execute(httPost);
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				HttpEntity httpEntity = httpResponse.getEntity();
				if (httpEntity != null) {
					String resp = EntityUtils.toString(httpEntity);
					httPost.abort();
					return resp;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			if(httPost != null)
				httPost.abort();
		}
		return null;
	}
	
}
