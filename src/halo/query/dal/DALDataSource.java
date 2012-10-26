package halo.query.dal;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;

import javax.sql.DataSource;

/**
 * 支持分布式数据源访问的数据源。数据源中包含了需要访问的所有真实数据源.<br>
 * 目前不支持单数据源访问<br>
 * 配置的第一个数据源是默认数据源
 * 
 * @author akwei
 */
public class DALDataSource implements DataSource {

	public static final String DEFAULT_DS_NAME = "default_ds";

	private LinkedHashMap<String, DataSource> dataSourceMap;

	private PrintWriter logWriter;

	private int loginTimeout = 3;

	private boolean debugConnection;

	public void setDebugConnection(boolean debugConnection) {
		this.debugConnection = debugConnection;
	}

	public boolean isDebugConnection() {
		return debugConnection;
	}

	/**
	 * 获得当可用的数据源，如果没有指定，获得默认的数据源
	 * 
	 * @return
	 */
	private DataSource getCurrentDataSource() {
		String name = DALStatus.getDsKey();
		if (name == null) {
			DataSource ds = this.dataSourceMap.values().iterator().next();
			if (ds == null) {
				throw new DALRunTimeException("can not get datasource");
			}
			return ds;
		}
		DataSource ds = this.dataSourceMap.get(name);
		if (ds == null) {
			throw new DALRunTimeException("no datasource forKey [ " + name
			        + " ]");
		}
		return ds;
	}

	/**
	 * 设定数据源key与真实数据源的对应关系.<br>
	 * map中的key为数据源key,value为真实数据源
	 * 
	 * @param dataSourceMap
	 */
	public void setDataSourceMap(LinkedHashMap<String, DataSource> dataSourceMap) {
		this.dataSourceMap = dataSourceMap;
	}

	public Connection getConnection() throws SQLException {
		return new DALConnection(this);
	}

	/**
	 * 获得当前可用的连接
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Connection getCurrentConnection() throws SQLException {
		return this.getCurrentDataSource().getConnection();
	}

	public Connection getConnection(String username, String password)
	        throws SQLException {
		throw new SQLException("only support getConnection()");
	}

	public PrintWriter getLogWriter() throws SQLException {
		return this.logWriter;
	}

	public int getLoginTimeout() throws SQLException {
		return this.loginTimeout;
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
		this.logWriter = out;
	}

	public void setLoginTimeout(int seconds) throws SQLException {
		this.loginTimeout = seconds;
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		return this.getCurrentDataSource().unwrap(iface);
	}
}