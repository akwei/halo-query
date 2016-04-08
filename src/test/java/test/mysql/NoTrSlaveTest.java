package test.mysql;

import halo.query.Query;
import halo.query.dal.DALStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import test.bean.TbUser;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/query-test3.xml"})
public class NoTrSlaveTest {

    @Autowired
    private Query query;

    /**
     * 事务的slave查询需要走master数据源
     *
     * @throws Exception
     */
    @Test
    public void getFromSlave() throws Exception {
        DALStatus.setGlobalSlaveMode();
//        DALStatus.setSlaveMode();
        DALStatus.addParam("userId", 1);
        query.objById(TbUser.class, 1);
    }
}
