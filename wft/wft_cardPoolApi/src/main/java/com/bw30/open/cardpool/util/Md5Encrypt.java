package com.bw30.open.cardpool.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

public class Md5Encrypt {
	
	private static final Logger logger = Logger.getLogger(Md5Encrypt.class);
	/** 用来将字节转换成 16 进制表示的字符 */
	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6',
		'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	
	/**
	 * MD5加密
	 * @param text
	 * @return
	 */
	public static String md5Enc (String text, String charset) {
		MessageDigest msgDigest = null;
	    try {
	    	msgDigest  = MessageDigest.getInstance("MD5");
	    } catch (NoSuchAlgorithmException e) {
	    	logger.error("NoSuchAlgorithmException@md5Enc", e);
	    }
	    try {
	    	 //TODO 如果接口编码格式有变，此处应指定
			msgDigest.update(text.getBytes(charset));
 
		} catch (UnsupportedEncodingException e) {
			logger.error("UnsupportedEncodingException@md5Enc", e);
		}
	        byte[] tmp = msgDigest.digest();
	        // MD5 的计算结果是一个 128 位的长整数，
	        int l = tmp.length;
			char[] out = new char[l << 1];
			// two characters form the hex value.
			for (int i = 0, j = 0; i < l; i++) {
				out[j++] = DIGITS[(0xF0 & tmp[i]) >>> 4];
				out[j++] = DIGITS[0x0F & tmp[i]];
			}
	    return new String(out);
	}
	
}
