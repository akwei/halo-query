package halo.cache;

/**
 * 缓存对象，此对象能表示有效数据和空值数据的缓存
 * Created by akwei on 6/5/15.
 */
public class CacheItem {

    /**
     * 空值数据标识
     */
    public static final byte nullFlag = 0;

    /**
     * 有效数据标识
     */
    public static final byte dataFlag = 2;

    /**
     * 是否有空值缓存
     */
    private boolean nullCached;

    /**
     * 有效的缓存数据
     */
    private Object object;

    /**
     * 数据字节数组
     */
    private byte[] objectBytes;

    public byte[] getObjectBytes() {
        return objectBytes;
    }

    public void setObjectBytes(byte[] objectBytes) {
        this.objectBytes = objectBytes;
    }

    /**
     * 是否有空值缓存
     *
     * @return
     */
    public boolean isNullCached() {
        return nullCached;
    }

    /**
     * set
     *
     * @param nullCached true/false
     */
    public void setNullCached(boolean nullCached) {
        this.nullCached = nullCached;
    }

    /**
     * 获得有效缓存数据
     *
     * @param <T> 数据泛型
     * @return 有效缓存数据
     */
    public <T> T getObject() {
        return (T) object;
    }

    /**
     * set
     *
     * @param object 缓存对象
     */
    public void setObject(Object object) {
        this.object = object;
    }

    /**
     * 是否有缓存对象
     *
     * @return
     */
    public boolean hasObject() {
        if (this.object != null) {
            return true;
        }
        return false;
    }

    /**
     * 对字节数组进行解析
     *
     * @param bytes 字节数组
     * @return 缓存对象item
     */
    public static CacheItem parse(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        CacheItem cacheItem = new CacheItem();
        byte flag = bytes[0];
        if (flag != dataFlag) {
            cacheItem.setNullCached(true);
            return cacheItem;
        }
        cacheItem.setNullCached(false);
        byte[] data = new byte[bytes.length - 1];
        System.arraycopy(bytes, 1, data, 0, data.length);
        cacheItem.setObjectBytes(data);
        return cacheItem;
    }

    public static byte[] buildCacheData(byte[] data) {
        if (data == null || data.length == 0) {
            return new byte[]{CacheItem.nullFlag};
        }
        byte[] bytes = new byte[data.length + 1];
        bytes[0] = CacheItem.dataFlag;
        System.arraycopy(data, 0, bytes, 1, data.length);
        return bytes;
    }
}
