package halo.cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.List;
import java.util.Map;

/**
 * 进行操作后的缓存数据清理工作
 * Created by akwei on 6/28/15.
 */
@Aspect
public class CacheCleanerAOP {
    private static final Log logger = LogFactory.getLog(CacheCleanerAOP.class);
    private List<CacheCleanerListener> cacheCleanerListeners;

    public List<CacheCleanerListener> getCacheCleanerListeners() {
        return cacheCleanerListeners;
    }

    public void setCacheCleanerListeners(List<CacheCleanerListener> cacheCleanerListeners) {
        this.cacheCleanerListeners = cacheCleanerListeners;
    }

    @Around("@annotation(halo.cache.CacheCleanAble)")
    public Object lockAround(ProceedingJoinPoint pjp) throws Throwable {
        try {
            return pjp.proceed();
        } finally {
            this.clean();
        }
    }

    private void clean() {
        try {
            Map<String, Object> map = CacheCleaner.getMap();
            for (CacheCleanerListener cacheCleanerListener : cacheCleanerListeners) {
                if (logger.isDebugEnabled()) {
                    logger.debug("clean cache data " + cacheCleanerListener.toString());
                }
                cacheCleanerListener.clean(map);
            }
        } finally {
            CacheCleaner.remove();
        }
    }
}
