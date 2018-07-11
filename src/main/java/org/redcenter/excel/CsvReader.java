package org.redcenter.excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.redcenter.scantool.apserver.ServerInfo;

import com.opencsv.bean.CsvToBeanBuilder;

/**
 * TODO
 * 
 * @author boneman
 * @param <T>
 */
public class CsvReader<T> {
	private File file;

	public CsvReader(File file) throws FileNotFoundException {
		this.file = file;
	}

	/**
	 * 
	 * @param records
	 * 
	 * @throws IOException
	 */
	public void read(List<T> records, Class<T> clazz) throws IOException {
		List<T> beans = new CsvToBeanBuilder<T>(new FileReader(file)).withType(clazz).build().parse();
		records.addAll(beans);
	}

	// TODO remove
	public static void main(String[] args) throws Exception {
		List<ServerInfo> records = new ArrayList<>();
		new CsvReader<ServerInfo>(new File("test.csv")).read(records, ServerInfo.class);
		for (ServerInfo serverInfo : records) {
			System.out.println(serverInfo);
		}

	}
}
