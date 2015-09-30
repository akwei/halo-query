package test.mysql;

import halo.query.Query;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import test.SuperBaseModelTest;
import test.UserServiceImpl;
import test.bean.User;
import test.bean.UserSex;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/query-test.xml"})
@Transactional
public class QueryTest2 extends SuperBaseModelTest {

    @Resource
    private UserServiceImpl userServiceImpl;

    @Resource
    Query query;

    private Map<String, Object> objMap = new HashMap<String, Object>();

    @After
    public void after() {
        User user = (User) objMap.get("user");
        user.delete();
    }

    @Before
    public void before() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        User user = new User();
        user.setAddr("abc");
        user.setCreatetime(new Timestamp(cal.getTimeInMillis()));
        user.setIntro("intro");
        user.setNick("我的昵称我的昵称袁伟aabb");
        user.setSex(1);
        user.setUuid(new BigInteger("18446744073709551615"));
        user.setUuid10(1234567890123L);
        user.setUuid11(1234567890);
        user.setUuid12(new BigDecimal("1111111111"));
        user.setUuid2(23.04);
        user.setUuid3(35.09);
        user.setUuid4(10.9f);
        user.setUuid5(10.7f);
        user.setUuid6((short) 12);
        user.setUuid7(Short.valueOf("11"));
        user.setUuid8((byte) 3);
        user.setUuid9(Byte.valueOf("5"));
        user.setUsersex(UserSex.FEMALE);
        user.create();
        objMap.put("user", user);
    }
}
