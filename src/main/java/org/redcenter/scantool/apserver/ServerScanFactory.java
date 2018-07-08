package org.redcenter.scantool.apserver;

public class ServerScanFactory {

	public static final String AP_SERVER_JBOSS6 = "JBOSS6";
	public static final String AP_SERVER_TOMCAT7 = "TOMCAT7";
	public static final String AP_SERVER_TOMCAT6 = "TOMCAT6";

	public static ServerScan getServerScan(String type) throws Exception {
		if (type.equals(AP_SERVER_JBOSS6)) {
			return new JBoss6AdminConsoleScan();
		} else if (type.equals(AP_SERVER_TOMCAT6)) {
			return new TomcatScan(6);
		} else if (type.equals(AP_SERVER_TOMCAT7)) {
			return new TomcatScan(7);
		}
		return null;
	}

}
