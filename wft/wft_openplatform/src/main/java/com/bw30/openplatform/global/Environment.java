package com.bw30.openplatform.global;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class Environment {

	private static final String GLOBAL_FILE = "/global.xml";
	private static final String GLOBAL_ROOT_NODE = "global_data";
	private static final String GLOBAL_SEC_NODE = "global";
	private static final String GLOBAL_FINAL_NODE = "param";
	public static Configuration configuration;
	private static Map<Integer, String> messageHandlerConfigMap;

	public static Configuration initConfiguration() {
		configuration = new Configuration(GLOBAL_FILE, "//" + GLOBAL_ROOT_NODE
				+ "/" + GLOBAL_SEC_NODE + "/" + GLOBAL_FINAL_NODE);
		return configuration;
	}

	public static Properties getGlobalProperties() {
		if (configuration == null) {
			configuration = initConfiguration();
		}
		Properties properties = new Properties();
		properties.putAll(configuration.getMap());
		return properties;
	}

	public static Map<String, String> getGlobalMap() {
		if (configuration == null) {
			configuration = initConfiguration();
		}
		return configuration.getMap();
	}

	@SuppressWarnings("unchecked")
	private static Map<Integer, String> initMessageHandlerConfigMap() {
		try {
			InputStream in = ConfigHelper.getConfigStream("messageHandler.xml");
			Document document = null;
			SAXReader reader = new SAXReader();
			document = reader.read(in);
			List<Element> handlerList = (List<Element>) document
					.getRootElement().selectNodes("handler");
			String mid = null;
			String handlerName = null;
			for (Element handler : handlerList) {
				mid = handler.attributeValue("mid");
				handlerName = handler.elementText("handler-name");
				messageHandlerConfigMap.put(Integer.valueOf(mid), handlerName);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return messageHandlerConfigMap;
	}

	public static Map<Integer, String> getMessageHandlerConfigMap() {
		if (messageHandlerConfigMap == null) {
			messageHandlerConfigMap = new HashMap<Integer, String>();
			messageHandlerConfigMap = initMessageHandlerConfigMap();
		}
		return messageHandlerConfigMap;
	}
}
