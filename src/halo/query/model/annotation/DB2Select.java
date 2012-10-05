/**
 * 
 */
package halo.query.model.annotation;

import halo.query.annotation.DefClass;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识sql的部分
 * 
 * @author akwei
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DB2Select {

	/**
	 * inner join 操作的Class[]，默认值无实际意义，只是作为一个默认标识存在
	 * 
	 * @return
	 */
	Class<?>[] join() default DefClass.class;

	/**
	 * sql中where表达式部分
	 * 
	 * @return
	 */
	String where();

	/**
	 * sql中orderBy表达式部分
	 * 
	 * @return
	 */
	String orderBy() default "";

	/**
	 * 分页开始记录号
	 * 
	 * @return
	 */
	int begin();

	/**
	 * 分页获取数量
	 * 
	 * @return
	 */
	int size();
}
