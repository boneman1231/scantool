package org.redcenter.scantool.apserver;

public class ServerScanFactory {

	public static final String AP_SERVER_TOMCAT6 = "TOMCAT6";
	public static final String AP_SERVER_TOMCAT7 = "TOMCAT7";
	public static final String AP_SERVER_JBOSS6 = "JBOSS6";
	public static final String AP_SERVER_JBOSS7 = "JBOSS7";
	public static final String AP_SERVER_WILDFLY8 = "WILDFLY8";
	public static final String AP_SERVER_WILDFLY9 = "WILDFLY9";
	public static final String AP_SERVER_WILDFLY10 = "WILDFLY10";

	public static ServerScan getServerScan(String type) throws Exception {
		if (type.equalsIgnoreCase(AP_SERVER_JBOSS6)) {
			return new JBoss6AdminConsoleScan();
		} else if (type.equalsIgnoreCase(AP_SERVER_TOMCAT6)) {
			return new TomcatManagerScan();
		} else if (type.equalsIgnoreCase(AP_SERVER_TOMCAT7)) {
			return new TomcatManagerScan();
		} else if (type.equalsIgnoreCase(AP_SERVER_JBOSS7)) {
			return new JBoss7AdminConsoleScan();
		} else if (type.equalsIgnoreCase(AP_SERVER_WILDFLY8)) {
			return new JBoss7AdminConsoleScan();
		} else if (type.equalsIgnoreCase(AP_SERVER_WILDFLY9)) {
			return new JBoss7AdminConsoleScan();
		} else if (type.equalsIgnoreCase(AP_SERVER_WILDFLY10)) {
			return new JBoss7AdminConsoleScan();
		}
		return null;
	}

	public static String[] getApServers() {
		return new String[] {
				AP_SERVER_TOMCAT6, 
				AP_SERVER_TOMCAT7, 
				AP_SERVER_JBOSS6, 
				AP_SERVER_JBOSS7, 
//				AP_SERVER_WILDFLY8,
//				AP_SERVER_WILDFLY9, 
//				AP_SERVER_WILDFLY10 
				};
	}
}
