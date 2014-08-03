package halo.query.dal;

import halo.datasource.HaloC3p0PropertiesDataSourceWrapper;
import halo.datasource.HaloDataSource;
import halo.query.mslb.MSLBC3p0PropertiesDataSource;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Created by akwei on 7/13/14.
 */
public class HaloDALC3p0PropertiesDataSource extends HaloDALDataSource {

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    private Map<String, String> createCfgMap(String name) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(name);
        Set<String> keySet = resourceBundle.keySet();
        Map<String, String> map = new HashMap<String, String>();
        for (String key : keySet) {
            String value = resourceBundle.getString(key);
            map.put(key, value);
        }
        return map;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(this.name);
        Set<String> keySet = resourceBundle.keySet();
        Map<String, Object> map = new HashMap<String, Object>();
        for (String key : keySet) {
            String value = resourceBundle.getString(key);
            if (key.endsWith(".ds")) {
                Map<String, String> vmap = this.createCfgMap(value);
                map.put(key, vmap);
            }
            else {
                map.put(key, value);
            }
        }
        this.create(map);
        super.afterPropertiesSet();
    }

    public void create(Map<String, Object> map) {
        String dsKeys = (String) map.get("dal");
        if (dsKeys == null || dsKeys.trim().length() == 0) {
            throw new RuntimeException("must has dal=[dbKey]");
        }
        Map<String, HaloDataSource> dsMap = new HashMap<String, HaloDataSource>();
        String[] dsKeyArr = dsKeys.split(",");
        for (String dsKey : dsKeyArr) {
            Map<String, Object> vmap = (Map<String, Object>) map.get(dsKey + ".ms");
            if (vmap != null) {
                MSLBC3p0PropertiesDataSource dataSource = new MSLBC3p0PropertiesDataSource();
                dataSource.create(vmap);
                dsMap.put(dsKey, dataSource);
            }
            else {
                HaloC3p0PropertiesDataSourceWrapper dataSourceWrapper = new HaloC3p0PropertiesDataSourceWrapper();
                dataSourceWrapper.create(dsKey, map);
                dsMap.put(dsKey, dataSourceWrapper);
            }
        }
        this.setDataSourceMap(dsMap);
    }
}
