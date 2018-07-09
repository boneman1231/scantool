package org.redcenter.scantool.apserver;

public class JBoss7AdminConsoleScan extends BasicAuthServerScan {

	/**
	 * JBoss 7: http://localhost:9990/console
	 */
	@Override
	protected String getPath() {
		return "/console";
	}
}
