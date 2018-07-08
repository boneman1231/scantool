package org.redcenter.scantool.apserver;

import java.io.IOException;
import java.net.ConnectException;

import org.apache.commons.codec.binary.Base64;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class TomcatScan extends ServerScan {

	private String path;
	
	public TomcatScan(int version) throws Exception {
		if (version == 6) {
			path = "/manager/list";
		} else if (version == 7) {
			path = "/manager/text/serverinfo";
		} else {
			throw new Exception();
		}
	}
	
	/**
	 * tomcat 6: http://localhost:8080/manager/list 
	 * tomcat 7: http://localhost:8080/manager/text/serverinfo
	 */
	public void scan(ServerInfo serverInfo) {
		String url = "http://" + serverInfo.getHost() + ":" + serverInfo.getPort() + "/manager/status";
//		String url = "http://" + serverInfo.getHost() + ":" + serverInfo.getPort() + path;
		try {
			// encode the authString using base64
			String authString = serverInfo.getAccount() + ":" + serverInfo.getPassword();
			String encodedString = new String(Base64.encodeBase64(authString.getBytes()));
			Jsoup.connect(url).header("Authorization", "Basic " + encodedString).get();

			serverInfo.setResult(true);
			serverInfo.setRemark("weak password");
			logger.info("ok");
		} catch (ConnectException e) {
			logger.error("not connect" + e.getMessage(), e);
			serverInfo.setResult(false);
			serverInfo.setRemark("not connect" + e.getMessage());
		} catch (HttpStatusException e) {			
			// TODO 401 403
			logger.error("status " + e.getStatusCode(), e);
			serverInfo.setResult(false);
			serverInfo.setRemark("status" + e.getStatusCode());
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}
}
