package org.redcenter.scantool.apserver;

import java.util.concurrent.TimeUnit;

import org.jsoup.HttpStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ServerScan {
	// 300 seconds
	public static final int TIMEOUT = (int) TimeUnit.MILLISECONDS.convert(300, TimeUnit.SECONDS);
	protected String basicInfo;
	protected Logger logger = LoggerFactory.getLogger(getClass());

	public void scan(ServerInfo serverInfo) {
		basicInfo = "[AP server " + serverInfo.getType() + " for account " + serverInfo.getAccount() + " with port "
				+ serverInfo.getPort() + "] ";
		internalScan(serverInfo);
	}

	protected abstract void internalScan(ServerInfo serverInfo);

	protected void processResult(ServerInfo info, String remark) {
		// set remark only if find issue
		if (info.isResult() && basicInfo != null) {
//			info.setRemark(basicInfo + remark);
//			if (info.getRemark() != null) {			
//				info.setRemark(info.getRemark() + basicInfo + remark);
//			}
		}
	}

	protected void handleException(HttpStatusException ex, ServerInfo serverInfo) {
		String msg;
		if (ex.getStatusCode() == 401) {
			msg = "Not authenticated by HTTP status: " + ex.getStatusCode() + " , " + ex.getMessage();
		} else if (ex.getStatusCode() == 403) {
			msg = "Not authorized by HTTP status: " + ex.getStatusCode() + " , " + ex.getMessage();
		} else if (ex.getStatusCode() == 404) {
			msg = "Page not found by HTTP status: " + ex.getStatusCode() + " , " + ex.getMessage();
		} else {
			msg = "HTTP status: " + ex.getStatusCode() + " , " + ex.getMessage();
		}
		logger.debug(msg, ex);
		serverInfo.setResult(false);
		processResult(serverInfo, msg);

	}
}
