package com.bw30.open.wft.common.cardpool;

import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * DES解密
 * 
 * @author Jack
 * @date 2014年7月15日 上午11:34:18
 */
public class DesDecrypter {
	private static final String ALGORITHM_DESEDE = "DESede/CBC/PKCS5Padding";

	private String key;
	private byte[] iv = { 1, 2, 3, 4, 5, 6, 7, 8 };
	private Cipher cipher = null;

	public void setKey(String key) {
		this.key = key;
	}

	// 初始化des解密
	public void init() throws Exception {
		System.out
				.println("=====================init for DES decrypter==========");
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		Security.addProvider(new BouncyCastleProvider());
		SecretKey desKey = new SecretKeySpec(this.key.getBytes(),
				ALGORITHM_DESEDE);
		this.cipher = Cipher.getInstance(ALGORITHM_DESEDE);
		this.cipher.init(Cipher.DECRYPT_MODE, desKey, ivSpec);
		System.out.println("init des decrypter success");
		System.out
				.println("====================================================");
	}

	/**
	 * des解密
	 * 
	 * @param 密文
	 * @return 明文
	 * @throws Exception
	 */
	public synchronized byte[] decrypt(byte[] data) throws Exception {
		if (null == data || 0 == data.length) {
			return null;
		}
		return this.cipher.doFinal(data);
	}

	// 测试
	public static final void main(String[] args) throws Exception {
		DesDecrypter dd = new DesDecrypter();
		String key = "测试key";
		dd.setKey(key);
		dd.init();
		String data = "测试密文";
		System.out.println("encrypted data: " + data);
		data = new String(dd.decrypt(data.getBytes("utf-8")), "utf-8");
		System.out.println("decrypted data: " + data);
	}

}
