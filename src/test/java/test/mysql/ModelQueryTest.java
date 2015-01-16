package test.mysql;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import test.SuperBaseModelTest;
import test.bean.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/query-test2.xml"})
@Transactional
public class ModelQueryTest extends SuperBaseModelTest {

    @Test
    public void insert_select_update_delete() {
        try {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MILLISECOND, 0);
            User user = new User();
            user.setAddr("abc");
            user.setCreatetime(new Timestamp(cal.getTimeInMillis()));
            user.setIntro("intro");
            user.setNick("我的昵称我的昵称袁伟1122");
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
            User dbUser = User.objById(user.getUserid());
            Assert.assertNotNull(dbUser);
            dbUser.update();
            Assert.assertEquals(user.isEnableflag(), dbUser.isEnableflag());
            Assert.assertEquals(user.getAddr(), dbUser.getAddr());
            Assert.assertEquals(user.getIntro(), dbUser.getIntro());
            Assert.assertEquals(user.getNick(), dbUser.getNick());
            Assert.assertEquals(user.getSex(), dbUser.getSex());
            Assert.assertEquals(user.getUserid(), dbUser.getUserid());
            Assert.assertEquals(user.getUuid(), dbUser.getUuid());
            Assert.assertEquals(user.getUuid2(), dbUser.getUuid2());
            Assert.assertEquals(String.valueOf(user.getUuid3()),
                    String.valueOf(dbUser.getUuid3()));
            Assert.assertEquals(String.valueOf(user.getUuid4()),
                    String.valueOf(dbUser.getUuid4()));
            Assert.assertEquals(user.getUuid5(), dbUser.getUuid5());
            Assert.assertEquals(user.getUuid6(), dbUser.getUuid6());
            Assert.assertEquals(user.getUuid7(), dbUser.getUuid7());
            Assert.assertEquals(user.getUuid8(), dbUser.getUuid8());
            Assert.assertEquals(user.getUuid9(), dbUser.getUuid9());
            Assert.assertEquals(user.getUuid10(), dbUser.getUuid10());
            Assert.assertEquals(user.getUuid11(), dbUser.getUuid11());
            Assert.assertEquals(user.getUuid12(), dbUser.getUuid12());
            Assert.assertEquals(user.getCreatetime().getTime(), dbUser
                    .getCreatetime().getTime());
            dbUser.delete();
            dbUser = User.objById(dbUser.getUserid());
            Assert.assertNull(dbUser);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getLocalizedMessage());
        }
    }

    @Test
    public void insert_select_update_deleteForNull() {
        try {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MILLISECOND, 0);
            User user = new User();
            user.setAddr("abc");
            user.setCreatetime(new Timestamp(cal.getTimeInMillis()));
            user.setIntro("intro");
            user.setNick("nickname");
            user.setSex(null);
            user.setUuid(new BigInteger("18446744073709551615"));
            user.setUuid10(null);
            user.setUuid11(1234567890);
            user.setUuid12(null);
            user.setUuid2(null);
            user.setUuid3(35.09);
            user.setUuid4(10.9f);
            user.setUuid5(null);
            user.setUuid6((short) 12);
            user.setUuid7(null);
            user.setUuid8((byte) 3);
            user.setUuid9(null);
            user.setEnableflag(true);
            user.setUsersex(UserSex.FEMALE);
            user.create();
            User dbUser = User.objById(user.getUserid());
            Assert.assertNotNull(dbUser);
            dbUser.update();
            Assert.assertEquals(user.isEnableflag(), dbUser.isEnableflag());
            Assert.assertEquals(user.getAddr(), dbUser.getAddr());
            Assert.assertEquals(user.getIntro(), dbUser.getIntro());
            Assert.assertEquals(user.getNick(), dbUser.getNick());
            Assert.assertNull(dbUser.getSex());
            Assert.assertEquals(user.getUserid(), dbUser.getUserid());
            Assert.assertEquals(user.getUuid(), dbUser.getUuid());
            Assert.assertNull(dbUser.getUuid2());
            Assert.assertEquals(String.valueOf(user.getUuid3()),
                    String.valueOf(dbUser.getUuid3()));
            Assert.assertEquals(String.valueOf(user.getUuid4()),
                    String.valueOf(dbUser.getUuid4()));
            Assert.assertNull(dbUser.getUuid5());
            Assert.assertEquals(user.getUuid6(), dbUser.getUuid6());
            Assert.assertNull(dbUser.getUuid7());
            Assert.assertEquals(user.getUuid8(), dbUser.getUuid8());
            Assert.assertNull(dbUser.getUuid9());
            Assert.assertNull(dbUser.getUuid10());
            Assert.assertEquals(user.getUuid11(), dbUser.getUuid11());
            Assert.assertNull(dbUser.getUuid12());
            Assert.assertEquals(user.getCreatetime().getTime(), dbUser
                    .getCreatetime().getTime());
            dbUser.delete();
            dbUser = User.objById(dbUser.getUserid());
            Assert.assertNull(dbUser);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void select() {
        try {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MILLISECOND, 0);
            Date d = cal.getTime();
            TestUser testUser = new TestUser();
            testUser.setCreatetime(d);
            testUser.setGender((byte) 1);
            testUser.setMoney(99.448f);
            testUser.setPurchase(89.345f);
            testUser.setNick("nickname");
            testUser.create();
            Member m = new Member();
            m.setUserid(testUser.getUserid());
            m.setGroupid(99);
            m.setNick("membernick");
            m.create();
            List<Member> list = Member.mysqlList(
                    "where 1=1 and member_.userid=?", 0,
                    10, new Object[]{m.getUserid()});
            Assert.assertEquals(1, list.size());
            Member o = list.get(0);
            Assert.assertEquals(m.getMemberUserId(), o.getMemberUserId());
            Assert.assertEquals(m.getUserid(), o.getUserid());
            Assert.assertEquals(m.getGroupid(), o.getGroupid());
            Assert.assertEquals(m.getNick(), o.getNick());
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void selectMultTable() {
        try {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MILLISECOND, 0);
            Date d = cal.getTime();
            TestUser testUser = new TestUser();
            testUser.setCreatetime(d);
            testUser.setGender((byte) 1);
            testUser.setMoney(99.448f);
            testUser.setPurchase(89.345f);
            testUser.setNick("nickname");
            testUser.create();
            Member m = new Member();
            m.setUserid(testUser.getUserid());
            m.setGroupid(99);
            m.setNick("membernick");
            m.create();
            List<Member> list =
                    Member.getQuery()
                            .
                                    mysqlList(
                                            new Class[]{TestUser.class,
                                                    Member.class},
                                            "where testuser_.userid=member_.userid and member_.userid=?",
                                            0,
                                            1,
                                            new Object[]{m.getUserid()},
                                            new RowMapper<Member>() {

                                                public Member mapRow(ResultSet rs,
                                                                     int rowNum)
                                                        throws SQLException {
                                                    Member mm = Member.getQuery()
                                                            .getRowMapper(
                                                                    Member.class)
                                                            .mapRow(rs, rowNum);
                                                    TestUser tu = Member.getQuery()
                                                            .getRowMapper(
                                                                    TestUser.class)
                                                            .mapRow(rs, rowNum);
                                                    mm.setTestUser(tu);
                                                    return mm;
                                                }
                                            });
            for (Member o : list) {
                Assert.assertEquals(m.getMemberUserId(), o.getMemberUserId());
                Assert.assertEquals(m.getUserid(), o.getUserid());
                Assert.assertEquals(m.getGroupid(), o.getGroupid());
                Assert.assertEquals(m.getNick(), o.getNick());
                TestUser tu = o.getTestUser();
                Assert.assertEquals(testUser.getMoney() + "", tu.getMoney()
                        + "");
                Assert.assertEquals(testUser.getNick(), tu.getNick());
                Assert.assertEquals(testUser.getUserid(), tu.getUserid());
                Assert.assertEquals(testUser.getCreatetime().getTime(), tu
                        .getCreatetime().getTime());
                Assert.assertEquals(testUser.getGender() + "", tu.getGender()
                        + "");
                Assert.assertEquals(testUser.getPurchase() + "",
                        tu.getPurchase() + "");
            }
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    /**
     * 对于联合主键的测试
     */
    @Test
    public void insertFor2Id() {
        Store store0 = new Store();
        store0.setStoreId(1);
        store0.setMerchantId(2);
        store0.setCreateTime(System.currentTimeMillis());
        store0.create();

        Store store1 = new Store();
        store1.setStoreId(2);
        store1.setMerchantId(2);
        store1.setCreateTime(System.currentTimeMillis());
        store1.create();

        Store storeDB = store0.objByIds(new Object[]{store0.getStoreId(),
                store0.getMerchantId()});
        Assert.assertNotNull(storeDB);
        Assert.assertEquals(store0.getStoreId(), storeDB.getStoreId());
        Assert.assertEquals(store0.getMerchantId(), storeDB.getMerchantId());
        Assert.assertEquals(store0.getCreateTime(), storeDB.getCreateTime());

        store0.setCreateTime(System.currentTimeMillis());
        store0.update();

        storeDB = store0.objByIds(new Object[]{store0.getStoreId(),
                store0.getMerchantId()});
        Assert.assertNotNull(storeDB);
        Assert.assertEquals(store0.getStoreId(), storeDB.getStoreId());
        Assert.assertEquals(store0.getMerchantId(), storeDB.getMerchantId());
        Assert.assertEquals(store0.getCreateTime(), storeDB.getCreateTime());

        store0.delete();

        storeDB = store0.objByIds(new Object[]{store0.getStoreId(),
                store0.getMerchantId()});
        Assert.assertNull(storeDB);

        try {
            storeDB = store0.objById(store0.getStoreId());
            Assert.fail("must has 2 arguments");
        } catch (Exception e) {
        }

    }
}
