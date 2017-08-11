package com.bw30.open.wft.common;

import java.util.Random;

public final class IdUtils {
	public static String randomAlphanumericString(int length) {
		return randomString(length, 36);
	}
	
	static String randomString(int length, boolean digitalOnly) {
		return randomString(length, digitalOnly?10:16);
	}
	
	static String randomString(int length, int base) {
		StringBuffer s = new StringBuffer();
		
		Random rand = new Random(/*System.currentTimeMillis()*/);
		
		for (int i=0; i<length; i++)
		{
			int ci = rand.nextInt(base);
			
			if (ci <= 9) {
				// digital
				s.append((char)('0'+ci));
			} else {
				// alphabet
				s.append((char)('a'+(ci-10)));
			}
		}
		
		return s.toString();
	}
	
}

