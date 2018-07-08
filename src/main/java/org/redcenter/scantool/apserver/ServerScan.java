package org.redcenter.scantool.apserver;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ServerScan {
	// 60 seconds 
	public static final int TIMEOUT = (int) TimeUnit.MILLISECONDS.convert(180, TimeUnit.SECONDS);
	
	protected Logger logger = LoggerFactory.getLogger(getClass());

	
	public abstract void scan(ServerInfo serverInfo);
}
