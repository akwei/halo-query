package halo.query.dal;

import halo.query.HaloConfig;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 数据源包装类
 * Created by akwei on 6/18/15.
 */
public class HaloDataSourceWrapper implements DataSource {

    /**
     * 连接池是否被停用,默认没有被停用
     */
    private boolean discard = false;

    private AtomicInteger counter = new AtomicInteger(0);

    private static Logger logger = Logger.getLogger(HaloDataSourceWrapper.class);

    private String master;

    private String slave;

    private DataSource dataSource;

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getSlave() {
        return slave;
    }

    public void setSlave(String slave) {
        this.slave = slave;
    }

    public boolean isDiscard() {
        return discard;
    }

    public void setDiscard(boolean discard) {
        this.discard = discard;
    }

    public HaloDataSourceWrapper(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection getConnection() throws SQLException {
        long begin = System.currentTimeMillis();
        Connection con = this.dataSource.getConnection();
        this.incrCounter();
        long end = System.currentTimeMillis();
        int result = (int) (end - begin);
        if (HaloConfig.getInstance().isSlowCon(result)) {
            try {
                logger.warn("master[" + master + "] slave[" + slave + "] getcon slow time:" + result);
            } catch (Exception e) {
                //ingore while logger write err
            }
        }
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

    public void incrCounter() {
        this.counter.incrementAndGet();
    }

    public void decrCounter() {
        this.counter.decrementAndGet();
    }
}
