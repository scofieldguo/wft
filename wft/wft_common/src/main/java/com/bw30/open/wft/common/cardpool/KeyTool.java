package com.bw30.open.wft.common.cardpool;

import java.io.File;
import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Random;

/**
 * 生成rsa、aes的key
 * 
 * @author Jack
 * @date 2014年7月18日 下午4:47:38
 */
public class KeyTool {
	public static final void main(String[] args) throws Exception {
//		genRsaKey();
		String aesKey = genAesKey(128);
		System.out.println(aesKey);
	}

	/**
	 * 生成rsa公钥、私钥，并写入pubkey.key、privatekey.key<br/>
	 * 
	 * @param len 密钥位数：512， 1024， 2048
	 * 
	 */
	public static void genRsaKey(int len) throws Exception {
		KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
		SecureRandom random = new SecureRandom();
		// 初始加密，512位已被破解，用1024位,最好用2048位
		keygen.initialize(len, random);
		// 取得密钥对
		KeyPair kp = keygen.generateKeyPair();

		RSAPrivateKey privateKey = (RSAPrivateKey) kp.getPrivate();
		byte[] data = privateKey.getEncoded();
		String fileName = null;
		if (null != data) {
			System.out.println("gen privateKey for rsa success");
			fileName = "privateKey.key";
			fileName = outToFile(data, fileName);
			System.out.println("write privateKey to file[" + fileName
					+ "] success");
		} else {
			System.err.println("gen privateKey for rsa fail");
			return;
		}

		RSAPublicKey publicKey = (RSAPublicKey) kp.getPublic();
		data = publicKey.getEncoded();
		if (null != data) {
			System.out.println("gen publicKey for rsa success");
			fileName = "publicKey.key";
			fileName = outToFile(data, fileName);
			System.out.println("write publicKey to file[" + fileName
					+ "] success");
		} else {
			System.err.println("gen publicKey for rsa fail");
			return;
		}

	}

	// 写入文件
	private static String outToFile(byte[] data, String fileName)
			throws Exception {
		File file = new File("./" + fileName);
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(data);
		fos.flush();
		fos.close();
		return file.getAbsolutePath();
	}

	private static final char[] CHARACTERS = new char[] { 'a', 'b', 'c', 'd',
			'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
			'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D',
			'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
			'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3',
			'4', '5', '6', '7', '8', '9' };

	/**
	 * 生成aes key
	 * 
	 * @param len 位数：128, 192， 256
	 * @return
	 */
	public static String genAesKey(int len) throws Exception{
		len = len >> 3;
		char[] key = new char[len];
		Random random = new Random();
		for(int i = 0; i < len; i++){
			key[i] = CHARACTERS[random.nextInt(CHARACTERS.length)];
		}
		return new String(key);
		
//		 KeyGenerator keyGen = KeyGenerator.getInstance("AES");  
//		 keyGen.init(len, new SecureRandom());  
//		 SecretKey key = keyGen.generateKey();
//		 BASE64Encoder base64Encoder = new BASE64Encoder();
//		 return base64Encoder.encodeBuffer(key.getEncoded());
	}

}
