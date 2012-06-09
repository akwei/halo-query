package halo.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

public class JdbcSupport extends SimpleJdbcDaoSupport {

    private final Log log = LogFactory.getLog(JdbcSupport.class);

    public boolean debugSQL;

    public void setDebugSQL(boolean debugSQL) {
        this.debugSQL = debugSQL;
    }

    public int[] batchUpdate(String sql, BatchPreparedStatementSetter bpss)
            throws QueryException {
        if (this.debugSQL) {
            this.log("batchUpdate sql [ " + sql + " ]");
        }
        try {
            return this.getJdbcTemplate().batchUpdate(sql, bpss);
        }
        catch (DataAccessException e) {
            throw new QueryException(e);
        }
    }

    public Object insert(final String sql, final Object[] values)
            throws QueryException {
        if (this.debugSQL) {
            this.log("insert sql [ " + sql + " ]");
        }
        try {
            return this.getJdbcTemplate().execute(
                    new PreparedStatementCreator() {

                        public PreparedStatement createPreparedStatement(
                                Connection con) throws SQLException {
                            return con.prepareStatement(sql,
                                    Statement.RETURN_GENERATED_KEYS);
                        }
                    }, new PreparedStatementCallback<Object>() {

                        public Object doInPreparedStatement(PreparedStatement ps)
                                throws SQLException, DataAccessException {
                            ResultSet rs = null;
                            try {
                                if (values != null) {
                                    int i = 1;
                                    for (Object value : values) {
                                        ps.setObject(i++, value);
                                    }
                                }
                                ps.executeUpdate();
                                rs = ps.getGeneratedKeys();
                                if (rs.next()) {
                                    return rs.getObject(1);
                                }
                                return 0;
                            }
                            finally {
                                if (rs != null) {
                                    rs.close();
                                }
                            }
                        }
                    });
        }
        catch (DataAccessException e) {
            throw new QueryException(e);
        }
    }

    public <T> List<T> list(String sql, Object[] values, RowMapper<T> rowMapper)
            throws QueryException {
        if (this.debugSQL) {
            this.log("queryForNumber sql [ " + sql + " ]");
        }
        try {
            return this.getJdbcTemplate().query(sql, values, rowMapper);
        }
        catch (DataAccessException e) {
            throw new QueryException(e);
        }
    }

    public Number num(String sql, Object[] values) throws QueryException {
        if (this.debugSQL) {
            this.log("queryForNumber sql [ " + sql + " ]");
        }
        try {
            return this.getJdbcTemplate().queryForObject(sql, values,
                    Number.class);
        }
        catch (DataAccessException e) {
            throw new QueryException(e);
        }
    }

    public int update(String sql, final Object[] values) throws QueryException {
        if (this.debugSQL) {
            this.log("update sql [ " + sql + " ]");
        }
        try {
            return this.getJdbcTemplate().update(sql,
                    new PreparedStatementSetter() {

                        public void setValues(PreparedStatement ps)
                                throws SQLException {
                            if (values != null) {
                                for (int i = 0; i < values.length; i++) {
                                    ps.setObject(i + 1, values[i]);
                                }
                            }
                        }
                    });
        }
        catch (DataAccessException e) {
            throw new QueryException(e);
        }
    }

    protected void log(String v) {
        log.info(v);
    }
}
