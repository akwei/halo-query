package halo.query.dal;

import java.util.HashMap;
import java.util.Map;

/**
 * 分区context
 * Created by akwei on 11/7/15.
 */
public class DALContext {

    private boolean enableSlave;

    private String slaveDsKey;

    private DALInfo dalInfo;

    private Map<String, Object> paramMap = new HashMap<String, Object>();

    public static DALContext create() {
        return new DALContext();
    }

    public String getSlaveDsKey() {
        return slaveDsKey;
    }

    /**
     * 是否设置slaveDsKey
     *
     * @return true/false
     */
    public boolean isSetSlaveDsKey() {
        if (this.slaveDsKey != null) {
            return true;
        }
        return false;
    }

    /**
     * 手动设置slaveDsKey
     *
     * @param slaveDsKey dsKey
     */
    public void setSlaveDsKey(String slaveDsKey) {
        this.slaveDsKey = slaveDsKey;
    }

    /**
     * 获得分区信息
     *
     * @return 手动设定的分区信息
     */
    public DALInfo getDalInfo() {
        return dalInfo;
    }

    /**
     * 设置指定分区信息
     *
     * @param dalInfo dal
     */
    public void setDalInfo(DALInfo dalInfo) {
        this.dalInfo = dalInfo;
    }

    /**
     * 添加分区解析时需要的参数 {@link DALParser#parse(Map)} 使用
     *
     * @param key   key
     * @param value value
     */
    public void addParam(String key, Object value) {
        this.paramMap.put(key, value);
    }

    /**
     * 是否设置了slave查询
     *
     * @return true/false
     */
    public boolean isEnableSlave() {
        return enableSlave;
    }

    /**
     * 是否开启slave查询
     *
     * @param enableSlave true/false
     */
    public void setEnableSlave(boolean enableSlave) {
        this.enableSlave = enableSlave;
    }

    /**
     * getter
     *
     * @return map
     */
    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    /**
     * 是否设置了分区使用的参数
     *
     * @return true/false
     */
    public boolean isParamMapEmpty() {
        return this.paramMap.isEmpty();
    }

}
