package halo.query.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表示与数据表字段对应
 *
 * @author akwei
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    /**
     * 默认与表字段相同，如果不同时，value=数据表的字段
     *
     * @return
     */
    String value() default "";

    /**
     * 返回枚举类型中通过int获得枚举值的方法。默认是 findByValue
     *
     * @return 自定义的方法名称, 实现 public static CustomEnum [methodName](int value)方法
     */
    String findEnumMethodName() default "findByValue";
}