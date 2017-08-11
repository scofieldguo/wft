package com.bw30.open.wft.common.translate.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public abstract class AbsTxtReader<T> implements IReader<T> {

	protected List<T> models = new ArrayList<T>();

	private InputStream is;

	public AbsTxtReader(InputStream is) throws Exception {
		this.is = is;
	}

	public AbsTxtReader(String fileName) throws Exception {
		this.is = new FileInputStream(new File(fileName));
	}

	public List<T> getModels(Object... objects) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(is),
					5 * 1024 * 1024);
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				this.processHandle(tempString, objects);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return models;
	}

	public abstract void processHandle(String string, Object... objects);

}
