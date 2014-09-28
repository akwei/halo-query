package halo.datasource;

import java.util.Map;

public class JsonObj {

	private Map<String, Object> map;

	public JsonObj(Map<String, Object> map) {
		this.map = map;
	}

	public String getString(String name) {
		if (map == null) {
			return null;
		}
		Object obj = map.get(name);
		if (obj instanceof Number) {
			return obj.toString();
		}
		return (String) map.get(name);
	}

	public boolean getBoolean(String name) {
		if (map == null) {
			return false;
		}
		return Boolean.parseBoolean((String) map.get(name));
	}

	public int getInt(String name) {
		if (map == null) {
			return 0;
		}
		String v = getString(name);
		if (v == null || v.equals("")) {
			return 0;
		}
		try {
			return Integer.parseInt(v);
		}
		catch (NumberFormatException e) {
			return 0;
		}
	}

	public long getLong(String name) {
		if (map == null) {
			return 0;
		}
		String v = getString(name);
		if (v == null || v.equals("")) {
			return 0;
		}
		try {
			return Long.parseLong(v);
		}
		catch (NumberFormatException e) {
			return 0;
		}
	}
}
