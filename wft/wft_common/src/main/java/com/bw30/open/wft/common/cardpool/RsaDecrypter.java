package com.bw30.open.wft.common.cardpool;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;

/**
 * RSA 解密
 * 
 * @author Jack
 * @date 2014年7月15日 上午11:06:34
 */
public class RsaDecrypter {
	private static final String ALGORITHM = "RSA";
	private static final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";
	
	private String privateKeyPath;
	
	private int privateKeyModulue = 0;//私钥模长
	private Cipher cipher;
	
	public void setPrivateKeyPath(String privateKeyPath) {
		this.privateKeyPath = privateKeyPath;
	}
	
	// 初始化解密私钥
	public void initPrivateKey() throws Exception {
		System.out
				.println("=====================init private key for RSA decrypter===========");
		InputStream in = new FileInputStream(this.privateKeyPath);
		byte[] bytes = new byte[1024];
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int length = in.read(bytes);
		while (length != -1) {
			out.write(bytes, 0, length);
			length = in.read(bytes);
		}

		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(out.toByteArray());
		in.close();
		out.close();
		KeyFactory kf = KeyFactory.getInstance(ALGORITHM);
		RSAPrivateKey privateKey = (RSAPrivateKey) kf.generatePrivate(keySpec);
		this.privateKeyModulue = privateKey.getModulus().bitLength()>>3;
//		System.out.println(this.privateKeyModulue);
		this.cipher = Cipher.getInstance(TRANSFORMATION);  
        this.cipher.init(Cipher.DECRYPT_MODE, privateKey); 
		System.out.println("init private key for Rsa decrypter success");
		System.out
				.println("=================================================================");
	}
	
	/**
	 * 解密
	 * 
	 * @param data 密文
	 * @return 明文
	 * @throws Exception
	 */
	public byte[] decrypt(byte[] data) throws Exception{
		if(null == data || 0 == data.length){
			return null;
		}
		
		byte[][] arrays = this.splitForDecrypte(data, this.privateKeyModulue);
        ByteArrayOutputStream ba=new ByteArrayOutputStream();
        for(byte[] arr : arrays){  
            ba.write(this.cipher.doFinal(arr));  
        }
        byte[] result = ba.toByteArray();
        ba.close();
        return result;
	}
	
	/**
	 * 分组解密，密文长度大于私钥模长则要分组
	 * @param data
	 * @param size
	 * @return
	 * @throws Exception
	 */
	private byte[][] splitForDecrypte(byte[] data, int size) throws Exception{
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
