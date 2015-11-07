package halo.query;

import halo.query.dal.DALContext;
import halo.query.dal.DALStatus;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Map;

/**
 * 对于分表分库需求可以使用此类
 */
@SuppressWarnings("unchecked")
public class QueryEx extends Query {

    private static QueryEx instance;

    public QueryEx() {
        instance = this;
    }

    public static QueryEx getInstance() {
        if (instance == null) {
            throw new RuntimeException("must create " + QueryEx.class.getName());
        }
        return instance;
    }

    private void processDALContext(DALContext dalContext) {
        if (dalContext != null) {
            if (dalContext.isEnableSlave()) {
                DALStatus.setSlaveMode();
                if (dalContext.isSetSlaveDsKey()) {
                    DALStatus.setSlaveDsKey(dalContext.getSlaveDsKey());
                }
            }
            if (!dalContext.isParamMapEmpty()) {
                DALStatus.addParamMap(dalContext.getParamMap());
            }
            if (dalContext.getDalInfo() != null) {
                DALStatus.setDalInfo(dalContext.getDalInfo());
            }
        }
    }

    public int count(Class<?>[] clazzes, String afterFrom, Object[] values,
                     DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.count(clazzes, afterFrom, values);
    }

    public int count(Class<?>[] clazzes, String afterFrom, List<?> values, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.count(clazzes, afterFrom, values);
    }

    public <T> int count(Class<T> clazz, String afterFrom, Object[] values, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.count(clazz, afterFrom, values);
    }

    public <T> int count2(Class<T> clazz, String afterFrom, List<?> values, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.count2(clazz, afterFrom, values);
    }

    public <T> int countInValues(Class<T> clazz, String afterFrom, String inColumn, Object[] values, Object[] inValues, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.countInValues(clazz, afterFrom, inColumn, values, inValues);
    }

    public <T> int countInValues2(Class<T> clazz, String afterFrom, String inColumn, List<?> values, List<?> inValues, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.countInValues2(clazz, afterFrom, inColumn, values, inValues);
    }

    public <T> List<T> list(Class<T> clazz, String afterFrom, Object[] values, RowMapper<T> rowMapper, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.list(clazz, afterFrom, values, rowMapper);
    }

    public <T> List<T> list2(Class<T> clazz, String afterFrom, List<?> values, RowMapper<T> rowMapper, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.list2(clazz, afterFrom, values, rowMapper);
    }

    public <T> List<T> list(Class<T> clazz, String afterFrom, Object[] values, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.list(clazz, afterFrom, values);
    }

    public <T> List<T> list2(Class<T> clazz, String afterFrom, List<?> values, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.list2(clazz, afterFrom, values);
    }

    public <T> List<T> listInValues(Class<T> clazz, String afterFrom, String inColumn, Object[] values, Object[] inValues, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.listInValues(clazz, afterFrom, inColumn, values, inValues);
    }

    public <T> List<T> listInValues2(Class<T> clazz, String afterFrom, String inColumn, String afterWhere, List<?> values, List<?> inValues, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.listInValues2(clazz, afterFrom, inColumn, afterWhere, values, inValues);
    }

    public <T> List<T> listInValues(Class<T> clazz, String afterFrom, String inColumn, String afterWhere, Object[] values, Object[] inValues, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.listInValues(clazz, afterFrom, inColumn, afterWhere, values, inValues);
    }

    public <T> List<T> listInValues2(Class<T> clazz, String afterFrom, String inColumn, List<?> values, List<?> inValues, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.listInValues2(clazz, afterFrom, inColumn, values, inValues);
    }

    public <E, T> Map<E, T> map(Class<T> clazz, String afterFrom, String inColumn, Object[] values, Object[] inValues, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.map(clazz, afterFrom, inColumn, values, inValues);
    }

    public <E, T> Map<E, T> map2(Class<T> clazz, String afterFrom, String inColumn, List<?> values, List<?> inValues, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.map2(clazz, afterFrom, inColumn, values, inValues);
    }

    public <T> List<T> db2List(Class<?>[] clazzes, String where, String orderBy, int begin, int size, Object[] values, RowMapper<T> rowMapper, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.db2List(clazzes, where, orderBy, begin, size, values, rowMapper);
    }

    public <T> List<T> db2List2(Class<?>[] clazzes, String where, String orderBy, int begin, int size, List<?> values, RowMapper<T> rowMapper, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.db2List2(clazzes, where, orderBy, begin, size, values, rowMapper);
    }

    public <T> List<T> db2List(Class<T> clazz, String where, String orderBy, int begin, int size, Object[] values, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.db2List(clazz, where, orderBy, begin, size, values);
    }

    public <T> List<T> db2List2(Class<T> clazz, String where, String orderBy, int begin, int size, List<?> values, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.db2List2(clazz, where, orderBy, begin, size, values);
    }

    public <T> List<T> db2List(Class<T> clazz, String where, String orderBy, int begin, int size, Object[] values, RowMapper<T> rowMapper, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.db2List(clazz, where, orderBy, begin, size, values, rowMapper);
    }

    public <T> List<T> db2List2(Class<T> clazz, String where, String orderBy, int begin, int size, List<?> values, RowMapper<T> rowMapper, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.db2List2(clazz, where, orderBy, begin, size, values, rowMapper);
    }

    public <T> int delete(Class<T> clazz, String afterFrom, Object[] values, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.delete(clazz, afterFrom, values);
    }

    public <T> int[] batchDelete(Class<T> clazz, String afterFrom, List<Object[]> valuesList, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.batchDelete(clazz, afterFrom, valuesList);
    }

    public <T> int delete2(Class<T> clazz, String afterFrom, List<?> values, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.delete2(clazz, afterFrom, values);
    }

    public <T> int delete(T t, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.delete(t);
    }

    public <T> int deleteById(Class<T> clazz, Object[] idValues, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.deleteById(clazz, idValues);
    }

    public <T> List<T> batchInsert(List<T> list, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.batchInsert(list);
    }

    public <T> void insert(T t, DALContext dalContext) {
        this.processDALContext(dalContext);
        super.insert(t);
    }

    public <T> Number replace(T t, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.replace(t);
    }

    public <T> Number insertIgnore(T t, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.insertIgnore(t);
    }

    public <T> Number insertForNumber(T t, InsertFlag insertFlag, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.insertForNumber(t, insertFlag);
    }

    public <T> Number insertForNumber(T t, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.insertForNumber(t);
    }

    public <T> List<T> mysqlList(Class<?>[] clazzes, String afterFrom, int begin, int size, Object[] values, RowMapper<T> rowMapper, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.mysqlList(clazzes, afterFrom, begin, size, values, rowMapper);
    }

    public <T> List<T> mysqlList2(Class<?>[] clazzes, String afterFrom, int begin, int size, List<?> values, RowMapper<T> rowMapper, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.mysqlList2(clazzes, afterFrom, begin, size, values, rowMapper);
    }

    public <T> List<T> mysqlList(Class<T> clazz, String afterFrom, int begin, int size, Object[] values, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.mysqlList(clazz, afterFrom, begin, size, values);
    }

    public <T> List<T> mysqlList2(Class<T> clazz, String afterFrom, int begin, int size, List<?> values, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.mysqlList2(clazz, afterFrom, begin, size, values);
    }

    public <T> List<T> mysqlList(Class<T> clazz, String afterFrom, int begin, int size, Object[] values, RowMapper<T> rowMapper, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.mysqlList(clazz, afterFrom, begin, size, values, rowMapper);
    }

    public <T> List<T> mysqlList2(Class<T> clazz, String afterFrom, int begin, int size, List<?> values, RowMapper<T> rowMapper, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.mysqlList2(clazz, afterFrom, begin, size, values, rowMapper);
    }

    public <T> T obj(Class<T> clazz, String afterFrom, Object[] values, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.obj(clazz, afterFrom, values);
    }

    public <T> T obj2(Class<T> clazz, String afterFrom, List<?> values, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.obj2(clazz, afterFrom, values);
    }

    public <T> T obj(Class<T> clazz, String afterFrom, Object[] values, RowMapper<T> rowMapper, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.obj(clazz, afterFrom, values, rowMapper);
    }

    public <T> T obj2(Class<T> clazz, String afterFrom, List<?> values, RowMapper<T> rowMapper, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.obj2(clazz, afterFrom, values, rowMapper);
    }

    public <T> T objById(Class<T> clazz, Object idValue, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.objById(clazz, idValue);
    }

    public <T> T objById(Class<T> clazz, Object idValue, boolean forUpdate, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.objById(clazz, idValue, forUpdate);
    }

    public <T> T objByIdForUpdate(Class<T> clazz, Object idValue, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.objByIdForUpdate(clazz, idValue);
    }

    public <T> T objByIds(Class<T> clazz, Object[] idValues, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.objByIds(clazz, idValues);
    }

    public <T> T objByIdsForUpdate(Class<T> clazz, Object[] idValues, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.objByIdsForUpdate(clazz, idValues);
    }

    public <T> T objByIds(Class<T> clazz, Object[] idValues, boolean forUpdate, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.objByIds(clazz, idValues, forUpdate);
    }

    public <T> T objByIds(Class<T> clazz, Object[] idValues, boolean forUpdate, RowMapper<T> rowMapper, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.objByIds(clazz, idValues, forUpdate, rowMapper);
    }

    public <T> int[] batchUpdate(Class<T> clazz, String updateSqlSeg, List<Object[]> valuesList, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.batchUpdate(clazz, updateSqlSeg, valuesList);
    }

    public <T> int update(Class<T> clazz, String updateSqlSeg, Object[] values, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.update(clazz, updateSqlSeg, values);
    }

    public <T> int update2(Class<T> clazz, String updateSqlSeg, List<?> values, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.update2(clazz, updateSqlSeg, values);
    }

    public <T> int update(T t, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.update(t);
    }

    public <T> int update(T t, T snapshot, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.update(t, snapshot);
    }
}