package halo.query.dal;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户自定义数据源与表信息
 * 
 * @author akwei
 */
public class DALInfo {

	private String dsKey;

	private final Map<String, String> tableMap = new HashMap<String, String>();

	/**
	 * 设置数据源
	 * 
	 * @param dsKey
	 */
	public void setDsKey(String dsKey) {
		this.dsKey = dsKey;
	}

	/**
	 * 获得数据源
	 * 
	 * @return
	 */
	public String getDsKey() {
		return dsKey;
	}

	/**
	 * 设置逻辑表与真实表的映射
	 * 
	 * @param cls
	 * @param realTableName
	 */
	public void setRealTable(Class<?> cls, String realTableName) {
		tableMap.put(cls.getName(), realTableName);
	}

	/**
	 * 获得真实表名
	 * 
	 * @param cls
	 * @return
	 */
	public String getRealTable(Class<?> cls) {
		return tableMap.get(cls.getName());
	}
}