package org.redcenter.scantool;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.redcenter.excel.CsvReader;
import org.redcenter.excel.CsvWriter;
import org.redcenter.scantool.apserver.ServerInfo;
import org.redcenter.scantool.apserver.ServerScan;
import org.redcenter.scantool.apserver.ServerScanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.bean.StatefulBeanToCsvBuilder;

public class Main {
	private static Logger LOGGER = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws Exception {
		// if (args == null || args.length == 0) {
		// ServerInfo info = new ServerInfo();
		// info.setHost("localhost");
		// info.setPort(8080);
		// info.setAccount("admin");
		// info.setPassword("adm2in");
		//
		// ServerScan scan =
		// ServerScanFactory.getServerScan(ServerScanFactory.AP_SERVER_TOMCAT7);
		// scan.scan(info);
		// System.out.println();
		// }

		try {
			// read config
			ServerConfigManager configManager = new ServerConfigManager();
			Map<String, List<Integer>> portMapping = configManager.getMapping();
			AuthManager authManager = new AuthManager();
			Map<String, String> authMapping = authManager.getMapping();

			// read input
			File file = new File("input.csv");
			List<ServerInfo> records = new ArrayList<>();
			new CsvReader<ServerInfo>(file).read(records, ServerInfo.class);

			// scan
			List<ServerInfo> resultRecords = new ArrayList<>();
			for (ServerInfo serverInfo : records) {

				// by server type
				String[] serverTypes = ServerScanFactory.getApServers();
				for (String serverType : serverTypes) {
					serverInfo.setType(serverType);;
					LOGGER.info("Scan host " + serverInfo.getHost() + " for AP server " + serverInfo.getType());
					ServerScan serverScan = ServerScanFactory.getServerScan(serverType);

					// by port
					List<Integer> ports = portMapping.get(serverType);
					for (Integer port : ports) {
						serverInfo.setPort(port);
						for (Entry<String, String> entry : authMapping.entrySet()) {
							serverInfo.setAccount(entry.getKey());
							serverInfo.setPassword(entry.getValue());
							serverScan.scan(serverInfo);
							if (serverInfo.isResult()) {
								resultRecords.add(new ServerInfo(serverInfo));
							}
						}
					}
				}
			}

			// TODO
			File outputFile = new File("output.csv");
			CsvWriter<ServerInfo> w = new CsvWriter<ServerInfo>(outputFile);
			w.write(resultRecords, true);
			w.close();
			
//			Writer writer = new FileWriter("output.csv");
//			StatefulBeanToCsvBuilder<ServerInfo> beanToCsv = StatefulBeanToCsvBuilder(writer).build();
//			beanToCsv.write(records);
//			writer.close();

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		LOGGER.info("End");
	}

}
