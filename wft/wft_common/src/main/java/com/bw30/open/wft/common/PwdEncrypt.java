package com.bw30.open.wft.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 密码加、解密<br/>
 * 
 * @version 2013-12-26 上午11:49:16
 * 
 */
public class PwdEncrypt {

	private static final String CHARSET = "UTF-8";

	// 热点ap密码加密当前版本
	private static final String AP_PWD_KEY_VERSION_CUR = "a";
	// 卡密码加密当前版本
	private static final String CARD_PWD_KEY_VERSION_CUR = "a";

	private static Map<String, String> apPwdKey = new HashMap<String, String>();
	private static Map<String, String> cardPwdKey = new HashMap<String, String>();
	private static final String cardPwdKey2;

	static {
		// 初始化key
		initApPwdKey();
		initCardPwdKey();
		cardPwdKey2 = "vkru2my4rb140101";
	}

	private static void initApPwdKey() {
		apPwdKey.put("a", "vkru2my4rb140101");
	}

	private static void initCardPwdKey() {
		cardPwdKey.put("a", "bwWifiTong140101");
	}

	/**
	 * 热点ap密码加密
	 * 
	 * @param pwd 明文
	 * @return 密文
	 */
	public static String encryptApPwd(String pwd) {
		String key = getApPwdKey(AP_PWD_KEY_VERSION_CUR);
		if (null == key || null == pwd) {
			return null;
		}
		try {
			byte[] result = AES.encrypt(pwd.getBytes(CHARSET),
					key.getBytes(CHARSET));
			String enPwd = Byte_hex.bytes2HexString(result);
			enPwd = AP_PWD_KEY_VERSION_CUR + enPwd;
			return enPwd;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 热点ap密码解密
	 * 
	 * @param pwd 密文
	 * @return 明文
	 */
	public static String decryptApPwd(String pwd) {
		if (null == pwd)
			return null;

		try {
			String v = pwd.substring(0, 1);
			String enpwd = pwd.substring(1);
			String key = getApPwdKey(v);
			if (null != key) {
				byte[] result = AES.decrypt(Byte_hex.hexString2Bytes(enpwd),
						key.getBytes(CHARSET));
				return new String(result, CHARSET);
			}
			return pwd;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 卡密码加密
	 * 
	 * @param pwd 明文
	 * @return 密文
	 */
	public static String encryptCardPwd(String pwd){
		String key = getCardPwdKey(CARD_PWD_KEY_VERSION_CUR);
		if (null == key || null == pwd) {
			return null;
		}
		try {
			byte[] result = AES.encrypt(pwd.getBytes(CHARSET),
					key.getBytes(CHARSET));
			String enPwd = Byte_hex.bytes2HexString(result);
			enPwd = CARD_PWD_KEY_VERSION_CUR + enPwd;
			return enPwd;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 卡密码解密
	 * 
	 * @param pwd 密文
	 * @return 明文
	 */
	public static String decryptCardPwd(String pwd) {
		if (null == pwd) {
			return null;
		}

		try {
			String v = pwd.substring(0, 1);
			String enpwd = pwd.substring(1);
			String key = getCardPwdKey(v);
			if(null != key){
				byte[] result = AES.decrypt(Byte_hex.hexString2Bytes(enpwd),
						key.getBytes());
				return new String(result, CHARSET);
			}
			return pwd;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String encryptCardPwd2(String pwd) {
		try {
			byte[] result = AES.encrypt(pwd.getBytes(CHARSET),
					cardPwdKey2.getBytes(CHARSET));
			String enPwd = Byte_hex.bytes2HexString(result);
			return enPwd;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static String decryptCardPwd2(String pwd) {
		try {
			byte[] result = AES.decrypt(Byte_hex.hexString2Bytes(pwd),
					cardPwdKey2.getBytes(CHARSET));
			return new String(result, CHARSET);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据加密版本取密钥
	 * 
	 * @param v
	 *            版本
	 * @return
	 */
	private static String getApPwdKey(String v) {
		return apPwdKey.get(v);
	}

	/**
	 * 根据加密版本取密钥
	 * 
	 * @param v
	 *            版本
	 * @return
	 */
	private static String getCardPwdKey(String v) {
		return cardPwdKey.get(v);
	}

	/**
	 * 内部类：16进制转换
	 * 
	 * @author zhouwei@bw30.com
	 * @version 2013-12-31 下午04:43:13
	 * 
	 */
	private static class Byte_hex {
		private final static byte[] hex = "0123456789ABCDEF".getBytes();

		private static int parse(char c) {
			if (c >= 'a')
				return (c - 'a' + 10) & 0x0f;
			if (c >= 'A')
				return (c - 'A' + 10) & 0x0f;
			return (c - '0') & 0x0f;
		}

		// 从字节数组到十六进制字符串转换
		public static String bytes2HexString(byte[] b) {
			byte[] buff = new byte[2 * b.length];
			for (int i = 0; i < b.length; i++) {
				buff[2 * i] = hex[(b[i] >> 4) & 0x0f];
				buff[2 * i + 1] = hex[b[i] & 0x0f];
			}
			return new String(buff);
		}

		// 从十六进制字符串到字节数组转换
		public static byte[] hexString2Bytes(String hexstr) {
			byte[] b = new byte[hexstr.length() / 2];
			int j = 0;
			for (int i = 0; i < b.length; i++) {
				char c0 = hexstr.charAt(j++);
				char c1 = hexstr.charAt(j++);
				b[i] = (byte) ((parse(c0) << 4) | parse(c1));
			}
			return b;
		}
	}

	public static void main(String[] args) throws Exception {
		String pwd = "54083118";// 39340144
		System.out.println("加密：");
		System.out.println(String.format("明文：%s", pwd));
		System.out.println(String.format("密文：%s", encryptCardPwd(pwd)));

		System.out.println();
		System.out.println("解密：");
		String enpwd = encryptCardPwd(pwd);
		System.out.println(String.format("密文：%s", enpwd));
		System.out.println(String.format("明文：%s", decryptCardPwd(enpwd)));
	}

}
