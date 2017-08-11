package com.bw30.openplatform.util;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class EncrityUtil {
	public static String encrity(String s){
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte[] bys = digest.digest(s.getBytes());
			//String md = new String(bys);
			return enbyBase64(bys);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static String enbyBase64(byte[] bys){
		BASE64Encoder encode = new BASE64Encoder();
		return encode.encode(bys);
	}
	
	public static byte[] dcbyBase64(String s) throws IOException{
		BASE64Decoder deccode = new BASE64Decoder();
		return deccode.decodeBuffer(s);
	}
	
	public static void main(String[] args){
		System.out.println(encrity("1ree"));
	}
}
