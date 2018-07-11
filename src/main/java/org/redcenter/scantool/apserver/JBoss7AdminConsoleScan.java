package org.redcenter.scantool.apserver;

public class JBoss7AdminConsoleScan extends BasicAuthServerScan {

	/**
	 * JBoss 7: http://localhost:9990/console
	 * Also support Wildfly
	 */
	@Override
	protected String getPath() {
		return "/console";
	}
}
