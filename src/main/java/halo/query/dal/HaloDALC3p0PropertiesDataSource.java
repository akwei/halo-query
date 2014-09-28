package halo.query.dal;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import halo.datasource.JsonUtil;

import javax.sql.DataSource;
import java.util.*;

/**
 * 通过properties文件创建dal使用的数据源
 * Created by akwei on 7/13/14.
 */
public class HaloDALC3p0PropertiesDataSource extends HaloDALDataSource {

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(this.name);
        Set<String> keySet = resourceBundle.keySet();
        Map<String, String> map = new HashMap<String, String>();
        for (String key : keySet) {
            String value = resourceBundle.getString(key);
            map.put(key, value);
        }
        this.create(map);
        super.afterPropertiesSet();
    }

    public void create(Map<String, String> map) {
        Set<Map.Entry<String, String>> set = map.entrySet();
        Map<String, DataSource> dsMap = new HashMap<String, DataSource>();
        for (Map.Entry<String, String> e : set) {
            if (e.getKey().equals("default")) {
                this.setDefaultDsKey(e.getValue());
            }
            else {
                Map<String, Object> cfgMap = (Map<String, Object>) JsonUtil.parse(e.getValue(),
                        Map.class);
                DataSource dataSource = this.createDataSource(cfgMap);
                dsMap.put(e.getKey(), dataSource);
            }
        }
        this.setDataSourceMap(dsMap);
    }

    private DataSource createDataSource(Map<String, Object> cfgMap) {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        Set<Map.Entry<String, Object>> set = cfgMap.entrySet();
        for (Map.Entry<String, Object> entry : set) {
            if (entry.getKey().equals("ds_slave")) {
                this.masterSlaveDsKeyMap.put(entry.getKey(),
                        (List<String>) entry.getValue());
                continue;
            }
            String key = entry.getKey();
            String methodName = C3p0DataSourceUtil.createSetterMethodName(key);
            C3p0DataSourceUtil.methodInvoke(dataSource, methodName, entry.getValue());
        }
        return dataSource;
    }
}
