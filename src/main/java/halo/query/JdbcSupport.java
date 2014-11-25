package halo.query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 使用spring jdbcTemplate来操作sql
 *
 * @author akwei
 */
public class JdbcSupport extends JdbcDaoSupport {

    private final Log log = LogFactory.getLog(JdbcSupport.class);

    /**
     * 批量更新。参考spring jdbc 调用方式
     *
     * @param sql
     * @param bpss
     * @return
     */
    public int[] batchUpdate(String sql, BatchPreparedStatementSetter bpss) {
        if (HaloQueryDebugInfo.getInstance().isEnableDebug()) {
            this.log("batchUpdate sql [ " + sql + " ]");
        }
        return this.getJdbcTemplate().batchUpdate(sql, bpss);
    }

    public int[] batchUpdate(final String sql, final List<Object[]> valuesList) {
        if (HaloQueryDebugInfo.getInstance().isEnableDebug()) {
            this.log("batch update sql [ " + sql + " ]");
        }
        if (valuesList == null || valuesList.isEmpty()) {
            throw new RuntimeException("batchInsert valuesList is empty");
        }
        return this.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Object[] values = valuesList.get(i);
                if (values != null) {
                    int k = 1;
                    for (Object value : values) {
                        if (value == null) {
                            // 貌似varchar通用mysql db2
                            ps.setNull(k++, Types.VARCHAR);
                        } else {
                            ps.setObject(k++, value);
                        }
                    }
                }
            }

            @Override
            public int getBatchSize() {
                return valuesList.size();
            }
        });
    }

    /**
     * insert 操作
     *
     * @param sql                 batch sql
     * @param valuesList          对应数据
     * @param canGetGeneratedKeys true:可以返回自增id，返回值为Number类型.false:返回null
     * @return
     */
    public List<Number> batchInsert(final String sql, final List<Object[]> valuesList, final boolean canGetGeneratedKeys) {
        if (HaloQueryDebugInfo.getInstance().isEnableDebug()) {
            this.log("batch insert sql [ " + sql + " ]");
        }
        if (valuesList == null || valuesList.isEmpty()) {
            throw new RuntimeException("batchInsert valuesList is empty");
        }
        return this.getJdbcTemplate().execute(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                if (canGetGeneratedKeys) {
                    return con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                }
                return con.prepareStatement(sql);
            }
        }, new PreparedStatementCallback<List<Number>>() {
            public List<Number> doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                ResultSet rs = null;
                try {
                    for (Object[] values : valuesList) {
                        if (values != null) {
                            int i = 1;
                            for (Object value : values) {
                                if (value == null) {
                                    // 貌似varchar通用mysql db2
                                    ps.setNull(i++, Types.VARCHAR);
                                } else {
                                    ps.setObject(i++, value);
                                }
                            }
                        }
                        ps.addBatch();
                    }
                    ps.executeBatch();
                    List<Number> numbers = new ArrayList<Number>();
                    if (canGetGeneratedKeys) {
                        rs = ps.getGeneratedKeys();
                        while (rs.next()) {
                            Number n = (Number) rs.getObject(1);
                            numbers.add(n);
                        }
                        return numbers;
                    }
                    return numbers;
                } finally {
                    JdbcUtils.closeResultSet(rs);
                }
            }
        });
    }

    /**
     * insert 操作
     *
     * @param sql
     * @param values
     * @param canGetGeneratedKeys true:可以返回自增id，返回值为Number类型.false:返回null
     * @return
     */
    public Object insert(final String sql, final Object[] values, final boolean canGetGeneratedKeys) {
        if (HaloQueryDebugInfo.getInstance().isEnableDebug()) {
            this.log("insert sql [ " + sql + " ]");
        }
        return this.getJdbcTemplate().execute(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                if (canGetGeneratedKeys) {
                    return con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                }
                return con.prepareStatement(sql);
            }
        }, new PreparedStatementCallback<Object>() {

            public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                ResultSet rs = null;
                try {
                    if (values != null) {
                        int i = 1;
                        for (Object value : values) {
                            if (value == null) {
                                // 貌似varchar通用mysql db2
                                ps.setNull(i++, Types.VARCHAR);
                            } else {
                                ps.setObject(i++, value);
                            }
                        }
                    }
                    ps.executeUpdate();
                    if (canGetGeneratedKeys) {
                        rs = ps.getGeneratedKeys();
                        if (rs.next()) {
                            return rs.getObject(1);
                        }
                        return 0;
                    }
                    return null;
                } finally {
                    JdbcUtils.closeResultSet(rs);
                }
            }
        });
    }

    /**
     * 查询集合
     *
     * @param sql
     * @param values    参数
     * @param rowMapper spring {@link RowMapper} 子类
     * @return
     */
    public <T> List<T> list(String sql, Object[] values, RowMapper<T> rowMapper) {
        if (HaloQueryDebugInfo.getInstance().isEnableDebug()) {
            this.log("list sql [ " + sql + " ]");
        }
        return this.getJdbcTemplate().query(sql, values, rowMapper);
    }

    /**
     * 查询并返回数字类型,如果没有符合条件的数据返回0
     *
     * @param sql
     * @param values 参数
     * @return 如果没有符合条件的数据，返回0
     */
    public Number num(String sql, Object[] values) {
        if (HaloQueryDebugInfo.getInstance().isEnableDebug()) {
            this.log("num sql [ " + sql + " ]");
        }
        return this.getJdbcTemplate().queryForObject(sql, values,
                Number.class);
    }

    /**
     * 更新操作,返回被更新的数据数量
     *
     * @param sql
     * @param values 参数
     * @return
     */
    public int update(String sql, final Object[] values) {
        if (HaloQueryDebugInfo.getInstance().isEnableDebug()) {
            this.log("update sql [ " + sql + " ]");
        }
        return this.getJdbcTemplate().update(sql,
                new PreparedStatementSetter() {

                    public void setValues(PreparedStatement ps)
                            throws SQLException {
                        if (values != null) {
                            int i = 1;
                            for (Object value : values) {
                                if (value == null) {
                                    // 貌似varchar通用mysql db2
                                    ps.setNull(i++, Types.VARCHAR);
                                } else {
                                    ps.setObject(i++, value);
                                }
                            }
                        }
                    }
                });
    }

    /**
     * 进行log输出
     *
     * @param v
     */
    protected void log(String v) {
        log.info(v);
    }
}
