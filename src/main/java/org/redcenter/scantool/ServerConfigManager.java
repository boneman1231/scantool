package org.redcenter.scantool;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerConfigManager {
	private static final String PATH = "./config/apserver.properties";
	private Map<String, List<Integer>> mapping = new HashMap<>();
	private Logger logger = LoggerFactory.getLogger(getClass());

	public ServerConfigManager() {
		Properties prop = new Properties();
		try {
			FileInputStream inputStream = new FileInputStream(PATH);
			prop.load(inputStream);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

		for (final Entry<Object, Object> entry : prop.entrySet()) {
			String value = (String) entry.getValue();
			if (value != null && !value.isEmpty()) {
				List<Integer> portList = new ArrayList<>();
				String[] portStrings = value.split(",");
				for (String portString : portStrings) {
					try {
						portList.add(Integer.parseInt(portString));
					} catch (NumberFormatException e) {
						logger.error(e.getMessage(), e);
					}
				}
				mapping.put((String) entry.getKey(), portList);
			}
		}
	}

	public Map<String, List<Integer>> getMapping() {
		return mapping;
	}

}
