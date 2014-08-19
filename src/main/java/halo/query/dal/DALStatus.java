package halo.query.dal;

import java.util.Map;

/**
 * 此类操作当前状态，包括数据源key，用户自定义信息
 *
 * @author akwei
 */
public class DALStatus {

    private static final ThreadLocal<Map<String, Object>> dalParserParametersTL = new ThreadLocal<Map<String, Object>>();

    private static final ThreadLocal<String> dsKeyTL = new ThreadLocal<String>();

    private static final ThreadLocal<DALInfo> dalInfoTL = new ThreadLocal<DALInfo>();

    private DALStatus() {
    }

    public static void setDsKey(String dsKey) {
        dsKeyTL.set(dsKey);
    }

    public static String getDsKey() {
        String key = dsKeyTL.get();
        if (key == null) {
            key = HaloDALDataSource.DEFAULT_DS_NAME;
        }
        return key;
    }

    public static void setParamMap(Map<String, Object> map) {
        dalParserParametersTL.set(map);
    }

    public static Map<String, Object> getParamMap() {
        return dalParserParametersTL.get();
    }

    public static void setDalInfo(DALInfo dalInfo) {
        dalInfoTL.set(dalInfo);
    }

    public static DALInfo getDalInfo() {
        return dalInfoTL.get();
    }

    public static void remove() {
        dalParserParametersTL.remove();
        dsKeyTL.remove();
        dalInfoTL.remove();
    }
}
