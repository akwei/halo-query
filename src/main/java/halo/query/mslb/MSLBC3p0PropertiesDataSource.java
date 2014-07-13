package halo.query.mslb;

import halo.datasource.HaloC3p0PropertiesDataSourceWrapper;
import halo.datasource.HaloDataSource;
import org.springframework.beans.factory.InitializingBean;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by akwei on 7/12/14.
 */
public class MSLBC3p0PropertiesDataSource extends MSLBDataSource implements InitializingBean {
    private String name;
    private String[] mastersKeys;
    private String[] slaveKeys;
    private final Map<String, String> cfgMap = new HashMap<String, String>();
    private final Map<String, HaloC3p0PropertiesDataSourceWrapper> dataSourceMap = new HashMap<String, HaloC3p0PropertiesDataSourceWrapper>();

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(this.name);
        Set<String> set = resourceBundle.keySet();
        Map<String, String> map = new HashMap<String, String>();
        for (String key : set) {
            String value = resourceBundle.getString(key);
            map.put(key, value);
        }
        this.create(map);
    }

    public void create(Map<String, String> map) {
        this.cfgMap.putAll(map);
        String masters = this.cfgMap.get("masters");
        String slaves = this.cfgMap.get("slaves");
        if (masters == null || masters.trim().length() == 0) {
            throw new RuntimeException(this.name + ".properties+ must has masters value");
        }
        if (slaves == null || slaves.trim().length() == 0) {
            throw new RuntimeException(this.name + ".properties+ must has slaves value");
        }
        this.mastersKeys = masters.split(",");
        this.slaveKeys = slaves.split(",");
        this.addDataSourceToMasters(null);
        if (this.slaveKeys != null) {
            this.slaveDataSources = new ArrayList<HaloDataSource>();
            for (int i = 0; i < this.slaveKeys.length; i++) {
                this.addDataSourceToSlaves(this.slaveKeys[i], null);
            }
        }
    }

    /**
     * 添加数据源到master中
     *
     * @param map 数据源的配置
     */
    public synchronized void addDataSourceToMasters(Map<String, String> map) {
        if (map != null) {
            this.cfgMap.putAll(map);
        }
        this.setMasterDataSource(this.createDataSource(this.mastersKeys[0]));
    }

    /**
     * 添加数据源到slave中
     *
     * @param prefix slave前缀
     * @param map    数据源的配置
     */
    public synchronized void addDataSourceToSlaves(String prefix, Map<String, String> map) {
        if (map != null) {
            this.cfgMap.putAll(map);
        }
        HaloDataSource dataSource = this.createDataSource(prefix);
        this.slaveDataSources.add(dataSource);
    }

    /**
     * @param prefix
     * @return
     */
    private synchronized HaloDataSource createDataSource(String prefix) {
        HaloC3p0PropertiesDataSourceWrapper dataSourceWrapper = new HaloC3p0PropertiesDataSourceWrapper();
        dataSourceWrapper.create(prefix, this.cfgMap);
        this.dataSourceMap.put(prefix, dataSourceWrapper);
        return dataSourceWrapper;
    }

    /**
     * 销毁连接池
     *
     * @param key 连接池key
     * @throws SQLException
     */
    public synchronized void destroyByKey(String key) throws SQLException {
        HaloC3p0PropertiesDataSourceWrapper dataSource = this.dataSourceMap.get(key);
        if (dataSource != null) {
            this.dataSourceMap.remove(key);
            if (this.masterDataSource.equals(dataSource)) {
                this.setMasterDataSource(null);
            }
            else {
                for (DataSource ds : this.slaveDataSources) {
                    if (ds.equals(dataSource)) {
                        this.slaveDataSources.remove(ds);
                        break;
                    }
                }
            }
            dataSource.destory();
        }
    }

    public void destory() {
        HaloC3p0PropertiesDataSourceWrapper masterWrapper = (HaloC3p0PropertiesDataSourceWrapper) this.getMasterDataSource();
        masterWrapper.destory();
        List<HaloDataSource> dsList = this.getSlaveDataSources();
        for (HaloDataSource ds : dsList) {
            HaloC3p0PropertiesDataSourceWrapper slaveWrapper = (HaloC3p0PropertiesDataSourceWrapper) ds;
            slaveWrapper.destory();
        }
    }
}

