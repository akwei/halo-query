package halo.query.mapping;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@SuppressWarnings("unchecked")
public class EntityTableInfoFactory {

    private static final Map<String, EntityTableInfo<?>> map = new HashMap<String, EntityTableInfo<?>>();

    private static final ReentrantReadWriteLock rwl = new
            ReentrantReadWriteLock();

    /**
     * 获得通过class获得实体类型相关数据
     *
     * @param clazz 类型
     * @param <T>   泛型
     * @return 实体类型相关数据，null表示找不到数据
     */
    public static <T> EntityTableInfo<T> getEntityTableInfo(
            Class<T> clazz) {
        rwl.readLock().lock();
        EntityTableInfo<T> info = (EntityTableInfo<T>) map.get(clazz.getName());
        if (info != null) {
            rwl.readLock().unlock();
            return info;
        }
        rwl.readLock().unlock();
        rwl.writeLock().lock();
        try {
            info = (EntityTableInfo<T>) map.get(clazz.getName());
            if (info != null) {
                return info;
            }
            info = new EntityTableInfo<T>(clazz);
            map.put(clazz.getName(), info);
            return info;
        } finally {
            rwl.writeLock().unlock();
        }
    }

    public static void clear() {
        map.clear();
    }
}
