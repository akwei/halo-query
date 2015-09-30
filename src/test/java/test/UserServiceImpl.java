package test;

import halo.query.Query;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import test.bean.User;

public class UserServiceImpl {

    private static final Log log = LogFactory.getLog(UserServiceImpl.class);

    @Autowired
    private Query query;

    public void createUserTx(User user) {
        user.setUserid(query.insertForNumber(user).longValue());
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(long userId) {
        long begin = System.currentTimeMillis();
        log.info("[thread " + Thread.currentThread().getId() + "] begin get user :" + userId);
        User user = this.query.objByIdForUpdate(User.class, userId);
        long end = System.currentTimeMillis();
        log.info("[thread " + Thread.currentThread().getId() + "] get user :" + user.getUserid() + "[ " + (end - begin) + " ]");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            //
        }
    }
}
