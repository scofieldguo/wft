package com.bw30.open.cardpool.service.cmcc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.bw30.open.cardpool.service.cmcc.WlanGetAllPackageRes.PackageInfo;
import com.bw30.open.cardpool.service.cmcc.WlanGetUsageRes.UsageDetail;
import com.bw30.open.wft.common.cardpool.rmi.bean.CardRecord;
import com.bw30.open.wft.common.cardpool.rmi.bean.OnlineBean;
import com.bw30.open.wft.common.cardpool.rmi.bean.RecordBean;
import com.bw30.open.wft.common.cardpool.rmi.bean.RespBean;
import com.thoughtworks.xstream.XStream;

public class CardCmccService {
	private static final Logger LOG = Logger.getLogger("CMCC");
	private static final String CHARSET = "UTF-8";

	private static final SimpleDateFormat SDF_ONLINE = new SimpleDateFormat(
			"yyyy/MM/dd HH:mm:ss");
	private static final SimpleDateFormat SDF_OFFLINE = new SimpleDateFormat(
			"yyyyMMdd HHmmss");
	private static final SimpleDateFormat SDF_YMD_HMS = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/**
	 * CMCC接口响应状态：成功
	 */
	private static final int CMCC_RESULT_OK = 0;

	private String url = null;
	private String URL_GET_BALANCE;
	private String URL_QUERY_ONLINE;
	private String URL_QUEYR_OFFLINE;

	public void setUrl(String url) {
		this.url = url;

		URL_GET_BALANCE = this.url + "wlanGetAllPackage.do";
		URL_QUERY_ONLINE = this.url + "wlanGetTerminalStatus.do";
		URL_QUEYR_OFFLINE = this.url + "wlanGetUsage.do";
	}

	/**
	 * 获取卡的剩余时长
	 * 
	 * @param cardNumber
	 * @param pwd
	 * @throws Exception
	 */
	public Integer getBalance(String cardNumber, String pwd) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("Account=").append(cardNumber).append("&")
				.append("Password=").append(pwd);
		WlanGetAllPackageRes res = this.callApi(URL_GET_BALANCE, sb.toString(),
				WlanGetAllPackageRes.class);
		if (null == res) {
			return null;
		}

		switch (res.getResultCode()) {
		case CMCC_RESULT_OK:
			// TODO 时长、流量怎么算
			List<PackageInfo> list = res.getCurFlowPkg();
			if (null != list && 0 < list.size()) {
				PackageInfo pi = list.get(0);
				return Integer.parseInt(pi.getPkgFreeRes());// .getPkgFlow();
			}
			return null;
		default:
			return null;
		}
	}

	/**
	 * 在线查询<br/>
	 * 移动允许多个用户同时在线，所以通过接入ip区分
	 * 
	 * @param cardNumber
	 * @param pwd
	 * @param ip
	 *            登陆热点后分配给用户的ip
	 * @throws Exception
	 */
	public OnlineBean queryOnline(String cardNumber, String pwd, String ip,
			Date dtime) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("Account=").append(cardNumber).append("&")
				.append("Password=").append(pwd);
		WlanGetTerminalStatusRes res = this.callApi(URL_QUERY_ONLINE,
				sb.toString(), WlanGetTerminalStatusRes.class);

		OnlineBean ob = new OnlineBean();
		ob.setResult(RespBean.RESULT_OK);

		if (null == res) {
			throw new Exception("queryOnline error: no data response");
			// ob.setResult(RespBean.RESULT_FAIL);
			// return ob;
		}

		switch (res.getResultCode()) {
		case CMCC_RESULT_OK:
			List<WlanGetTerminalStatusRes.TerminalInfo> list = res
					.getTerminalList();
			if (null != list && 0 < list.size()) {
				for (WlanGetTerminalStatusRes.TerminalInfo ti : list) {
					if (null == ip) {// 没有ip，则用分卡时间进行匹配
						if (null == dtime) {
							ob.setResult(RespBean.RESULT_FAIL);
							return ob;
						}
						Date startTime = SDF_ONLINE.parse(ti.getLogintime());
						long len = startTime.getTime() - dtime.getTime();
						len /= 1000;// 秒
						if (0 < len && len < 2 * 60) {
							ob.setStarttime(SDF_YMD_HMS.format(startTime));
							ob.setFramedip(ti.getWlanuserip());
							ob.setNasip(ti.getWlanacip());
							ob.setNasport(ti.getNasport());
							ob.setSessionid(ti.getSessionid());
							return ob;
						}
					} else if (ti.getWlanuserip().equalsIgnoreCase(ip)) {// 根据ip匹配在线
						Date startTime = SDF_ONLINE.parse(ti.getLogintime());
						ob.setStarttime(SDF_YMD_HMS.format(startTime));
						ob.setFramedip(ti.getWlanuserip());
						ob.setNasip(ti.getWlanacip());
						ob.setNasport(ti.getNasport());
						ob.setSessionid(ti.getSessionid());
						return ob;
					}
				}
			}

			// ob.setResult("" + res.getResultCode());//不在线
			// return ob;
			throw new Exception("queryOnline: no online matched");
		default:
			ob.setResult("" + res.getResultCode());// 查询失败
			return ob;
		}
	}

	/**
	 * 清单查询
	 * 
	 * @param cardNumber
	 * @param pwd
	 * @param startDate
	 *            开始时间，yyyyMMdd HHmmss
	 * @param endDate
	 *            结束时间，yyyyMMdd HHmmss
	 * @throws Exception
	 */
	public RecordBean queryOffline(String cardNumber, String pwd,
			String startDate, String endDate) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("Account=").append(cardNumber).append("&")
				.append("Password=").append(pwd).append("&")
				.append("QryBegin=")
				.append(URLEncoder.encode(startDate, "UTF-8")).append("&")
				.append("QryEnd=").append(URLEncoder.encode(endDate, "UTF-8"));
		WlanGetUsageRes res = this.callApi(URL_QUEYR_OFFLINE, sb.toString(),
				WlanGetUsageRes.class);

		RecordBean rb = new RecordBean();
		rb.setCno(cardNumber);
		rb.setResult(RespBean.RESULT_OK);

		if (null == res) {
			// rb.setResult(RespBean.RESULT_FAIL);
			// return rb;

			throw new Exception("queryOffline error: no data response");
		}

		switch (res.getResultCode()) {
		case CMCC_RESULT_OK:
			List<UsageDetail> list = res.getUsageDetailList();
			if (null != list && 0 < list.size()) {
				List<CardRecord> rList = new ArrayList<CardRecord>(list.size());
				Date startTime = null;
				Date stopTime = null;
				for (UsageDetail ud : list) {
					CardRecord r = new CardRecord();
					startTime = SDF_OFFLINE.parse(ud.getStartTime());
					stopTime = SDF_OFFLINE.parse(ud.getStopTime());
					r.setCno(cardNumber);
					r.setStarttime(SDF_YMD_HMS.format(startTime));
					r.setEndtime(SDF_YMD_HMS.format(stopTime));
					r.setTimelen(ud.getResUsage());
					rList.add(r);
				}
				rb.setRecordList(rList);
			}
			return rb;
		default:
			rb.setResult("" + res.getResultCode());
			return rb;
		}

	}

	/**
	 * 调用移动api接口
	 * 
	 * @param url
	 * @param jsonStr
	 * @return
	 * @throws Exception
	 */
	private <T> T callApi(String url, String queryStr, Class<T> clz)
			throws Exception {
		LOG.info("req to [" + url + "] :" + queryStr);
		System.out.println("req to [" + url + "] :" + queryStr);
		url = url + "?" + queryStr;

		HttpClient client = new DefaultHttpClient();
		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 50000);
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response = client.execute(httpGet);
		int statusCode = response.getStatusLine().getStatusCode();
		LOG.info("[" + url + "] resp http code :" + statusCode);
		if (HttpStatus.SC_OK == statusCode) {
			String str = EntityUtils.toString(response.getEntity(), CHARSET);
			httpGet.abort();
			LOG.info("[" + url + "] resp :" + str);
			System.out.println("[" + url + "] resp :" + str);
			if (null == str || 0 == str.length()) {
				return null;
			}
			return this.parseXml(str, clz);
		}
		return null;
	}

	/**
	 * 解析xml
	 * 
	 * @param str
	 * @param clz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <T> T parseXml(String str, Class<T> clz) {
		XStream xStream = new XStream();
		xStream.processAnnotations(clz);
		return (T) xStream.fromXML(str);
	}

	public static void main(String[] args) throws Exception {
		CardCmccService ccs = new CardCmccService();
		ccs.setUrl("http://218.206.68.67:8080/cmccinter/");

//		Integer balance = ccs.getBalance("15172077131", "RYDZ9Q");
//		while(null == balance){
//			balance = ccs.getBalance("15172077131", "RYDZ9Q");
//		}
//		 ccs.queryOnline("15172077131", "PHR186", "127.0.0.1", null);
//		 ccs.queryOffline("15172077131", "PHR186", "20140930 000000",
//		 "20140930 235959");
		
		int i = 0;
		RecordBean rb = null;
		while(null == rb){
			i++;
			System.out.println(i);
			try{
				rb = ccs.queryOffline("15172077131", "RYDZ9Q", "20140930 000000",
						 "20140930 235959");
			}catch(Exception e){
				
			}
		}
		

		// BufferedReader br = new BufferedReader(new
		// FileReader("d:/cmcc流量.txt"));
		// String line = null;
		// PrintWriter pw1 = new PrintWriter(new
		// FileWriter("d:/cmcc流量-res.txt"));
		// PrintWriter pw2 = new PrintWriter(new
		// FileWriter("d:/cmcc流量-res2.txt"));
		// int i = 0;
		// while(null != (line = br.readLine())){
		// i++;
		// System.out.println(i);
		// String[] strs = line.split(",");
		// Integer flow = null;
		// try{
		// flow = ccs.getBalance(strs[0], strs[1]);
		// }catch(Exception e){
		// e.printStackTrace();
		// }
		//
		// if(null != flow){
		// pw1.println(line + "," + flow);
		// }else{
		// pw2.println(line);
		// }
		// }
		// br.close();
		// pw1.close();
		// pw2.close();
		
//		test();

	}

	private static void test() throws Exception {
		CardCmccService ccs = new CardCmccService();
		ccs.setUrl("http://218.206.68.67:8080/cmccinter/");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		BufferedReader br = new BufferedReader(new FileReader("d://cmcc.txt"));
		String pwd = "&Password=0qtpnoX4";
		List<String> cards = new ArrayList<String>();
		String line = null;
		while (null != (line = br.readLine())) {
			cards.add(line);
		}
		br.close();

		PrintWriter pw = new PrintWriter(new FileWriter("d://cmcc-res.txt"));
		PrintWriter succPw = new PrintWriter(
				new FileWriter("d://cmcc-succ.txt"));
		PrintWriter errPwdPw = new PrintWriter(new FileWriter(
				"d://cmcc-errpwd.txt"));
		PrintWriter errPw = new PrintWriter(new FileWriter("d://cmcc-err.txt"));

		Iterator<String> it = null;
		int size = cards.size();
		String cno = null;
		int i = 0;
		int j = 0;
		int succ = 0;
		int errPwd = 0;
		Long time = System.currentTimeMillis();
		while (size > 0) {
			i++;
			System.out.println(new StringBuilder("loop-").append(i)
					.append(" start: ").append(size).toString());
			it = cards.iterator();
			while (it.hasNext()) {
				j++;
				cno = it.next();

				WlanGetAllPackageRes res = ccs
						.callApi(
								"http://218.206.68.67:8080/cmccinter/wlanGetAllPackage.do",
								new StringBuilder("Account=").append(cno)
										.append(pwd).toString(),
								WlanGetAllPackageRes.class);
				
				System.out.println(new StringBuilder("loop-").append(i)
						.append(" remains: ").append(size - j).toString());
				
				if (null == res) {// time out
					continue;
				}

				it.remove();
				switch (res.getResultCode()) {
				case CMCC_RESULT_OK:
					succ++;
					List<PackageInfo> list = res.getCurFlowPkg();
					if (null != list && 0 < list.size()) {
						PackageInfo pi = list.get(0);
						succPw.println(new StringBuilder(cno).append(",")
								.append(pi.getPkgFreeRes()).append(",").append(sdf.format(new Date())).toString());
					}
					break;
				case 4:
				case 5:
				case 9:
					errPwd++; // verify code
					errPwdPw.println("cno" + "," + res.getResultCode() + ","
							+ res.getResultDesc());
					break;
				default:
					errPw.println("cno" + "," + res.getResultCode() + ","
							+ res.getResultDesc());
				}

			}
			
			pw.println(String.format("loop-%s result: size=%s, succ=%s, errPwd=%s", i, size, succ, errPwd));
			pw.flush();
			succPw.flush();
			errPwdPw.flush();
			errPw.flush();
			
			size = cards.size();
			succ = 0;
			errPwd = 0;
		}

		pw.close();
		succPw.close();
		errPwdPw.close();
		errPw.close();
		
		time = System.currentTimeMillis() - time;
		time /= 1000;// 秒
		System.out.println("finish: " + time + "s");
	}

}
