package halo.query.dal;

import java.util.HashMap;
import java.util.Map;

/**
 * 此类操作当前状态，包括数据源key，用户自定义信息
 * 
 * @author akwei
 */
public class DALStatus {

	private static final ThreadLocal<Map<String, Object>> dalParserParametersTL = new ThreadLocal<Map<String, Object>>();

	private static final ThreadLocal<String> dsKeyTL = new ThreadLocal<String>();

	private static final ThreadLocal<Map<String, DALInfo>> dalInfoTL = new ThreadLocal<Map<String, DALInfo>>();

	private DALStatus() {
	}

	public static void setDsKey(String dsKey) {
		dsKeyTL.set(dsKey);
	}

	public static String getDsKey() {
		String key = dsKeyTL.get();
		if (key == null) {
			key = HaloDALDataSource.DEFAULT_DS_NAME;
		}
		return key;
	}

	public static void setParamMap(Map<String, Object> map) {
		dalParserParametersTL.set(map);
	}

	public static Map<String, Object> getParamMap() {
		return dalParserParametersTL.get();
	}

	public static void setDalInfo(Class<?> cls, DALInfo dalInfo) {
		Map<String, DALInfo> map = dalInfoTL.get();
		if (map == null) {
			map = new HashMap<String, DALInfo>();
			dalInfoTL.set(map);
		}
		map.put(cls.getName(), dalInfo);
	}

	public static DALInfo getDalInfo(Class<?> cls) {
		Map<String, DALInfo> map = dalInfoTL.get();
		if (map == null) {
			return null;
		}
		return map.get(cls.getName());
	}

	public static void remove() {
		dalParserParametersTL.remove();
		dsKeyTL.remove();
		dalInfoTL.remove();
	}
}
