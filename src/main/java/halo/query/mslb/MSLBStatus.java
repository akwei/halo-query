package halo.query.mslb;

/**
 * Created by akwei on 7/6/14.
 */
public class MSLBStatus {
    private static final ThreadLocal<Boolean> mslbStatusThreadLocal = new ThreadLocal<Boolean>();

    /**
     * 设置当前操作是否为读取
     *
     * @param readFlag
     */
    public static void set(boolean readFlag) {
        mslbStatusThreadLocal.set(readFlag);
    }

    public static void setOpSlave() {
        MSLBStatus.set(true);
    }

    public static void remove() {
        mslbStatusThreadLocal.remove();
    }

    public static Boolean get() {
        return mslbStatusThreadLocal.get();
    }
}
