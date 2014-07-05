package halo.query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.sql.*;
import java.util.List;

/**
 * 使用spring jdbcTemplate来操作sql
 *
 * @author akwei
 */
public class JdbcSupport extends JdbcDaoSupport {

    private final Log log = LogFactory.getLog(JdbcSupport.class);

    public boolean debugSQL;

    public void setDebugSQL(boolean debugSQL) {
        this.debugSQL = debugSQL;
    }

    /**
     * 批量更新。参考spring jdbc 调用方式
     *
     * @param sql
     * @param bpss
     * @return
     */
    public int[] batchUpdate(String sql, BatchPreparedStatementSetter bpss) {
        if (this.debugSQL) {
            this.log("batchUpdate sql [ " + sql + " ]");
        }
        return this.getJdbcTemplate().batchUpdate(sql, bpss);
    }

    /**
     * insert 操作
     *
     * @param sql
     * @param values
     * @param canGetGeneratedKeys true:可以返回自增id，返回值为Number类型.false:返回null
     * @return
     */
    public Object insert(final String sql, final Object[] values,
                         final boolean canGetGeneratedKeys) {
        if (this.debugSQL) {
            this.log("insert sql [ " + sql + " ]");
        }
        return this.getJdbcTemplate().execute(
                new PreparedStatementCreator() {

                    public PreparedStatement createPreparedStatement(
                            Connection con) throws SQLException {
                        if (canGetGeneratedKeys) {
                            return con.prepareStatement(sql,
                                    Statement.RETURN_GENERATED_KEYS);
                        }
                        return con.prepareStatement(sql);
                    }
                }, new PreparedStatementCallback<Object>() {

                    public Object doInPreparedStatement(PreparedStatement ps)
                            throws SQLException, DataAccessException {
                        ResultSet rs = null;
                        try {
                            if (values != null) {
                                int i = 1;
                                for (Object value : values) {
                                    if (value == null) {
                                        // 貌似varchar通用mysql db2
                                        ps.setNull(i++, Types.VARCHAR);
                                    }
                                    else {
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
                        }
                        finally {
                            if (rs != null) {
                                rs.close();
                            }
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
        if (this.debugSQL) {
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
        if (this.debugSQL) {
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
        if (this.debugSQL) {
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
                                }
                                else {
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
