package org.redcenter.scantool;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AuthManager {
	private static final String USER_PATH = "./config/user.properties";
	private static final String PWD_PATH = "./config/pwd.properties";
	private List<String> userList = null;
	private List<String> pwdList = null;

	public AuthManager() throws FileNotFoundException {
		userList = getStringList(USER_PATH, false);
		pwdList = getStringList(PWD_PATH, true);
	}

	private List<String> getStringList(String path, boolean decrypted) throws FileNotFoundException {
		List<String> list = new ArrayList<>();
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(path));
			scanner.useDelimiter(System.lineSeparator());
			while (scanner.hasNext()) {
				String value = scanner.next().trim();
				if (value.isEmpty()) {
					continue;
				}
				if (decrypted) {
					value = decrypt(value);
				}
				list.add(value);
			}
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
		return list;
	}

	public List<String> getUserList() {
		return userList;
	}

	public List<String> getPwdList() {
		return pwdList;
	}

	private String decrypt(String value) {
		// TODO
		return value;
	}

	// TODO remove
	public static void main(String[] args) throws FileNotFoundException {
		AuthManager manager = new AuthManager();
		List<String> list = manager.getUserList();
		for (String entry : list) {
			System.out.println(entry);
		}
		System.out.println("end");
	}
}
