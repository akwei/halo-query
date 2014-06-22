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
}