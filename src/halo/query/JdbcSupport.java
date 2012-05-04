package halo.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

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

    public Object insertBySQL(String sql, Object[] values)
            throws QueryException {
        if (this.debugSQL) {
            this.log("insert sql [ " + sql + " ]");
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = this.getConnection();
        try {
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
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
        catch (SQLException e) {
            JdbcUtils.closeStatement(ps);
            ps = null;
            DataSourceUtils.releaseConnection(con, getDataSource());
            con = null;
            throw new QueryException(e);
        }
        finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(con, getDataSource());
        }
    }

    public <T> List<T> listBySQL(String sql, Object[] values,
            RowMapper<T> rowMapper) throws QueryException {
        if (this.debugSQL) {
            this.log("query sql [ " + sql + " ]");
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = this.getConnection();
        try {
            ps = con.prepareStatement(sql);
            if (values != null) {
                for (int i = 0; i < values.length; i++) {
                    ps.setObject(i + 1, values[i]);
                }
            }
            rs = ps.executeQuery();
            int rowNum = 1;
            List<T> list = new ArrayList<T>();
            while (rs.next()) {
                list.add(rowMapper.mapRow(rs, rowNum++));
            }
            return list;
        }
        catch (SQLException e) {
            JdbcUtils.closeStatement(ps);
            ps = null;
            DataSourceUtils.releaseConnection(con, getDataSource());
            con = null;
            throw new QueryException(e);
        }
        finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(con, getDataSource());
        }
    }

    public Number numBySQL(String sql, Object[] values) throws QueryException {
        if (this.debugSQL) {
            this.log("queryForNumber sql [ " + sql + " ]");
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = this.getConnection();
        try {
            ps = con.prepareStatement(sql);
            if (values != null) {
                for (int i = 0; i < values.length; i++) {
                    ps.setObject(i + 1, values[i]);
                }
            }
            rs = ps.executeQuery();
            int size = 0;
            Number res = 0;
            while (rs.next()) {
                res = (Number) rs.getObject(1);
                size++;
            }
            if (size == 0) {
                return res;
            }
            if (size > 1) {
                throw new IncorrectResultSizeDataAccessException(1, size);
            }
            return res;
        }
        catch (SQLException e) {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(ps);
            ps = null;
            DataSourceUtils.releaseConnection(con, getDataSource());
            con = null;
            throw new QueryException(e);
        }
        finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(ps);
            ps = null;
            DataSourceUtils.releaseConnection(con, getDataSource());
            con = null;
        }
    }

    public int updateBySQL(String sql, Object[] values) throws QueryException {
        if (this.debugSQL) {
            this.log("update sql [ " + sql + " ]");
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = this.getConnection();
        try {
            ps = con.prepareStatement(sql);
            if (values != null) {
                for (int i = 0; i < values.length; i++) {
                    ps.setObject(i + 1, values[i]);
                }
            }
            return ps.executeUpdate();
        }
        catch (SQLException e) {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(ps);
            ps = null;
            DataSourceUtils.releaseConnection(con, getDataSource());
            con = null;
            throw new QueryException(e);
        }
        finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(ps);
            ps = null;
            DataSourceUtils.releaseConnection(con, getDataSource());
            con = null;
        }
    }

    protected void log(String v) {
        log.info(v);
    }
}
