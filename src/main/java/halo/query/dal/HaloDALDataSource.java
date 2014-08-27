package halo.query.dal;

import halo.datasource.HaloDataSource;
import halo.query.HaloQueryDALDebugInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * 支持分布式数据源访问的数据源。数据源中包含了需要访问的所有真实数据源.<br>
 * 目前不支持单数据源访问<br>
 * 配置的第一个数据源是默认数据源
 *
 * @author akwei
 */
public class HaloDALDataSource extends HaloDataSource implements InitializingBean {

    public static final String DEFAULT_DS_NAME = "default_ds";

    private Map<String, HaloDataSource> dataSourceMap;

    private String defaultDsKey;

    private PrintWriter logWriter;

    private int loginTimeout = 0;

    private final Log logger = LogFactory.getLog(HaloDALDataSource.class);

    /**
     * 获得当可用的数据源，如果没有指定，获得默认的数据源
     *
     * @return
     */
    private DataSource getCurrentDataSource() {
        String name = DALStatus.getDsKey();
        HaloDataSource ds = this.dataSourceMap.get(name);
        if (ds == null) {
            throw new DALRunTimeException("no datasource forKey [" + name + "]");
        }
        if (HaloQueryDALDebugInfo.getInstance().isEnableDebug()) {
            logger.info("get real datasource from dsKey [" + name + "]");
        }
        return ds;
    }

    public void afterPropertiesSet() throws Exception {
        HaloDataSource ds = null;
        if (this.defaultDsKey != null) {
            ds = this.dataSourceMap.get(this.defaultDsKey);
        }
        if (ds == null) {
            if (this.dataSourceMap.size() == 1) {
                ds = this.dataSourceMap.values().iterator().next();
            }
        }
        if (ds == null) {
            throw new DALRunTimeException("datasource empty");
        }
        this.dataSourceMap.put(DEFAULT_DS_NAME, ds);
    }

    /**
     * 设置默认的数据源key
     *
     * @param defaultDsKey 默认数据源key
     */
    public void setDefaultDsKey(String defaultDsKey) {
        this.defaultDsKey = defaultDsKey;
    }

    /**
     * 设定数据源key与真实数据源的对应关系.<br>
     * map中的key为数据源key,value为真实数据源
     *
     * @param dataSourceMap 数据源的map
     */
    public void setDataSourceMap(Map<String, HaloDataSource> dataSourceMap) {
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