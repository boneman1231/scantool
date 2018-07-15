package org.redcenter.scantool.apserver;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class JBoss6AdminConsoleScan extends ServerScan {
	
	/**
	 * login_form=login_form&login_form:name=admin&login_form:password=admin&login_form:submit=Login&javax.faces.ViewState=
	 */
	protected void internalScan(ServerInfo serverInfo) {
		String msg;
//		String url = "http://localhost:8080/admin-console/login.seam;jsessionid=4416F53DDE1DBC8081CDBDCDD1666FB0";
		String url = "http://" + serverInfo.getHost() + ":" + serverInfo.getPort() + "/admin-console/login.seam";
		try {
			Connection.Response loginForm = Jsoup.connect(url).timeout(TIMEOUT).method(Connection.Method.GET).execute();
			Document document = loginForm.parse();
			Element element = document.getElementById("javax.faces.ViewState");
			Document doc = Jsoup.connect(url).timeout(TIMEOUT)
					.data("login_form", "login_form")
					.data("login_form:name", serverInfo.getAccount())
					.data("login_form:password", serverInfo.getPassword())
					.data("login_form:submit", "Login")
					.data("javax.faces.ViewState",element.attr("value"))
					.cookies(loginForm.cookies()).post();

			if (doc == null) {
				serverInfo.setResult(false);
				msg = "no weak password";				
			} else {
				// authorized html: <li> : <a href="/admin-console/secure/summary.seam?>
				Elements elements = doc.select("a[href*=/admin-console/secure/summary.seam?]");
				if (elements.isEmpty()) {
					serverInfo.setResult(false);
					msg = "no weak password";
				} else {
					serverInfo.setResult(true);
					msg = "find weak password";
				}
			}			
			
			logger.info(basicInfo + msg);
			processRemark(serverInfo, msg);						
		} catch (ConnectException e) {
			msg = "Connection fail: " + e.getMessage();
			logger.debug(msg, e);
			serverInfo.setResult(false);
			processRemark(serverInfo, msg);
		} catch (SocketTimeoutException e) {
			// consider timeout for admin console initialize			
			msg = "Connection timeout: " + e.getMessage();
			logger.debug(msg, e);
			serverInfo.setResult(true); // not complete scan
			processRemark(serverInfo, msg);
		} catch (HttpStatusException e) {
			handleHttpException(e, serverInfo);
		} catch (IOException e) {
			msg = "Not complete scan: " + e.getMessage();
			logger.error(msg, e);
			serverInfo.setResult(true); // not complete scan
			processRemark(serverInfo, msg);
		}
	}
}
