package halo.query;

import halo.query.dal.DALInfo;
import halo.query.dal.DALParser;
import halo.query.dal.DALParserUtil;
import halo.query.dal.DALStatus;
import halo.query.idtool.DefIdGeneratorImpl;
import halo.query.idtool.IdGenerator;
import halo.query.mapping.EntityTableInfo;
import halo.query.mapping.EntityTableInfoFactory;
import halo.query.mapping.SQLMapper;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Query {

    private static Query instance;

    protected JdbcSupport jdbcSupport;

    protected IdGenerator idGenerator = new DefIdGeneratorImpl();

    public Query() {
        instance = this;
    }

    public static Query getInstance() {
        if (instance == null) {
            throw new RuntimeException("must create " + Query.class.getName());
        }
        return instance;
    }

    public void setIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    /**
     * select count(*) 查询。查询中的表别名必须与表名相同
     *
     * @param clazzes   查询对象类型数组
     * @param afterFrom from table 之后的sql,例如select * from table where uid=?
     *                  order name desc, afterFrom为where uid=? order name desc
     * @param values    参数化查询值
     * @return sql统计数字
     */
    public int count(Class<?>[] clazzes, String afterFrom, Object[] values) {
        StringBuilder sb = new StringBuilder("select count(*) from ");
        for (Class<?> clazz : clazzes) {
            this.addTableNameAndSetDsKey(sb, clazz, true, true);
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(" ");
        sb.append(afterFrom);
        return jdbcSupport.num(sb.toString(), values).intValue();
    }

    public int count(Class<?>[] clazzes, String afterFrom, List<?> values) {
        return this.count(clazzes, afterFrom, buildArgs(values));
    }


    private <T> void addTableNameAndSetDsKey(StringBuilder sb, Class<T> clazz, boolean addTableAlias, boolean addComma) {
        EntityTableInfo<T> info = getEntityTableInfo(clazz);
        DALInfo dalInfo = Query.process(clazz, info.getDalParser());
        if (dalInfo == null) {
            sb.append(info.getTableName());
        } else {
            String realTableName = dalInfo.getRealTable(clazz);
            if (realTableName == null) {
                sb.append(info.getTableName());
            } else {
                sb.append(realTableName);
            }

        }
        if (addTableAlias) {
            sb.append(" as ");
            sb.append(info.getTableAlias());
        }
        if (addComma) {
            sb.append(",");
        }
    }

    public static <T> String getTableNameAndSetDsKey(Class<T> clazz) {
        EntityTableInfo<T> info = getEntityTableInfo(clazz);
        DALInfo dalInfo = Query.process(clazz, info.getDalParser());
        if (dalInfo == null) {
            return info.getTableName();
        } else {
            String realTableName = dalInfo.getRealTable(clazz);
            if (realTableName == null) {
                return info.getTableName();
            }
            return realTableName;
        }
    }

    /**
     * select count(*) 查询
     *
     * @param clazz     查询对象类型
     * @param afterFrom from table 之后的sql,例如select * from table where uid=?
     *                  order name desc, afterFrom为where uid=? order name desc
     * @param values    参数化查询值
     * @return sql统计数字
     */
    public <T> int count(Class<T> clazz, String afterFrom, Object[] values) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) from ");
        this.addTableNameAndSetDsKey(sql, clazz, true, false);
        sql.append(" ");
        if (afterFrom != null) {
            sql.append(afterFrom);
        }
        return jdbcSupport.num(sql.toString(), values).intValue();
    }

    public <T> int count2(Class<T> clazz, String afterFrom, List<?> values) {
        return this.count(clazz, afterFrom, buildArgs(values));
    }

    /**
     * 对sql中有 in (?,?)的count封装，目前只支持 单个in
     *
     * @param clazz     操作的类
     * @param afterFrom from之后的sql，例如 where col=?,但是不包括 inColumn
     * @param inColumn  进行in sql操作的列
     * @param values    ?替换符对应的参数，不包括inColumn的参数
     * @param inValues  inColumn对应的参数
     * @param <T>       集合中对象泛型
     * @return
     */
    public <T> int countInValues(Class<T> clazz, String afterFrom, String inColumn, Object[] values, Object[] inValues) {
        if (inValues == null || inValues.length == 0) {
            return 0;
        }
        List<Object> paramlist = new ArrayList<Object>();
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                paramlist.add(values[i]);
            }
        }
        for (int i = 0; i < inValues.length; i++) {
            paramlist.add(inValues[i]);
        }

        String _where;
        if (afterFrom == null) {
            _where = "where ";
        } else {
            _where = afterFrom + " and ";
        }
        return count(clazz, _where + createInSql(inColumn, inValues.length), buildArgs(paramlist));
    }

    /**
     * 对sql中有 in (?,?)的count封装，目前只支持 单个in
     *
     * @param clazz     操作的类
     * @param afterFrom from之后的sql，例如 where col=?,但是不包括 inColumn
     * @param inColumn  进行in sql操作的列
     * @param values    ?替换符对应的参数，不包括inColumn的参数
     * @param inValues  inColumn对应的参数
     * @param <T>       集合中对象泛型
     * @return
     */
    public <T> int countInValues2(Class<T> clazz, String afterFrom, String inColumn, List<?> values, List<?> inValues) {
        return countInValues(clazz, afterFrom, inColumn, buildArgs(values), buildArgs(inValues));
    }

    public <T> List<T> list(Class<T> clazz, String afterFrom, Object[] values, RowMapper<T> rowMapper) {
        EntityTableInfo<T> info = getEntityTableInfo(clazz);
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        sql.append(info.getSelectedFieldSQL());
        sql.append(" from ");
        this.addTableNameAndSetDsKey(sql, clazz, true, false);
        sql.append(" ");
        if (afterFrom != null) {
            sql.append(afterFrom);
        }
        return jdbcSupport.list(sql.toString(), values, rowMapper);
    }

    public <T> List<T> list2(Class<T> clazz, String afterFrom,
                             List<?> values, RowMapper<T> rowMapper) {
        return this.list(clazz, afterFrom, buildArgs(values), rowMapper);
    }

    public <T> List<T> list(Class<T> clazz, String afterFrom, Object[] values) {
        return this.list(clazz, afterFrom, values, getRowMapper(clazz));
    }

    public <T> List<T> list2(Class<T> clazz, String afterFrom, List<?> values) {
        return this.list(clazz, afterFrom, buildArgs(values));
    }

    /**
     * 使用 column in (?,?)的方式来获得集合数据
     *
     * @param clazz     操作的类
     * @param afterFrom from之后的sql，例如 where col=?,但是不包括 inColumn
     * @param inColumn  进行in sql操作的列
     * @param values    ?替换符对应的参数，不包括inColumn的参数
     * @param inValues  inColumn对应的参数
     * @param <T>       集合中对象泛型
     * @return 查询集合
     */
    public <T> List<T> listInValues(Class<T> clazz, String afterFrom,
                                    String inColumn, Object[] values, Object[] inValues) {
        return this.listInValues(clazz, afterFrom, inColumn, null, values, inValues);
    }

    public <T> List<T> listInValues2(Class<T> clazz, String afterFrom, String inColumn, String afterWhere, List<?> values, List<?> inValues) {
        return this.listInValues(clazz, afterFrom, inColumn, afterWhere, buildArgs(values), buildArgs(inValues));
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
     * @param <T>        集合中对象泛型
     * @return 查询集合
     */
    public <T> List<T> listInValues(Class<T> clazz, String afterFrom, String inColumn, String afterWhere, Object[] values, Object[] inValues) {
        if (inValues == null || inValues.length == 0) {
            DALStatus.remove();
            return new ArrayList<T>(0);
        }
        List<Object> paramlist = new ArrayList<Object>();
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                paramlist.add(values[i]);
            }
        }
        for (int i = 0; i < inValues.length; i++) {
            paramlist.add(inValues[i]);
        }
        StringBuilder sb = new StringBuilder();
        if (afterFrom == null) {
            sb.append("where ");
        } else {
            sb.append(afterFrom).append(" and ");
        }
        sb.append(createInSql(inColumn, inValues.length));
        if (afterWhere != null) {
            sb.append(" ").append(afterWhere);
        }
        return list(clazz, sb.toString(), buildArgs(paramlist));
    }

    /**
     * 使用 column in (?,?)的方式来获得集合数据
     *
     * @param clazz     操作的类
     * @param afterFrom from之后的sql，例如 where col=?,但是不包括 inColumn
     * @param inColumn  进行in sql操作的列
     * @param values    ?替换符对应的参数，不包括inColumn的参数
     * @param inValues  inColumn对应的参数
     * @param <T>       集合中对象泛型
     * @return
     */
    public <T> List<T> listInValues2(Class<T> clazz, String afterFrom,
                                     String inColumn, List<?> values, List<?> inValues) {
        return listInValues(clazz, afterFrom, inColumn, buildArgs(values), buildArgs(inValues));
    }

    /**
     * @param clazz     操作的类
     * @param afterFrom from之后的sql，例如 where col=?,但是不包括 inColumn
     * @param inColumn  进行in sql操作的列
     * @param values    ?替换符对应的参数，不包括inColumn的参数
     * @param inValues  inColumn对应的参数
     * @param <E>       map中key的类型
     * @param <T>       集合中对象泛型
     * @return map对象
     */
    public <E, T> Map<E, T> map(Class<T> clazz, String afterFrom, String inColumn, Object[] values, Object[]
            inValues) {
        Map<E, T> map = new HashMap<E, T>(0);
        if (inValues == null || inValues.length == 0) {
            DALStatus.remove();
            return map;
        }
        List<T> list = listInValues(clazz, afterFrom, inColumn, values, inValues);
        EntityTableInfo<T> entityTableInfo = getEntityTableInfo(clazz);
        Field field = entityTableInfo.getField(inColumn);
        for (T t : list) {
            map.put((E) entityTableInfo.getFieldValue(t, field), t);
        }
        return map;
    }

    /**
     * @param clazz     操作的类
     * @param afterFrom from之后的sql，例如 where col=?,但是不包括 inColumn
     * @param inColumn  进行in sql操作的列
     * @param values    ?替换符对应的参数，不包括inColumn的参数
     * @param inValues  incolumn对应的参数
     * @param <E>       map中key的类型
     * @param <T>       集合中对象泛型
     * @return map对象
     */
    public <E, T> Map<E, T> map2(Class<T> clazz, String afterFrom,
                                 String inColumn, List<?> values, List<?> inValues) {
        return map(clazz, afterFrom, inColumn, buildArgs(values), buildArgs(inValues));
    }

    public static String createInSql(String column, int argCount) {
        if (argCount <= 0) {
            throw new IllegalArgumentException("argCount must be > 0");
        }
        StringBuilder sb = new StringBuilder();
        sb.append(column).append(" in(");
        for (int i = 0; i < argCount; i++) {
            sb.append("?,");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        return sb.toString();
    }

    /**
     * db2 sql inner join 分页查询。表的别名与表名相同
     *
     * @param clazzes   需要inner join的类
     * @param where     条件表达式
     * @param orderBy   排序表达式
     * @param begin     分页开始位置
     * @param size      分页获取数量
     * @param values    参数话查询的值
     * @param rowMapper spring {@link RowMapper} 对象
     * @return 查询集合
     */
    public <T> List<T> db2List(Class<?>[] clazzes, String where, String orderBy, int begin, int size, Object[] values, RowMapper<T> rowMapper) {
        EntityTableInfo<T> info;
        StringBuilder sql = new StringBuilder();
        sql.append("select * from ( select ");
        for (Class<?> clazz : clazzes) {
            info = getEntityTableInfo(clazz);
            sql.append(info.getSelectedFieldSQL());
            sql.append(",");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" ,rownumber() over (");
        if (orderBy != null) {
            sql.append(orderBy);
        }
        sql.append(") as rowid from ");
        for (Class<?> clazz : clazzes) {
            addTableNameAndSetDsKey(sql, clazz, true, true);
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" ");
        if (where != null) {
            sql.append(where);
        }
        sql.append(") temp where temp.rowid >= ");
        sql.append(begin + 1);
        sql.append(" and temp.rowid <= ");
        sql.append(begin + size);
        return jdbcSupport.list(sql.toString(), values, rowMapper);
    }

    public <T> List<T> db2List2(Class<?>[] clazzes, String where,
                                String orderBy, int begin, int size, List<?> values, RowMapper<T> rowMapper) {
        return this.db2List(clazzes, where, orderBy, begin, size, buildArgs(values), rowMapper);
    }

    /**
     * db2 sql分页查询。返回集合类型为clazz
     *
     * @param clazz   需要查询的类
     * @param where   where表达式
     * @param orderBy order by表达式
     * @param begin   分页开始位置
     * @param size    分页获取数量
     * @param values  参数化查询值
     * @return 查询集合
     */
    public <T> List<T> db2List(Class<T> clazz, String where, String orderBy, int begin, int size, Object[] values) {
        return this.db2List(clazz, where, orderBy, begin, size, values, getRowMapper(clazz));
    }

    public <T> List<T> db2List2(Class<T> clazz, String where, String orderBy,
                                int begin, int size, List<?> values) {
        return this.db2List(clazz, where, orderBy, begin, size, buildArgs(values));
    }

    /**
     * db2 sql分页查询。返回集合类型为clazz
     *
     * @param clazz     需要查询的类
     * @param where     where表达式
     * @param orderBy   order by表达式
     * @param begin     分页开始位置
     * @param size      分页获取数量
     * @param values    参数化查询值
     * @param rowMapper spring {@link RowMapper}
     * @return 查询集合
     */
    public <T> List<T> db2List(Class<T> clazz, String where, String orderBy, int begin, int size, Object[] values, RowMapper<T> rowMapper) {
        EntityTableInfo<T> info = getEntityTableInfo(clazz);
        StringBuilder sql = new StringBuilder();
        sql.append("select * from ( select ");
        sql.append(info.getSelectedFieldSQL());
        sql.append(" ,rownumber() over (");
        if (orderBy != null) {
            sql.append(orderBy);
        }
        sql.append(") as rowid from ");
        addTableNameAndSetDsKey(sql, clazz, true, false);
        sql.append(" ");
        if (where != null) {
            sql.append(where);
        }
        sql.append(") temp where temp.rowid >= ");
        sql.append(begin + 1);
        sql.append(" and temp.rowid <= ");
        sql.append(begin + size);
        return jdbcSupport.list(sql.toString(), values, rowMapper);
    }

    public <T> List<T> db2List2(Class<T> clazz, String where, String orderBy,
                                int begin, int size, List<?> values, RowMapper<T> rowMapper) {
        return this.db2List(clazz, where, orderBy, begin, size, buildArgs(values), rowMapper);
    }

    /**
     * delete sql.根据条件删除.例如: delete table where field0=? and ....
     *
     * @param clazz     要删除的对象类型
     * @param afterFrom delete table 之后的语句,例如:delete table where field0=?,afterFrom为where field0=?
     * @param values    参数化查询值
     * @return 删除的记录数
     */
    public <T> int delete(Class<T> clazz, String afterFrom, Object[] values) {
        StringBuilder sql = new StringBuilder();
        sql.append("delete from ");
        addTableNameAndSetDsKey(sql, clazz, false, false);
        sql.append(" ");
        if (afterFrom != null) {
            sql.append(afterFrom);
        }
        return this.jdbcSupport.update(sql.toString(), values);
    }

    /**
     * 批量删除
     *
     * @param clazz      要删除的对象
     * @param afterFrom  delete table 之后的语句,例如:delete table where field0=?,afterFrom为where field0=?
     * @param valuesList 批量操作的参数集合
     * @param <T>        类泛型
     * @return
     */
    public <T> int[] batchDelete(Class<T> clazz, String afterFrom, List<Object[]> valuesList) {
        StringBuilder sql = new StringBuilder();
        sql.append("delete from ");
        addTableNameAndSetDsKey(sql, clazz, false, false);
        sql.append(" ");
        if (afterFrom != null) {
            sql.append(afterFrom);
        }
        return this.jdbcSupport.batchUpdate(sql.toString(), valuesList);
    }

    /**
     * 删除
     *
     * @param clazz     要删除的对象
     * @param afterFrom delete table 之后的语句,例如:delete table where field0=?,afterFrom为where field0=?
     * @param values    参数
     * @param <T>       类泛型
     * @return
     */
    public <T> int delete2(Class<T> clazz, String afterFrom, List<?> values) {
        return this.delete(clazz, afterFrom, buildArgs(values));
    }

    /**
     * delete sql,返回删除的记录数量
     *
     * @param t 要删除的对象，必须有id
     * @return 删除的记录数
     */
    public <T> int delete(T t) {
        SQLMapper<T> mapper = getSqlMapper(t.getClass());
        return this.deleteById(t.getClass(), mapper.getIdParams(t));
    }

    //    /**
    //     * delete sql,根据id删除。返回删除的记录数量
    //     *
    //     * @param clazz   要删除的对象的类型
    //     * @param idValue id的参数
    //     * @return @ sql操作失败的异常
    //     */
    //    public <T> int deleteById(Class<T> clazz, Object idValue) {
    //        return this.jdbcSupport.update(buildDeleteSQL(clazz),
    //                new Object[]{idValue});
    //    }

    /**
     * delete sql,根据id删除。返回删除的记录数量
     *
     * @param clazz    要删除的对象的类型
     * @param idValues 主键id值
     * @return @ sql操作失败的异常
     */

    public <T> int deleteById(Class<T> clazz, Object[] idValues) {
        return this.jdbcSupport.update(buildDeleteSQL(clazz), idValues);
    }

    public static <T> String buildDeleteSQL(Class<T> clazz) {
        EntityTableInfo<T> info = getEntityTableInfo(clazz);
        StringBuilder sb = new StringBuilder();
        sb.append("delete from ").append(getTableNameAndSetDsKey(clazz)).append(" where ");
        if (info.getIdColumnNames().isEmpty()) {
            throw new HaloIdException(clazz.getName() + " must has id when build object delete sql");
        }
        for (String idColumnName : info.getIdColumnNames()) {
            sb.append(idColumnName).append("=? and ");
        }
        sb.delete(sb.length() - 5, sb.length());
        return sb.toString();
    }

    /**
     * 批量insert,对于使用数据库自增id方式，不会返回自增id，请使用应用自行获得自增id
     *
     * @param list 批量床架的对象
     * @param <T>  对象类型
     * @return 返回自增id，如果id不是自增，就返回值为0的集合
     */
    public <T> List<T> batchInsert(final List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new RuntimeException("batchInsert list must be not empty");
        }
        EntityTableInfo<T> info = getEntityTableInfo(list.get(0).getClass());
        String sql = buildInsertSQL(list.get(0).getClass(), true);
        List<Object[]> valuesList = new ArrayList<Object[]>();
        try {
            for (T t : list) {
                Object[] params = new Object[info.getTableFields().size()];
                int i = 0;
                for (Field field : info.getTableFields()) {
                    params[i++] = field.get(t);
                }
                valuesList.add(params);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        List<Number> ids = this.jdbcSupport.batchInsert(sql, valuesList, true);
        if (info.getIdFields().isEmpty()) {
            return list;
        }
        Field idField = info.getIdFields().get(0);
        try {
            for (int i = 0; i < list.size(); i++) {
                T t = list.get(i);
                if (this.isNumberIdType(idField)) {
                    Object idValue = idField.get(t);
                    if (idValue == null) {
                        this.setIdValue(t, idField, ids.get(i));
                    } else {
                        if (((Number) idValue).longValue() <= 0) {
                            this.setIdValue(t, idField, ids.get(i));
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    /**
     * insert sql
     *
     * @param t insert的对象
     */
    public <T> void insert(T t) {
        SQLMapper<T> mapper = getSqlMapper(t.getClass());
        this.jdbcSupport.insert(buildInsertSQL(t.getClass(), true), mapper.getParamsForInsert(t, true), false);
    }

    /**
     * replace into sql,此操作，如果是更新数据，将不会返回自增id
     *
     * @param t   数据对象
     * @param <T> 泛型
     * @return 当进行replace操作时，返回0
     */
    public <T> Number replace(T t) {
        return this.insertForNumber(t, InsertFlag.REPLACE_INTO);
    }

    /**
     * insert ignore into sql
     *
     * @param t   数据对象
     * @param <T> 泛型
     * @return 当插入不成功时，返回0
     */
    public <T> Number insertIgnore(T t) {
        return this.insertForNumber(t, InsertFlag.INSERT_IGNORE_INTO);
    }

    /**
     * @param t          数据对象
     * @param insertFlag 操作标识
     * @param <T>        泛型
     * @return 返回自增id，如果没有自增id，返回0
     */
    public <T> Number insertForNumber(T t, InsertFlag insertFlag) {
        EntityTableInfo<T> info = getEntityTableInfo(t.getClass());
        SQLMapper<T> mapper = getSqlMapper(t.getClass());
        if (info.getIdFields().size() > 1) {
            this.jdbcSupport.insert(buildInsertSQL(t.getClass(), true, insertFlag), mapper.getParamsForInsert(t, true), false);
            return 0;
        }
        if (info.getIdFields().isEmpty()) {
            this.jdbcSupport.insert(buildInsertSQL(t.getClass(), true, insertFlag), mapper.getParamsForInsert(t, true), false);
            return 0;
        }
        Field idField = info.getIdFields().get(0);
        Object idValue;
        try {
            idValue = idField.get(t);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // id 为数字时，只支持 int long
        if (this.isNumberIdType(idField)) {
            if (idValue == null) {
                idValue = 0;
            }
            Number num = (Number) idValue;
            // id = 0,需要获得自增id
            if (num.longValue() <= 0) {
                // sequence 获取id
                if (info.isHasSequence()) {
                    long id = this.idGenerator.nextKey(info.getDataFieldMaxValueIncrementer());
                    this.setIdValue(t, idField, id);
                    this.jdbcSupport.insert(buildInsertSQL(t.getClass(), true, insertFlag), mapper.getParamsForInsert(t, true), false);
                    return id;
                }
                // 为自增id方式
                Number n = (Number) (this.jdbcSupport.insert(buildInsertSQL(t.getClass(), false), mapper.getParamsForInsert(t, false), true));
                if (n != null && n.intValue() > 0) {
                    this.setIdValue(t, idField, n);
                }
                return n;
            }
            // id>0,不需要赋值，返回0
            this.jdbcSupport.insert(buildInsertSQL(t.getClass(), true, insertFlag), mapper.getParamsForInsert(t, true), false);
            return 0;
        }
        // 非数字id时,不需要赋值
        this.jdbcSupport.insert(buildInsertSQL(t.getClass(), true, insertFlag), mapper.getParamsForInsert(t, true), false);
        return 0;
    }

    /**
     * insert sql,返回自增数字id，联合主键的表，返回0. 如果表没有主键，直接insert,返回0
     *
     * @param t insert的对象
     * @return insert之后的自增数字
     */
    public <T> Number insertForNumber(T t) {
        return this.insertForNumber(t, InsertFlag.INSERT_INTO);
    }

    /**
     * 创建insert sql
     *
     * @param clazz       对象类型
     * @param hasIdColumn 是否包含idColumn，对于联合主键此参数无效
     * @param <T>         泛型
     * @return
     */
    public static <T> String buildInsertSQL(Class<T> clazz, boolean hasIdColumn) {
        return buildInsertSQL(clazz, hasIdColumn, InsertFlag.INSERT_INTO);
    }

    /**
     * @param clazz
     * @param hasIdColumn
     * @param insertFlag  0:insert into 1:replace into 2:insert ignore
     * @param <T>
     * @return
     */
    public static <T> String buildInsertSQL(Class<T> clazz, boolean hasIdColumn, InsertFlag insertFlag) {
        boolean _hasIdColumn = hasIdColumn;
        EntityTableInfo<T> info = getEntityTableInfo(clazz);
        if (info.getIdFields().size() > 1) {
            _hasIdColumn = true;
        }
        StringBuilder sb = new StringBuilder();
        if (insertFlag.equals(InsertFlag.INSERT_INTO)) {
            sb.append("insert into ");
        } else if (insertFlag.equals(InsertFlag.REPLACE_INTO)) {
            sb.append("replace into ");
        } else if (insertFlag.equals(InsertFlag.INSERT_IGNORE_INTO)) {
            sb.append("insert ignore into ");
        } else {
            throw new RuntimeException("insertFlag[" + insertFlag + "] not supported");
        }
        String tableName = getTableNameAndSetDsKey(clazz);
        sb.append(tableName);
        sb.append("(");
        List<String> columnNames = info.getColumnNames();
        for (String col : columnNames) {
            if (!_hasIdColumn && info.isIdColumnName(col)) {
                continue;
            }
            sb.append(col);
            sb.append(",");
        }
        if (!columnNames.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append(")");
        sb.append(" values");
        sb.append("(");
        int len = columnNames.size();
        if (!hasIdColumn) {
            len = len - 1;
        }
        for (int i = 0; i < len; i++) {
            sb.append("?,");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        return sb.toString();
    }

    private boolean isNumberIdType(Field field) {
        Class<?> cls = field.getType();
        return cls.equals(int.class) || cls.equals(Integer.class) || cls.equals(long.class) || cls.equals(Long.class) || cls.equals(BigInteger.class);
    }

    private <T> void setIdValue(T t, Field idField, Number n) {
        try {
            if (idField.getType().equals(Integer.class) || idField.getType().equals(int.class)) {
                idField.set(t, n.intValue());
            } else if (idField.getType().equals(Long.class) || idField.getType().equals(long.class)) {
                idField.set(t, n.longValue());
            } else {
                idField.set(t, n.longValue());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * mysql的分页查询。查询中的表别名必须与表名相同
     *
     * @param clazzes   查询对象类型数组
     * @param afterFrom from table 之后的sql,例如select * from table where uid=?
     *                  order name desc, afterFrom为where uid=? order name
     * @param begin     开始位置
     * @param size      查询数量
     * @param values    参数化查询值
     * @param rowMapper spring RowMapper
     * @return 查询集合
     */
    public <T> List<T> mysqlList(Class<?>[] clazzes, String afterFrom, int begin, int size, Object[] values, RowMapper<T> rowMapper) {
        StringBuilder sql = new StringBuilder("select ");
        EntityTableInfo<T> info;
        for (Class<?> clazz : clazzes) {
            info = getEntityTableInfo(clazz);
            sql.append(info.getSelectedFieldSQL());
            sql.append(",");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" from ");
        for (Class<?> clazz : clazzes) {
            this.addTableNameAndSetDsKey(sql, clazz, true, true);
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" ");
        sql.append(afterFrom);
        if (size > 0) {
            sql.append(" limit ");
            sql.append(begin);
            sql.append(",");
            sql.append(size);
        }
        return jdbcSupport.list(sql.toString(), values, rowMapper);
    }

    public <T> List<T> mysqlList2(Class<?>[] clazzes, String afterFrom,
                                  int begin, int size, List<?> values, RowMapper<T> rowMapper) {
        return this.mysqlList(clazzes, afterFrom, begin, size, buildArgs(values), rowMapper);
    }

    /**
     * mysql的分页查询。
     *
     * @param clazz     查询对象类型
     * @param afterFrom from table 之后的sql,例如select * from table where uid=?
     *                  order name desc, afterFrom为where uid=? order name
     * @param begin     开始位置
     * @param size      查询数量
     * @param values    参数化查询值
     * @return 查询集合
     */
    public <T> List<T> mysqlList(Class<T> clazz, String afterFrom, int begin, int size, Object[] values) {
        return this.mysqlList(clazz, afterFrom, begin, size, values, getRowMapper(clazz));
    }

    public <T> List<T> mysqlList2(Class<T> clazz, String afterFrom,
                                  int begin, int size, List<?> values) {
        return this.mysqlList(clazz, afterFrom, begin, size, buildArgs(values));
    }

    /**
     * mysql的分页查询。
     *
     * @param clazz     查询对象类型
     * @param afterFrom from table 之后的sql,例如select * from table where uid=?
     *                  order name desc, afterFrom为where uid=? order name
     * @param begin     开始位置
     * @param size      查询数量
     * @param values    参数化查询值
     * @param rowMapper spring RowMapper
     * @return 查询集合
     */
    public <T> List<T> mysqlList(Class<T> clazz, String afterFrom, int begin, int size, Object[] values, RowMapper<T> rowMapper) {
        EntityTableInfo<T> info = getEntityTableInfo(clazz);
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        sql.append(info.getSelectedFieldSQL());
        sql.append(" from ");
        addTableNameAndSetDsKey(sql, clazz, true, false);
        sql.append(" ");
        if (afterFrom != null) {
            sql.append(afterFrom);
        }
        if (size > 0) {
            sql.append(" limit ");
            sql.append(begin);
            sql.append(",");
            sql.append(size);
        }
        return jdbcSupport.list(sql.toString(), values, rowMapper);
    }

    public <T> List<T> mysqlList2(Class<T> clazz, String afterFrom,
                                  int begin, int size, List<?> values, RowMapper<T> rowMapper) {
        return this.mysqlList(clazz, afterFrom, begin, size, buildArgs(values), rowMapper);
    }

    /**
     * select sql 返回对象
     *
     * @param clazz     查询对象类型
     * @param afterFrom from table 之后的sql,例如select * from table where uid=?
     *                  order name desc, afterFrom为where uid=? order name desc
     * @param values    参数化查询值
     * @return 查询对象
     */
    public <T> T obj(Class<T> clazz, String afterFrom, Object[] values) {
        return this.obj(clazz, afterFrom, values, getRowMapper(clazz));
    }

    public <T> T obj2(Class<T> clazz, String afterFrom, List<?> values) {
        return this.obj(clazz, afterFrom, buildArgs(values));
    }

    /**
     * select sql 返回对象
     *
     * @param clazz     查询对象类型
     * @param afterFrom from table 之后的sql,例如select * from table where uid=?
     *                  order name desc, afterFrom为where uid=? order name desc
     * @param values    参数化查询值
     * @param rowMapper spring RowMapper
     * @return 查询对象
     */
    public <T> T obj(Class<T> clazz, String afterFrom, Object[] values, RowMapper<T> rowMapper) {
        EntityTableInfo<T> info = getEntityTableInfo(clazz);
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        sql.append(info.getSelectedFieldSQL());
        sql.append(" from ");
        addTableNameAndSetDsKey(sql, clazz, true, false);
        sql.append(" ");
        if (afterFrom != null) {
            sql.append(afterFrom);
        }
        List<T> list = jdbcSupport.list(sql.toString(), values, rowMapper);
        if (list.isEmpty()) {
            return null;
        }
        if (list.size() == 1) {
            return list.get(0);
        }
        throw new IncorrectResultSizeDataAccessException(1, list.size());
    }

    public <T> T obj2(Class<T> clazz, String afterFrom, List<?> values,
                      RowMapper<T> rowMapper) {
        return this.obj(clazz, afterFrom, buildArgs(values), rowMapper);
    }

    /**
     * select sql 根据id查询，返回对象
     *
     * @param clazz   查询对象类型
     * @param idValue id参数
     * @return 查询对象
     */
    public <T> T objById(Class<T> clazz, Object idValue) {
        return this.objByIds(clazz, new Object[]{idValue});
    }

    /**
     * select sql 根据id查询,并且实现sql select ... for update 功能
     *
     * @param clazz     查询对象类型
     * @param idValue   id参数
     * @param forUpdate 是否锁数据
     * @param <T>       对象泛型
     * @return 查询结果
     */
    public <T> T objById(Class<T> clazz, Object idValue, boolean forUpdate) {
        return this.objByIds(clazz, new Object[]{idValue}, forUpdate, getRowMapper(clazz));
    }

    /**
     * select 根据id查询对象，并使用for update 锁定该行数据
     *
     * @param clazz   查询对象类型
     * @param idValue id参数
     * @param <T>     对象泛型
     * @return 查询结果
     */
    public <T> T objByIdForUpdate(Class<T> clazz, Object idValue) {
        return this.objByIds(clazz, new Object[]{idValue}, true, getRowMapper(clazz));
    }

    /**
     * select sql 根据id查询，返回对象
     *
     * @param clazz    查询对象类型
     * @param idValues id参数
     * @return 查询对象
     */
    public <T> T objByIds(Class<T> clazz, Object[] idValues) {
        return this.objByIds(clazz, idValues, false, getRowMapper(clazz));
    }

    /**
     * select 根据id查询对象,并使用for update 锁定该行数据
     *
     * @param clazz    查询对象类型
     * @param idValues id参数
     * @param <T>      对象泛型
     * @return 查询结果
     */
    public <T> T objByIdsForUpdate(Class<T> clazz, Object[] idValues) {
        return this.objByIds(clazz, idValues, true, getRowMapper(clazz));
    }

    /**
     * select 根据id查询对象,并使用for update 锁定该行数据
     *
     * @param clazz     查询对象类型
     * @param idValues  id参数
     * @param forUpdate 是否锁对象
     * @param <T>       对象泛型
     * @return 查询结果
     */
    public <T> T objByIds(Class<T> clazz, Object[] idValues, boolean forUpdate) {
        return this.objByIds(clazz, idValues, forUpdate, getRowMapper(clazz));
    }

    /**
     * select sql 根据id查询，返回对象
     *
     * @param clazz     查询对象类型
     * @param idValues  id参数
     * @param forUpdate 是否使用sql for update进行锁数据
     * @param rowMapper spring RowMapper
     * @return 查询对象
     */
    public <T> T objByIds(Class<T> clazz, Object[] idValues, boolean forUpdate, RowMapper<T> rowMapper) {
        EntityTableInfo<T> info = getEntityTableInfo(clazz);
        int idSize = info.getIdColumnNames().size();
        if (idValues.length != idSize) {
            throw new RuntimeException(clazz.getName() + " has " + idSize + " id. " + "please input " + idSize + " arguments");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("where ");
        for (String idColumnName : info.getIdColumnNames()) {
            sb.append(idColumnName).append("=? and ");
        }
        sb.delete(sb.length() - 5, sb.length());
        if (forUpdate) {
            sb.append(" for update");
        }
        return this.obj(clazz, sb.toString(), idValues, rowMapper);
    }

    /**
     * 批量更新
     *
     * @param clazz        更新的类型
     * @param updateSqlSeg sql片段,为update table 之后的sql。例如：set field0=?,field1=? where field3=?
     * @param valuesList   批量操作的参数集合
     * @param <T>          类泛型
     * @return
     */
    public <T> int[] batchUpdate(Class<T> clazz, String updateSqlSeg, List<Object[]> valuesList) {
        StringBuilder sql = new StringBuilder();
        sql.append("update ");
        this.addTableNameAndSetDsKey(sql, clazz, false, false);
        sql.append(" ");
        sql.append(updateSqlSeg);
        return this.jdbcSupport.batchUpdate(sql.toString(), valuesList);
    }

    /**
     * update sql，返回更新的记录数量。只更新选中的字段 例如: update table set field0=?,field1=?
     * where field3=?
     *
     * @param clazz        需要更新的类
     * @param updateSqlSeg sql片段,为update table 之后的sql。例如：set field0=?,field1=? where field3=?
     * @param values       参数化查询值
     * @return 更新数量
     */
    public <T> int update(Class<T> clazz, String updateSqlSeg, Object[] values) {
        StringBuilder sql = new StringBuilder();
        sql.append("update ");
        this.addTableNameAndSetDsKey(sql, clazz, false, false);
        sql.append(" ");
        sql.append(updateSqlSeg);
        return this.jdbcSupport.update(sql.toString(), values);
    }

    public <T> int update2(Class<T> clazz, String updateSqlSeg,
                           List<?> values) {
        return this.update(clazz, updateSqlSeg, buildArgs(values));
    }

    /**
     * update sql ,返回更新的记录数量
     *
     * @param t update的对象
     * @return 更新数量
     */
    public <T> int update(T t) {
        SQLMapper<T> mapper = getSqlMapper(t.getClass());
        return this.jdbcSupport.update(buildUpdateSQL(t.getClass()), mapper.getParamsForUpdate(t));
    }

    /**
     * 对实体对象进行属性快照，记录当前实体中filed 的值到新的对象中
     *
     * @param t   实体对象
     * @param <T>
     * @return
     */
    public static <T> T snapshot(Object t) {
        EntityTableInfo<T> entityTableInfo = Query.getEntityTableInfo(t.getClass());
        if (entityTableInfo == null) {
            throw new RuntimeException(t.getClass().getName() + " is not a table entity");
        }
        try {
            T snapshoot = entityTableInfo.getConstructor().newInstance();
            EntityUtil.copy(t, snapshoot);
            return snapshoot;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 对实体进行update操作，更新是比较快照与当前实体的值，如果当前实体的值发生变化，才进行更新。
     *
     * @param t        要更新的对象
     * @param snapshot 可为空，如果为空就执行 update(T t)
     * @param <T>
     * @return
     */
    public <T> int update(T t, T snapshot) {
        if (snapshot == null) {
            return this.update(t);
        }
        StringBuilder sb = new StringBuilder("set ");
        EntityTableInfo<T> entityTableInfo = getEntityTableInfo(t.getClass());
        List<Object> values = new ArrayList<Object>();
        try {
            int i = 0;
            for (Field field : entityTableInfo.getTableFields()) {
                Object valueT = field.get(t);
                Object valueSnapshootObj = field.get(snapshot);
                if (entityTableInfo.isIdField(field)) {
                    continue;
                }
                if (!valueT.equals(valueSnapshootObj)) {
                    i++;
                    values.add(valueT);
                    sb.append(entityTableInfo.getColumn(field.getName()));
                    sb.append("=?,");
                }
            }
            if (i == 0) {
                return 0;
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(" where ");
            if (entityTableInfo.getIdColumnNames().size() == 0) {
                throw new HaloIdException(t.getClass().getName() + " must has id when update(T t, T snapshot)");
            }
            for (String idColumnName : entityTableInfo.getIdColumnNames()) {
                values.add(entityTableInfo.getField(idColumnName).get(t));
                sb.append(idColumnName).append("=? and ");
            }
            sb.delete(sb.length() - 5, sb.length());
            return this.update2(t.getClass(), sb.toString(), values);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> String buildUpdateSQL(Class<T> clazz) {
        StringBuilder sb = new StringBuilder("update ");
        sb.append(getTableNameAndSetDsKey(clazz));
        EntityTableInfo<T> info = getEntityTableInfo(clazz);
        sb.append(" set ");
        List<String> columnNames = info.getColumnNames();
        for (String col : columnNames) {
            if (info.isIdColumnName(col)) {
                continue;
            }
            sb.append(col);
            sb.append("=?,");
        }
        if (!columnNames.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append(" where ");
        if (info.getIdColumnNames().isEmpty()) {
            throw new HaloIdException(clazz.getName() + " must has id when build object update sql");
        }
        for (String idColumnName : info.getIdColumnNames()) {
            sb.append(idColumnName).append("=? and ");
        }
        sb.delete(sb.length() - 5, sb.length());
        return sb.toString();
    }

    /**
     * 获得自增id
     *
     * @param clazz 提供自增id的类，需要配置sequence
     * @return 自增id
     */
    public <T> long nextKey(Class<T> clazz) {
        return this.idGenerator.nextKey(getEntityTableInfo(clazz).getDataFieldMaxValueIncrementer());
    }

    public void setJdbcSupport(JdbcSupport jdbcSupport) {
        this.jdbcSupport = jdbcSupport;
    }

    public static <T> EntityTableInfo<T> getEntityTableInfo(Class<?> clazz) {
        return (EntityTableInfo<T>) EntityTableInfoFactory.getEntityTableInfo(clazz);
    }

    public JdbcSupport getJdbcSupport() {
        return jdbcSupport;
    }

    public static <T> RowMapper<T> getRowMapper(Class<T> clazz) {
        return (RowMapper<T>) getEntityTableInfo(clazz).getRowMapper();
    }

    public static <T> SQLMapper<T> getSqlMapper(Class<?> clazz) {
        return (SQLMapper<T>) getEntityTableInfo(clazz).getSqlMapper();
    }

    public static Object[] buildArgs(List<?> values) {
        if (values == null) {
            return null;
        }
        Object[] args = new Object[values.size()];
        int i = 0;
        for (Object value : values) {
            args[i] = value;
            i++;
        }
        return args;
    }

    /**
     * 解析sql路由，设置当前数据源key，返回解析后数据
     *
     * @param clazz     需要解析的 class
     * @param dalParser 解析器
     * @return
     */
    public static DALInfo process(Class clazz, DALParser dalParser) {
        DALParserUtil.process(clazz, dalParser, DALStatus.getParamMap());
        return DALStatus.getDalInfo();
    }

    /**
     * 解析sql路由，设置当前数据源key，返回解析后数据
     *
     * @param clazz 需要解析的类
     * @param <T>   泛型
     * @return 解析后的路由数据
     */
    public static <T> DALInfo process(Class<T> clazz) {
        EntityTableInfo<T> entityTableInfo = getEntityTableInfo(clazz);
        return process(entityTableInfo.getClazz(), entityTableInfo.getDalParser());
    }

    public enum InsertFlag {
        INSERT_INTO(0),
        REPLACE_INTO(1),
        INSERT_IGNORE_INTO(2);

        private final int value;

        private InsertFlag(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static InsertFlag findByValue(int value) {
            switch (value) {
                case 0:
                    return INSERT_INTO;
                case 1:
                    return REPLACE_INTO;
                case 2:
                    return INSERT_IGNORE_INTO;
                default:
                    return null;
            }
        }
    }
}