package com.bw30.open.wft.common.cardpool;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;

import org.apache.log4j.Logger;

/**
 * RSA加密
 * 
 * @author Jack
 * @date 2014年7月15日 上午11:06:53
 */
public class RsaEncrypter {
	private static final Logger LOG = Logger.getLogger(RsaEncrypter.class);
	private static final String ALGORITHM = "RSA";
	private static final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";
	
	private String publicKeyPath;
	
	private int publicKeyModulue = 0;
	private Cipher cipher;
	
	public void setPublicKeyPath(String publicKeyPath) {
		this.publicKeyPath = publicKeyPath;
	}
	
	//初始化加密公钥
	public void initPublicKey() throws Exception {
		System.out
				.println("=====================init public key for RSA encrypter================");

		InputStream in = new FileInputStream(this.publicKeyPath);
		byte[] bytes = new byte[1024];
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int length = in.read(bytes);
		while (length != -1) {
			out.write(bytes, 0, length);
			length = in.read(bytes);
		}

		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(out.toByteArray());
		in.close();
		out.close();
		KeyFactory ky = KeyFactory.getInstance(ALGORITHM);
		RSAPublicKey publicKey = (RSAPublicKey) ky.generatePublic(keySpec);
		this.publicKeyModulue = publicKey.getModulus().bitLength() >> 3;
		this.cipher = Cipher.getInstance(TRANSFORMATION);
		this.cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		System.out.println("init public key success");
		System.out
				.println("==================================================================");
	}
	
	/**
	 * 公钥加密
	 * 
	 * @param data 明文,UTF-8编码
	 * @return 密文
	 * @throws Exception
	 */
	public synchronized byte[] encrypt(byte[] data) throws Exception {
		//加密数据长度 <= 模长-11，否则要分组加密
		LOG.debug("rsa encrypt data: size = " + data.length);
		byte[][] datas = this.splitForEncrypt(data, this.publicKeyModulue - 11);
		LOG.debug("split for encrypt: array size = " + datas.length);
		ByteArrayOutputStream ba=new ByteArrayOutputStream();
		for (byte[] s : datas) {
			 ba.write(this.cipher.doFinal(s));
		}
		return ba.toByteArray();
	}
	
	/**
	 * 分组加密，加密内容大于117字节时，需要先分组再加密
	 * 
	 * @param data
	 * @param size
	 * @return
	 */
	private byte[][] splitForEncrypt(byte[] data, int size){
		int x = data.length / size;
		int y = data.length % size;
		x = (y == 0 ? x : x+1);
		byte[][] result = new byte[x][];
		int i;
		for(i = 0; i<x-1; i++){
			result[i] = Arrays.copyOfRange(data, i*size, (i+1)*size);
		}
		result[x-1]  = Arrays.copyOfRange(data, i*size, data.length);
		return result;
	}
	
}
