package halo.locker;

import java.lang.annotation.*;

/**
 * 标识在方法上表示使用锁的aop进行管理
 * Created by akwei on 6/18/15.
 */
@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistLockAble {
    /**
     * 锁定资源key
     *
     * @return
     */
    String key() default "";

    /**
     * 锁定的过期时间
     *
     * @return
     */
    int time() default 0;
}
