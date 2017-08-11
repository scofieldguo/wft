package com.bw30.open.wft.common.ipparser;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class MatchUtil {
	/**
	 * 根据匹配规则，解析字符串
	 * 
	 * @param regex
	 * @param input
	 * @return
	 */
	public static Matcher match(String regex, String input) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		return matcher;
	}

	public static String Decrypt(String key, String str)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		SecretKeySpec sS = new SecretKeySpec(key.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, sS);
		byte[] b = Base64.decode(str, Base64.DEFAULT);
		byte[] decrypted = cipher.doFinal(b);
		String decryptingCode = new String(decrypted);

		return decryptingCode;
	}
	
	public static String reColor(String color){
		Pattern pattern = Pattern.compile("#(.*)");
		Matcher matcher = pattern.matcher(color);
		if(matcher.find()){
			 color = "0xFF" + matcher.group(1); 
		}
		return color;
	}
}
