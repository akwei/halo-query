package halo.query.mslb;

import java.sql.*;
import java.util.*;

/**
 * 支持分布式数据源访问的Connection，此类暂时不支持非PreparedStatement方式分布式读写。
 *
 * @author akwei
 */
public class MSLBConnection implements Connection {

    /**
     * 存储调用的方法的参数
     */
    private List<Map<String, Object>> methodInvokedList = new ArrayList<Map<String, Object>>(4);
    private static final int METHODINDEX_SETTRANSACTIONISOLATION = 2;
    private static final int METHODINDEX_SETREADONLY = 4;
    private static final int METHODINDEX_SETAUTOCOMMIT = 6;

    private Connection connection;
    private boolean autoCommit = true;
    private int transactionIsolation = Connection.TRANSACTION_NONE;
    private boolean readOnly = false;
    private MSLBDataSource mslbDataSource;
    private static final String KEY_SELECT = "select";
    private String catalog;

    public MSLBConnection(MSLBDataSource mslbDataSource) throws SQLException {
        this.mslbDataSource = mslbDataSource;
        this.setAutoCommit(true);
    }

    public Connection getConnection() {
        if (this.connection == null) {
            throw new RuntimeException("could not init real connection");
        }
        return connection;
    }

    /**
     * 是否当前有可用的真实数据库链接
     *
     * @return
     */
    private boolean hasCurrentConnection() {
        return this.connection != null;
    }

    private void addInvoke(int methodIndex, Object[] args) {
        Map<String, Object> map = new HashMap<String, Object>(2);
        map.put("method_index", methodIndex);
        map.put("args", args);
        this.methodInvokedList.add(map);
    }

    /**
     * 根据sql类型选择实际使用的Connection.
     * 只要是select开头,并且在非事物操作的情况下,就可以自动选择slave数据源.
     * 对于insert update delete 只能使用master数据源
     *
     * @param sql 执行的sql
     */
    private void selectConnection(String sql) throws SQLException {
        if (this.connection != null) {
            return;
        }
        boolean canAutoSelectSlave = false;
        if (this.isReadOnly()) {
            String sub = sql.substring(0, 6);
            if (sub.equalsIgnoreCase(KEY_SELECT)) {
                canAutoSelectSlave = true;
            }
        }
        else {
            Boolean readFlag = MSLBStatus.get();
            if (readFlag == null || readFlag.booleanValue() == false) {
                canAutoSelectSlave = false;
            }
            else {
                canAutoSelectSlave = true;
            }
        }
        if (canAutoSelectSlave) {
            this.connection = this.mslbDataSource.getRandomSlaveDataSource().getConnection();
        }
        else {
            this.connection = this.mslbDataSource.getMasterDataSource().getConnection();
        }
        this.initCurrentConnection(this.getConnection());
    }

    private void initCurrentConnection(Connection con) throws SQLException {
        for (Map<String, Object> map : this.methodInvokedList) {
            int methodIndex = (Integer) map.get("method_index");
            Object[] args = (Object[]) map.get("args");
            this.invoke(con, methodIndex, args);
        }
    }

    private void invoke(Connection con, int methodIndex, Object[] args) throws SQLException {
        switch (methodIndex) {
            case METHODINDEX_SETAUTOCOMMIT: {
                con.setAutoCommit(((Boolean) args[0]));
                break;
            }
            case METHODINDEX_SETREADONLY: {
                con.setReadOnly(((Boolean) args[0]));
                break;
            }
            case METHODINDEX_SETTRANSACTIONISOLATION: {
                con.setTransactionIsolation(((Integer) args[0]));
                break;
            }
        }
    }

    public void clearWarnings() throws SQLException {
        this.getConnection().clearWarnings();
    }

    public void close() throws SQLException {
        MSLBStatus.remove();
        this.getConnection().close();
    }

    public void commit() throws SQLException {
        this.getConnection().commit();
    }

    public Statement createStatement() throws SQLException {
        throw new RuntimeException("createStatement is not supported");
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency)
            throws SQLException {
        throw new RuntimeException("createStatement is not supported");
    }

    public Statement createStatement(int resultSetType,
                                     int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
        throw new RuntimeException("createStatement is not supported");
    }

    public boolean getAutoCommit() throws SQLException {
        if (this.hasCurrentConnection()) {
            return this.getConnection().getAutoCommit();
        }
        return this.autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) throws SQLException {
        this.autoCommit = autoCommit;
        if (this.hasCurrentConnection()) {
            this.getConnection().setAutoCommit(autoCommit);
        }
        else {
            this.addInvoke(METHODINDEX_SETAUTOCOMMIT, new Object[]{autoCommit});
        }
    }

    public int getHoldability() throws SQLException {
        return this.getConnection().getHoldability();
    }

    public void setHoldability(int holdability) throws SQLException {
        this.getConnection().setHoldability(holdability);
    }

    public DatabaseMetaData getMetaData() throws SQLException {
        return this.getConnection().getMetaData();
    }

    public int getTransactionIsolation() throws SQLException {
        if (this.hasCurrentConnection()) {
            return this.getConnection().getTransactionIsolation();
        }
        return this.transactionIsolation;
    }

    public void setTransactionIsolation(int level) throws SQLException {
        this.transactionIsolation = level;
        if (this.hasCurrentConnection()) {
            this.getConnection().setTransactionIsolation(level);
        }
        else {
            this.addInvoke(METHODINDEX_SETTRANSACTIONISOLATION, new Object[]{level});
        }
    }

    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return this.getConnection().getTypeMap();
    }

    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        this.getConnection().getTypeMap();
    }

    public SQLWarning getWarnings() throws SQLException {
        if (this.hasCurrentConnection()) {
            return this.getConnection().getWarnings();
        }
        return null;
    }

    public boolean isClosed() throws SQLException {
        if (this.hasCurrentConnection()) {
            return this.getConnection().isClosed();
        }
        return true;
    }

    public boolean isReadOnly() throws SQLException {
        if (this.hasCurrentConnection()) {
            return this.getConnection().isReadOnly();
        }
        return this.readOnly;
    }

    public void setReadOnly(boolean readOnly) throws SQLException {
        this.readOnly = readOnly;
        if (this.hasCurrentConnection()) {
            this.getConnection().setReadOnly(readOnly);
        }
        else {
            this.addInvoke(METHODINDEX_SETREADONLY, new Object[]{readOnly});
        }
    }

    public String nativeSQL(String sql) throws SQLException {
        if (this.hasCurrentConnection()) {
            return this.getConnection().nativeSQL(sql);
        }
        return sql;
    }

    public CallableStatement prepareCall(String sql) throws SQLException {
        this.selectConnection(sql);
        return this.getConnection().prepareCall(sql);
    }

    public CallableStatement prepareCall(String sql, int resultSetType,
                                         int resultSetConcurrency) throws SQLException {
        this.selectConnection(sql);
        return this.getConnection().prepareCall(sql, resultSetType,
                resultSetConcurrency);
    }

    public CallableStatement prepareCall(String sql, int resultSetType,
                                         int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
        this.selectConnection(sql);
        return this.getConnection().prepareCall(sql, resultSetType,
                resultSetConcurrency, resultSetHoldability);
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        this.selectConnection(sql);
        return this.getConnection().prepareStatement(sql);
    }

    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
            throws SQLException {
        this.selectConnection(sql);
        return this.getConnection().prepareStatement(sql,
                autoGeneratedKeys);
    }

    public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
            throws SQLException {
        this.selectConnection(sql);
        return this.getConnection().prepareStatement(sql, columnIndexes);
    }

    public PreparedStatement prepareStatement(String sql, String[] columnNames)
            throws SQLException {
        this.selectConnection(sql);
        return this.getConnection().prepareStatement(sql, columnNames);
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType,
                                              int resultSetConcurrency) throws SQLException {
        this.selectConnection(sql);
        return this.getConnection().prepareStatement(sql, resultSetType,
                resultSetConcurrency);
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType,
                                              int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
        this.selectConnection(sql);
        return this.getConnection().prepareStatement(sql, resultSetType,
                resultSetConcurrency, resultSetHoldability);
    }

    public void rollback() throws SQLException {
        this.getConnection().rollback();
    }

    public String getCatalog() throws SQLException {
        if (this.hasCurrentConnection()) {
            return this.getConnection().getCatalog();
        }
        return this.catalog;
    }

    public void setCatalog(String catalog) throws SQLException {
        if (this.hasCurrentConnection()) {
            this.getConnection().setCatalog(catalog);
        }
        this.catalog = catalog;
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
        return this.getConnection().createArrayOf(typeName, elements);
    }

    public Blob createBlob() throws SQLException {
        return this.getConnection().createBlob();
    }

    public Clob createClob() throws SQLException {
        return this.getConnection().createClob();
    }

    public NClob createNClob() throws SQLException {
        return this.getConnection().createNClob();
    }

    public SQLXML createSQLXML() throws SQLException {
        return this.getConnection().createSQLXML();
    }

    public Struct createStruct(String typeName, Object[] attributes)
            throws SQLException {
        return this.getConnection().createStruct(typeName, attributes);
    }

    public Properties getClientInfo() throws SQLException {
        return this.getConnection().getClientInfo();
    }

    public void setClientInfo(Properties properties)
            throws SQLClientInfoException {
        this.getConnection().setClientInfo(properties);
    }

    public String getClientInfo(String name) throws SQLException {
        return this.getConnection().getClientInfo(name);
    }

    public boolean isValid(int timeout) throws SQLException {
        return this.getConnection().isValid(timeout);
    }

    public void setClientInfo(String name, String value)
            throws SQLClientInfoException {
        this.getConnection().setClientInfo(name, value);
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return this.getConnection().isWrapperFor(iface);
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        return this.getConnection().unwrap(iface);
    }
}