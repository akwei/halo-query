package halo.query.mapping;

import java.util.HashMap;
import java.util.Map;

public class EntityTableInfoFactory {

	private static final Map<String, EntityTableInfo<?>> map = new HashMap<String, EntityTableInfo<?>>();

	@SuppressWarnings("unchecked")
	public synchronized static EntityTableInfo<?> getEntityTableInfo(
			Class<?> clazz) {
		EntityTableInfo<?> info = map.get(clazz.getName());
		if (info == null) {
			info = new EntityTableInfo<Object>((Class<Object>) clazz);
			map.put(clazz.getName(), info);
		}
		return info;
	}
}
