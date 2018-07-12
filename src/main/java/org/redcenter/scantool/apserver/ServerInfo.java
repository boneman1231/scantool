package org.redcenter.scantool.apserver;

import org.redcenter.excel.annotation.ExcelColumn;
import org.redcenter.excel.annotation.ExcelSheet;

@ExcelSheet
public class ServerInfo {

	@ExcelColumn
	private String host;

	private int port;

	private String account;

	private String password;

	@ExcelColumn
	private String type;

	/**
	 * true if issue found
	 */
	@ExcelColumn
	private boolean result;

	@ExcelColumn
	private String remark;

	public ServerInfo() {
	}

	public ServerInfo(ServerInfo info) {
		this.host = info.getHost();
		this.port = info.getPort();
		this.type = info.getType();
		this.result = info.isResult();
		this.remark = info.getRemark();
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "ServerInfo [host=" + host + ", port=" + port + ", account=" + account 
				+ ", type=" + type + ", result=" + result + ", remark=" + remark + "]";
	}
}
