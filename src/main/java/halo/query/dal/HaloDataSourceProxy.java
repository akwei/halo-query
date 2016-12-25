package halo.query.dal;

import halo.query.HaloConfig;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据源代理类，在获取Connection时使用，不会长久存储到内存中
 * Created by akwei on 9/17/16.
 */
class HaloDataSourceProxy {

    private static Logger logger = Logger.getLogger(HaloDataSourceProxy.class);

    private String master;

    private String slave;

    /**
     * 当前数据源引用其他数据源时，需要设置db为实际的数据库名称
     */
    private String db;

    private HaloDataSourceWrapper dataSourceWrapper;

    String getDb() {
        return db;
    }

    void setDb(String db) {
        this.db = db;
    }

    String getMaster() {
        return master;
    }

    void setMaster(String master) {
        this.master = master;
    }

    String getSlave() {
        return slave;
    }

    void setSlave(String slave) {
        this.slave = slave;
    }

    void setDataSourceWrapper(HaloDataSourceWrapper dataSourceWrapper) {
        this.dataSourceWrapper = dataSourceWrapper;
    }

    Connection getConnection() throws SQLException {
        long begin = System.currentTimeMillis();
        Connection con = this.dataSourceWrapper.getConnection();
        long end = System.currentTimeMillis();
        int result = (int) (end - begin);
        if (HaloConfig.getInstance().isSlowCon(result)) {
            try {
                logger.warn("master[" + master + "] slave[" + slave + "] getcon slow time:" + result);
            } catch (Exception ignored) {
            }
        }
        return con;
    }
}
