package test.mysql;

import halo.query.Query;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import test.SuperBaseModelTest;
import test.UserServiceImpl;
import test.bean.Member;
import test.bean.Role;
import test.bean.TestUser;
import test.bean.User;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/query-test.xml"})
@Transactional
public class QueryTest extends SuperBaseModelTest {

    int roleId;

    Role role;

    @Resource
    private UserServiceImpl userServiceImpl;

    @Resource
    Query query;

    @After
    public void after() {
        User user = (User) objMap.get("user");
        User user1 = (User) objMap.get("user1");
        user.delete();
        user1.delete();
    }

    @Before
    public void before() {
        role = new Role();
        role.setCreateTime(new Date());
        try {
            roleId = query.insertForNumber(role).intValue();
            role.setRoleId(roleId);
        }
        catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        objMap = new HashMap<String, Object>();
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
        user.create();
        objMap.put("user", user);
        User user1 = new User();
        user1.setAddr("abc");
        user1.setCreatetime(new Timestamp(cal.getTimeInMillis()));
        user1.setIntro("intro");
        user1.setNick("我的昵称我的昵称袁伟");
        user1.setSex(1);
        user1.setUuid(new BigInteger("18446744073709551615"));
        user1.setUuid10(1234567890123L);
        user1.setUuid11(1234567890);
        user1.setUuid12(new BigDecimal("1111111111"));
        user1.setUuid2(23.04);
        user1.setUuid3(35.09);
        user1.setUuid4(10.9f);
        user1.setUuid5(10.7f);
        user1.setUuid6((short) 12);
        user1.setUuid7(Short.valueOf("11"));
        user1.setUuid8((byte) 3);
        user1.setUuid9(Byte.valueOf("5"));
        user1.create();
        objMap.put("user1", user1);
    }


    @Test
    public void testUserServcice() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        User user = new User();
        user.setAddr("abc");
        user.setCreatetime(new Timestamp(cal.getTimeInMillis()));
        user.setIntro("intro");
        user.setNick("我的昵称我的昵称袁伟3344");
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
        this.userServiceImpl.createUserTx(user);
    }

    @Test
    public void insert_select_update_delete() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        User user = new User();
        user.setAddr("abc");
        user.setCreatetime(new Timestamp(cal.getTimeInMillis()));
        user.setIntro("intro");
        user.setNick("我的昵称我的昵称袁伟5566");
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
        user.setUserid(query.insertForNumber(user).longValue());
        User dbUser = query.objById(User.class, user.getUserid());
        Assert.assertNotNull(dbUser);
        query.update(dbUser);
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
        query.delete(dbUser);
        dbUser = query.objById(User.class, dbUser.getUserid());
        Assert.assertNull(dbUser);
    }

    @Test
    public void insert_select_update_deleteForNull() {
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
        user.setUserid(query.insertForNumber(user).longValue());
        User dbUser = query.objById(User.class, user.getUserid());
        Assert.assertNotNull(dbUser);
        query.update(dbUser);
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
        query.delete(dbUser);
        dbUser = query.objById(User.class, dbUser.getUserid());
        Assert.assertNull(dbUser);
    }

    @Test
    public void select() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        Date d = cal.getTime();
        TestUser testUser = new TestUser();
        testUser.setCreatetime(d);
        testUser.setGender((byte) 1);
        testUser.setMoney(99.448d);
        testUser.setPurchase(89.345f);
        testUser.setNick("nickname");
        testUser.setUserid(query.insertForNumber(testUser)
                .longValue());
        Member m = new Member();
        m.setUserid(testUser.getUserid());
        m.setGroupid(99);
        m.setNick("membernick");
        m.setMemberUserId(query.insertForNumber(m).longValue());
        List<Member> list = query.mysqlList(Member.class,
                "where 1=1 and member_.userid=?", 0,
                10, new Object[]{m.getUserid()});
        Member o = list.get(0);
        Assert.assertEquals(m.getMemberUserId(), o.getMemberUserId());
        Assert.assertEquals(m.getUserid(), o.getUserid());
        Assert.assertEquals(m.getGroupid(), o.getGroupid());
        Assert.assertEquals(m.getNick(), o.getNick());
    }

    @Test
    public void selectMultTable() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        Date d = cal.getTime();
        TestUser testUser = new TestUser();
        testUser.setCreatetime(d);
        testUser.setGender((byte) 1);
        testUser.setMoney(99.448d);
        testUser.setPurchase(89.345f);
        testUser.setNick("nickname");
        testUser.setUserid(query.insertForNumber(testUser)
                .longValue());
        Member m = new Member();
        m.setUserid(testUser.getUserid());
        m.setGroupid(99);
        m.setNick("membernick");
        m.setMemberUserId(query.insertForNumber(m).longValue());
        List<Member> list = query
                .mysqlList(
                        new Class[]{TestUser.class,
                                Member.class},
                        "where testuser_.userid=member_.userid and member_.userid=?",
                        0, 1,
                        new Object[]{m.getUserid()},
                        new RowMapper<Member>() {

                            public Member mapRow(ResultSet rs, int rowNum)
                                    throws SQLException {
                                Member mm = query
                                        .getRowMapper(Member.class)
                                        .mapRow(rs, rowNum);
                                TestUser tu = query.getRowMapper(
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
    }

    @Test
    public void count1() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        Date d = cal.getTime();
        TestUser testUser = new TestUser();
        testUser.setCreatetime(d);
        testUser.setGender((byte) 1);
        testUser.setMoney(99.448d);
        testUser.setPurchase(89.345f);
        testUser.setNick("nickname");
        testUser.setUserid(query.insertForNumber(testUser)
                .longValue());
        TestUser testUser1 = new TestUser();
        testUser1.setCreatetime(d);
        testUser1.setGender((byte) 1);
        testUser1.setMoney(99.448d);
        testUser1.setPurchase(89.345f);
        testUser1.setNick("nickname");
        testUser1.setUserid(query.insertForNumber(testUser1)
                .longValue());
        int count = query.count(TestUser.class, "where money=?",
                new Object[]{99.448d});
        Assert.assertEquals(2, count);
    }

    @Test
    public void updateSeg() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        Date d = cal.getTime();
        TestUser testUser = new TestUser();
        testUser.setCreatetime(d);
        testUser.setGender((byte) 1);
        testUser.setMoney(99.448d);
        testUser.setPurchase(89.345f);
        testUser.setNick("nickname");
        testUser.setUserid(query.insertForNumber(testUser)
                .longValue());
        float money = 190.899f;
        float add = 19.89f;
        query.update(TestUser.class, "set money=? where userid=?",
                new Object[]{money, testUser.getUserid()});
        TestUser dbo = query.objById(TestUser.class, testUser.getUserid());
        Assert.assertNotNull(dbo);
        Assert.assertEquals(money + "", dbo.getMoney() + "");
        query.update(TestUser.class, "set money=money+? where userid=?",
                new Object[]{add, testUser.getUserid()});
        dbo = query.objById(TestUser.class, testUser.getUserid());
        Assert.assertNotNull(dbo);
        Assert.assertEquals((money + add) + "", dbo.getMoney() + "");
    }

    @Test
    public void deleteSeg() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        Date d = cal.getTime();
        TestUser testUser = new TestUser();
        testUser.setCreatetime(d);
        testUser.setGender((byte) 1);
        testUser.setMoney(99.448d);
        testUser.setPurchase(89.345f);
        testUser.setNick("nickname");
        testUser.setUserid(query.insertForNumber(testUser)
                .longValue());
        query.delete(TestUser.class, "where userid=?",
                new Object[]{testUser.getUserid()});
        TestUser dbo = query.objById(TestUser.class, testUser.getUserid());
        Assert.assertNull(dbo);
    }

    @Test
    public void list1() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        Date d = cal.getTime();
        TestUser testUser = new TestUser();
        testUser.setCreatetime(d);
        testUser.setGender((byte) 1);
        testUser.setMoney(99.448d);
        testUser.setPurchase(89.345f);
        testUser.setNick("nickname");
        testUser.setUserid(query.insertForNumber(testUser)
                .longValue());
        Assert.assertEquals(1, query.list(TestUser.class, "where userid=?",
                new Object[]{testUser.getUserid()}).size());
    }

    @Test
    public void obj1() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        Date d = cal.getTime();
        TestUser testUser = new TestUser();
        testUser.setCreatetime(d);
        testUser.setGender((byte) 1);
        testUser.setMoney(99.448d);
        testUser.setPurchase(89.345f);
        testUser.setNick("nickname");
        testUser.setUserid(query.insertForNumber(testUser)
                .longValue());
        TestUser dbo = query.obj(TestUser.class, "where userid=?",
                new Object[]{testUser.getUserid()});
        Assert.assertNotNull(dbo);
    }

    private Map<String, Object> objMap;


    @Test
    public void update() {
        User user = (User) objMap.get("user");
        user.setNick("ooo");
        user.update();
        String nick = "akweiwei";
        User.update("set nick=? where userid=?",
                new Object[]{nick, user.getUserid()});
        User dbUser = User.objById(user.getUserid());
        Assert.assertNotNull(dbUser);
        Assert.assertEquals(nick, dbUser.getNick());
    }

    @Test
    public void list() {
        User user = (User) objMap.get("user");
        List<User> list = User.list("where userid=?",
                new Object[]{user.getUserid()});
        Assert.assertEquals(1, list.size());
        list = User.list(null, null);
        if (list.isEmpty()) {
            Assert.fail("must not empty list");
        }
    }

    @Test
    public void objById() {
        User user = (User) objMap.get("user");
        User dbUser = User.objById(user.getUserid());
        Assert.assertNotNull(dbUser);
    }

    @Test
    public void obj() {
        User user = (User) objMap.get("user");
        User dbUser = User.obj("where userid=?",
                new Object[]{user.getUserid()});
        Assert.assertNotNull(dbUser);
    }

    @Test
    public void deleteById() {
        User user = (User) objMap.get("user");
        user.delete();
        User dbUser = User.objById(user.getUserid());
        Assert.assertNull(dbUser);
    }

    @Test
    public void mysqlList() {
        User user = (User) objMap.get("user");
        List<User> list = User.mysqlList("where userid=?", 0, 5,
                new Object[]{user.getUserid()});
        Assert.assertEquals(1, list.size());
        list = User.mysqlList("where sex=?", 0, 5,
                new Object[]{user.getSex()});
        Assert.assertEquals(2, list.size());
    }

    @Test
    public void count() {
        User user = (User) objMap.get("user");
        int count = User.count("where userid=?",
                new Object[]{user.getUserid()});
        Assert.assertEquals(1, count);
        count = User.count("where sex=?",
                new Object[]{user.getSex()});
        Assert.assertEquals(2, count);
    }

    @Test
    public void update1() {
        try {
            int result = query.update(role);
            Assert.assertEquals(1, result);
        }
        catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void deleteById1() {
        try {
            int result = query.deleteById(Role.class, new Object[]{roleId});
            Assert.assertEquals(1, result);
        }
        catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void deleteWhere() {
        try {
            int result = query.delete(Role.class, "where role_id=?",
                    new Object[]{roleId});
            Assert.assertEquals(1, result);
        }
        catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void map() {
        User user = (User) objMap.get("user");
        User user1 = (User) objMap.get("user1");
        Map<Long, User> map = query.map(User.class, "where sex=?", "userid",
                new Object[]{1},
                new Object[]{user.getUserid(), user1.getUserid()});
        Assert.assertNotNull(map);
        Assert.assertEquals(2, map.size());
        User u0 = map.get(user.getUserid());
        User u1 = map.get(user.getUserid());
        List<Integer> p0 = new ArrayList<Integer>();
        p0.add(1);
        List<Long> p1 = new ArrayList<Long>();
        p1.add(user.getUserid());
        p1.add(user1.getUserid());
        map = query.map2(User.class, "where sex=?", "userid", p0, p1);
        Assert.assertNotNull(map);
        Assert.assertEquals(2, map.size());
        Assert.assertNotNull(u0);
        Assert.assertNotNull(u1);
    }
}
