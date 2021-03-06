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
     * @return 表名称
     */
    String name();

    /**
     * 分表分库的解析器类型
     *
     * @return 解析器class
     */
    Class<?> dalParser() default BaseDALParser.class;

}