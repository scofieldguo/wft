package com.bw30.open.wft.common.cardpool;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密
 * 
 * @author Jack
 * @date 2014年7月15日 下午2:41:42
 */
public class AesEncrypter {
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
				.println("=====================init for AES encrypter============");
		Key k = new SecretKeySpec(this.key.getBytes(), ALGORITHM);
		this.cipher = Cipher.getInstance(TRANSFORMATION);
		IvParameterSpec ivSpec = new IvParameterSpec(IV);
		this.cipher.init(Cipher.ENCRYPT_MODE, k, ivSpec);
		System.out.println("init for AES enctypter success");
		System.out
				.println("=======================================================");
	}

	/**
	 * 加密
	 * 
	 * @param data
	 *            明文
	 * @return 密文
	 * @throws Exception
	 */
	public byte[] encrypt(byte[] data) throws Exception {
		return this.cipher.doFinal(data);
	}
	
	/**
	 * 加密
	 * 
	 * @param data 明文
	 * @param key 加密key
	 * @return
	 * @throws Exception
	 */
	public byte[] encrypt(byte[] data, String key) throws Exception{
		Key k = new SecretKeySpec(key.getBytes(), ALGORITHM);
		Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		IvParameterSpec ivSpec = new IvParameterSpec(IV);
		cipher.init(Cipher.ENCRYPT_MODE, k, ivSpec);
		return cipher.doFinal(data);
	}

	// 测试
	public static final void main(String[] args) throws Exception {
		AesEncrypter ae = new AesEncrypter();
		String key = "测试key";
		ae.setKey(key);
		ae.init();
		String data = "测试数据";
		System.out.println("data:" + data);
		data = new String(ae.encrypt(data.getBytes("utf-8")), "utf-8");
		System.out.println("encrypted data:" + data);
	}

}
