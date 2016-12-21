package halo.query.dal;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

/**
 * 数据源包装类
 * Created by akwei on 6/18/15.
 */
public class HaloDataSourceWrapper implements DataSource {

//    private static Logger logger = Logger.getLogger(HaloDataSourceWrapper.class);

//    /**
//     * 连接池是否被停用,默认没有被停用
//     */
//    private boolean discarded = false;

//    private AtomicInteger counter = new AtomicInteger(0);

    private String dsKey;

    private DataSource dataSource;

    private String refDsKey;

    private String db;

    public HaloDataSourceWrapper(String dsKey, DataSource dataSource, String refDsKey, String db) {
        this.dsKey = dsKey;
        this.dataSource = dataSource;
        this.refDsKey = refDsKey;
        this.db = db;
    }

    public String getDb() {
        return db;
    }

    public String getRefDsKey() {
        return refDsKey;
    }

    public boolean isRef() {
        return this.refDsKey != null;
    }

    public String getDsKey() {
        return dsKey;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

//    public boolean isDiscarded() {
//        return discarded;
//    }
//
//    public void setDiscarded(boolean discarded) {
//        this.discarded = discarded;
//    }

    @Override
    public Connection getConnection() throws SQLException {
        Connection con = this.dataSource.getConnection();
//        this.incrCounter();
        return new HaloConnectionWrapper(con, this);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return this.dataSource.getConnection(username, password);
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return this.dataSource.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return this.dataSource.isWrapperFor(iface);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return this.dataSource.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        this.dataSource.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        this.dataSource.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return this.dataSource.getLoginTimeout();
    }

    @Override
    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return this.dataSource.getParentLogger();
    }

//    public void incrCounter() {
//        this.counter.incrementAndGet();
//    }

//    public void decrCounter() {
//        this.counter.decrementAndGet();
//    }
}
