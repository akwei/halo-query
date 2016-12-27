package halo.query.dal;

import java.util.HashMap;
import java.util.Map;

/**
 * 此类操作当前状态，包括数据源key，用户自定义信息
 *
 * @author akwei
 */
public class DALStatus {

    private static final ThreadLocal<Map<String, Object>> dalParserParametersTL = new ThreadLocal<>();

    private static final ThreadLocal<Boolean> mslbStatusThreadLocal = new ThreadLocal<>();

    private static final ThreadLocal<DALInfo> dalInfoTL = new ThreadLocal<>();

    private static final ThreadLocal<String> msDsKeyTL = new ThreadLocal<>();

    private static final ThreadLocal<DALConnection> currentDALConTL = new ThreadLocal<>();

    /**
     * 全局使用slave模式,需要调用主动清除
     */
    private static final ThreadLocal<Boolean> globalSlaveTL = new ThreadLocal<>();

    private DALStatus() {
    }

    /**
     * 是否存在dal解析参数
     *
     * @return true:存在
     */
    public static boolean hasDALParam() {
        return dalParserParametersTL.get() != null;
    }

    /**
     * 是否存在dalinfo
     *
     * @return true:存在
     */
    public static boolean hasDALInfo() {
        return dalInfoTL.get() != null;
    }

    /**
     * 是否存在master slave dsKey
     *
     * @return true:存在
     */
    public static boolean hasMsDsKey() {
        return msDsKeyTL.get() != null;
    }

    /**
     * 是否存在当前未释放的DALConnection
     *
     * @return true:存在
     */
    public static boolean hasCurrentDALCon() {
        return currentDALConTL.get() != null;
    }

    /**
     * 是否存在全局slave设置
     *
     * @return true:存在
     */
    public static boolean hasGlobalSlave() {
        return globalSlaveTL.get() != null;
    }

    /**
     * 是否存在slave设置
     *
     * @return true:存在
     */
    public static boolean hasMslbStatus() {
        return mslbStatusThreadLocal.get() != null;
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

    /**
     * 设置全局启用slave模式,此设置不会跟随Connection关闭而释放,需要手动释放
     */
    public static void setGlobalSlaveMode() {
        globalSlaveTL.set(true);
    }

    /**
     * 是否是全局slave模式
     *
     * @return true:开启了全局slave,所有查询可以走slave数据源
     */
    public static boolean isEnableGlobalSlaveMode() {
        Boolean bool = globalSlaveTL.get();
        if (bool == null) {
            return false;
        }
        return bool;
    }

    public static void clearGlobalSlaveMode() {
        globalSlaveTL.remove();
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
            map = new HashMap<>();
            dalParserParametersTL.set(map);
        }
        map.put(key, value);
    }

    public static void addParamMap(Map<String, Object> paramMap) {
        Map<String, Object> map = dalParserParametersTL.get();
        if (map == null) {
            map = new HashMap<>();
            dalParserParametersTL.set(map);
        }
        map.putAll(paramMap);
    }

    public static Map<String, Object> getParamMap() {
        return dalParserParametersTL.get();
    }

    /**
     * 设置开启slave模式
     */
    public static void setSlaveMode() {
        mslbStatusThreadLocal.set(true);
    }

    public static void clearSlaveMode() {
        mslbStatusThreadLocal.remove();
    }

    /**
     * 设置开启slave模式,并指定数据源
     *
     * @param slaveDsKey null时表示不指定数据源,不为null时表示指定数据源
     */
    public static void setSlaveMode(String slaveDsKey) {
        setSlaveMode();
        if (slaveDsKey != null) {
            setSlaveDsKey(slaveDsKey);
        }
    }

    /**
     * 当前是否支持slave,也会判断目前是不是全局slave
     *
     * @return true:支持slave
     */
    public static boolean isEnableSlave() {
        Boolean bool = isEnableGlobalSlaveMode();
        if (bool) {
            return true;
        }
        bool = mslbStatusThreadLocal.get();
        if (bool == null) {
            return false;
        }
        return bool;
    }

    public static void setDalInfo(DALInfo dalInfo) {
        dalInfoTL.set(dalInfo);
    }

    public static DALInfo getDalInfo() {
        return dalInfoTL.get();
    }

    public static DALConnection getCurrentDALConnection() {
        return currentDALConTL.get();
    }

    static void setCurrentDALConnection(DALConnection dalConnection) {
        currentDALConTL.set(dalConnection);
    }

    public static void removeCurrentDALConnection() {
        currentDALConTL.remove();
    }

    public static void remove() {
        dalParserParametersTL.remove();
        mslbStatusThreadLocal.remove();
        msDsKeyTL.remove();
        dalInfoTL.remove();
    }

    /**
     * 如果没有进行有效sql运行直接返回时,需要调用线程变量清除方法
     */
    public static void processDALConClose() {
        DALConnection dalConnection = DALStatus.getCurrentDALConnection();
        if (dalConnection == null) {
            DALStatus.removeCurrentDALConnection();
            DALStatus.remove();
            //实际数据并没有进行更新,但是需要操作DALConnectionListener#onDALClosed
            //但须需要判断当前是否在一个事务操作里
            if (DALConnectionListenerFactory.hasListener()) {
                for (DALConnectionListener listener : DALConnectionListenerFactory.getInstance().getDalConnectionListeners()) {
                    listener.onDALClosed();
                }
            }
        }
    }
}
