package halo.query.dal;

import java.util.HashMap;
import java.util.Map;

/**
 * 方便设置分区参数的类
 * Created by akwei on 11/7/15.
 */
public class DALContext {

    private boolean enableSlave;

    private String slaveDsKey;

    private DALInfo dalInfo;

    public String getSlaveDsKey() {
        return slaveDsKey;
    }

    public boolean isSetSlaveDsKey() {
        if (this.slaveDsKey != null) {
            return true;
        }
        return false;
    }

    public void setSlaveDsKey(String slaveDsKey) {
        this.slaveDsKey = slaveDsKey;
    }

    private Map<String, Object> paramMap = new HashMap<String, Object>();

    public DALInfo getDalInfo() {
        return dalInfo;
    }

    public void setDalInfo(DALInfo dalInfo) {
        this.dalInfo = dalInfo;
    }

    public void addParam(String key, Object value) {
        this.paramMap.put(key, value);
    }

    public boolean isEnableSlave() {
        return enableSlave;
    }

    public void setEnableSlave(boolean enableSlave) {
        this.enableSlave = enableSlave;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public boolean isParamMapEmpty() {
        return this.paramMap.isEmpty();
    }

    public void setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }
}
