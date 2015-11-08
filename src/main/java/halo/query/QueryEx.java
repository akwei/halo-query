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

    /**
     * 设置分区解析需要的各种参数
     *
     * @param dalContext 分区context
     */
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

    /**
     * select count(*) 查询。查询中的表别名必须与表名相同
     *
     * @param clazzes    查询对象类型数组
     * @param afterFrom  from table 之后的sql,例如select * from table where uid=?
     *                   order name desc, afterFrom为where uid=? order name desc
     * @param values     参数化查询值
     * @param dalContext 分区context
     * @return sql统计数字
     */
    public int count(Class<?>[] clazzes, String afterFrom, Object[] values,
                     DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.count(clazzes, afterFrom, values);
    }

    /**
     * @param clazzes    查询对象类型数组
     * @param afterFrom  from table 之后的sql,例如select * from table where uid=?
     *                   order name desc, afterFrom为where uid=? order name desc
     * @param values     参数化查询值集合
     * @param dalContext 分区context
     * @return 查询数量
     */
    public int count(Class<?>[] clazzes, String afterFrom, List<?> values, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.count(clazzes, afterFrom, values);
    }

    /**
     * select count(*) 查询
     *
     * @param clazz      查询对象类型
     * @param afterFrom  from table 之后的sql,例如select * from table where uid=?
     *                   order name desc, afterFrom为where uid=? order name desc
     * @param values     参数化查询值
     * @param dalContext 分区context
     * @return 查询数量
     */
    public <T> int count(Class<T> clazz, String afterFrom, Object[] values, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.count(clazz, afterFrom, values);
    }

    /**
     * select count(*) 查询
     *
     * @param clazz      查询对象类型
     * @param afterFrom  from table 之后的sql,例如select * from table where uid=?
     *                   order name desc, afterFrom为where uid=? order name desc
     * @param values     参数化查询值集合
     * @param dalContext 分区context
     * @return 查询数量
     */
    public <T> int count2(Class<T> clazz, String afterFrom, List<?> values, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.count2(clazz, afterFrom, values);
    }

    /**
     * 对sql中有 in (?,?)的count封装，目前只支持 单个in
     *
     * @param clazz      操作的类
     * @param afterFrom  from之后的sql，例如 where col=?,但是不包括 inColumn
     * @param inColumn   进行in sql操作的列
     * @param values     ?替换符对应的参数，不包括inColumn的参数
     * @param inValues   inColumn对应的参数
     * @param dalContext 分区context
     * @param <T>        集合中对象泛型
     * @return 查询数量
     */
    public <T> int countInValues(Class<T> clazz, String afterFrom, String inColumn, Object[] values, Object[] inValues, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.countInValues(clazz, afterFrom, inColumn, values, inValues);
    }

    /**
     * 对sql中有 in (?,?)的count封装，目前只支持 单个in
     *
     * @param clazz     操作的类
     * @param afterFrom from之后的sql，例如 where col=?,但是不包括 inColumn
     * @param inColumn  进行in sql操作的列
     * @param values    ?替换符对应的参数，不包括inColumn的参数
     * @param <T>       集合中对象泛型
     * @param inValues  inColumn对应的参数
     * @return 查询数量
     */
    public <T> int countInValues2(Class<T> clazz, String afterFrom, String inColumn, List<?> values, List<?> inValues, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.countInValues2(clazz, afterFrom, inColumn, values, inValues);
    }

    /**
     * sql select
     *
     * @param clazz      查询结果类型
     * @param afterFrom  from之后的sql，例如 where col=? order by uid desc,
     * @param values     参数化查询值
     * @param rowMapper  spring {@link RowMapper} 对象
     * @param dalContext 分区context
     * @param <T>        泛型
     * @return 查询结果 T 类型的集合
     */
    public <T> List<T> list(Class<T> clazz, String afterFrom, Object[] values, RowMapper<T> rowMapper, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.list(clazz, afterFrom, values, rowMapper);
    }

    /**
     * sql select
     *
     * @param clazz      查询结果类型
     * @param afterFrom  from之后的sql，例如 where col=? order by uid desc,
     * @param values     参数化查询值集合
     * @param rowMapper  spring {@link RowMapper} 对象
     * @param dalContext 分区context
     * @param <T>        泛型
     * @return 查询结果 T 类型的集合
     */
    public <T> List<T> list2(Class<T> clazz, String afterFrom, List<?> values, RowMapper<T> rowMapper, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.list2(clazz, afterFrom, values, rowMapper);
    }

    /**
     * sql select
     *
     * @param clazz      查询结果类型
     * @param afterFrom  from之后的sql，例如 where col=? order by uid desc,
     * @param values     参数化查询值
     * @param dalContext 分区context
     * @param <T>        泛型
     * @return 查询结果 T 类型的集合
     */
    public <T> List<T> list(Class<T> clazz, String afterFrom, Object[] values, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.list(clazz, afterFrom, values);
    }

    /**
     * sql select
     *
     * @param clazz      查询结果类型
     * @param afterFrom  from之后的sql，例如 where col=? order by uid desc,
     * @param values     参数化查询值集合
     * @param dalContext 分区context
     * @param <T>        泛型
     * @return 查询结果 T 类型的集合
     */
    public <T> List<T> list2(Class<T> clazz, String afterFrom, List<?> values, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.list2(clazz, afterFrom, values);
    }

    /**
     * 使用 column in (?,?)的方式来获得集合数据
     *
     * @param clazz      操作的类
     * @param afterFrom  from之后的sql，例如 where col=?,但是不包括 inColumn
     * @param inColumn   进行in sql操作的列
     * @param values     ?替换符对应的参数，不包括inColumn的参数
     * @param inValues   inColumn对应的参数
     * @param dalContext 分区context
     * @param <T>        集合中对象泛型
     * @return 查询结果 T 类型的集合
     */
    public <T> List<T> listInValues(Class<T> clazz, String afterFrom, String inColumn, Object[] values, Object[] inValues, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.listInValues(clazz, afterFrom, inColumn, values, inValues);
    }

    public <T> List<T> listInValues2(Class<T> clazz, String afterFrom, String inColumn, String afterWhere, List<?> values, List<?> inValues, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.listInValues2(clazz, afterFrom, inColumn, afterWhere, values, inValues);
    }

    /**
     * 使用 column in (?,?)的方式来获得集合数据
     *
     * @param clazz      操作的类
     * @param afterFrom  from之后的sql，例如 where col=?,但是不包括 inColumn
     * @param inColumn   进行in sql操作的列
     * @param afterWhere 条件语句之后的例如 order by, group by 等语句
     * @param values     ?替换符对应的参数，不包括inColumn的参数
     * @param inValues   inColumn对应的参数
     * @param dalContext 分区context
     * @param <T>        集合中对象泛型
     * @return 查询结果 T 类型的集合
     */
    public <T> List<T> listInValues(Class<T> clazz, String afterFrom, String inColumn, String afterWhere, Object[] values, Object[] inValues, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.listInValues(clazz, afterFrom, inColumn, afterWhere, values, inValues);
    }

    /**
     * 使用 column in (?,?)的方式来获得集合数据
     *
     * @param clazz      操作的类
     * @param afterFrom  from之后的sql，例如 where col=?,但是不包括 inColumn
     * @param inColumn   进行in sql操作的列
     * @param values     ?替换符对应的参数，不包括inColumn的参数
     * @param inValues   inColumn对应的参数
     * @param dalContext 分区context
     * @param <T>        集合中对象泛型
     * @return 查询结果 T 类型的集合
     */
    public <T> List<T> listInValues2(Class<T> clazz, String afterFrom, String inColumn, List<?> values, List<?> inValues, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.listInValues2(clazz, afterFrom, inColumn, values, inValues);
    }

    /**
     * @param clazz      操作的类
     * @param afterFrom  from之后的sql，例如 where col=?,但是不包括 inColumn
     * @param inColumn   进行in sql操作的列
     * @param values     ?替换符对应的参数，不包括inColumn的参数
     * @param inValues   inColumn对应的参数
     * @param dalContext 分区context
     * @param <E>        map中key的类型
     * @param <T>        集合中对象泛型
     * @return map对象
     */
    public <E, T> Map<E, T> map(Class<T> clazz, String afterFrom, String inColumn, Object[] values, Object[] inValues, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.map(clazz, afterFrom, inColumn, values, inValues);
    }

    /**
     * @param clazz      操作的类
     * @param afterFrom  from之后的sql，例如 where col=?,但是不包括 inColumn
     * @param inColumn   进行in sql操作的列
     * @param values     ?替换符对应的参数，不包括inColumn的参数
     * @param inValues   incolumn对应的参数
     * @param dalContext 分区context
     * @param <E>        map中key的类型
     * @param <T>        集合中对象泛型
     * @return map对象
     */
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

    /**
     * delete sql.根据条件删除.例如: delete table where field0=? and ....
     *
     * @param clazz      要删除的对象类型
     * @param afterFrom  delete table 之后的语句,例如:delete table where field0=?,afterFrom为where field0=?
     * @param values     参数化查询值
     * @param dalContext 分区context
     * @return 删除的记录数
     */
    public <T> int delete(Class<T> clazz, String afterFrom, Object[] values, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.delete(clazz, afterFrom, values);
    }

    /**
     * 批量删除
     *
     * @param clazz      要删除的对象
     * @param afterFrom  delete table 之后的语句,例如:delete table where field0=?,afterFrom为where field0=?
     * @param valuesList 批量操作的参数集合
     * @param dalContext 分区context
     * @param <T>        类泛型
     * @return delete result
     */
    public <T> int[] batchDelete(Class<T> clazz, String afterFrom, List<Object[]> valuesList, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.batchDelete(clazz, afterFrom, valuesList);
    }

    /**
     * 删除
     *
     * @param clazz      要删除的对象
     * @param afterFrom  delete table 之后的语句,例如:delete table where field0=?,afterFrom为where field0=?
     * @param values     参数
     * @param dalContext 分区context
     * @param <T>        类泛型
     * @return delete result
     */
    public <T> int delete2(Class<T> clazz, String afterFrom, List<?> values, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.delete2(clazz, afterFrom, values);
    }

    /**
     * delete sql,返回删除的记录数量
     *
     * @param t          要删除的对象，必须有id
     * @param dalContext 分区context
     * @return 删除的记录数
     */
    public <T> int delete(T t, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.delete(t);
    }

    /**
     * delete sql,根据id删除。返回删除的记录数量
     *
     * @param clazz      要删除的对象的类型
     * @param idValues   主键id值
     * @param dalContext 分区context
     * @return sql操作失败的异常
     */
    public <T> int deleteById(Class<T> clazz, Object[] idValues, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.deleteById(clazz, idValues);
    }

    /**
     * 批量insert,对于使用数据库自增id方式，不会返回自增id，请使用应用自行获得自增id
     *
     * @param list       批量床架的对象
     * @param dalContext 分区context
     * @param <T>        对象类型
     * @return 返回自增id，如果id不是自增，就返回值为0的集合
     */
    public <T> List<T> batchInsert(List<T> list, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.batchInsert(list);
    }

    /**
     * insert sql
     *
     * @param t          insert的对象
     * @param dalContext 分区context
     */
    public <T> void insert(T t, DALContext dalContext) {
        this.processDALContext(dalContext);
        super.insert(t);
    }

    /**
     * replace into sql,此操作，如果是更新数据，将不会返回自增id
     *
     * @param t          数据对象
     * @param dalContext 分区context
     * @param <T>        泛型
     * @return 当进行replace操作时，返回0
     */
    public <T> Number replace(T t, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.replace(t);
    }

    /**
     * insert ignore into sql
     *
     * @param t          数据对象
     * @param dalContext 分区context
     * @param <T>        泛型
     * @return 当插入不成功时，返回0
     */
    public <T> Number insertIgnore(T t, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.insertIgnore(t);
    }

    /**
     * @param t          数据对象
     * @param insertFlag 操作标识
     * @param dalContext 分区context
     * @param <T>        泛型
     * @return 返回自增id，如果没有自增id，返回0
     */
    public <T> Number insertForNumber(T t, InsertFlag insertFlag, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.insertForNumber(t, insertFlag);
    }

    /**
     * insert sql,返回自增数字id，联合主键的表，返回0. 如果表没有主键，直接insert,返回0
     *
     * @param t          insert的对象
     * @param dalContext 分区context
     * @return insert之后的自增数字
     */
    public <T> Number insertForNumber(T t, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.insertForNumber(t);
    }

    /**
     * mysql的分页查询。查询中的表别名必须与表名相同
     *
     * @param clazzes    查询对象类型数组
     * @param afterFrom  from table 之后的sql,例如select * from table where uid=?
     *                   order name desc, afterFrom为where uid=? order name
     * @param begin      开始位置
     * @param size       查询数量
     * @param values     参数化查询值
     * @param rowMapper  spring RowMapper
     * @param dalContext 分区context
     * @return 查询结果 T 类型的集合
     */
    public <T> List<T> mysqlList(Class<?>[] clazzes, String afterFrom, int begin, int size, Object[] values, RowMapper<T> rowMapper, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.mysqlList(clazzes, afterFrom, begin, size, values, rowMapper);
    }

    /**
     * mysql的分页查询。查询中的表别名必须与表名相同
     *
     * @param clazzes    查询对象类型数组
     * @param afterFrom  from table 之后的sql,例如select * from table where uid=?
     *                   order name desc, afterFrom为where uid=? order name
     * @param begin      开始位置
     * @param size       查询数量
     * @param values     参数化查询值集合
     * @param rowMapper  spring RowMapper
     * @param dalContext 分区context
     * @return 查询结果 T 类型的集合
     */
    public <T> List<T> mysqlList2(Class<?>[] clazzes, String afterFrom, int begin, int size, List<?> values, RowMapper<T> rowMapper, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.mysqlList2(clazzes, afterFrom, begin, size, values, rowMapper);
    }

    /**
     * mysql的分页查询。
     *
     * @param clazz      查询对象类型
     * @param afterFrom  from table 之后的sql,例如select * from table where uid=?
     *                   order name desc, afterFrom为where uid=? order name
     * @param begin      开始位置
     * @param size       查询数量
     * @param values     参数化查询值
     * @param dalContext 分区context
     * @return 查询结果 T 类型的集合
     */
    public <T> List<T> mysqlList(Class<T> clazz, String afterFrom, int begin, int size, Object[] values, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.mysqlList(clazz, afterFrom, begin, size, values);
    }

    /**
     * mysql的分页查询。
     *
     * @param clazz      查询对象类型
     * @param afterFrom  from table 之后的sql,例如select * from table where uid=?
     *                   order name desc, afterFrom为where uid=? order name
     * @param begin      开始位置
     * @param size       查询数量集合
     * @param values     参数化查询值
     * @param dalContext 分区context
     * @return 查询结果 T 类型的集合
     */
    public <T> List<T> mysqlList2(Class<T> clazz, String afterFrom, int begin, int size, List<?> values, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.mysqlList2(clazz, afterFrom, begin, size, values);
    }

    /**
     * mysql的分页查询。
     *
     * @param clazz      查询对象类型
     * @param afterFrom  from table 之后的sql,例如select * from table where uid=?
     *                   order name desc, afterFrom为where uid=? order name
     * @param begin      开始位置
     * @param size       查询数量
     * @param values     参数化查询值
     * @param rowMapper  spring RowMapper
     * @param dalContext 分区context
     * @return 查询结果 T 类型的集合
     */
    public <T> List<T> mysqlList(Class<T> clazz, String afterFrom, int begin, int size, Object[] values, RowMapper<T> rowMapper, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.mysqlList(clazz, afterFrom, begin, size, values, rowMapper);
    }

    /**
     * mysql的分页查询。
     *
     * @param clazz      查询对象类型
     * @param afterFrom  from table 之后的sql,例如select * from table where uid=?
     *                   order name desc, afterFrom为where uid=? order name
     * @param begin      开始位置
     * @param size       查询数量
     * @param values     参数化查询值集合
     * @param rowMapper  spring RowMapper
     * @param dalContext 分区context
     * @return 查询结果 T 类型的集合
     */
    public <T> List<T> mysqlList2(Class<T> clazz, String afterFrom, int begin, int size, List<?> values, RowMapper<T> rowMapper, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.mysqlList2(clazz, afterFrom, begin, size, values, rowMapper);
    }

    /**
     * select sql 返回对象
     *
     * @param clazz      查询对象类型
     * @param afterFrom  from table 之后的sql,例如select * from table where uid=?
     *                   order name desc, afterFrom为where uid=? order name desc
     * @param values     参数化查询值
     * @param dalContext 分区context
     * @return 查询 T 类型对象，null表示没有搜索结果
     */
    public <T> T obj(Class<T> clazz, String afterFrom, Object[] values, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.obj(clazz, afterFrom, values);
    }

    /**
     * select sql 返回对象
     *
     * @param clazz      查询对象类型
     * @param afterFrom  from table 之后的sql,例如select * from table where uid=?
     *                   order name desc, afterFrom为where uid=? order name desc
     * @param values     参数化查询值集合
     * @param dalContext 分区context
     * @return 查询 T 类型对象，null表示没有搜索结果
     */
    public <T> T obj2(Class<T> clazz, String afterFrom, List<?> values, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.obj2(clazz, afterFrom, values);
    }

    /**
     * select sql 返回对象
     *
     * @param clazz      查询对象类型
     * @param afterFrom  from table 之后的sql,例如select * from table where uid=?
     *                   order name desc, afterFrom为where uid=? order name desc
     * @param values     参数化查询值
     * @param rowMapper  spring RowMapper
     * @param dalContext 分区context
     * @return 查询 T 类型对象，null表示没有搜索结果
     */
    public <T> T obj(Class<T> clazz, String afterFrom, Object[] values, RowMapper<T> rowMapper, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.obj(clazz, afterFrom, values, rowMapper);
    }

    /**
     * select sql 返回对象
     *
     * @param clazz      查询对象类型
     * @param afterFrom  from table 之后的sql,例如select * from table where uid=?
     *                   order name desc, afterFrom为where uid=? order name desc
     * @param values     参数化查询值集合
     * @param rowMapper  spring RowMapper
     * @param dalContext 分区context
     * @return 查询 T 类型对象，null表示没有搜索结果
     */
    public <T> T obj2(Class<T> clazz, String afterFrom, List<?> values, RowMapper<T> rowMapper, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.obj2(clazz, afterFrom, values, rowMapper);
    }

    /**
     * select sql 根据id查询，返回对象
     *
     * @param clazz      查询对象类型
     * @param idValue    id参数
     * @param dalContext 分区context
     * @return 查询 T 类型对象，null表示没有搜索结果
     */
    public <T> T objById(Class<T> clazz, Object idValue, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.objById(clazz, idValue);
    }

    /**
     * select sql 根据id查询,并且实现sql select ... for update 功能
     *
     * @param clazz      查询对象类型
     * @param idValue    id参数
     * @param forUpdate  是否锁数据
     * @param dalContext 分区context
     * @param <T>        对象泛型
     * @return 查询 T 类型对象，null表示没有搜索结果
     */
    public <T> T objById(Class<T> clazz, Object idValue, boolean forUpdate, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.objById(clazz, idValue, forUpdate);
    }

    /**
     * select 根据id查询对象，并使用for update 锁定该行数据
     *
     * @param clazz      查询对象类型
     * @param idValue    id参数
     * @param dalContext 分区context
     * @param <T>        对象泛型
     * @return 查询 T 类型对象，null表示没有搜索结果
     */
    public <T> T objByIdForUpdate(Class<T> clazz, Object idValue, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.objByIdForUpdate(clazz, idValue);
    }

    /**
     * select sql 根据id查询，返回对象
     *
     * @param clazz      查询对象类型
     * @param idValues   id参数
     * @param dalContext 分区context
     * @return 查询 T 类型对象，null表示没有搜索结果
     */
    public <T> T objByIds(Class<T> clazz, Object[] idValues, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.objByIds(clazz, idValues);
    }

    /**
     * select 根据id查询对象,并使用for update 锁定该行数据
     *
     * @param clazz      查询对象类型
     * @param idValues   id参数
     * @param dalContext 分区context
     * @param <T>        对象泛型
     * @return 查询 T 类型对象，null表示没有搜索结果
     */
    public <T> T objByIdsForUpdate(Class<T> clazz, Object[] idValues, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.objByIdsForUpdate(clazz, idValues);
    }

    /**
     * select 根据id查询对象,并使用for update 锁定该行数据
     *
     * @param clazz      查询对象类型
     * @param idValues   id参数
     * @param forUpdate  是否锁对象
     * @param dalContext 分区context
     * @param <T>        对象泛型
     * @return 查询 T 类型对象，null表示没有搜索结果
     */
    public <T> T objByIds(Class<T> clazz, Object[] idValues, boolean forUpdate, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.objByIds(clazz, idValues, forUpdate);
    }

    /**
     * select sql 根据id查询，返回对象
     *
     * @param clazz      查询对象类型
     * @param idValues   id参数
     * @param forUpdate  是否使用sql for update进行锁数据
     * @param rowMapper  spring RowMapper
     * @param dalContext 分区context
     * @return 查询 T 类型对象，null表示没有搜索结果
     */
    public <T> T objByIds(Class<T> clazz, Object[] idValues, boolean forUpdate, RowMapper<T> rowMapper, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.objByIds(clazz, idValues, forUpdate, rowMapper);
    }

    /**
     * 批量更新
     *
     * @param clazz        更新的类型
     * @param updateSqlSeg sql片段,为update table 之后的sql。例如：set field0=?,field1=? where field3=?
     * @param valuesList   批量操作的参数集合
     * @param dalContext   分区context
     * @param <T>          类泛型
     * @return update result
     */
    public <T> int[] batchUpdate(Class<T> clazz, String updateSqlSeg, List<Object[]> valuesList, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.batchUpdate(clazz, updateSqlSeg, valuesList);
    }

    /**
     * update sql，返回更新的记录数量。只更新选中的字段 例如: update table set field0=?,field1=?
     * where field3=?
     *
     * @param clazz        需要更新的类
     * @param updateSqlSeg sql片段,为update table 之后的sql。例如：set field0=?,field1=? where field3=?
     * @param values       参数化查询值
     * @param dalContext   分区context
     * @return 更新数量
     */
    public <T> int update(Class<T> clazz, String updateSqlSeg, Object[] values, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.update(clazz, updateSqlSeg, values);
    }

    /**
     * update sql，返回更新的记录数量。只更新选中的字段 例如: update table set field0=?,field1=?
     *
     * @param clazz        需要更新的类
     * @param updateSqlSeg sql片段,为update table 之后的sql。例如：set field0=?,field1=? where field3=?
     * @param values       参数化查询值
     * @param dalContext   分区context
     * @param <T>          类泛型
     * @return 更新数量
     */
    public <T> int update2(Class<T> clazz, String updateSqlSeg, List<?> values, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.update2(clazz, updateSqlSeg, values);
    }

    /**
     * update sql ,返回更新的记录数量
     *
     * @param t          update的对象
     * @param dalContext 分区context
     * @return 更新数量
     */
    public <T> int update(T t, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.update(t);
    }

    /**
     * 对实体进行update操作，更新是比较快照与当前实体的值，如果当前实体的值发生变化，才进行更新。
     *
     * @param t          要更新的对象
     * @param snapshot   可为空，如果为空就执行 update(T t)
     * @param dalContext 分区context
     * @param <T>        泛型
     * @return update result
     */
    public <T> int update(T t, T snapshot, DALContext dalContext) {
        this.processDALContext(dalContext);
        return super.update(t, snapshot);
    }
}