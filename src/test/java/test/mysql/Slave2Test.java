package test.mysql;

import halo.query.Query;
import halo.query.dal.DALStatus;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import test.bean.TbUser;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/query-slave2.xml"})
@Transactional
public class Slave2Test {

    @Autowired
    private Query query;

    public void before() {
        DALStatus.clearSlaveMode();
        DALStatus.clearGlobalSlaveMode();
    }

    @Test
    public void slave001() throws Exception {
        DALStatus.setSlaveMode();
        DALStatus.addParam("userId", 1);
        query.objById(TbUser.class, 1);
        Assert.assertNull(DALStatus.getSlaveDsKey());

        DALStatus.addParam("userId", 2);
        query.objById(TbUser.class, 2);
        Assert.assertNull(DALStatus.getSlaveDsKey());
    }

}
