package halo.query.idtool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.incrementer.AbstractColumnMaxValueIncrementer;

/**
 * mysql自增表的id必须是myISAM类型，为了保证不同请求都能获得唯一id
 * 
 * @author akwei
 */
public class HaloMySQLMaxValueIncrementer extends
        AbstractColumnMaxValueIncrementer {

	private static final String VALUE_SQL = "select last_insert_id()";

	public HaloMySQLMaxValueIncrementer() {
		super();
	}

	public HaloMySQLMaxValueIncrementer(DataSource dataSource,
	        String incrementerName, String columnName) {
		super(dataSource, incrementerName, columnName);
	}

	@Override
	protected long getNextKey() throws DataAccessException {
		long id = 0;
		Connection con = DataSourceUtils.getConnection(getDataSource());
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			DataSourceUtils.applyTransactionTimeout(stmt, getDataSource());
			String columnName = getColumnName();
			stmt.executeUpdate("update " + getIncrementerName() + " set "
			        + columnName +
			        " = last_insert_id(" + columnName + " + " + getCacheSize()
			        + ")");
			ResultSet rs = stmt.executeQuery(VALUE_SQL);
			try {
				if (!rs.next()) {
					throw new DataAccessResourceFailureException(
					        "last_insert_id() failed after executing an update");
				}
				id = rs.getLong(1);
			}
			finally {
				JdbcUtils.closeResultSet(rs);
			}
		}
		catch (SQLException ex) {
			throw new DataAccessResourceFailureException(
			        "Could not obtain last_insert_id()", ex);
		}
		finally {
			JdbcUtils.closeStatement(stmt);
			DataSourceUtils.releaseConnection(con, getDataSource());
		}
		return id;
	}
}
