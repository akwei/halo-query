package halo.query;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by akwei on 9/15/14.
 */
class BeanUtil {

    public static final Map<String, BeanCopier> beanIfaceMap = new HashMap<String, BeanCopier>();

    public static <T, E> void copy(T from, E to) {
        BeanCopier beanCopier = getBeanCopier(from.getClass(), to.getClass());
        beanCopier.copy(from, to);
    }

    private static <T, E> BeanCopier createBeanIfaceImpl(Class<T> fromClazz, Class<E> toClazz) {
        JavassistBeanCopierClassCreater creater = new JavassistBeanCopierClassCreater(fromClazz, toClazz);
        Class<BeanCopier> clazz = creater.getMapperClass();
        try {
            BeanCopier beanCopier = clazz.getConstructor().newInstance();
            return beanCopier;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized static <T, E> BeanCopier getBeanCopier(Class<T> fromClazz, Class<E> toClazz) {
        String key = fromClazz.getName() + "_" + toClazz.getName();
        BeanCopier beanCopier = beanIfaceMap.get(key);
        if (beanCopier == null) {
            beanCopier = createBeanIfaceImpl(fromClazz, toClazz);
            beanIfaceMap.put(key, beanCopier);
        }
        return beanCopier;
    }
}
