package org.redcenter.scantool;

import org.redcenter.scantool.apserver.ServerInfo;
import org.redcenter.scantool.apserver.ServerScan;
import org.redcenter.scantool.apserver.ServerScanFactory;

public class Main {

	public static void main(String[] args) throws Exception {
		ServerInfo info = new ServerInfo();
		info.setHost("localhost");
		info.setPort(8080);
		info.setAccount("admin");
		info.setPassword("adm2in");
		
		ServerScan scan = ServerScanFactory.getServerScan(ServerScanFactory.AP_SERVER_TOMCAT7);
		scan.scan(info);
		System.out.println();		
	}

}
