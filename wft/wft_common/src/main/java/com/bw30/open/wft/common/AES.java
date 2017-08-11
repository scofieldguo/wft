package com.bw30.open.wft.common;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {
	private static final byte[] biv = { 15, -12, 19, 82, 45, 69, 86, 12, 74,122, -33, 45, 88, 18, 111, 32 };

	public static byte[] decrypt(byte[] content, byte[] key) {
		try {
			Key k = new SecretKeySpec(key, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//Cipher.getInstance("AES/CBC/PKCS5Padding","BC");
			IvParameterSpec iv = new IvParameterSpec(biv);
			cipher.init(Cipher.DECRYPT_MODE, k, iv);
			return cipher.doFinal(content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public static byte[] encrypt(byte[] content, byte[] key) {
		try {
			Key k = new SecretKeySpec(key, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//Cipher.getInstance("AES/CBC/PKCS5Padding","BC");
			IvParameterSpec iv = new IvParameterSpec(biv);
			cipher.init(Cipher.ENCRYPT_MODE, k, iv);
			return cipher.doFinal(content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
