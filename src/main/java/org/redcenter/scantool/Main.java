package org.redcenter.scantool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.redcenter.excel.CsvReader;
import org.redcenter.excel.CsvWriter;
import org.redcenter.scantool.apserver.ServerInfo;
import org.redcenter.scantool.apserver.ServerScan;
import org.redcenter.scantool.apserver.ServerScanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

public class Main {
	private static Logger LOGGER = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws Exception {
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		context.reset();
		try {
			JoranConfigurator configurator = new JoranConfigurator();
			configurator.setContext(context);
			configurator.doConfigure("./config/logback.xml");
		} catch (JoranException je) {
			// StatusPrinter will handle this
		}
		StatusPrinter.printInCaseOfErrorsOrWarnings(context);		

		try {
			// read config
			ServerConfigManager configManager = new ServerConfigManager();
			Map<String, List<Integer>> portMapping = configManager.getMapping();
			AuthManager authManager = new AuthManager();

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
					serverInfo.setType(serverType);
					LOGGER.info("Scan host " + serverInfo.getHost() + " for AP server " + serverInfo.getType());
					ServerScan serverScan = ServerScanFactory.getServerScan(serverType);

					// by port
					List<Integer> ports = portMapping.get(serverType);
					for (Integer port : ports) {
						serverInfo.setPort(port);
						scanByAccount(serverInfo, serverScan, resultRecords, authManager);
					}
				}
			}

			// TODO
			File outputFile = new File("output.csv");
			CsvWriter<ServerInfo> w = new CsvWriter<ServerInfo>(outputFile);
			w.write(resultRecords, true);
			w.close();

			// Writer writer = new FileWriter("output.csv");
			// StatefulBeanToCsvBuilder<ServerInfo> beanToCsv =
			// StatefulBeanToCsvBuilder(writer).build();
			// beanToCsv.write(records);
			// writer.close();

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		LOGGER.info("End");
	}

	private static void scanByAccount(ServerInfo serverInfo, ServerScan serverScan, List<ServerInfo> resultRecords,
			AuthManager authManager) {
		for (String user : authManager.getUserList()) {
			for (String pwd : authManager.getPwdList()) {
				serverInfo.setAccount(user);
				serverInfo.setPassword(pwd);
				serverInfo.setResult(true);
				serverInfo.setRemark("");

				serverScan.scan(serverInfo);
				if (serverInfo.isResult()) {
					resultRecords.add(new ServerInfo(serverInfo));
					return; // return if account and password found
				}
			}
		}
	}
}
