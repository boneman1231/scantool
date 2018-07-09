package org.redcenter.scantool.apserver;

import java.io.IOException;
import java.net.ConnectException;

import org.apache.commons.codec.binary.Base64;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;

public abstract class BasicAuthServerScan extends ServerScan {

	public void scan(ServerInfo serverInfo) {
		String url = "http://" + serverInfo.getHost() + ":" + serverInfo.getPort() + getPath();
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
			// TODO 401 not authenticate, 403 not authorized
			logger.error("status " + e.getStatusCode(), e);
			serverInfo.setResult(false);
			serverInfo.setRemark("status" + e.getStatusCode());
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	protected abstract String getPath();
}
