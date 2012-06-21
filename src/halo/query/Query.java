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
        EntityTableInfo<T> info = this.getEntityTableInfo(t.getClass());
        SQLMapper<T> mapper = this.getSqlMapper(t.getClass());
        Object obj = this.jdbcSupport.insert(info.getInsertSQL(),
                mapper.getParamsForInsert(t));
        return (Number) obj;
    }

    public <T> void insert(T t) throws QueryException {
        EntityTableInfo<T> info = this.getEntityTableInfo(t.getClass());
        SQLMapper<T> mapper = this.getSqlMapper(t.getClass());
        this.jdbcSupport.insert(info.getInsertSQL(),
                mapper.getParamsForInsert(t));
    }

    public <T> int update(T t) throws QueryException {
        EntityTableInfo<T> info = this.getEntityTableInfo(t.getClass());
        SQLMapper<T> mapper = this.getSqlMapper(t.getClass());
        return this.jdbcSupport.update(info.getUpdateSQL(),
                mapper.getParamsForUpdate(t));
    }

    public <T> int update(Class<T> clazz, String updateSql, Object[] values)
            throws QueryException {
        EntityTableInfo<T> info = this.getEntityTableInfo(clazz);
        return this.jdbcSupport.update("update " + info.getTableName() + " "
                + updateSql, values);
    }

    public <T> int delete(T t) throws QueryException {
        SQLMapper<T> mapper = this.getSqlMapper(t.getClass());
        return this.deleteById(t.getClass(), mapper.getIdParam(t));
    }

    public <T> int deleteById(Class<T> clazz, Object idValue)
            throws QueryException {
        EntityTableInfo<T> info = this.getEntityTableInfo(clazz);
        return this.jdbcSupport.update(info.getDeleteSQL(),
                new Object[] { idValue });
    }

    public <T> int delete(Class<T> clazz, String sqlAfterTable, Object[] values)
            throws QueryException {
        EntityTableInfo<T> info = this.getEntityTableInfo(clazz);
        String sql = "delete from " + info.getTableName() + " " + sqlAfterTable;
        return this.jdbcSupport.update(sql, values);
    }

    public <T> List<T> list(Class<T> clazz, String sqlAfterTable,
            Object[] values) throws QueryException {
        return this
                .list(clazz, sqlAfterTable, values, this.getRowMapper(clazz));
    }

    public <T> List<T> list(Class<T> clazz, String sqlAfterTable,
            Object[] values, RowMapper<T> rowMapper) throws QueryException {
        return jdbcSupport.list(
                "select * from "
                        + this.getEntityTableInfo(clazz).getTableName() + " "
                        + sqlAfterTable, values, rowMapper);
    }

    public <T> T obj(Class<T> clazz, String sqlAfterTable, Object[] values,
            RowMapper<T> rowMapper) throws QueryException {
        List<T> list = this.list(clazz, sqlAfterTable, values, rowMapper);
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

    public <T> T objById(Class<T> clazz, Object idValue, RowMapper<T> rowMapper)
            throws QueryException {
        EntityTableInfo<T> info = this.getEntityTableInfo(clazz);
        String sqlAfterTable = "where " + info.getIdColumnName() + "=?";
        return this.obj(clazz, sqlAfterTable, new Object[] { idValue },
                rowMapper);
    }

    public <T> T objById(Class<T> clazz, Object idValue) throws QueryException {
        return this.objById(clazz, idValue, this.getRowMapper(clazz));
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
        StringBuilder sb = new StringBuilder("select * from ");
        EntityTableInfo<T> info;
        for (Class<?> clazz : clazzes) {
            info = this.getEntityTableInfo(clazz);
            sb.append(info.getTableName());
            sb.append(" ");
            sb.append(info.getTableName());
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(" ");
        sb.append(sqlAfterTable);
        return jdbcSupport.list(sb.toString(), values, rowMapper);
    }

    public <T> List<T> listMySQL(Class<T> clazz, String sqlAfterTable,
            int begin, int size, Object[] values, RowMapper<T> rowMapper)
            throws QueryException {
        String _sqlAfterTable;
        if (begin < 0 || size < 0) {
            _sqlAfterTable = sqlAfterTable;
        }
        else {
            _sqlAfterTable = sqlAfterTable + " limit " + begin + "," + size;
        }
        return this.list(clazz, _sqlAfterTable, values, rowMapper);
    }

    public <T> List<T> listMySQL(Class<T> clazz, String sqlAfterTable,
            int begin, int size, Object[] values) throws QueryException {
        return this.listMySQL(clazz, sqlAfterTable, begin, size, values,
                this.getRowMapper(clazz));
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
        String _sqlAfterTable;
        if (begin < 0 || size < 0) {
            _sqlAfterTable = sqlAfterTable;
        }
        else {
            _sqlAfterTable = sqlAfterTable + " limit " + begin + "," + size;
        }
        return this.list(clazzes, _sqlAfterTable, values, rowMapper);
    }

    public <T> int count(Class<T> clazz, String sqlAfterFrom, Object[] values)
            throws QueryException {
        EntityTableInfo<T> info = this.getEntityTableInfo(clazz);
        String sql = "select count(*) from " + info.getTableName() + " "
                + sqlAfterFrom;
        return jdbcSupport.num(sql, values).intValue();
    }

    /**
     * 表别名与表名相同
     * 
     * @param clazzes
     * @param aliases
     * @param sqlAfterTable
     * @param values
     * @return
     * @throws QueryException
     */
    public <T> int count(Class<?>[] clazzes, String[] aliases,
            String sqlAfterTable, Object[] values) throws QueryException {
        StringBuilder sb = new StringBuilder("select count(*) from ");
        int i = 0;
        for (Class<?> clazz : clazzes) {
            EntityTableInfo<T> info = this.getEntityTableInfo(clazz);
            sb.append(info.getTableName());
            sb.append(" ");
            sb.append(aliases[i]);
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
}