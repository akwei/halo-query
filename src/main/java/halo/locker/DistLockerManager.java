package halo.locker;

import java.util.*;

/**
 * 分布式锁资源管理器
 * Created by akwei on 6/28/15.
 */
public class DistLockerManager {

    private DistLocker distLocker;

    private final static ThreadLocal<Set<String>> lockSetTL = new ThreadLocal<Set<String>>();

    private int time;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public DistLocker getDistLocker() {
        return distLocker;
    }

    public void setDistLocker(DistLocker distLocker) {
        this.distLocker = distLocker;
    }

    public boolean lock(String key, boolean waitLock, int time) throws DistLockException {
        if (this.hasLocked(key)) {
            return true;
        }
        int _time = time;
        if (time <= 0) {
            _time = this.time;
        }
        this.distLocker.lock(key, waitLock, _time);
        this.addLock(key);
        return true;
    }

    public List<String> release() throws DistLockException {
        Set<String> lockSet = this.getLockSet();
        if (lockSet.size() > 0) {
            List<String> lockList = new ArrayList<String>(lockSet);
            Collections.reverse(lockList);
            try {
                for (String key : lockList) {
                    this.distLocker.release(key);
                }
            } finally {
                lockSetTL.remove();
            }
            return lockList;
        }
        lockSetTL.remove();
        return new ArrayList<String>(0);
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
}
