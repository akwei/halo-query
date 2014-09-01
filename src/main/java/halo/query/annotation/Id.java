package halo.query.annotation;

import java.lang.annotation.*;

/**
 * 表示数据表的id
 *
 * @author akwei
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Id {

    /**
     * 出现的顺序，如果是联合主键，请设置不同的顺序
     *
     * @return
     */
    int value() default 0;
}