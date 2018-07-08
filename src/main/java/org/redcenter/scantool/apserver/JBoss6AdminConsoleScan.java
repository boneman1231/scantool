package org.redcenter.scantool.apserver;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class JBoss6AdminConsoleScan extends ServerScan {
	
	/**
	 * login_form=login_form&login_form:name=admin&login_form:password=admin&login_form:submit=Login&javax.faces.ViewState=
	 */
	public void scan(ServerInfo serverInfo) {
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
				serverInfo.setRemark("no weak password");
			} else {
				// authorized html: <li> : <a href="/admin-console/secure/summary.seam?>
				Elements elements = doc.select("a[href*=/admin-console/secure/summary.seam?]");
				if (elements.isEmpty()) {
					serverInfo.setResult(false);
					serverInfo.setRemark("no weak password");
				} else {
					serverInfo.setResult(true);
					serverInfo.setRemark("weak password");
				}
			}
		} catch (ConnectException e) {
			logger.error("not connect"+e.getMessage(), e);
			serverInfo.setResult(false);
			serverInfo.setRemark("not connect"+e.getMessage());
		} catch (SocketTimeoutException e) {
			// TODO consider timeout for init
			logger.error("timeout"+e.getMessage(), e);			
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}
}
