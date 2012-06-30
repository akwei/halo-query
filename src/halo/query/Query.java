package halo.query;

import halo.query.mapping.DefEntityTableInfoFactory;
import halo.query.mapping.EntityTableInfo;
import halo.query.mapping.EntityTableInfoFactory;
import halo.query.mapping.SQLMapper;

import java.util.List;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;

public class Query {

    private JdbcSupport jdbcSupport;

    private EntityTableInfoFactory entityTableInfoFactory = new DefEntityTableInfoFactory();

    public <T> Number insertForNumber(T t) throws QueryException {
        return this.insertForNumber(t, null);
    }

    public <T> Number insertForNumber(T t, String tablePostfix)
            throws QueryException {
        EntityTableInfo<T> info = this.getEntityTableInfo(t.getClass());
        SQLMapper<T> mapper = this.getSqlMapper(t.getClass());
        Object obj = this.jdbcSupport.insert(info.getInsertSQL(tablePostfix),
                mapper.getParamsForInsert(t));
        return (Number) obj;
    }

    public <T> void insert(T t) throws QueryException {
        this.insert(t, null);
    }

    public <T> void insert(T t, String tablePostfix) throws QueryException {
        EntityTableInfo<T> info = this.getEntityTableInfo(t.getClass());
        SQLMapper<T> mapper = this.getSqlMapper(t.getClass());
        this.jdbcSupport.insert(info.getInsertSQL(tablePostfix),
                mapper.getParamsForInsert(t));
    }

    public <T> int update(T t) throws QueryException {
        return this.update(t, null);
    }

    public <T> int update(T t, String tablePostfix) throws QueryException {
        EntityTableInfo<T> info = this.getEntityTableInfo(t.getClass());
        SQLMapper<T> mapper = this.getSqlMapper(t.getClass());
        return this.jdbcSupport.update(info.getUpdateSQL(tablePostfix),
                mapper.getParamsForUpdate(t));
    }

    public <T> int update(Class<T> clazz, String updateSqlSeg, Object[] values)
            throws QueryException {
        return this.update(clazz, null, updateSqlSeg, values);
    }

    public <T> int update(Class<T> clazz, String tablePostfix,
            String updateSqlSeg, Object[] values) throws QueryException {
        EntityTableInfo<T> info = this.getEntityTableInfo(clazz);
        StringBuilder sql = new StringBuilder();
        sql.append("update ");
        sql.append(info.getTableName());
        if (this.isNotEmpty(tablePostfix)) {
            sql.append(tablePostfix);
        }
        sql.append(" ");
        sql.append(updateSqlSeg);
        return this.jdbcSupport.update(sql.toString(), values);
    }

    public <T> int delete(T t) throws QueryException {
        return this.delete(t, null);
    }

    public <T> int delete(T t, String tablePostfix) throws QueryException {
        SQLMapper<T> mapper = this.getSqlMapper(t.getClass());
        return this
                .deleteById(t.getClass(), tablePostfix, mapper.getIdParam(t));
    }

    public <T> int deleteById(Class<T> clazz, Object idValue)
            throws QueryException {
        return this.deleteById(clazz, null, idValue);
    }

    public <T> int deleteById(Class<T> clazz, String tablePostfix,
            Object idValue) throws QueryException {
        EntityTableInfo<T> info = this.getEntityTableInfo(clazz);
        return this.jdbcSupport.update(info.getDeleteSQL(tablePostfix),
                new Object[] { idValue });
    }

    public <T> int delete(Class<T> clazz, String sqlAfterTable, Object[] values)
            throws QueryException {
        return this.delete(clazz, null, sqlAfterTable, values);
    }

    public <T> int delete(Class<T> clazz, String tablePostfix,
            String sqlAfterTable, Object[] values) throws QueryException {
        EntityTableInfo<T> info = this.getEntityTableInfo(clazz);
        StringBuilder sql = new StringBuilder();
        sql.append("delete from ");
        sql.append(info.getTableName());
        if (this.isNotEmpty(tablePostfix)) {
            sql.append(tablePostfix);
        }
        sql.append(" ");
        sql.append(sqlAfterTable);
        return this.jdbcSupport.update(sql.toString(), values);
    }

    public <T> List<T> list(Class<T> clazz, String sqlAfterTable,
            Object[] values) throws QueryException {
        return this.list(clazz, null, sqlAfterTable, values);
    }

    public <T> List<T> list(Class<T> clazz, String tablePostfix,
            String sqlAfterTable, Object[] values) throws QueryException {
        return this.list(clazz, tablePostfix, sqlAfterTable, values,
                this.getRowMapper(clazz));
    }

    public <T> List<T> list(Class<T> clazz, String sqlAfterTable,
            Object[] values, RowMapper<T> rowMapper) throws QueryException {
        return this.list(clazz, null, sqlAfterTable, values, rowMapper);
    }

    public <T> List<T> list(Class<T> clazz, String tablePostfix,
            String sqlAfterTable, Object[] values, RowMapper<T> rowMapper)
            throws QueryException {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from ");
        sql.append(this.getEntityTableInfo(clazz).getTableName());
        if (this.isNotEmpty(tablePostfix)) {
            sql.append(tablePostfix);
        }
        sql.append(" ");
        sql.append(sqlAfterTable);
        return jdbcSupport.list(sql.toString(), values, rowMapper);
    }

    public <T> T obj(Class<T> clazz, String sqlAfterTable, Object[] values,
            RowMapper<T> rowMapper) throws QueryException {
        return obj(clazz, sqlAfterTable, values, rowMapper);
    }

    public <T> T obj(Class<T> clazz, String tablePostfix, String sqlAfterTable,
            Object[] values, RowMapper<T> rowMapper) throws QueryException {
        List<T> list = this.list(clazz, tablePostfix, sqlAfterTable, values,
                rowMapper);
        if (list.isEmpty()) {
            return null;
        }
        if (list.size() == 1) {
            return list.get(0);
        }
        throw new IncorrectResultSizeDataAccessException(1, list.size());
    }

    public <T> T obj(Class<T> clazz, String sqlAfterTable, Object[] values)
            throws QueryException {
        return this.obj(clazz, sqlAfterTable, values, this.getRowMapper(clazz));
    }

    public <T> T obj(Class<T> clazz, String tablePostfix, String sqlAfterTable,
            Object[] values) throws QueryException {
        return this.obj(clazz, tablePostfix, sqlAfterTable, values,
                this.getRowMapper(clazz));
    }

    public <T> T objById(Class<T> clazz, Object idValue, RowMapper<T> rowMapper)
            throws QueryException {
        return this.objById(clazz, null, idValue, rowMapper);
    }

    public <T> T objById(Class<T> clazz, String tablePostfix, Object idValue,
            RowMapper<T> rowMapper) throws QueryException {
        EntityTableInfo<T> info = this.getEntityTableInfo(clazz);
        String sqlAfterTable = "where " + info.getIdColumnName() + "=?";
        return this.obj(clazz, tablePostfix, sqlAfterTable,
                new Object[] { idValue }, rowMapper);
    }

    public <T> T objById(Class<T> clazz, Object idValue) throws QueryException {
        return this.objById(clazz, idValue, this.getRowMapper(clazz));
    }

    public <T> T objById(Class<T> clazz, String tablePostfix, Object idValue)
            throws QueryException {
        return this.objById(clazz, tablePostfix, idValue,
                this.getRowMapper(clazz));
    }

    /**
     * 表别名与表名相同
     * 
     * @param clazzes
     * @param sqlAfterTable
     * @param values
     * @param rowMapper
     * @return
     * @throws QueryException
     */
    public <T> List<T> list(Class<?>[] clazzes, String sqlAfterTable,
            Object[] values, RowMapper<T> rowMapper) throws QueryException {
        return this.list(clazzes, null, sqlAfterTable, values, rowMapper);
    }

    /**
     * 表别名与表名相同
     * 
     * @param clazzes
     * @param tablePostfix
     * @param sqlAfterTable
     * @param values
     * @param rowMapper
     * @return
     * @throws QueryException
     */
    public <T> List<T> list(Class<?>[] clazzes, String[] tablePostfix,
            String sqlAfterTable, Object[] values, RowMapper<T> rowMapper)
            throws QueryException {
        StringBuilder sb = new StringBuilder("select * from ");
        EntityTableInfo<T> info;
        int i = 0;
        for (Class<?> clazz : clazzes) {
            info = this.getEntityTableInfo(clazz);
            sb.append(info.getTableName());
            if (tablePostfix != null && this.isNotEmpty(tablePostfix[i])) {
                sb.append(tablePostfix[i]);
            }
            sb.append(" ");
            sb.append(info.getTableName());
            sb.append(",");
            i++;
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(" ");
        sb.append(sqlAfterTable);
        return jdbcSupport.list(sb.toString(), values, rowMapper);
    }

    public <T> List<T> listMySQL(Class<T> clazz, String sqlAfterTable,
            int begin, int size, Object[] values, RowMapper<T> rowMapper)
            throws QueryException {
        return this.listMySQL(clazz, null, sqlAfterTable, begin, size, values,
                rowMapper);
    }

    public <T> List<T> listMySQL(Class<T> clazz, String tablePostfix,
            String sqlAfterTable, int begin, int size, Object[] values,
            RowMapper<T> rowMapper) throws QueryException {
        String _sqlAfterTable;
        if (begin < 0 || size < 0) {
            _sqlAfterTable = sqlAfterTable;
        }
        else {
            _sqlAfterTable = sqlAfterTable + " limit " + begin + "," + size;
        }
        return this
                .list(clazz, tablePostfix, _sqlAfterTable, values, rowMapper);
    }

    public <T> List<T> listMySQL(Class<T> clazz, String sqlAfterTable,
            int begin, int size, Object[] values) throws QueryException {
        return this.listMySQL(clazz, null, sqlAfterTable, begin, size, values);
    }

    public <T> List<T> listMySQL(Class<T> clazz, String tablePostfix,
            String sqlAfterTable, int begin, int size, Object[] values)
            throws QueryException {
        return this.listMySQL(clazz, tablePostfix, sqlAfterTable, begin, size,
                values, this.getRowMapper(clazz));
    }

    /**
     * 表别名与表名相同
     * 
     * @param clazzes
     * @param sqlAfterTable
     * @param begin
     * @param size
     * @param values
     * @param rowMapper
     * @return
     * @throws QueryException
     */
    public <T> List<T> listMySQL(Class<?>[] clazzes, String sqlAfterTable,
            int begin, int size, Object[] values, RowMapper<T> rowMapper)
            throws QueryException {
        return this.listMySQL(clazzes, null, sqlAfterTable, begin, size,
                values, rowMapper);
    }

    /**
     * 表别名与表名相同
     * 
     * @param clazzes
     * @param sqlAfterTable
     * @param begin
     * @param size
     * @param values
     * @param rowMapper
     * @return
     * @throws QueryException
     */
    public <T> List<T> listMySQL(Class<?>[] clazzes, String[] tablePostfix,
            String sqlAfterTable, int begin, int size, Object[] values,
            RowMapper<T> rowMapper) throws QueryException {
        String _sqlAfterTable;
        if (begin < 0 || size < 0) {
            _sqlAfterTable = sqlAfterTable;
        }
        else {
            _sqlAfterTable = sqlAfterTable + " limit " + begin + "," + size;
        }
        return this.list(clazzes, tablePostfix, _sqlAfterTable, values,
                rowMapper);
    }

    public <T> int count(Class<T> clazz, String sqlAfterFrom, Object[] values)
            throws QueryException {
        return this.count(clazz, null, sqlAfterFrom, values);
    }

    public <T> int count(Class<T> clazz, String tablePostfix,
            String sqlAfterFrom, Object[] values) throws QueryException {
        EntityTableInfo<T> info = this.getEntityTableInfo(clazz);
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) from ");
        sql.append(info.getTableName());
        if (this.isNotEmpty(tablePostfix)) {
            sql.append(tablePostfix);
        }
        sql.append(" ");
        sql.append(sqlAfterFrom);
        return jdbcSupport.num(sql.toString(), values).intValue();
    }

    /**
     * 表别名与表名相同
     * 
     * @param clazzes
     * @param sqlAfterTable
     * @param values
     * @return
     * @throws QueryException
     */
    public <T> int count(Class<?>[] clazzes, String sqlAfterTable,
            Object[] values) throws QueryException {
        return this.count(clazzes, null, sqlAfterTable, values);
    }

    /**
     * 表别名与表名相同
     * 
     * @param clazzes
     * @param tablePostfix
     * @param sqlAfterTable
     * @param values
     * @return
     * @throws QueryException
     */
    public <T> int count(Class<?>[] clazzes, String[] tablePostfix,
            String sqlAfterTable, Object[] values) throws QueryException {
        StringBuilder sb = new StringBuilder("select count(*) from ");
        int i = 0;
        for (Class<?> clazz : clazzes) {
            EntityTableInfo<T> info = this.getEntityTableInfo(clazz);
            sb.append(info.getTableName());
            if (tablePostfix != null && this.isNotEmpty(tablePostfix[i])) {
                sb.append(tablePostfix[i]);
            }
            sb.append(" ");
            sb.append(",");
            i++;
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(" ");
        sb.append(sqlAfterTable);
        return jdbcSupport.num(sb.toString(), values).intValue();
    }

    @SuppressWarnings("unchecked")
    private <T> EntityTableInfo<T> getEntityTableInfo(Class<?> clazz) {
        return (EntityTableInfo<T>) this.entityTableInfoFactory
                .getEntityTableInfo(clazz);
    }

    @SuppressWarnings("unchecked")
    private <T> SQLMapper<T> getSqlMapper(Class<?> clazz) {
        return (SQLMapper<T>) this.getEntityTableInfo(clazz).getSqlMapper();
    }

    @SuppressWarnings("unchecked")
    public <T> RowMapper<T> getRowMapper(Class<T> clazz) {
        return (RowMapper<T>) this.getEntityTableInfo(clazz).getRowMapper();
    }

    public void setEntityTableInfoFactory(
            EntityTableInfoFactory entityTableInfoFactory) {
        this.entityTableInfoFactory = entityTableInfoFactory;
    }

    public void setJdbcSupport(JdbcSupport jdbcSupport) {
        this.jdbcSupport = jdbcSupport;
    }

    public JdbcSupport getJdbcSupport() {
        return jdbcSupport;
    }

    private boolean isNotEmpty(String tablePostfix) {
        if (tablePostfix != null && tablePostfix.trim().length() > 0) {
            return true;
        }
        return false;
    }
}