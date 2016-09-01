package halo.query.dal;

import halo.query.HaloConfig;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据源包装类
 * Created by akwei on 6/18/15.
 */
public class HaloDataSourceWrapper {

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

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection getConnection() throws SQLException {
        long begin = System.currentTimeMillis();
        Connection con = this.dataSource.getConnection();
        long end = System.currentTimeMillis();
        int result = (int) (end - begin);
        if (HaloConfig.getInstance().isSlowCon(result)) {
            logger.warn("master[" + master + "] slave[" + slave + "] getcon slow time:" + result);
        }
        return con;
    }
}
