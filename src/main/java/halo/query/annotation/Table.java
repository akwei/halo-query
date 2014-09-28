package halo.query.annotation;

import halo.query.dal.BaseDALParser;

import java.lang.annotation.*;

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
     * 逻辑表名称。表的别名为name+"_"
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
     * 分表分库的解析器类型
     *
     * @return
     */
    Class<?> dalParser() default BaseDALParser.class;

    /**
     * 自增序列的分区解析器
     *
     * @return
     */
    Class<?> seqDalParser() default BaseDALParser.class;
}