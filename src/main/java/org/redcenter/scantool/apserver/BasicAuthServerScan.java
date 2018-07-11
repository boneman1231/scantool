package org.redcenter.scantool.apserver;

import java.io.IOException;
import java.net.ConnectException;

import org.apache.commons.codec.binary.Base64;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;

public abstract class BasicAuthServerScan extends ServerScan {

	protected void internalScan(ServerInfo serverInfo) {
		String msg;
		String url = "http://" + serverInfo.getHost() + ":" + serverInfo.getPort() + getPath();
		try {
			// encode the authString using base64
			String authString = serverInfo.getAccount() + ":" + serverInfo.getPassword();
			String encodedString = new String(Base64.encodeBase64(authString.getBytes()));
			Jsoup.connect(url).header("Authorization", "Basic " + encodedString).get();

			msg = "find weak password";
			logger.info(msg);
			serverInfo.setResult(true);
			processResult(serverInfo, msg);
		} catch (ConnectException e) {
			msg = "Connection fail: " + e.getMessage();
			logger.debug(msg, e);
			serverInfo.setResult(false);
//			remark.append(msg);
//			serverInfo.setRemark(remark.toString());
		} catch (HttpStatusException e) {
			handleException(e, serverInfo);
		} catch (IOException e) {
			msg = e.getMessage();
			logger.error(msg, e);
			serverInfo.setResult(true); // not complete scan
			processResult(serverInfo, msg);
		}
	}

	protected abstract String getPath();
}
