package org.redcenter.scantool;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthManager {
	private static final String PATH = "./config/auth.properties";
	private Map<String, String> mapping = new HashMap<>();
	private Logger logger = LoggerFactory.getLogger(getClass());

	public AuthManager() {
		Properties prop = new Properties();
		try {
			FileInputStream inputStream = new FileInputStream(PATH);
			prop.load(inputStream);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		
		for (final Entry<Object, Object> entry : prop.entrySet()) {
			String pwd = decrypt((String) entry.getValue());
			mapping.put((String) entry.getKey(), pwd);
		}
	}
	
	public Map<String, String> getMapping() {
		return mapping;
	}

	private String decrypt(String value) {
		// TODO
		return value;
	}

	//TODO remove
	public static void main(String[] args) {
		AuthManager manager = new AuthManager();
		Map<String, String> map = manager.getMapping();
		for (Entry<String, String> entry : map.entrySet()) {
			System.out.println(entry.getKey() + "=" + entry.getValue());
		}
	}
}
