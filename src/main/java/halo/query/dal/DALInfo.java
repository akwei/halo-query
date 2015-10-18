package halo.query.dal;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户自定义数据源与表信息
 *
 * @author akwei
 */
public class DALInfo {

    private final Map<String, String> tableMap = new HashMap<String, String>();

    private String dsKey;

    /**
     * 分区信息的指定方式，可以是手动/自动
     */
    private boolean specify;

    /**
     * 是否是手动指定的分区信息
     *
     * @return
     */
    public boolean isSpecify() {
        return specify;
    }

    /**
     * 设置分区信息指定方式,当指定手动时，将要执行的sql操作不再进行默认的解析器操作，完全以指定的方式为准
     *
     * @param specify false:自动 true:手动
     */
    public void setSpecify(boolean specify) {
        this.specify = specify;
    }

    public void setDsKey(String dsKey) {
        this.dsKey = dsKey;
    }

    public String getDsKey() {
        return dsKey;
    }

    /**
     * 设置逻辑表与真实表的映射
     *
     * @param cls
     * @param realTableName
     */
    public void setRealTable(Class<?> cls, String realTableName) {
        tableMap.put(cls.getName(), realTableName);
    }

    public void setRealTableMap(Map<Class<?>, String> map) {
        for (Map.Entry<Class<?>, String> entry : map.entrySet()) {
            this.setRealTable(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 获得真实表名
     *
     * @param cls
     * @return
     */
    public String getRealTable(Class<?> cls) {
        return tableMap.get(cls.getName());
    }

    public String getRealTable(String key) {
        return tableMap.get(key);
    }

//    public void addFromDALInfo(DALInfo dalInfo) {
//        this.tableMap.putAll(dalInfo.tableMap);
//        if (dalInfo.getDsKey() != null) {
//            this.setDsKey(dalInfo.getDsKey());
//        }
//    }
}
