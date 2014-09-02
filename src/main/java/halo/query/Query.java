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
import java.util.List;

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

    private <T> void addTableNameAndSetDsKey(StringBuilder sb, Class<T> clazz,
            boolean addTableAlias,
            boolean addComma) {
        EntityTableInfo<T> info = getEntityTableInfo(clazz);
        DALInfo dalInfo = Query.process(clazz, info.getDalParser());
        if (dalInfo == null) {
            sb.append(info.getTableName());
        }
        else {
            String dsKey = dalInfo.getDsKey();
            if (dsKey != null) {
                DALStatus.setDsKey(dsKey);
            }
            String realTableName = dalInfo.getRealTable(clazz);
            if (realTableName == null) {
                sb.append(info.getTableName());
            }
            else {
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
        }
        else {
            String dsKey = dalInfo.getDsKey();
            if (dsKey != null) {
                DALStatus.setDsKey(dsKey);
            }
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

    public <T> List<T> list(Class<T> clazz, String afterFrom, Object[] values,
            RowMapper<T> rowMapper) {
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

    public <T> List<T> list(Class<T> clazz, String afterFrom, Object[] values) {
        return this.list(clazz, afterFrom, values, getRowMapper(clazz));
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
    public <T> List<T> db2List(Class<?>[] clazzes, String where,
            String orderBy, int begin, int size, Object[] values,
            RowMapper<T> rowMapper) {
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
    public <T> List<T> db2List(Class<T> clazz, String where, String orderBy,
            int begin, int size, Object[] values) {
        return this.db2List(clazz, where, orderBy, begin, size, values,
                getRowMapper(clazz));
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
    public <T> List<T> db2List(Class<T> clazz, String where, String orderBy,
            int begin, int size, Object[] values, RowMapper<T> rowMapper) {
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

    /**
     * delete sql.根据条件删除.例如: delete table where field0=? and ....
     *
     * @param clazz     要删除的对象类型
     * @param afterFrom delete table 之后的语句,例如:delete table where
     *                  field0=?,afterFrom为where field0=?
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
        sb.append("delete from ").append(getTableNameAndSetDsKey(clazz))
                .append(" where ");
        for (String idColumnName : info.getIdColumnNames()) {
            sb.append(idColumnName).append("=? and ");
        }
        sb.delete(sb.length() - 5, sb.length());
        return sb.toString();
    }

    /**
     * insert sql
     *
     * @param t insert的对象
     */
    public <T> void insert(T t) {
        SQLMapper<T> mapper = getSqlMapper(t.getClass());
        this.jdbcSupport.insert(buildInsertSQL(t.getClass(), true),
                mapper.getParamsForInsert(t, true), false);
    }

    /**
     * insert sql,返回自增数字id，联合主键的表，返回0
     *
     * @param t insert的对象
     * @return insert之后的自增数字
     */
    public <T> Number insertForNumber(T t) {
        EntityTableInfo<T> info = getEntityTableInfo(t.getClass());
        SQLMapper<T> mapper = getSqlMapper(t.getClass());
        if (info.getIdFields().size() > 1) {
            this.jdbcSupport.insert(buildInsertSQL(t.getClass(), true),
                    mapper.getParamsForInsert(t, true), false);
            return 0;
        }
        Field idField = info.getIdFields().get(0);
        Object idValue;
        try {
            idValue = idField.get(t);
        }
        catch (Exception e) {
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
                    long id = this.idGenerator.nextKey(info
                            .getDataFieldMaxValueIncrementer());
                    this.setIdValue(t, idField, id);
                    this.jdbcSupport.insert(
                            buildInsertSQL(t.getClass(), true),
                            mapper.getParamsForInsert(t, true), false);
                    return id;
                }
                // 为自增id方式
                Number n = (Number) (this.jdbcSupport.insert(
                        buildInsertSQL(t.getClass(), false),
                        mapper.getParamsForInsert(t, false), true));
                this.setIdValue(t, idField, n);
                return n;
            }
            // id>0,不需要赋值，返回0
            this.jdbcSupport.insert(buildInsertSQL(t.getClass(), true),
                    mapper.getParamsForInsert(t, true), false);
            return 0;
        }
        // 非数字id时,不需要赋值
        this.jdbcSupport.insert(buildInsertSQL(t.getClass(), true),
                mapper.getParamsForInsert(t, true), false);
        return 0;
    }

    /**
     * 创建insert sql
     *
     * @param clazz       对象类型
     * @param hasIdColumn 是否包含idColumn，对于联合主键此参数无效
     * @param <T>         泛型
     * @return
     */
    public static <T> String buildInsertSQL(Class<T> clazz,
            boolean hasIdColumn) {
        boolean _hasIdColumn = hasIdColumn;
        EntityTableInfo<T> info = getEntityTableInfo(clazz);
        if (info.getIdFields().size() > 1) {
            _hasIdColumn = true;
        }
        StringBuilder sb = new StringBuilder("insert into ");
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
            if (idField.getType().equals(Integer.class)
                    || idField.getType().equals(int.class)) {
                idField.set(t, n.intValue());
            }
            else if (idField.getType().equals(Long.class)
                    || idField.getType().equals(long.class)) {
                idField.set(t, n.longValue());
            }
            else {
                idField.set(t, n.longValue());
            }
        }
        catch (Exception e) {
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
    public <T> List<T> mysqlList(Class<?>[] clazzes, String afterFrom,
            int begin, int size, Object[] values, RowMapper<T> rowMapper) {
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
    public <T> List<T> mysqlList(Class<T> clazz, String afterFrom,
            int begin, int size, Object[] values) {
        return this.mysqlList(clazz, afterFrom, begin, size,
                values, getRowMapper(clazz));
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
    public <T> List<T> mysqlList(Class<T> clazz, String afterFrom,
            int begin, int size, Object[] values, RowMapper<T> rowMapper) {
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
    public <T> T obj(Class<T> clazz, String afterFrom, Object[] values,
            RowMapper<T> rowMapper) {
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
     * select sql 根据id查询，返回对象
     *
     * @param clazz    查询对象类型
     * @param idValues id参数
     * @return 查询对象
     */
    public <T> T objByIds(Class<T> clazz, Object[] idValues) {
        return this.objByIds(clazz, idValues, getRowMapper(clazz));
    }

    /**
     * select sql 根据id查询，返回对象
     *
     * @param clazz     查询对象类型
     * @param idValues  id参数
     * @param rowMapper spring RowMapper
     * @return 查询对象
     */
    public <T> T objByIds(Class<T> clazz, Object[] idValues,
            RowMapper<T> rowMapper) {
        EntityTableInfo<T> info = getEntityTableInfo(clazz);
        int idSize = info.getIdColumnNames().size();
        if (idValues.length != idSize) {
            throw new RuntimeException(clazz.getName() + " has " + idSize + " id. " +
                    "please input " + idSize + " arguments");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("where ");
        for (String idColumnName : info.getIdColumnNames()) {
            sb.append(idColumnName).append("=? and ");
        }
        sb.delete(sb.length() - 5, sb.length());
        return this.obj(clazz, sb.toString(), idValues, rowMapper);
    }

    /**
     * update sql，返回更新的记录数量。只更新选中的字段 例如: update table set field0=?,field1=?
     * where field3=?
     *
     * @param clazz        需要更新的类
     * @param updateSqlSeg sql片段,为update table 之后的sql。例如：set field0=?,field1=?
     *                     where field3=?
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

    /**
     * update sql ,返回更新的记录数量
     *
     * @param t update的对象
     * @return 更新数量
     */
    public <T> int update(T t) {
        SQLMapper<T> mapper = getSqlMapper(t.getClass());
        return this.jdbcSupport.update(buildUpdateSQL(t.getClass()),
                mapper.getParamsForUpdate(t));
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
        EntityTableInfo<T> info = getEntityTableInfo(clazz);
        return this.idGenerator.nextKey(info.getDataFieldMaxValueIncrementer());
    }

    public void setJdbcSupport(JdbcSupport jdbcSupport) {
        this.jdbcSupport = jdbcSupport;
    }

    public static <T> EntityTableInfo<T> getEntityTableInfo(Class<?> clazz) {
        return (EntityTableInfo<T>) EntityTableInfoFactory
                .getEntityTableInfo(clazz);
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
}