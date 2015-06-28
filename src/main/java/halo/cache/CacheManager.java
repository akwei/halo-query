package halo.cache;

import java.util.*;

/**
 * cache 接口
 */
public interface CacheManager {

    /**
     * 从缓存中获取数据
     *
     * @param dataKey 数据key
     * @return 缓存对象
     */
    <E, T> CacheItem get(Class<T> clazz, E dataKey);

    /**
     * 从缓存中获取数据，特殊的缓存实现可支持
     *
     * @param clazz   数据类型
     * @param dataKey dataKey 数据key
     * @param <E>
     * @param <T>
     * @param <R>
     * @return 缓存对象
     */
    <E, T, R> R gets(Class<T> clazz, E dataKey);

    /**
     * 获得多个缓存数据
     *
     * @param clazz    对象类型
     * @param dataKeys 数据keys
     * @param <E>      key泛型
     * @return map:key= E 类型 , value=缓存对象
     */
    <E, T> Map<E, CacheItem> getMulti(Class<T> clazz, List<E> dataKeys);

    /**
     * 获得多个缓存数据，数据会分为存在的和不存在的2种，具体看 MapResult
     *
     * @param clazz    数据类型
     * @param dataKeys key集合
     * @param <E>      key泛型
     * @param <T>      数据泛型
     * @return mapResult
     */
    <E, T> MapResult getMultiForMapResult(Class<T> clazz, List<E> dataKeys);

    /**
     * 缓存数据，如果存在就替换原有缓存
     *
     * @param dataKey 数据key
     * @param t       缓存的数据
     * @param expiry  过期时间
     * @param <E>     key泛型
     * @param <T>     数据类型
     */
    <E, T> void set(E dataKey, T t, Date expiry);

    /**
     * 缓存数据，如果存在，就不替换
     *
     * @param dataKey 数据key
     * @param t       缓存的数据
     * @param expiry  过期时间
     * @param <E>     key泛型
     * @param <T>     数据类型
     * @return true:缓存成功
     */
    <E, T> boolean add(E dataKey, T t, Date expiry);

    /**
     * check and set
     *
     * @param dataKey   数据key
     * @param t         缓存的数据
     * @param expiry    过期时间
     * @param casUnique cas token
     * @param <E>       key泛型
     * @param <T>       数据类型
     * @return true:缓存成功
     */
    <E, T> boolean cas(E dataKey, T t, Date expiry, long casUnique);

    /**
     * 缓存多个数据
     *
     * @param tmap   数据map
     * @param expiry 过期时间
     * @param <E>    key泛型
     * @param <T>    数据类型
     */
    <E, T> void setMulti(Map<E, T> tmap, Date expiry);

    /**
     * 把map数据放入缓存
     *
     * @param clazz    数据类型
     * @param dataKeys 数据key集合
     * @param tmap     数据map
     * @param expiry   数据有效期，null值表示不过期，自动遵循cache过期
     * @param <E>      key泛型
     * @param <T>      数据泛型
     */
    <E, T> void setMultiIE(Class<T> clazz, List<E> dataKeys, Map<E, T> tmap, Date expiry);

    /**
     * 把map数据放入缓存，如果数据已经存在就不替换原有缓存
     *
     * @param clazz    数据类型
     * @param dataKeys 数据key集合
     * @param tmap     数据map
     * @param expiry   数据有效期，null值表示不过期，自动遵循cache过期
     * @param <E>      key泛型
     * @param <T>      数据泛型
     */
    <E, T> void addMultiIE(Class<T> clazz, List<E> dataKeys, Map<E, T>
            tmap, Date expiry);

    /**
     * 缓存null对象
     *
     * @param clazz   对象类型
     * @param dataKey 数据key
     * @param <E>     key泛型
     * @param <T>     数据泛型
     */
    <E, T> void setNull(Class<T> clazz, E dataKey);

    /**
     * 缓存null对象
     *
     * @param clazz   对象类型
     * @param dataKey 数据key
     * @param <E>     key泛型
     * @param <T>     数据泛型
     * @return true:缓存成功
     */
    <E, T> boolean addNull(Class<T> clazz, E dataKey);

    /**
     * check and set，缓存空数据
     *
     * @param clazz     数据类型
     * @param dataKey   数据key
     * @param expiry    过期时间
     * @param casUnique cas token
     * @param <E>       key泛型
     * @param <T>       数据泛型
     * @return true:缓存成功
     */
    <E, T> boolean casNull(Class<T> clazz, E dataKey, Date expiry, long
            casUnique);

    /**
     * 如果数据是null,存储空值和有效期。如果不是null，存储数据并设置有效期
     *
     * @param clazz   对象类型
     * @param dataKey 数据key
     * @param t       数据
     * @param expiry  非null数据过期时间
     * @param <E>     key泛型
     * @param <T>     数据泛型
     */
    <E, T> void setIE(Class<T> clazz, E dataKey, T t, Date expiry);

    /**
     * 如果数据是null,存储空值和有效期。如果不是null，存储数据并设置有效期
     *
     * @param clazz   对象类型
     * @param dataKey 数据key
     * @param t       数据
     * @param expiry  非null数据过期时间
     * @param <E>     key泛型
     * @param <T>     数据泛型
     * @return true:缓存成功
     */
    <E, T> boolean addIE(Class<T> clazz, E dataKey, T t, Date expiry);

    /**
     * 如果数据是null,存储空值和有效期。如果不是null，存储数据并设置有效期
     *
     * @param clazz     对象类型
     * @param dataKey   数据key
     * @param t         数据
     * @param expiry    非null数据过期时间
     * @param casUnique cas token
     * @param <E>       key泛型
     * @param <T>       数据泛型
     * @return true:缓存成功
     */
    <E, T> boolean casIE(Class<T> clazz, E dataKey, T t, Date nullExpiry,
                         Date expiry, long casUnique);

    /**
     * 删除缓存数据
     *
     * @param clazz   对象类型
     * @param dataKey 数据key
     * @param <E>     key泛型
     * @return true:success false:fail
     */
    <E, T> boolean delete(Class<T> clazz, E dataKey);

    /**
     * 获得的缓存数据结果
     *
     * @param <E>
     * @param <T>
     */
    class MapResult<E, T> {

        /**
         * 数据map E=数据key T=数据泛型
         */
        private Map<E, T> dataMap;

        /**
         * 缓存数据map 包括空值数据缓存
         */
        private Map<E, CacheItem> cacheItemMap;

        /**
         * 没有缓存数据的key集合
         */
        private List<E> noCacheDataKeys;

        /**
         * 获得的缓存结果
         *
         * @return
         */
        public Map<E, CacheItem> getCacheItemMap() {
            return cacheItemMap;
        }

        public void setCacheItemMap(Map<E, CacheItem> cacheItemMap) {
            this.cacheItemMap = cacheItemMap;
        }

        /**
         * 获得没有缓存数据的key集合
         *
         * @return
         */
        public List<E> getNoCacheDataKeys() {
            return noCacheDataKeys;
        }

        public int getNoCacheDataKeysSize() {
            if (this.noCacheDataKeys == null) {
                return 0;
            }
            return this.noCacheDataKeys.size();
        }

        private void setNoCacheDataKeys(List<E> noCacheDataKeys) {
            this.noCacheDataKeys = noCacheDataKeys;
        }

        private void addNotCacheDataKey(E dataKey) {
            if (this.noCacheDataKeys == null) {
                this.noCacheDataKeys = new ArrayList<E>();
            }
            this.noCacheDataKeys.add(dataKey);
        }

        public Map<E, T> getDataMap() {
            return dataMap;
        }

        public Map<E, T> getOrCreateDataMap() {
            if (this.dataMap == null) {
                return new HashMap<E, T>();
            }
            return this.dataMap;
        }

        public void addDataToMap(E dataKay, T t) {
            if (this.dataMap == null) {
                this.dataMap = new HashMap<E, T>();
            }
            this.dataMap.put(dataKay, t);
        }
    }
}
