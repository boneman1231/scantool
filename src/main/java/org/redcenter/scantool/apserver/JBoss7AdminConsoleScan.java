package org.redcenter.scantool.apserver;

import java.io.IOException;
import java.net.ConnectException;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.nodes.Document;

public class JBoss7AdminConsoleScan extends BasicAuthServerScan {

	/**
	 * JBoss 7: http://localhost:9990/console
	 * Also support Wildfly
	 */
	@Override
	protected String getPath() {
//		return "/console";
		return "/management";
	}
	
	protected boolean processResult(Document doc, String url) {
		//TODO handle no user yet page
		return true;
	}
	
	protected void internalScan(ServerInfo serverInfo) {
		String msg;
		String url = "http://" + serverInfo.getHost() + ":" + serverInfo.getPort() + getPath();
		String username = serverInfo.getAccount();
		String password = serverInfo.getPassword();		

		CloseableHttpResponse response = null;
		try {
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			credsProvider.setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
					new UsernamePasswordCredentials(username, password));
			CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
			HttpGet httpGet = new HttpGet(url);
			response = httpClient.execute(httpGet);
			
			logger.debug("Response: " + response);
			if (response.getStatusLine().getStatusCode() == 200) {
				serverInfo.setResult(true);
				msg = "find weak password";
			} else {
				serverInfo.setResult(false);
				msg = "no weak password";
			}						
			
			logger.info(basicInfo + msg);
			processRemark(serverInfo, msg);
		} catch (ConnectException e) {
			msg = "Connection fail: " + e.getMessage();
			logger.debug(msg, e);
			serverInfo.setResult(false);			
		} catch (IOException e) {
			msg = e.getMessage();
			logger.error(msg, e);
			serverInfo.setResult(true); // not complete scan
			processRemark(serverInfo, msg);
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		ServerInfo info = new ServerInfo();
		info.setHost("localhost");
		info.setPort(9990);
		info.setAccount("admin");
		info.setPassword("admin");
		info.setType(ServerScanFactory.AP_SERVER_WILDFLY10);

		ServerScan scan = ServerScanFactory.getServerScan(info.getType());
		scan.scan(info);
		System.out.println(info);
	}
}
