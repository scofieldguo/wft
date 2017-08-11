package com.bw30.open.wft.common.cardpool;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES解密
 * 
 * @author Jack
 * @date 2014年7月15日 下午2:42:30
 */
public class AesDecrypter {
	private static final String ALGORITHM = "AES";
	private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";

	private String key;
	private static final byte[] IV = { 15, -12, 19, 82, 45, 69, 86, 12, 74,
			122, -33, 45, 88, 18, 111, 32 };
	private Cipher cipher;

	public void setKey(String key) {
		this.key = key;
	}

	public void init() throws Exception {
		System.out
				.println("=====================init for AES decrypter============");
		Key k = new SecretKeySpec(this.key.getBytes(), ALGORITHM);
		this.cipher = Cipher.getInstance(TRANSFORMATION);
		IvParameterSpec ivSpec = new IvParameterSpec(IV);
		this.cipher.init(Cipher.DECRYPT_MODE, k, ivSpec);
		System.out.println("init for AES decrypter success");
		System.out
				.println("=======================================================");
	}

	/**
	 * 解密
	 * 
	 * @param data
	 *            密文
	 * @return 明文
	 * @throws Exception
	 */
	public byte[] decrypt(byte[] data) throws Exception {
		return this.cipher.doFinal(data);
	}
	
	/**
	 * 解密
	 * 
	 * @param data 密文
	 * @param key 密钥
	 * @return
	 * @throws Exception
	 */
	public byte[] decrypt(byte[] data, String key) throws Exception{
		Key k = new SecretKeySpec(key.getBytes(), ALGORITHM);
		Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		IvParameterSpec ivSpec = new IvParameterSpec(IV);
		cipher.init(Cipher.DECRYPT_MODE, k, ivSpec);
		return cipher.doFinal(data);
	}

	// 测试
	public static final void main(String[] args) throws Exception {
		AesDecrypter ad = new AesDecrypter();
		String key = "测试key";
		ad.setKey(key);
		ad.init();
		String data = "测试数据";
		System.out.println("data:" + data);
		data = new String(ad.decrypt(data.getBytes()), "utf-8");
		System.out.println("decrypted data:" + data);
	}

}
