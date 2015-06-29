package halo.locker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.List;

/**
 * Created by akwei on 6/28/15.
 */
@Aspect
public class DistLockerAOP {

    private static final Log logger = LogFactory.getLog(DistLockerAOP.class);

    private DistLocker distLocker;

    public DistLocker getDistLocker() {
        return distLocker;
    }

    public void setDistLocker(DistLocker distLocker) {
        this.distLocker = distLocker;
    }

    @Around("@annotation(halo.locker.DistLockAble)")
    public Object lockAround(ProceedingJoinPoint pjp) throws Throwable {
        try {
            this.processLock(pjp);
            return pjp.proceed();
        } finally {
            this.processRelease();
        }
    }

    private void processLock(ProceedingJoinPoint pjp) {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        DistLockAble distLockAble = methodSignature.getMethod().getAnnotation(DistLockAble.class);
        String key = distLockAble.key();
        if (null != key && !key.equals("")) {
            this.distLocker.lock(key, true, distLockAble.time());
            if (logger.isDebugEnabled()) {
                logger.debug("get lock[" + key + "] waitLock[true] time[" + distLockAble.time() + "]");
            }
        }
    }

    private void processRelease() {
        List<String> locks = this.distLocker.release();
        if (logger.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder();
            for (String s : locks) {
                sb.append(s).append(" , ");
            }
            logger.debug("release lock[" + sb.toString() + "]");
        }
    }
}
