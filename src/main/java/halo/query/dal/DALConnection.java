package halo.query.dal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 支持分布式数据源访问的Connection，此类暂时不支持非PreparedStatement方式分布式读写。
 *
 * @author akwei
 */
public class DALConnection implements Connection {

    /**
     * 存储实际的连接，可以保存多个
     */
    private final LinkedHashMap<String, Connection> conMap = new LinkedHashMap<String, Connection>();
    private final Log logger = LogFactory.getLog(DALConnection.class);
    private boolean autoCommit = true;
    private int transactionIsolation = Connection.TRANSACTION_NONE;
    private int holdability = 0;
    private boolean readOnly = false;
    private DALDataSource dalDataSource;

    public DALConnection(DALDataSource dalDataSource) throws SQLException {
        this.dalDataSource = dalDataSource;
        this.setAutoCommit(true);
    }

    public void clearWarnings() throws SQLException {
        this.getCurrentConnection().clearWarnings();
    }

    public void close() throws SQLException {
        try {
            Collection<Connection> c = this.conMap.values();
            for (Connection con : c) {
                con.close();
                if (this.dalDataSource.isDebugInfo()) {
                    logger.info("close real connection [" + con + "]");
                }
            }
        } finally {
            DALStatus.remove();
        }
    }

    public void commit() throws SQLException {
        Collection<Connection> c = this.conMap.values();
        for (Connection con : c) {
            con.commit();
            if (this.dalDataSource.isDebugInfo()) {
                logger.info("commit real connection [" + con + "]");
            }
        }
    }

    public Statement createStatement() throws SQLException {
        return this.getCurrentConnection().createStatement();
    }

    /**
     * 获得当前需要使用的Connection
     *
     * @return
     */
    public Connection getCurrentConnection() {
        String name = DALStatus.getDsKey();
        Connection con = this.conMap.get(name);
        if (con == null) {
            try {
                con = this.dalDataSource.getCurrentConnection();
                this.initCurrentConnection(con);
                this.conMap.put(name, con);
            } catch (SQLException e) {
                throw new DALRunTimeException(e);
            }
        }
        return con;
    }

    /**
     * 是否当前有可用的真实数据库链接
     *
     * @return
     */
    private boolean hasCurrentConnection() {
        String name = DALStatus.getDsKey();
        if (name == null || name.length() == 0) {
            return false;
        }
        return this.conMap.containsKey(name);
    }

    private void initCurrentConnection(Connection con) throws SQLException {
        if (!this.autoCommit) {
            con.setAutoCommit(this.autoCommit);
        }
        if (this.transactionIsolation != Connection.TRANSACTION_NONE) {
            con.setTransactionIsolation(this.transactionIsolation);
        }
        con.setReadOnly(this.readOnly);
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency)
            throws SQLException {
        return this.getCurrentConnection().createStatement(resultSetType,
                resultSetConcurrency);
    }

    public Statement createStatement(int resultSetType,
                                     int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
        return this.getCurrentConnection().createStatement(resultSetType,
                resultSetConcurrency, resultSetHoldability);
    }

    public boolean getAutoCommit() throws SQLException {
        if (this.hasCurrentConnection()) {
            return this.getCurrentConnection().getAutoCommit();
        }
        return this.autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) throws SQLException {
        this.autoCommit = autoCommit;
        if (this.hasCurrentConnection()) {
            this.getCurrentConnection().setAutoCommit(autoCommit);
        }
    }

    public int getHoldability() throws SQLException {
        return this.getCurrentConnection().getHoldability();
    }

    public void setHoldability(int holdability) throws SQLException {
        this.getCurrentConnection().setHoldability(holdability);
    }

    public DatabaseMetaData getMetaData() throws SQLException {
        return this.getCurrentConnection().getMetaData();
    }

    public int getTransactionIsolation() throws SQLException {
        if (this.hasCurrentConnection()) {
            return this.getCurrentConnection().getTransactionIsolation();
        }
        return this.transactionIsolation;
    }

    public void setTransactionIsolation(int level) throws SQLException {
        this.transactionIsolation = level;
        if (this.hasCurrentConnection()) {
            this.getCurrentConnection().setTransactionIsolation(level);
        }
    }

    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return this.getCurrentConnection().getTypeMap();
    }

    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        this.getCurrentConnection().getTypeMap();
    }

    public SQLWarning getWarnings() throws SQLException {
        if (this.hasCurrentConnection()) {
            return this.getCurrentConnection().getWarnings();
        }
        return null;
    }

    public boolean isClosed() throws SQLException {
        if (this.hasCurrentConnection()) {
            return this.getCurrentConnection().isClosed();
        }
        return true;
    }

    public boolean isReadOnly() throws SQLException {
        if (this.hasCurrentConnection()) {
            return this.getCurrentConnection().isReadOnly();
        }
        return this.readOnly;
    }

    public void setReadOnly(boolean readOnly) throws SQLException {
        this.readOnly = readOnly;
        if (this.hasCurrentConnection()) {
            this.getCurrentConnection().setReadOnly(readOnly);
        }
    }

    public String nativeSQL(String sql) throws SQLException {
        if (this.hasCurrentConnection()) {
            return this.getCurrentConnection().nativeSQL(sql);
        }
        return sql;
    }

    public CallableStatement prepareCall(String sql) throws SQLException {
        return this.getCurrentConnection().prepareCall(sql);
    }

    public CallableStatement prepareCall(String sql, int resultSetType,
                                         int resultSetConcurrency) throws SQLException {
        return this.getCurrentConnection().prepareCall(sql, resultSetType,
                resultSetConcurrency);
    }

    public CallableStatement prepareCall(String sql, int resultSetType,
                                         int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
        return this.getCurrentConnection().prepareCall(sql, resultSetType,
                resultSetConcurrency, resultSetHoldability);
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return this.getCurrentConnection().prepareStatement(sql);
    }

    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
            throws SQLException {
        return this.getCurrentConnection().prepareStatement(sql,
                autoGeneratedKeys);
    }

    public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
            throws SQLException {
        return this.getCurrentConnection().prepareStatement(sql, columnIndexes);
    }

    public PreparedStatement prepareStatement(String sql, String[] columnNames)
            throws SQLException {
        return this.getCurrentConnection().prepareStatement(sql, columnNames);
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType,
                                              int resultSetConcurrency) throws SQLException {
        return this.getCurrentConnection().prepareStatement(sql, resultSetType,
                resultSetConcurrency);
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType,
                                              int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
        return this.getCurrentConnection().prepareStatement(sql, resultSetType,
                resultSetConcurrency, resultSetHoldability);
    }

    public void rollback() throws SQLException {
        Collection<Connection> c = this.conMap.values();
        for (Connection con : c) {
            con.rollback();
        }
    }

    public String getCatalog() throws SQLException {
        return this.getCurrentConnection().getCatalog();
    }

    public void setCatalog(String catalog) throws SQLException {
        this.getCurrentConnection().setCatalog(catalog);
    }

    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        throw new SQLException("dal do not support releaseSavepoint(Savepoint savepoint)");
    }

    public void rollback(Savepoint savepoint) throws SQLException {
        throw new SQLException("dal do not support rollback(Savepoint savepoint)");
    }

    public Savepoint setSavepoint() throws SQLException {
        throw new SQLException("dal do not support setSavepoint()");
    }

    public Savepoint setSavepoint(String name) throws SQLException {
        throw new SQLException("dal do not support setSavepoint(String name)");
    }

    public Array createArrayOf(String typeName, Object[] elements)
            throws SQLException {
        return this.getCurrentConnection().createArrayOf(typeName, elements);
    }

    public Blob createBlob() throws SQLException {
        return this.getCurrentConnection().createBlob();
    }

    public Clob createClob() throws SQLException {
        return this.getCurrentConnection().createClob();
    }

    public NClob createNClob() throws SQLException {
        return this.getCurrentConnection().createNClob();
    }

    public SQLXML createSQLXML() throws SQLException {
        return this.getCurrentConnection().createSQLXML();
    }

    public Struct createStruct(String typeName, Object[] attributes)
            throws SQLException {
        return this.getCurrentConnection().createStruct(typeName, attributes);
    }

    public Properties getClientInfo() throws SQLException {
        return this.getCurrentConnection().getClientInfo();
    }

    public void setClientInfo(Properties properties)
            throws SQLClientInfoException {
        this.getCurrentConnection().setClientInfo(properties);
    }

    public String getClientInfo(String name) throws SQLException {
        return this.getCurrentConnection().getClientInfo(name);
    }

    public boolean isValid(int timeout) throws SQLException {
        return this.getCurrentConnection().isValid(timeout);
    }

    public void setClientInfo(String name, String value)
            throws SQLClientInfoException {
        this.getCurrentConnection().setClientInfo(name, value);
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return this.getCurrentConnection().isWrapperFor(iface);
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        return this.getCurrentConnection().unwrap(iface);
    }
}