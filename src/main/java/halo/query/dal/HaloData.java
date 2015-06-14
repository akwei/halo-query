package halo.query.dal;

import java.util.HashMap;
import java.util.Map;

/**
 * 线程数据传递使用
 * Created by akwei on 6/14/15.
 */
public class HaloData {

    private static final ThreadLocal<Map<String, Object>> resourceTL = new ThreadLocal<Map<String, Object>>();

    /**
     * 添加数据
     *
     * @param key   key
     * @param value 需要传递的数据
     */
    public static void addData(String key, Object value) {
        Map<String, Object> map = resourceTL.get();
        if (map == null) {
            map = new HashMap<String, Object>();
            resourceTL.set(map);
        }
        map.put(key, value);
    }

    /**
     * 获得数据map
     *
     * @return map。如果map没有初始化，就会返回null
     */
    public static Map<String, Object> getDataMap() {
        return resourceTL.get();
    }

    /**
     * 获得数据
     *
     * @param key key
     * @param <T> 数据类型
     * @return map中存在的数据。如果不存在，就返回null
     */
    public static <T> T getData(String key) {
        Map<String, Object> map = resourceTL.get();
        if (map == null) {
            return null;
        }
        Object obj = map.get(key);
        if (obj == null) {
            return null;
        }
        return (T) obj;
    }

    /**
     * 移除所有线程数据
     */
    public static void remove() {
        Map<String, Object> map = resourceTL.get();
        if (map == null) {
            return;
        }
        map.clear();
        resourceTL.remove();
    }
}
