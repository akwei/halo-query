package halo.query.dal;

import halo.query.dal.slave.DefSlaveSelectStrategy;
import halo.query.dal.slave.SlaveSelectStrategy;
import org.springframework.beans.factory.InitializingBean;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

/**
 * 支持分布式数据源访问的数据源。数据源中包含了需要访问的所有真实数据源.<br>
 * 目前不支持单数据源访问<br>
 * 配置的第一个数据源是默认数据源
 *
 * @author akwei
 */
public abstract class HaloDALDataSource implements DataSource, InitializingBean {

    private static HaloDALDataSource instance;

    private final Map<String, HaloDataSourceWrapper> dataSourceMap = new ConcurrentHashMap<>();

    private final Map<String, List<String>> masterSlaveDsKeyMap = new ConcurrentHashMap<>();

    private String defaultDsKey;

    private PrintWriter logWriter;

    private int loginTimeout = 0;

    public static HaloDALDataSource getInstance() {
        return instance;
    }

    private SlaveSelectStrategy slaveSelectStrategy = new DefSlaveSelectStrategy();

    public SlaveSelectStrategy getSlaveSelectStrategy() {
        return slaveSelectStrategy;
    }

    public void setSlaveSelectStrategy(SlaveSelectStrategy slaveSelectStrategy) {
        this.slaveSelectStrategy = slaveSelectStrategy;
    }

    protected void addSlave2Master(String masterDsKey, String slaveDsKey) {
        List<String> list = this.masterSlaveDsKeyMap.get(masterDsKey);
        if (list == null) {
            list = new CopyOnWriteArrayList<>();
            list.add(slaveDsKey);
            this.masterSlaveDsKeyMap.put(masterDsKey, list);
        } else {
            list.add(slaveDsKey);
        }
    }

    protected boolean setSlaves2Master(String masterDsKey, List<String> slaveDsKeys) {
        if (slaveDsKeys != null && slaveDsKeys.size() > 0) {
            this.masterSlaveDsKeyMap.put(masterDsKey, new CopyOnWriteArrayList<>(slaveDsKeys));
            return true;
        }
        return false;
    }


    String getDefaultDsKey() {
        return defaultDsKey;
    }

    /**
     * 获得当可用的数据源，如果没有指定，获得默认的数据源
     *
     * @return 数据源包装类
     */
    HaloDataSourceProxy getCurrentDataSourceProxy() {
        String master = DALStatus.getDsKey();
        String slave = null;
        if (DALStatus.isEnableSlave()) {
            slave = DALStatus.getSlaveDsKey();
            if (slave == null) {
                List<String> slaveDsKeys = this.masterSlaveDsKeyMap.get(master);
                List<String> copyList = null;
                if (slaveDsKeys != null) {
                    copyList = new ArrayList<>();
                    copyList.addAll(slaveDsKeys);
                }
                slave = this.slaveSelectStrategy.parse(master, copyList);
                if (slave != null) {
                    DALStatus.setSlaveDsKey(slave);
                }
            }
        }
        String name;
        if (slave == null) {
            name = master;
        } else {
            name = slave;
        }
        HaloDataSourceWrapper haloDataSourceWrapper = this.dataSourceMap.get(name);
        if (haloDataSourceWrapper == null) {
            throw new DALRunTimeException("no datasource forKey [" + name + "]");
        }
        HaloDataSourceProxy proxy = new HaloDataSourceProxy();
        proxy.setDataSourceWrapper(haloDataSourceWrapper);
        proxy.setMaster(master);
        proxy.setSlave(slave);
        return proxy;
    }

    /**
     * 设置默认的数据源key
     *
     * @param defaultDsKey 默认数据源key
     */
    public void setDefaultDsKey(String defaultDsKey) {
        this.defaultDsKey = defaultDsKey;
    }

    protected void addDataSource(HaloDataSourceWrapper haloDataSourceWrapper) {
        this.dataSourceMap.put(haloDataSourceWrapper.getDsKey(), haloDataSourceWrapper);
    }

    public Connection getConnection() throws SQLException {
        DALConnection connection = new DALConnection(this);
        DALStatus.setCurrentDALConnection(connection);
        if (DALConnectionListenerFactory.hasListener()) {
            for (DALConnectionListener listener : DALConnectionListenerFactory.getInstance().getDalConnectionListeners()) {
                listener.onDALOpened();
            }
        }
        return connection;
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

    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
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
        throw new SQLException("unsupported unwrap");
    }

    public void destory() {
        Set<Map.Entry<String, HaloDataSourceWrapper>> set = this.dataSourceMap.entrySet();
        for (Map.Entry<String, HaloDataSourceWrapper> e : set) {
            HaloDataSourceUtil.destory(e.getValue());
        }
    }

    public void afterPropertiesSet() throws Exception {
        instance = this;
        if (this.defaultDsKey != null) {
            DataSource ds = this.dataSourceMap.get(this.defaultDsKey);
            if (ds == null) {
                throw new RuntimeException("default ds must be not empty");
            }
        }
    }

    public List<HaloDataSourceWrapper> getDataSources() {
        if (this.dataSourceMap.isEmpty()) {
            return new ArrayList<>(0);
        }
        return new ArrayList<>(this.dataSourceMap.values());
    }

    /**
     * 加载数据源，并指定当前数据源为 masterDsKey 的 slave数据源
     *
     * @param ctxMap      数据
     * @param masterDsKey 当前数据源为指定的 masterDsKey 的slave数据源
     */
    public abstract void loadDataSource(Map<String, Object> ctxMap, String masterDsKey);

    /**
     * 删除数据源
     *
     * @param dsKey 数据源key
     */
    public void removeDataSource(String dsKey) {
        HaloDataSourceWrapper dataSourceWrapper = this.dataSourceMap.remove(dsKey);
        if (dataSourceWrapper != null) {
            HaloDataSourceUtil.destory(dataSourceWrapper);
        }

        Collection<List<String>> values = this.masterSlaveDsKeyMap.values();
        for (List<String> keys : values) {
            for (String key : keys) {
                if (key.equals(dsKey)) {
                    keys.remove(key);
                }
            }
        }
    }

    public List<String> getSlaveDsKeys(String masterDsKey) {
        return this.masterSlaveDsKeyMap.get(masterDsKey);
    }
}