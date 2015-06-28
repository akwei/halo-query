package halo.cache;

import java.lang.annotation.*;

/**
 * 表示对缓存的数据进行清理
 * Created by akwei on 6/18/15.
 */
@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheCleanAble {

}
