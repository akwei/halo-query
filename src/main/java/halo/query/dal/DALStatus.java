package halo.query.dal;

import java.util.HashMap;
import java.util.Map;

/**
 * 此类操作当前状态，包括数据源key，用户自定义信息
 *
 * @author akwei
 */
public class DALStatus {

    private static final ThreadLocal<Map<String, Object>> dalParserParametersTL = new ThreadLocal<Map<String, Object>>();

    private static final ThreadLocal<Boolean> mslbStatusThreadLocal = new ThreadLocal<Boolean>();

    private static final ThreadLocal<DALInfo> dalInfoTL = new ThreadLocal<DALInfo>();

    private static final ThreadLocal<String> msDsKeyTL = new ThreadLocal<String>();

    private DALStatus() {
    }

    public static String getDsKey() {
        String key = null;
        DALInfo dalInfo = DALStatus.getDalInfo();
        if (dalInfo != null) {
            key = dalInfo.getDsKey();
        }
        if (key == null) {
            key = HaloDALDataSource.getInstance().getDefaultDsKey();
        }
        return key;
    }

    public static String getSlaveOrMasterDsKey() {
        String slave = getSlaveDsKey();
        if (slave != null) {
            return slave;
        }
        return getDsKey();
    }

    public static void setSlaveDsKey(String dsKey) {
        msDsKeyTL.set(dsKey);
    }

    public static String getSlaveDsKey() {
        return msDsKeyTL.get();
    }

    private static void setParamMap(Map<String, Object> map) {
        dalParserParametersTL.set(map);
    }

    public static void addParam(String key, Object value) {
        Map<String, Object> map = dalParserParametersTL.get();
        if (map == null) {
            map = new HashMap<String, Object>();
            dalParserParametersTL.set(map);
        }
        map.put(key, value);
    }

    public static Map<String, Object> getParamMap() {
        return dalParserParametersTL.get();
    }

    /**
     * 设置是否开启slave模式
     */
    public static void setSlaveMode() {
        mslbStatusThreadLocal.set(true);
    }

    /**
     * 当前是否支持slave
     *
     * @return
     */
    public static boolean isEnableSlave() {
        Boolean v = mslbStatusThreadLocal.get();
        if (v == null) {
            return false;
        }
        return v;
    }

    public static void setDalInfo(DALInfo dalInfo) {
        dalInfoTL.set(dalInfo);
    }

    public static DALInfo getDalInfo() {
        return dalInfoTL.get();
    }

    public static void remove() {
        dalParserParametersTL.remove();
        mslbStatusThreadLocal.remove();
        msDsKeyTL.remove();
        dalInfoTL.remove();
    }
}
