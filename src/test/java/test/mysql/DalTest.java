package test.mysql;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import test.SuperBaseModelTest;
import test.bean.TbUser;

/**
 * Created by akwei on 9/28/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/query-test3.xml"})
@Transactional
public class DalTest extends SuperBaseModelTest {

    @Test
    public void dal() {
        TbUser user0 = new TbUser();
        user0.buildUserId();
        user0.setName("akwei");
        user0.create();
        TbUser user1 = new TbUser();
        user1.buildUserId();
        user1.setName("akwei");
        user1.create();
    }
}
