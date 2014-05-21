package com.finefit.base;

import java.util.Map;
import java.util.TreeMap;

public class CsvData {
	
	private TreeMap<String, String> map;

	public CsvData(TreeMap<String, String> map) {
		this.map = map;
	}

	public String get(String key) {
		return map.get(key);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			sb.append(entry.getKey()).append("_").append(entry.getValue())
					.append("_");
		}
		return sb.toString();
	}
}
