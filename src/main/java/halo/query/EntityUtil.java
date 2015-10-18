package halo.query;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 实体对象工具类
 * Created by akwei on 9/15/14.
 */
@SuppressWarnings("unchecked")
class EntityUtil {

    public static final Map<String, EntityCopier> beanIfaceMap = new HashMap<String, EntityCopier>();

    private static final ReentrantReadWriteLock rwl = new
            ReentrantReadWriteLock();

    public static <T, E> void copy(T from, E to) {
        EntityCopier entityCopier = getBeanCopier(from.getClass(), to.getClass());
        entityCopier.copy(from, to);
    }

    private static <T, E> EntityCopier createBeanIfaceImpl(Class<T> fromClazz, Class<E> toClazz) {
        JavassistEntityCopierClassCreater creater = new JavassistEntityCopierClassCreater(fromClazz, toClazz);
        Class<EntityCopier> clazz = creater.getMapperClass();
        try {
            return clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T, E> EntityCopier getBeanCopier(Class<T> fromClazz, Class<E> toClazz) {
        rwl.readLock().lock();
        String key = fromClazz.getName() + "_" + toClazz.getName();
        EntityCopier entityCopier = beanIfaceMap.get(key);
        if (entityCopier != null) {
            rwl.readLock().unlock();
            return entityCopier;
        }
        rwl.readLock().unlock();
        rwl.writeLock().lock();
        try {
            entityCopier = beanIfaceMap.get(key);
            if (entityCopier != null) {
                return entityCopier;
            }
            entityCopier = createBeanIfaceImpl(fromClazz, toClazz);
            beanIfaceMap.put(key, entityCopier);
            return entityCopier;
        } finally {
            rwl.writeLock().unlock();
        }
    }
}
