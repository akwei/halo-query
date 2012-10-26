package halo.query.annotation;

import halo.query.dal.BaseDALParser;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 逻辑表名称
 * 
 * @author akwei
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

	/**
	 * 逻辑表名称
	 * 
	 * @return
	 */
	String name();

	/**
	 * db2 sequence
	 * 
	 * @return
	 */
	String db2_sequence() default "";

	/**
	 * oracle sequence
	 * 
	 * @return
	 */
	String oracle_sequence() default "";

	/**
	 * mysql 自增表
	 * 
	 * @return
	 */
	String mysql_sequence() default "";

	/**
	 * mysql 表的自增字段
	 * 
	 * @return
	 */
	String mysql_sequence_column_name() default "";

	/**
	 * sequence使用的数据源在spring配置中的beanid
	 * 
	 * @return
	 */
	String sequence_ds_bean_id() default "";

	/**
	 * 分表分库的解析器类型
	 * 
	 * @return
	 */
	Class<?> dalParser() default BaseDALParser.class;
}