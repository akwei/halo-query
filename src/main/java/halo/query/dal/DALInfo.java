package halo.query.dal;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户自定义数据源与表信息
 *
 * @author akwei
 */
public class DALInfo {

    /**
     * 存储对象类型名称与分区后表名称的对应
     */
    private final Map<String, String> tableMap = new HashMap<String, String>();

    /**
     * 与配置文件对应的key
     */
    private String dsKey;

    /**
     * 分区信息的指定方式，可以是手动/自动
     */
    private boolean specify;

    /**
     * 创建一个对象，并设置为手动指定分区模式
     *
     * @return 创建的对象
     */
    public static DALInfo createForManual() {
        DALInfo dalInfo = new DALInfo();
        dalInfo.setSpecify(true);
        return dalInfo;
    }

    /**
     * 是否是手动指定的分区信息
     *
     * @return true/false
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

    /**
     * 设置数据源key
     *
     * @param dsKey 与配置文件一直的dsKey
     */
    public void setDsKey(String dsKey) {
        this.dsKey = dsKey;
    }

    /**
     * getter
     *
     * @return dsKey
     */
    public String getDsKey() {
        return dsKey;
    }

    /**
     * 设置逻辑表与真实表的映射
     *
     * @param cls           对象类型
     * @param realTableName 分区后的表名称
     */
    public void setRealTable(Class<?> cls, String realTableName) {
        tableMap.put(cls.getName(), realTableName);
    }

    /**
     * 设置对象与分区后表名称的对应
     *
     * @param map map
     */
    public void setRealTableMap(Map<Class<?>, String> map) {
        for (Map.Entry<Class<?>, String> entry : map.entrySet()) {
            this.setRealTable(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 获得真实表名
     *
     * @param cls 对象类型
     * @return 分区后的表名称
     */
    public String getRealTable(Class<?> cls) {
        return tableMap.get(cls.getName());
    }
}
