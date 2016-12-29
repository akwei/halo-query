package halo.query.dal;

import halo.query.JsonUtil;

import javax.sql.DataSource;
import java.text.MessageFormat;
import java.util.*;

/**
 * 属性配置文件的数据源
 * Created by akwei on 9/15/16.
 */
@SuppressWarnings("unchecked")
public class HaloPropertiesDataSource extends HaloDALDataSource {

    /**
     * 设置数据源的className
     */
    private String dataSourceClassName;

    private static final String GLOBAL_KEY = "global.";

    private static final String DEFAULT_KEY = "default";

    private static final String DS_SLAVE_KEY = "ds_slave";

    private static final String URL_KEY = "url";

    private static final String JDBCURL_KEY = "jdbcUrl";

    private static final String REF_DSKEY_KEY = "ref";

    private static final String DB_KEY = "db";

    private final Map<String, String> GLOBAL_CONFIG_MAP = new HashMap<>();

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(this.name);
        Set<String> keySet = resourceBundle.keySet();
        Map<String, String> map = new HashMap<>();
        for (String key : keySet) {
            String value = resourceBundle.getString(key);
            if (key.startsWith(GLOBAL_KEY)) {
                GLOBAL_CONFIG_MAP.put(key.substring(GLOBAL_KEY.length()), value);
                continue;
            }
            if (key.equals(DEFAULT_KEY)) {
                this.setDefaultDsKey(value);
            } else {
                map.put(key, value);
            }
        }

        for (Map.Entry<String, String> e : map.entrySet()) {
            String dsKey = e.getKey();
            Map<String, Object> cfgMap = (Map<String, Object>) JsonUtil.parse(e.getValue(), Map.class);
            this.addDataSource(this.createDataSource(dsKey, cfgMap));
        }
        super.afterPropertiesSet();
    }

    @Override
    public void loadDataSource(Map<String, Object> ctxMap, String masterDsKey) {
        String dsKey = (String) ctxMap.get("dsKey");
        Map<String, Object> cfgMap = (Map<String, Object>) ctxMap.get("cfgMap");
        this.addDataSource(this.createDataSource(dsKey, cfgMap));
        this.addSlave2Master(masterDsKey, dsKey);
    }

    private HaloDataSourceWrapper createDataSource(String dsKey, Map<String, Object> cfgMap) {
        if (cfgMap == null) {
            throw new IllegalArgumentException("dsKey[" + dsKey + "] config must be not empty");
        }
        if (cfgMap.containsKey(REF_DSKEY_KEY)) {
            return this.createRefDataSource(dsKey, cfgMap);
        }
        if (!cfgMap.containsKey(URL_KEY) && !cfgMap.containsKey(JDBCURL_KEY)) {
            return this.createSlaveModeDataSource(dsKey, cfgMap);
        }
        return this.createNormalDataSource(dsKey, cfgMap);
    }

    private HaloDataSourceWrapper createRefDataSource(String dsKey, Map<String, Object> cfgMap) {
        Map<String, Object> _cfgMap = new HashMap<>(cfgMap);
        List<String> slaveDsKeys = (List<String>) _cfgMap.get(DS_SLAVE_KEY);
        if (slaveDsKeys != null && slaveDsKeys.size() > 0) {
            this.setSlaves2Master(dsKey, slaveDsKeys);
        }
        String refDsKey = (String) cfgMap.get(REF_DSKEY_KEY);
        String db = (String) cfgMap.get(DB_KEY);
        return new HaloDataSourceWrapper(dsKey, null, refDsKey, db, false);
    }

    private HaloDataSourceWrapper createSlaveModeDataSource(String dsKey, Map<String, Object> cfgMap) {
        Map<String, Object> _cfgMap = new HashMap<>(cfgMap);
        List<String> slaveDsKeys = (List<String>) _cfgMap.get(DS_SLAVE_KEY);
        if (slaveDsKeys != null && slaveDsKeys.size() > 0) {
            this.setSlaves2Master(dsKey, slaveDsKeys);
        }
        return new HaloDataSourceWrapper(dsKey, null, null, null, true);
    }

    private HaloDataSourceWrapper createNormalDataSource(String dsKey, Map<String, Object> cfgMap) {
        Map<String, Object> _cfgMap = new HashMap<>(cfgMap);
        List<String> slaveDsKeys = (List<String>) _cfgMap.get(DS_SLAVE_KEY);
        if (slaveDsKeys != null && slaveDsKeys.size() > 0) {
            this.setSlaves2Master(dsKey, slaveDsKeys);
        }
        _cfgMap.remove(DS_SLAVE_KEY);
        for (Map.Entry<String, String> entry : GLOBAL_CONFIG_MAP.entrySet()) {
            String propertyKey = entry.getKey();
            if (propertyKey.equals(JDBCURL_KEY) || propertyKey.equals(URL_KEY)) {
                continue;
            }
            Object value = _cfgMap.get(propertyKey);
            if (value == null) {
                _cfgMap.put(propertyKey, entry.getValue());
            }
        }

        //jdbcUrl
        String prvJdbcUrl = (String) _cfgMap.get(JDBCURL_KEY);
        if (HaloDataSourceUtil.isEmpty(prvJdbcUrl)) {
            String globalJdbcUrl = GLOBAL_CONFIG_MAP.get(JDBCURL_KEY);
            if (globalJdbcUrl == null) {
                throw new IllegalArgumentException("global.jdbcUrl or [db].jdbcUrl must be not empty");
            }
            String prvUrl = (String) _cfgMap.get(URL_KEY);
            Object jdbcUrl;
            if (HaloDataSourceUtil.isNotEmpty(prvUrl)) {
                jdbcUrl = this.buildJdbcUrl(prvUrl, globalJdbcUrl);
            } else {
                jdbcUrl = globalJdbcUrl;
            }
            _cfgMap.put(JDBCURL_KEY, jdbcUrl);
        }
        _cfgMap.remove(URL_KEY);
        DataSource dataSource = HaloDataSourceUtil.createDataSource(this.dataSourceClassName, _cfgMap);
        return new HaloDataSourceWrapper(dsKey, dataSource, null, null, false);
    }

    private String buildJdbcUrl(String url, String globalJdbcUrlTpl) {
        if (HaloDataSourceUtil.isEmpty(url)) {
            throw new IllegalArgumentException("url must be not null");
        }
        if (HaloDataSourceUtil.isEmpty(globalJdbcUrlTpl)) {
            throw new IllegalArgumentException("globalJdbcUrlTpl must be not null");
        }
        return MessageFormat.format(globalJdbcUrlTpl, url);
    }

    public String getDataSourceClassName() {
        return dataSourceClassName;
    }

    public void setDataSourceClassName(String dataSourceClassName) {
        this.dataSourceClassName = dataSourceClassName;
    }
}
