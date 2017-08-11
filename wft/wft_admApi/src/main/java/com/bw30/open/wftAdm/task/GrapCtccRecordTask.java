package com.bw30.open.wftAdm.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;


import com.bw30.open.common.dao.mapper.AccountMapper;
import com.bw30.open.common.dao.mapper.WftChannelMapper;
import com.bw30.open.common.dao.mapper.WftConsumeStatMapper;
import com.bw30.open.common.dao.mapper.WftOpenPlatFormStatMapper;
import com.bw30.open.common.model.OpenPlatformAccount;
import com.bw30.open.common.model.WftCardActive;
import com.bw30.open.common.model.WftChannel;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.common.model.stat.WftConsumeStat;
import com.bw30.open.common.model.stat.WftOpenPlatFormStat;
import com.bw30.open.wft.common.DateUtil;
import com.bw30.open.wftAdm.service.CardService;
import com.bw30.open.wftAdm.util.MailService;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

/**
 * 从电信账单文件中分渠道统计消耗时长
 * 
 * @author Jack
 * 
 *         2014年10月11日
 */
public class GrapCtccRecordTask {
	private static final Logger LOG = Logger
			.getLogger(GrapCtccRecordTask.class);

	private static final SimpleDateFormat SDF_YMD = new SimpleDateFormat(
			"yyyy-MM-dd");

	private String ftpUrl;
	private String username;
	private String password;

	private String filePath;

	private String billFilePath;

	private List<String> emails;// 收件人

	@Resource
	private CardService cardService;
	@Resource
	private WftChannelMapper wftChannelMapper;
	@Resource
	private WftConsumeStatMapper wftConsumeStatMapper;
	@Resource
	private AccountMapper accountMapper;
	@Resource
	private WftOpenPlatFormStatMapper wftOpenPlatFormStatMapper;
	@Resource
	private MailService mailService;

	/**
	 * 通过程序抓取ftp文件 解析并统计时长
	 */
	/*
	 * public void grapRecord() { LOG.info("grap ctcc record start..."); try{
	 * Date yestoday = DateUtil.getNextNumDay(new Date(), -1); String dateStr =
	 * SDF_YMD.format(yestoday); String fileName = "pool_BWTX_" + dateStr +
	 * ".csv"; //账单文件生成时间不定，需循环判断是否已抓取下来，然后解析并统计 List<CtccRecord> crList = null;
	 * int i = 0; while(i < 30){ i++;
	 * LOG.info(String.format("grap ctcc record-%s: download file:%s", i,
	 * fileName)); if(this.downFile(fileName, this.filePath +
	 * fileName)){//抓取ftp文件成功
	 * LOG.info(String.format("grap ctcc record-%s: download file success:%s",
	 * i, fileName)); crList = this.parseFile(this.filePath + fileName); if(null
	 * == crList){
	 * LOG.warn(String.format("grap ctcc record-%s: parse file error:%s", i,
	 * fileName)); } LOG.info("grap ctcc record: parse file success:card size="
	 * + crList.size()); break; }else{
	 * LOG.warn(String.format("grap ctcc record-%s: download file error:%s", i,
	 * fileName)); } Thread.sleep(30 * 60 * 1000);//sleep 30分钟 }
	 * 
	 * if(null == crList){ LOG.info("grap ctcc record end"); return; }
	 * 
	 * //分渠道统计时长 List<WftChannel> channelList = this.wftChannelMapper.findAll();
	 * if (null != channelList && 0 < channelList.size()) { int ctccValue = 0;
	 * for (WftChannel channel : channelList) { //卡账单文件目录 String filePath =
	 * this.billFilePath + "bill_" + channel.getCode(); filePath +=
	 * ("/BEIWEI_ChinaNet_CARD_" + new
	 * SimpleDateFormat("yyyyMMdd").format(yestoday) + ".txt");
	 * 
	 * WftConsumeStat consume = new WftConsumeStat(); consume.setDairy(dateStr);
	 * consume.setChannel(channel.getCode());
	 * consume.setChannelname(channel.getName());
	 * consume.setOpid(WftOperator.OP_ID_CTCC); if(0 < crList.size()){ ctccValue
	 * = this.statForChannel(channel.getCode(), crList, filePath); }else{
	 * ctccValue = 0; } consume.setTvalue(ctccValue); consume.setIntime(new
	 * Date());
	 * 
	 * LOG.info(String.format("grap ctcc record result: channel=%s, sum=%s s",
	 * channel.getCode(), ctccValue));
	 * this.wftConsumeStatMapper.insert(consume); } }
	 * 
	 * //不存在的卡，可能在测试环境 if(0 < crList.size()){ for(CtccRecord cr : crList){
	 * LOG.warn
	 * (String.format("grap ctcc record result: card not exists: no=%s, time=%s s"
	 * , cr.getNo(), cr.getTime())); } } }catch(Exception e){
	 * LOG.error("grap ctcc record error", e); }
	 * LOG.info("grap ctcc record end"); }
	 */

	/**
	 * 账单文件由脚本程序去ftp抓取，整个文件读入内存，解析和统计
	 */
	public void grapRecord() {
		LOG.info("grap ctcc record start...");
		try {
			Date yestoday = DateUtil.getNextNumDay(new Date(), -1);
			String dateStr = SDF_YMD.format(yestoday);
			String fileName = "pool_BWTX_" + dateStr + ".csv";
			LOG.info("grap ctcc record: fileName="+fileName);
			File file = new File(this.filePath + fileName);

			// 账单文件生成时间不定，需循环判断是否已抓取下来，然后解析并统计
			List<CtccRecord> crList = null;
			int i = 0;
			while (i < 30) {
				i++;
				if (file.exists()) {// 账单文件已存在
					LOG.info("grap ctcc record: fileName=" + fileName
							+ ", fileSize=" + file.length());
					// 解析文件
					crList = this.parseFile(this.filePath + fileName);
					if (null != crList && 0 < crList.size()) {
						LOG.info("grap ctcc record: card size=" + crList.size());
						break;
					}
					LOG.info("grap ctcc record: file not completed");
				} else {
					LOG.info("grap ctcc record: file not exists-" + i);
					if (i == 30) {
						LOG.info("grap ctcc record end");
						return;
					}
				}

				Thread.sleep(30 * 60 * 1000);// sleep 30分钟
			}

			// 分渠道统计时长
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("status", WftChannel.STATUS_ENABLE);
			paramMap.put("isbill", WftChannel.ISBILL_YES);
			List<WftChannel> channelList = this.wftChannelMapper.findByParam(paramMap);
			if (null != channelList && 0 < channelList.size()) {
				int ctccValue = 0;
				WftChannel channel = null;
				for (int j = 0, s = channelList.size(); j < s; j++) {
					channel = channelList.get(j);
					// 卡账单文件目录
					String filePath = this.billFilePath + "bill_"
							+ channel.getCode();
					File billFile = new File(filePath);
					if (!billFile.exists() || !billFile.isDirectory()) {
						LOG.info("grap ctcc record:" + filePath);
						if (file.mkdir()) {
							LOG.info("grap ctcc record: mkdir ok");
						} else {
							LOG.info("grap ctcc record: mkdir fail");
							continue;
						}
					}
					filePath += ("/BEIWEI_ChinaNet_CARD_"
							+ new SimpleDateFormat("yyyyMMdd").format(yestoday) + ".txt");
					if (0 < crList.size()) {
						ctccValue = this.statForChannel(channel,
								crList, filePath);
					} else {
						ctccValue = 0;
					}

					/*
					 * 检查账单是否正确： 只对第一个渠道进行检查， 若时长误差在10%以内则认为正确
					 */
					if (0 == j) {
						if (!this.check(channel.getCode(), ctccValue)) {
							return;
						}
					}

					WftConsumeStat consume = new WftConsumeStat();
					consume.setDairy(dateStr);
					consume.setChannel(channel.getCode());
					consume.setChannelname(channel.getName());
					consume.setOpid(WftOperator.OP_ID_CTCC);
					consume.setTvalue(ctccValue);
					consume.setIntime(new Date());

					LOG.info(String.format(
							"grap ctcc record result: channel=%s, sum=%s s",
							channel.getCode(), ctccValue));
					this.wftConsumeStatMapper.insert(consume);
				}
			}

			// 不存在的卡，可能在测试环境
			if (0 < crList.size()) {
				for (CtccRecord cr : crList) {
					LOG.warn(String
							.format("grap ctcc record result: card not exists: no=%s, time=%s s",
									cr.getNo(), cr.getTime()));
				}
			}

			LOG.info("grap ctcc record end");
		} catch (Exception e) {
			LOG.error("grap ctcc record error", e);
		}
		LOG.info("grap ctcc record end");
	}

	/**
	 * 
	 * @param channel
	 * @param ctccValue
	 * @return
	 */
	private boolean check(String channel, int ctccValue) {
		Date yestoday = DateUtil.getNextNumDay(new Date(), -1);
		WftOpenPlatFormStat stat = this.wftOpenPlatFormStatMapper
				.findByOpidDairyChannel(WftOperator.OP_ID_CTCC,
						new SimpleDateFormat("yyyy-MM-dd").format(yestoday),
						channel);
		if (null == stat) {
			return true;
		}

		Long utvalue = stat.getUtvalueop();
		if (0 == utvalue || 0 == ctccValue) {
			return true;
		}

		Double r = utvalue.intValue() * 1D;
		r /= ctccValue;

		if (0.8D <= r && r <= 1.2D) {// 时长误差小于20%，则认为正确
			return true;
		}

		LOG.info(String.format(
				"grap ctcc record:check for %s fail:bwValue=%s, ctccValue=%s, r = %s",
				channel, utvalue, ctccValue, r));
		StringBuilder sb = new StringBuilder();
		sb.append("<p>你好:</p>").append("<p>电信时长账单异常，请检查确认。</p>").append("<p>")
				.append("账单日期：")
				.append(new SimpleDateFormat("yyyy-MM-dd").format(yestoday))
				.append("</p>").append("<p>").append("合作方：").append(channel)
				.append("</p>").append("<p>").append("北纬统计时长：").append(utvalue)
				.append("</p>").append("<p>").append("电信统计时长：")
				.append(ctccValue).append("</p>");
		this.mailService.sendMail(emails, "系统告警邮件：电信时长账单异常", sb.toString());
		return false;
	}

	/**
	 * 统计渠道电信消耗时长，写卡账单文件
	 * 
	 * @param channel
	 * @param crList
	 * @param filePath
	 * 
	 * @return 消耗总时长，秒
	 */
	public int statForChannel(WftChannel wftChannel, List<CtccRecord> crList,
			String filePath) {
		String channel = wftChannel.getCode();
		Set<String> cards = new HashSet<String>();
		List<WftCardActive> caList = this.cardService.findByOpidAndCstatus(
				channel, WftOperator.OP_ID_CTCC, null);
		if (null != caList && 0 < caList.size()) {
			LOG.info("grap ctcc record: channel=" + channel + ", cardsize="
					+ caList.size());
			for (WftCardActive ca : caList) {
				cards.add(ca.getNo());
			}
		}

		int sum = 0;
		if (0 < crList.size()) {
			Iterator<CtccRecord> it = crList.iterator();
			CtccRecord cr = null;
			PrintWriter pw = null;
			try {
				pw = new PrintWriter(new File(filePath));
				while (it.hasNext()) {
					cr = it.next();
					if (cards.contains(cr.getNo())) {
						sum += cr.getTime();
						pw.println(new StringBuffer(cr.getNo()).append("|")
								.append(cr.getTime()).append("|").toString());
						it.remove();// 渠道不会共用卡
					}
				}
				pw.flush();
				pw.close();
			} catch (Exception e) {
				LOG.error("", e);
			} finally {
				if (null != pw) {
					pw.close();
				}
			}

			/**
			 * 平安不记录使用时长，直接返回
			 * 在开卡动作中直接记录开卡时长
			 * */
			if("10009".equals(channel)||"10010".equals(channel)||wftChannel.getIsbill()==WftChannel.ISBILL_NO){
				return sum;
			}
			
			OpenPlatformAccount account = this.accountMapper
					.getAccountByUserId(Integer.parseInt(channel));
			if (null != account) {
				BigInteger bi = this.statUsetime(channel);
				bi = bi.add(new BigInteger(String.valueOf(sum)));
				account = new OpenPlatformAccount();
				account.setId(Integer.parseInt(channel));
				account.setUsetime(bi.toString());
				this.accountMapper.update(account);
			}
		}

		return sum;
	}

	private BigInteger statUsetime(String channel) {
		List<WftConsumeStat> list = this.wftConsumeStatMapper.findByCondition(
				null, channel, WftOperator.OP_ID_CTCC);
		BigInteger bi = new BigInteger("0");
		if (null != list && 0 < list.size()) {
			for (WftConsumeStat consume : list) {
				bi = bi.add(new BigInteger(String.valueOf(consume.getTvalue())));
			}
		}
		return bi;
	}

	/**
	 * 从文件中解析数据
	 * 
	 * @param filePath
	 * @return
	 */
	// public List<CtccRecord> parseFile(String filePath){
	// int i = 1;
	// String line = null;
	// String no = null;
	// Long time = 0L;
	// BufferedReader br = null;
	// try{
	// br = new BufferedReader(new InputStreamReader(new
	// FileInputStream(filePath), "GBK"));
	// line = br.readLine();//表头
	// List<CtccRecord> crList = new LinkedList<CtccRecord>();
	// while (null != (line = br.readLine())) {//从第二行开始读数据
	// i++;
	// String[] strs = line.split(",");
	// no = strs[0];
	// time = Long.parseLong(strs[1]);
	// crList.add(new CtccRecord(no, time));
	// }
	// br.close();
	// return crList;
	// }catch(Exception e){
	// LOG.error("grap ctcc record error: parse file error:" + i + ":" + line,
	// e);
	// return null;
	// }finally{
	// if(null != br){
	// try{
	// br.close();
	// }catch(Exception e){
	//
	// }
	// }
	// }
	// }

	public List<CtccRecord> parseFile(String filePath) {
		CSVReader csvReader = null;
		try {
			csvReader = new CSVReader(new InputStreamReader(
					new FileInputStream(filePath), "GBK"), ',');
			if (null != csvReader) {
				csvReader.readNext();// 第一行标题
				String[] csvRow = null;// row
				String no = null;
				Long time = 0L;
				List<CtccRecord> crList = new LinkedList<CtccRecord>();
				while ((csvRow = csvReader.readNext()) != null) {
					no = csvRow[0];
					time = Long.parseLong(csvRow[1]);
					// System.out.println(no + ", " + time);
					crList.add(new CtccRecord(no, time));
				}
				csvReader.close();
				return crList;
			}
		} catch (FileNotFoundException fe) {
			LOG.error("grap ctcc record error: file not found", fe);
		} catch (IOException ioe) {
			LOG.error("grap ctcc record error: read file error", ioe);
		} catch (Exception e) {
			LOG.error("grap ctcc record error: parse file error", e);
		} finally {
			if (null != csvReader) {
				try {
					csvReader.close();
				} catch (Exception ioe) {

				}

			}
		}
		return null;
	}

	/**
	 * ftp下载账单文件
	 * 
	 * @param fileName
	 * @param saveFile
	 * @return
	 */
	public boolean downFile(String fileName, String saveFile) {
		FTPClient ftpClient = new FTPClient();
		FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
		ftpClient.configure(conf);
		String remoteDir = "/";
		try {
			ftpClient.connect(this.ftpUrl, 21);
			ftpClient.setControlEncoding("GBK");
			ftpClient.login(this.username, this.password);
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

			// ftpClient.enterLocalPassiveMode();//被动模式
			ftpClient.enterLocalActiveMode();// 主动模式
			FTPFile[] files = ftpClient.listFiles(remoteDir);
			String name = null;
			if (null != files) {
				for (FTPFile file : files) {
					name = file.getName();
					// LOG.info("ftp files:" + name);
					if (name.equals(fileName)) {
						FileOutputStream fos = new FileOutputStream(new File(
								saveFile));
						ftpClient.retrieveFile(remoteDir + "/" + fileName, fos);
						fos.close();
						return true;
					}
				}
			}
			return false;
		} catch (Exception e) {
			LOG.error("download file from ctcc ftp error: " + fileName, e);
			return false;
		}

	}

	public static class CtccRecord {
		String no;
		Long time;

		public CtccRecord(String no, Long time) {
			this.no = no;
			this.time = time;
		}

		public String getNo() {
			return no;
		}

		public void setNo(String no) {
			this.no = no;
		}

		public Long getTime() {
			return time;
		}

		public void setTime(Long time) {
			this.time = time;
		}

	}

	public void setCardService(CardService cardService) {
		this.cardService = cardService;
	}

	public void setWftChannelMapper(WftChannelMapper wftChannelMapper) {
		this.wftChannelMapper = wftChannelMapper;
	}

	public void setWftConsumeStatMapper(
			WftConsumeStatMapper wftConsumeStatMapper) {
		this.wftConsumeStatMapper = wftConsumeStatMapper;
	}

	public void setAccountMapper(AccountMapper accountMapper) {
		this.accountMapper = accountMapper;
	}

	public void setWftOpenPlatFormStatMapper(
			WftOpenPlatFormStatMapper wftOpenPlatFormStatMapper) {
		this.wftOpenPlatFormStatMapper = wftOpenPlatFormStatMapper;
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

	public void setFtpUrl(String ftpUrl) {
		this.ftpUrl = ftpUrl;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public void setBillFilePath(String billFilePath) {
		this.billFilePath = billFilePath;
	}

	public void setEmails(List<String> emails) {
		this.emails = emails;
	}

	public static void main(String[] args)  {
	//	GrapCtccRecordTask gcrt = new GrapCtccRecordTask();
//		gcrt.setFtpUrl("180.166.7.148");
//		gcrt.setFtpUrl("222.66.199.180");
//		gcrt.setUsername("bwtx");
//		gcrt.setPassword("bwtx2014");
//		gcrt.setFilePath("e://ctccdata");
//		gcrt.downFile("pool_BWTX_2015-04-21.csv",
//				"e://ctccdata/pool_BWTX_2015-04-21.csv");

		// gcrt.parseFile("d://data/pool_BWTX_2015-01-06.csv");

		// formatCSV("d://data/pool_BWTX_2015-01-08.csv",
		// "d://pool_BWTX_2015-01-08.csv");
		System.out.println(11111);
		Date yestoday = DateUtil.getNextNumDay(new Date(), -1);
		String dateStr = SDF_YMD.format(yestoday);
		String fileName = "pool_BWTX_" + dateStr + ".csv";
		System.out.println(fileName);
	}

	public static void formatCSV(String srcPath, String dstPath) {
		CSVReader csvReader = null;
		CSVWriter csvWriter = null;
		try {
			csvReader = new CSVReader(new InputStreamReader(
					new FileInputStream(srcPath), "GBK"), ',');
			csvWriter = new CSVWriter(new OutputStreamWriter(
					new FileOutputStream(dstPath), "GBK"), ',');
			if (null != csvReader) {
				String[] csvRow = null;
				int i = 0;
				while ((csvRow = csvReader.readNext()) != null) {
					i++;
					csvWriter.writeNext(csvRow);
				}
				csvReader.close();
				csvWriter.close();
				System.out.println(i);
			}
		} catch (Exception e) {
			LOG.error("grap ctcc record error: parse file error", e);
		}
	}

}
