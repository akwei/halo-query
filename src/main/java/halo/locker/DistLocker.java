package halo.locker;

/**
 * Created by akwei on 6/22/15.
 */
public interface DistLocker {

    /**
     * 锁定资源
     *
     * @param key  资源key
     * @param time 需要锁定的时间(毫秒)
     * @return true:成功锁定 false:未获得锁
     */
    boolean lock(String key, boolean waitLock, long time);

    /**
     * 解除锁定
     *
     * @param key
     * @return true:解除的锁定是自己创建的锁, false:锁已经过期，不需要解除
     */
    boolean release(String key);


}
