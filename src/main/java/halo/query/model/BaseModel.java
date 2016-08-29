package halo.query.model;

import halo.query.Query;

import java.util.List;
import java.util.Map;

/**
 * 直接使用static方法的调用方式，可以省掉dao的部分代码,目前不支持inner join 方式的查询
 *
 * @author akwei
 */
public class BaseModel {


    private static final String exceptionMsg = "class must override this method ";


    public static Query getQuery() {
        return Query.getInstance();
    }

    /**
     * 创建数据到数据库
     */
    public void create() {
        getQuery().insertForNumber(this);
    }

    /**
     * 更新已存在的数据，数据必须有id
     */
    public void update() {
        getQuery().update(this);
    }

    /**
     * 删除已存在的数据，数据必须有id
     */
    public void delete() {
        getQuery().delete(this);
    }

    public static int count(String afterFrom, Object[] values) {
        throw new RuntimeException(exceptionMsg + "count(String afterFrom, Object[] values)");
    }

    public static int count2(String afterFrom, List<?> values) {
        throw new RuntimeException(exceptionMsg + "count2(String afterFrom, List<?> values)");
    }

    public static <T> T objById(Object idValue) {
        throw new RuntimeException(exceptionMsg + "objById(Object idValue)");
    }

    public static <T> T objByIds(Object[] idValues) {
        throw new RuntimeException(exceptionMsg + "objByIds(Object[] idValues)");
    }

    public static <T> T obj(String afterFrom, Object[] values) {
        throw new RuntimeException(exceptionMsg + "obj(String afterFrom, Object[] values)");
    }

    public static <T> T obj2(String afterFrom, List<?> values) {
        throw new RuntimeException(exceptionMsg + "obj2(String afterFrom, List<?> values)");
    }

    public static <T> List<T> mysqlList(String afterFrom, int begin, int size, Object[] values) {
        throw new RuntimeException(exceptionMsg + "mysqlList(String afterFrom, int begin, int size, Object[] values)");
    }

    public static <T> List<T> mysqlList2(String afterFrom, int begin, int size, List<?> values) {
        throw new RuntimeException(exceptionMsg + "mysqlList2(String afterFrom, int begin, int size, List<?> values)");
    }

    public static <T> List<T> list(String afterFrom, Object[] values) {
        throw new RuntimeException(exceptionMsg + "list(String afterFrom, Object[] values)");
    }

    public static <T> List<T> list2(String afterFrom, List<?> values) {
        throw new RuntimeException(exceptionMsg + "list2(String afterFrom, List<?> values)");
    }

    public static int update(String updateSqlSeg, Object[] values) {
        throw new RuntimeException(exceptionMsg + "update(String updateSqlSeg, Object[] values)");
    }

    public static int update2(String updateSqlSeg, List<?> values) {
        throw new RuntimeException(exceptionMsg + "update2(String updateSqlSeg, List<?> values)");
    }

    public static <T> List<T> listInValues(String afterFrom, String inColumn, Object[] values, Object[] inValues) {
        throw new RuntimeException(exceptionMsg + "listInValues(String afterFrom,String inColumn, Object[] values, Object[] inValues)");
    }

    public static <T> List<T> listInValues2(String afterFrom, String inColumn, List<?> values, List<?> inValues) {
        throw new RuntimeException(exceptionMsg + "listInValues2(String afterFrom,String inColumn, List<?> values, List<?> inValues)");
    }

    public static <E, T> Map<E, T> map(String afterFrom, String inColumn, Object[] values, Object[] inValues) {
        throw new RuntimeException(exceptionMsg + "map(String afterFrom, String inColumn, Object[] values, Object[] inValues)");
    }

    public static <E, T> Map<E, T> map2(String afterFrom, String inColumn, List<?> values, List<?> inValues) {
        throw new RuntimeException(exceptionMsg + "map(String afterFrom, String inColumn, List<?> values, List<?> inValues)");
    }

}
