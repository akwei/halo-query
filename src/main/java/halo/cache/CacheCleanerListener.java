package halo.cache;

import java.util.Map;

/**
 * Created by akwei on 6/28/15.
 */
public interface CacheCleanerListener {
    void clean(Map<String, Object> ctxMap);
}
