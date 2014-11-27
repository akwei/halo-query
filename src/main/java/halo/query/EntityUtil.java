package halo.query;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by akwei on 9/15/14.
 */
class EntityUtil {

    public static final Map<String, EntityCopier> beanIfaceMap = new HashMap<String, EntityCopier>();

    public static <T, E> void copy(T from, E to) {
        EntityCopier entityCopier = getBeanCopier(from.getClass(), to.getClass());
        entityCopier.copy(from, to);
    }

    private static <T, E> EntityCopier createBeanIfaceImpl(Class<T> fromClazz, Class<E> toClazz) {
        JavassistEntityCopierClassCreater creater = new JavassistEntityCopierClassCreater(fromClazz, toClazz);
        Class<EntityCopier> clazz = creater.getMapperClass();
        try {
            EntityCopier entityCopier = clazz.getConstructor().newInstance();
            return entityCopier;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized static <T, E> EntityCopier getBeanCopier(Class<T> fromClazz, Class<E> toClazz) {
        String key = fromClazz.getName() + "_" + toClazz.getName();
        EntityCopier entityCopier = beanIfaceMap.get(key);
        if (entityCopier == null) {
            entityCopier = createBeanIfaceImpl(fromClazz, toClazz);
            beanIfaceMap.put(key, entityCopier);
        }
        return entityCopier;
    }
}
