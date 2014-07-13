package halo.query.mslb;

import halo.datasource.HaloDataSource;
import halo.query.HaloQueryMSLDBDebugInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

/**
 * 实现负载均衡使用的数据源
 * Created by akwei on 7/5/14.
 */
public class MSLBDataSource extends HaloDataSource {
    private static final Log log = LogFactory.getLog(MSLBDataSource.class);
    /**
     * 主数据源
     */
    protected HaloDataSource masterDataSource;

    /**
     * 提供只读数据源，不可写入，不能进行insert update delete操作
     */
    protected List<HaloDataSource> slaveDataSources;

    /**
     * 设置只读数据，不可写入，不能进行insert update delete操作
     *
     * @param slaveDataSources
     */
    public void setSlaveDataSources(List<HaloDataSource> slaveDataSources) {
        this.slaveDataSources = slaveDataSources;
    }

    /**
     * 设置主数据源
     *
     * @param masterDataSource
     */
    public void setMasterDataSource(HaloDataSource masterDataSource) {
        this.masterDataSource = masterDataSource;
    }

    /**
     * 获得可用的读操作数据源，当没有可用的slave数据源时，直接使用master数据源
     *
     * @return
     */
    public synchronized HaloDataSource getRandomSlaveDataSource() {
        if (this.slaveDataSources == null || this.slaveDataSources.isEmpty()) {
            if (HaloQueryMSLDBDebugInfo.getInstance().isEnableDebug()) {
                log.info("slave datasource is empty.will return master datasource");
            }
            return this.getMasterDataSource();
        }
        Random random = new Random();
        int index = random.nextInt(this.slaveDataSources.size());
        if (HaloQueryMSLDBDebugInfo.getInstance().isEnableDebug()) {
            log.info("will return slave datasource " + index);
        }
        return this.slaveDataSources.get(index);
    }

    public HaloDataSource getMasterDataSource() {
        return masterDataSource;
    }

    public List<HaloDataSource> getSlaveDataSources() {
        return slaveDataSources;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return new MSLBConnection(this);
    }

    @Override
    public Connection getConnection(String s, String s2) throws SQLException {
        throw new RuntimeException("could not support getConnection(String s, String s2)");
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter printWriter) throws SQLException {
    }

    @Override
    public void setLoginTimeout(int i) throws SQLException {
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public <T> T unwrap(Class<T> tClass) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> aClass) throws SQLException {
        return false;
    }
}
