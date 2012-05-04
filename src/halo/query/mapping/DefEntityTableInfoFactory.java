package halo.query.mapping;

import java.util.HashMap;
import java.util.Map;

public class DefEntityTableInfoFactory implements EntityTableInfoFactory {

    private Map<String, EntityTableInfo<?>> map = new HashMap<String, EntityTableInfo<?>>();

    @SuppressWarnings("unchecked")
    public synchronized EntityTableInfo<?> getEntityTableInfo(Class<?> clazz) {
        EntityTableInfo<?> info = map.get(clazz.getName());
        if (info == null) {
            info = new EntityTableInfo(clazz);
            map.put(clazz.getName(), info);
        }
        return info;
    }
}
