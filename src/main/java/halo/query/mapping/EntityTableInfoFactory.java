package halo.query.mapping;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class EntityTableInfoFactory {

    private static final Map<String, EntityTableInfo<?>> map = new HashMap<String, EntityTableInfo<?>>();

    public synchronized static <T> EntityTableInfo<T> getEntityTableInfo(
            Class<T> clazz) {
        EntityTableInfo<T> info = (EntityTableInfo<T>) map.get(clazz.getName());
        if (info == null) {
            info = new EntityTableInfo<T>(clazz);
            map.put(clazz.getName(), info);
        }
        return info;
    }

    public static void clear() {
        map.clear();
    }
}
