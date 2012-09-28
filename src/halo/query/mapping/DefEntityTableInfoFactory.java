package halo.query.mapping;

import java.util.HashMap;
import java.util.Map;

public class DefEntityTableInfoFactory implements EntityTableInfoFactory {

	private Map<String, EntityTableInfo<? extends Object>> map = new HashMap<String, EntityTableInfo<? extends Object>>();

	@SuppressWarnings("unchecked")
	public synchronized EntityTableInfo<? extends Object> getEntityTableInfo(
			Class<? extends Object> clazz) {
		EntityTableInfo<? extends Object> info = map.get(clazz.getName());
		if (info == null) {
			info = new EntityTableInfo<Object>((Class<Object>) clazz);
			map.put(clazz.getName(), info);
		}
		return info;
	}
}
