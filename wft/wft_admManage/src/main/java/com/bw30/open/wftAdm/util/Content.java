package com.bw30.open.wftAdm.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;

public class Content {

	// private static Properties configProperties;
	// private static String mail;

	// static {
	// try {
	// URL mails=Content.class.getClassLoader().getResource("mail.properties");
	// mail=mails.getFile();
	// mail="F:\\test.properties";
	// BufferedReader reader = new BufferedReader(new FileReader(new
	// File(mail)));
	// configProperties = new Properties();
	// configProperties.load(reader);
	// reader.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// System.exit(1);
	// }
	// }

	public static void writeConfig(String name, String value) {
		// BufferedWriter bw;
		try {
			// bw = new BufferedWriter(new FileWriter(new File(mail)));
			URL mails = Content.class.getClassLoader().getResource(
					"mail.properties");
			String mail = mails.getFile();
			Properties configProperties = new Properties();
			BufferedReader reader = new BufferedReader(new FileReader(new File(
					mail)));
			configProperties.load(reader);
			OutputStream fos = new FileOutputStream(new File(mail));
			configProperties.setProperty(name, value);
			configProperties.store(fos, "Update '" + name + "'" + value);
			fos.close();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}

	public static String readConfig(String name) {
		String send_mail = "";
		try {
			URL mails = Content.class.getClassLoader().getResource(
					"mail.properties");
			String mail = mails.getFile();
			Properties configProperties = new Properties();
			BufferedReader reader = new BufferedReader(new FileReader(new File(
					mail)));
			configProperties.load(reader);
			if (configProperties.get(name) != null) {
				send_mail = (String) configProperties.get(name);
			}
			reader.close();
			return send_mail;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return send_mail;

	}
}
