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

/**
 * 使用spring jdbcTemplate来操作sql
 * 
 * @author akwei
 */
public class JdbcSupport extends SimpleJdbcDaoSupport {

	private final Log log = LogFactory.getLog(JdbcSupport.class);

	public boolean debugSQL;

	public void setDebugSQL(boolean debugSQL) {
		this.debugSQL = debugSQL;
	}

	public int[] batchUpdate(String sql, BatchPreparedStatementSetter bpss)
	{
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
							        ps.setObject(i++, value);
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

	public Object insert(final String sql, final Object[] values) {
		return this.insert(sql, values, true);
	}

	public <T> List<T> list(String sql, Object[] values, RowMapper<T> rowMapper)
	{
		if (this.debugSQL) {
			this.log("queryForNumber sql [ " + sql + " ]");
		}
		return this.getJdbcTemplate().query(sql, values, rowMapper);
	}

	public Number num(String sql, Object[] values) {
		if (this.debugSQL) {
			this.log("queryForNumber sql [ " + sql + " ]");
		}
		return this.getJdbcTemplate().queryForObject(sql, values,
		        Number.class);
	}

	public int update(String sql, final Object[] values) {
		if (this.debugSQL) {
			this.log("update sql [ " + sql + " ]");
		}
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

	protected void log(String v) {
		log.info(v);
	}
}
