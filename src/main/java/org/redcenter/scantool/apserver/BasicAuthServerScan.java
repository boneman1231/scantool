package org.redcenter.scantool.apserver;

import java.io.IOException;
import java.net.ConnectException;

import org.apache.commons.codec.binary.Base64;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public abstract class BasicAuthServerScan extends ServerScan {

	protected void internalScan(ServerInfo serverInfo) {
		String msg;
		String url = "http://" + serverInfo.getHost() + ":" + serverInfo.getPort() + getPath();
		String account = serverInfo.getAccount();
		String password = serverInfo.getPassword();
		try {
			// encode the authString using base64 
			String authString = account + ":" + password;
			String encodedString = new String(Base64.encodeBase64(authString.getBytes()));
			Document doc = Jsoup.connect(url).header("Authorization", "Basic " + encodedString).get();

			if (processResult(doc, url)) {
				serverInfo.setResult(true);
				msg = "find weak password for " + basicInfo;
			} else {
				serverInfo.setResult(false);
				msg = "no weak password for " + basicInfo;
			}
			
			logger.info(msg);
			processRemark(serverInfo, msg);
		} catch (ConnectException e) {
			msg = "Connection fail: " + e.getMessage();
			logger.debug(msg, e);
			serverInfo.setResult(false);
//			remark.append(msg);
//			serverInfo.setRemark(remark.toString());
		} catch (HttpStatusException e) {
			handleHttpException(e, serverInfo);
		} catch (IOException e) {
			msg = e.getMessage();
			logger.error(msg, e);
			serverInfo.setResult(true); // not complete scan
			processRemark(serverInfo, msg);
		}
	}

	protected abstract String getPath();
	
	protected boolean processResult(Document doc, String url) {
		return true;
	}
}
