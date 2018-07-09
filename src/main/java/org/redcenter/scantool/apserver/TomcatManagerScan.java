package org.redcenter.scantool.apserver;

public class TomcatManagerScan extends BasicAuthServerScan {

	/**
	 * tomcat 6: http://localhost:8080/manager/list 
	 * tomcat 7: http://localhost:8080/manager/text/serverinfo
	 * tomcat: http://localhost:8080/manager/html => /manager/status
	 */
	@Override
	protected String getPath() {
		// use /manager/status for Tomcat 7 all users
		return "/manager/status";
	}
}
