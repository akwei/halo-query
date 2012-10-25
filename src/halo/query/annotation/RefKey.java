package halo.query.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义实体之间在数据库中的关联关系为，表示field为所指向类的引用关系，field为join查询时使用的连接key
 * 
 * @author akwei
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RefKey {

	/**
	 * 关联的类
	 * 
	 * @return
	 */
	Class<?> refClass();

	String fieldName() default "";
}