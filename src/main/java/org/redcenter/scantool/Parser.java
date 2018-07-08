package org.redcenter.scantool;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Base64;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Parser {
	private static final int TIMEOUT = (int) TimeUnit.MILLISECONDS.convert(60, TimeUnit.SECONDS);

	public String getPage(String url) {
		try {
			Document doc = parseHtml(url);
			String name = doc.select("h2").text(); // get comic name
			name = name.replaceAll("[\u0001-\u001f<>:\"/\\\\|?*\u007f]+", "_");
			if (name.endsWith("_")) {
				name = name.substring(0, name.length() - 1);
			}
			return name;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public String getComicMainPageFromImagePage(String imageUrl) {
		try {
			Document doc = Jsoup.parse(new URL(imageUrl), TIMEOUT);
			Elements element = doc.select("a[href*=" + "]");
			// ex: /photos-index-aid-10548.html
			String urlName = element.attr("href");

			int lastIndex = imageUrl.lastIndexOf("/");
			String parentUrl = imageUrl.substring(0, lastIndex);
			String url = parentUrl + urlName;
			System.out.println("Find " + url + " for " + imageUrl);
			return url;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	private Document parseHtml(String url) throws IOException {
		return Jsoup.connect(url).timeout(TIMEOUT)
				.userAgent(
						"Mozilla/5.0 (Windows; U; WindowsNT 6.1; en-US; rv1.8.1.6) " + "Gecko/20070725 Firefox/2.0.0.6")
				.referrer("http://www.google.com").get();
	}

	public void parse() {
		jboss();
		// tomcat();

		System.out.println();
	}

	private void tomcat() {
		// String imageUrl = "http://localhost:8080/manager/list"; //6

		// String imageUrl = "http://localhost:8080/manager/text/serverinfo"; //7
		String imageUrl = "http://localhost:8080/manager/status"; // 7 401 403

		try {
			String strUserId = "admin";
			String strPasword = "admin";

			// encode the authString using base64
			String authString = strUserId + ":" + strPasword;
			String encodedString = new String(Base64.encodeBase64(authString.getBytes()));

			// Document doc = Jsoup.parse(new URL(imageUrl), TIMEOUT);
			Document doc = Jsoup.connect(imageUrl).header("Authorization", "Basic " + encodedString).get();

			System.out.println(doc);
		} catch (HttpStatusException e) {
			e.printStackTrace();
			System.err.println(e.getStatusCode());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * login_form=login_form&login_form%3Aname=test&login_form%3Apassword=tes&login_form%3Asubmit=Login&javax.faces.ViewState=
	 *
	 * HTTP/1.1 302 Moved Temporarily 
	 * Server: Apache-Coyote/1.1 
	 * X-Powered-By: Servlet 2.5; JBoss-5.0/JBossWeb-2.1 
	 * X-Powered-By: JSF/1.2 
	 * Location: http://wwww.example.com/admin-console/secure/summary.seam?conversationId=391
	 * Set-Cookie: JSESSIONID=9D6DCB5F2E0CA1AAE374FE763EED9C79; Path=/admin-console
	 */
	// consider timeout for init
	private void jboss() {
		String imageUrl = "http://localhost:8080/admin-console/login.seam;jsessionid=4416F53DDE1DBC8081CDBDCDD1666FB0";
		try {
			// Connection.Response loginForm = Jsoup.connect(imageUrl)
			// .method(Connection.Method.GET).execute();
			// Document document =
			// Jsoup.connect("https://www.desco.org.bd/ebill/authentication.php")
			// .data("cookieexists", "false").data("username", "32007702").data("login",
			// "Login")
			// .cookies(loginForm.cookies()).post();

			Connection.Response loginForm = Jsoup.connect(imageUrl).method(Connection.Method.GET).execute();
			Document document = loginForm.parse();
//			Elements element = document.select("input");
//			Elements element2 = document.select("input#javax.faces.ViewState");
//			Elements element3 = document.select("#javax.faces.ViewState");
			Element element = document.getElementById("javax.faces.ViewState");
			Document doc = Jsoup.connect(imageUrl)
					.data("login_form", "login_form")
					.data("login_form:name", "admin")
					.data("login_form:password", "admin")
					.data("login_form:submit", "Login")
					.data("javax.faces.ViewState",element.attr("value"))
					.cookies(loginForm.cookies()).post();

			// Document doc = Jsoup.connect(imageUrl)
			// .data("name", "admin").data("password", "admin")
			// .post();

			System.out.println(doc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
