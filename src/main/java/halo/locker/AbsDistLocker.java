package halo.locker;

import java.util.*;

/**
 * Created by akwei on 6/28/15.
 */
public abstract class AbsDistLocker implements DistLocker {

    private final static ThreadLocal<Set<String>> lockSetTL = new ThreadLocal<Set<String>>();

    private static final ThreadLocal<Map<String, Object>> mapTL = new ThreadLocal<Map<String, Object>>();

    /**
     * 探测锁的间隔时间,单位:毫秒.默认50毫秒
     */
    private int detectInterval = 50;

    /**
     * 探测锁的次数
     */
    private int detectTimes = 3;

    /**
     * 锁定时长,单位:秒
     */
    private int expireTime = 5;

    public int getExpireTime() {
        return expireTime;
    }

    /**
     * 设置锁定时长,单位:秒
     *
     * @param expireTime 锁定时长,单位:秒
     */
    public void setExpireTime(int expireTime) {
        this.expireTime = expireTime;
    }

    public int getDetectTimes() {
        return detectTimes;
    }

    /**
     * 设置探测锁的次数
     *
     * @param detectTimes 探测锁的次数
     */
    public void setDetectTimes(int detectTimes) {
        this.detectTimes = detectTimes;
    }

    public int getDetectInterval() {
        return detectInterval;
    }

    /**
     * 设置探测锁的间隔时间,单位:毫秒
     *
     * @param detectInterval 探测锁的间隔时间,单位:毫秒
     */
    public void setDetectInterval(int detectInterval) {
        this.detectInterval = detectInterval;
    }

    public abstract boolean lockInternal(String key, long time);

    public abstract void releaseInternal(String key);

    @Override
    public boolean lock(String key, boolean waitLock, long time) {
        if (this.hasLocked(key)) {
            return true;
        }
        if (!waitLock) {
            return this.lockInternal(key, time);
        }
        boolean locked = false;
        int i = 0;
        try {
            while (!locked) {
                locked = this.lockInternal(key, time);
                if (locked) {
                    this.addLock(key);
                    return locked;
                }
                i++;
                if (i >= this.detectTimes) {
                    throw new DistLockException("lock expire[times:" + this.detectTimes + ",interval:" + this.detectInterval + "]");
                }
                this.sleep(this.detectInterval);
            }
        } catch (InterruptedException e) {
            throw new DistLockException(e);
        }
        return false;
    }

    @Override
    public List<String> release() {
        Set<String> lockSet = this.getLockSet();
        if (lockSet.isEmpty()) {
            lockSetTL.remove();
            mapTL.remove();
            return new ArrayList<String>(0);
        }
        List<String> lockList = new ArrayList<String>(lockSet);
        Collections.reverse(lockList);
        try {
            for (String key : lockList) {
                this.releaseInternal(key);
            }
        } finally {
            lockSetTL.remove();
            mapTL.remove();
        }
        return lockList;
    }

    private void sleep(long t) throws InterruptedException {
        Thread.sleep(t);
    }

    private boolean hasLocked(String key) {
        return this.getLockSet().contains(key);
    }

    private Set<String> getLockSet() {
        Set<String> lockSet = lockSetTL.get();
        if (lockSet == null) {
            lockSet = new LinkedHashSet<String>();
            lockSetTL.set(lockSet);
        }
        return lockSet;
    }

    private boolean addLock(String key) {
        Set<String> set = this.getLockSet();
        return set.add(key);
    }
    protected <T> T getData(String key) {
        Map<String, Object> map = mapTL.get();
        if (map == null) {
            return null;
        }
        return (T) map.get(key);
    }

    protected void setData(String key, Object data) {
        Map<String, Object> map = mapTL.get();
        if (map == null) {
            map = new HashMap<String, Object>();
            mapTL.set(map);
        }
        map.put(key, data);
    }

    private void removeData() {

    }

}
