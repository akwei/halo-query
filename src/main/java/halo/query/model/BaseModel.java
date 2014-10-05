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

    /**
     * @param afterFrom
     * @param values
     * @return
     */
    public static int count(String afterFrom, Object[] values) {
        throw new RuntimeException(exceptionMsg + "count(String afterFrom, Object[] values)");
    }

    /**
     * @param afterFrom
     * @param values
     * @return
     */
    public static int count2(String afterFrom, List<?> values) {
        return count(afterFrom, Query.buildArgs(values));
    }

    /**
     * @param idValue
     * @return
     */
    public static <T> T objById(Object idValue) {
        throw new RuntimeException(exceptionMsg + "objById(Object idValue)");
    }

    public static <T> T objByIds(Object[] idValues) {
        throw new RuntimeException(exceptionMsg + "objByIds(Object[] idValues)");
    }

    /**
     * @param afterFrom
     * @param values
     * @return
     */
    public static <T> T obj(String afterFrom, Object[] values) {
        throw new RuntimeException(exceptionMsg + "obj(String afterFrom, Object[] values)");
    }

    /**
     * @param afterFrom
     * @param values
     * @return
     */
    public static <T> T obj2(String afterFrom, List<?> values) {
        return obj(afterFrom, Query.buildArgs(values));
    }

    /**
     * @param afterFrom
     * @param begin
     * @param size
     * @param values
     * @return
     */
    public static <T> List<T> mysqlList(String afterFrom, int begin, int size, Object[] values) {
        throw new RuntimeException(exceptionMsg + "mysqlList(String afterFrom, int begin, int size, Object[] values)");
    }

    /**
     * @param afterFrom
     * @param begin
     * @param size
     * @param values
     * @return
     */
    public static <T> List<T> mysqlList2(String afterFrom, int begin,
            int size, List<?> values) {
        return mysqlList(afterFrom, begin, size, Query.buildArgs(values));
    }

    /**
     * @param where
     * @param orderBy
     * @param begin
     * @param size
     * @param values
     * @return
     */
    public static <T> List<T> db2List(String where, String orderBy, int begin, int size, Object[] values) {
        throw new RuntimeException(exceptionMsg + "db2List(String where, String orderBy,int begin, int size, Object[] values)");
    }

    /**
     * @param where
     * @param orderBy
     * @param begin
     * @param size
     * @param values
     * @return
     */
    public static <T> List<T> db2List2(String where, String orderBy,
            int begin, int size, List<?> values) {
        return db2List(where, orderBy, begin, size, Query.buildArgs(values));
    }

    /**
     * @param afterFrom
     * @param values
     * @return
     */
    public static <T> List<T> list(String afterFrom, Object[] values) {
        throw new RuntimeException(exceptionMsg + "list(String afterFrom, Object[] values)");
    }

    /**
     * @param afterFrom
     * @param values
     * @return
     */
    public static <T> List<T> list2(String afterFrom, List<?> values) {
        return list(afterFrom, Query.buildArgs(values));
    }

    /**
     * @param updateSqlSeg
     * @param values
     * @return
     */
    public static int update(String updateSqlSeg, Object[] values) {
        throw new RuntimeException(exceptionMsg + "update(String updateSqlSeg, Object[] values)");
    }

    /**
     * @param updateSqlSeg
     * @param values
     * @return
     */
    public static int update2(String updateSqlSeg, List<?> values) {
        return update(updateSqlSeg, Query.buildArgs(values));
    }

    /**
     * @param where
     * @param inColumn
     * @param values
     * @param inValues
     * @param <E>
     * @param <T>
     * @return
     */
    public static <E, T> Map<E, T> map(String where, String inColumn,
            Object[] values, Object[] inValues) {
        throw new RuntimeException(exceptionMsg + "map(String where, " +
                "String inColumn, Object[] values, Object[] inValues)");
    }

    public static <E, T> Map<E, T> map2(String where, String inColumn,
            List<?> values, List<?> inValues) {
        return map(where, inColumn, Query.buildArgs(values),
                Query.buildArgs(inValues));
    }

}
