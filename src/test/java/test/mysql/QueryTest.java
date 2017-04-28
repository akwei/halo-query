package test.mysql;

import halo.query.HaloIdException;
import halo.query.Query;
import halo.query.dal.DALStatus;
import halo.query.dal.HaloDALC3p0PropertiesDataSource;
import halo.query.dal.HaloDALDataSource;
import halo.query.dal.HaloPropertiesDataSource;
import halo.query.dal.slave.DefSlaveSelectStrategy;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import test.SuperBaseModelTest;
import test.UserServiceImpl;
import test.bean.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/query-test.xml"})
@Transactional
public class QueryTest extends SuperBaseModelTest {

    int roleId;

    Role role;

    @Resource
    private UserServiceImpl userServiceImpl;

    @Resource
    private HaloDALDataSource haloDALDataSource;

    @Resource
    Query query;

    private Map<String, Object> objMap;

    @After
    public void after() {
        User user = (User) objMap.get("user");
        User user1 = (User) objMap.get("user1");
        if (user != null) {
            query.delete(user);
        }
        if (user1 != null) {
            query.delete(user1);
        }
        if (role != null) {
            query.delete(role);
        }
    }

    @Before
    public void before() {
        DALStatus.clearSlaveMode();
        DALStatus.clearGlobalSlaveMode();
        DALStatus.setDalInfo(null);

        role = new Role();
        role.setCreateTime(new Date());
        roleId = query.insertForNumber(role).intValue();
        role.setRoleId(roleId);
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
        user.setUsersex(UserSex.FEMALE);
        user.setEnableflag(true);
        user.setUserid(query.insertForNumber(user).longValue());
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
        user1.setUsersex(UserSex.MALE);
        user1.setEnableflag(false);
        user1.setUserid(query.insertForNumber(user1).longValue());
        objMap.put("user1", user1);
    }


    @Test
    public void t001_testUserServcice() {
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
        user.setUsersex(UserSex.FEMALE);
        user.setEnableflag(true);
        this.userServiceImpl.createUserTx(user);
    }

    @Test
    public void t002_insert_select_update_delete() {
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
        user.setUsersex(UserSex.MALE);
        user.setEnableflag(true);
        //insert
        user.setUserid(query.insertForNumber(user).longValue());
        //select
        User dbUser = query.objById(User.class, user.getUserid());
        this._validateUser(user, dbUser);
        //update
        query.update(dbUser);
        dbUser = query.objById(User.class, user.getUserid());
        this._validateUser(user, dbUser);
        //delete
        query.delete(dbUser);
        dbUser = query.objById(User.class, dbUser.getUserid());
        Assert.assertNull(dbUser);
    }

    @Test
    public void t003_insert_select_update_deleteForNull() {
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
        user.setUsersex(UserSex.MALE);
        user.setEnableflag(true);
        user.setUserid(query.insertForNumber(user).longValue());
        User dbUser = query.objById(User.class, user.getUserid());
        Assert.assertNotNull(dbUser);
        query.update(dbUser);
        dbUser = query.objById(User.class, user.getUserid());
        this._validateUser(user, dbUser);
        query.delete(dbUser);
        dbUser = query.objById(User.class, dbUser.getUserid());
        Assert.assertNull(dbUser);
    }

    @Test
    public void t004_select() {
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
    public void t005_selectMultTable() {
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
    public void t006_count1() {
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
    public void t007_updateSeg() {
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
    public void t008_eleteSeg() {
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
    public void t009_list1() {
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
    public void t010_obj1() {
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

    @Test
    public void t011_update() {
        User user = (User) objMap.get("user");
        user.setNick("ooo");
        this.query.update(user);
        User dbUser = query.objById(User.class, user.getUserid());
        this._validateUser(user, dbUser);
        String nick = "akweiwei";
        query.update(User.class, "set nick=? where userid=?",
                new Object[]{nick, user.getUserid()});
        dbUser = query.objById(User.class, user.getUserid());
        Assert.assertNotNull(dbUser);
        Assert.assertEquals(nick, dbUser.getNick());
    }

    @Test
    public void t012_list() {
        User user = (User) objMap.get("user");
        List<User> list = query.list(User.class, "where userid=?", new Object[]{user.getUserid()});
        Assert.assertEquals(1, list.size());
        list = query.list(User.class, null, null);
        if (list.isEmpty()) {
            Assert.fail("must not empty list");
        }
        this._validateUser(user, list.get(0));
    }

    @Test
    public void t013_objById() {
        User user = (User) objMap.get("user");
        User dbUser = this.query.objById(User.class, user.getUserid());
        this._validateUser(user, dbUser);
    }

    @Test
    public void t014_obj() {
        User user = (User) objMap.get("user");
        User dbUser = this.query.obj(User.class, "where userid=?", new Object[]{user.getUserid()});
        this._validateUser(user, dbUser);
    }

    @Test
    public void t015_deleteById() {
        User user = (User) objMap.get("user");
        this.query.delete(user);
        User dbUser = this.query.objById(User.class, user.getUserid());
        Assert.assertNull(dbUser);
    }

    @Test
    public void t016_mysqlList() {
        User user = (User) objMap.get("user");
        List<User> list = this.query.mysqlList(User.class, "where userid=?", 0, 5, new Object[]{user.getUserid()});
        Assert.assertEquals(1, list.size());
        this._validateUser(user, list.get(0));
        list = this.query.mysqlList(User.class, "where sex=?", 0, 5, new Object[]{user.getSex()});
        Assert.assertEquals(2, list.size());
    }

    @Test
    public void t017_count() {
        User user = (User) objMap.get("user");
        int count = this.query.count(User.class, "where userid=?", new Object[]{user.getUserid()});
        Assert.assertEquals(1, count);
        count = this.query.count(User.class, "where sex=?", new Object[]{user.getSex()});
        Assert.assertEquals(2, count);
    }

    @Test
    public void t018_update1() {
        int result = query.update(role);
        Assert.assertEquals(1, result);
    }

    @Test
    public void t019_deleteById1() {
        int result = query.deleteById(Role.class, new Object[]{roleId});
        Assert.assertEquals(1, result);
    }

    @Test
    public void t020_deleteWhere() {
        int result = query.delete(Role.class, "where role_id=?", new Object[]{roleId});
        Assert.assertEquals(1, result);
    }

    @Test
    public void t021_map() {
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
        query.map2(User.class, "where sex=?", "userid", p0, p1);
        map = query.map2(User.class, "where sex=?", "userid", p0, p1);
        Assert.assertNotNull(map);
        Assert.assertEquals(2, map.size());
        Assert.assertNotNull(u0);
        Assert.assertNotNull(u1);
        List<User> list = this.query.listInValues2(User.class, "where sex=?", "userid", "order by userid desc", p0, p1);
        Assert.assertEquals(2, list.size());
        Assert.assertEquals(user1.getUserid(), list.get(0).getUserid());
    }

    @Test
    public void t022_updateForSnapshot1() {
        User user = (User) objMap.get("user");
        User snapshot = Query.snapshot(user);
        user.setAddr("akweidinegd" + Math.random());
        user.setSex(null);
        user.setCreatetime(new Timestamp(System.currentTimeMillis() / 1000 * 1000));
        query.update(user, snapshot);
        User userdb = query.objById(User.class, user.getUserid());
        this._validateUser(user, userdb);
    }

    @Test
    public void t023_updateForSnapshot2() {
        User user = (User) objMap.get("user");
        user.setSex(null);
        User snapshot = Query.snapshot(user);
        user.setAddr("akweidinegd" + Math.random());
        user.setSex(null);
        user.setCreatetime(new Timestamp(System.currentTimeMillis() / 1000 * 1000));
        query.update(user, snapshot);
        User userdb = query.objById(User.class, user.getUserid());
        Assert.assertNotNull(userdb.getSex());
        Assert.assertEquals(user.getCreatetime().getTime() / 1000 * 1000, userdb.getCreatetime().getTime());
    }

    @Test
    public void t024_updateForSnapshot3() {
        User user = (User) objMap.get("user");
        user.setSex(null);
        User snapshot = Query.snapshot(user);
        user.setAddr("akweidinegd" + Math.random());
        user.setSex(UserSex.MALE.getValue());
        user.setCreatetime(new Timestamp(System.currentTimeMillis() / 1000 * 1000));
        query.update(user, snapshot);
        User userdb = query.objById(User.class, user.getUserid());
        this._validateUser(user, userdb);
    }

    @Test
    public void t025_updateForSnapshotNoChange() {
        User user = (User) objMap.get("user");
        User snapshot = Query.snapshot(user);
        Assert.assertEquals(0, query.update(user, snapshot));
        User userdb = query.objById(User.class, user.getUserid());
        this._validateUser(user, userdb);
    }

    @Test
    public void t026_testBatchInsert() throws Exception {
        int size = 5;
        List<Role> roles = new ArrayList<Role>();
        for (int i = 0; i < size; i++) {
            Role role = new Role();
            role.setCreateTime(new Date());
            roles.add(role);
        }
        List<Role> roles2 = query.batchInsert(roles);
        Assert.assertNotNull(roles2);
        Assert.assertEquals(roles.size(), roles2.size());
        for (Role r : roles2) {
            Assert.assertNotEquals(0, r.getRoleId());
        }
    }

    @Test
    public void testBatchInsert0() throws Exception {
        List<UserRef> list = new ArrayList<UserRef>();
        for (int i = 0; i < 3; i++) {
            UserRef ur = new UserRef();
            ur.setRefid(i);
            list.add(ur);
        }
        List<UserRef> dblist = query.batchInsert(list);
        Assert.assertNotNull(dblist);
        Assert.assertEquals(list.size(), dblist.size());
    }

    @Test
    public void testBatchUpdate() throws Exception {
        User user = (User) objMap.get("user");
        User user1 = (User) objMap.get("user1");
        String nick = "akwei";
        String nick1 = "akwei1";
        List<Object[]> valuesList = new ArrayList<Object[]>();
        valuesList.add(new Object[]{nick, user.getUserid()});
        valuesList.add(new Object[]{nick1, user1.getUserid()});
        int[] res = query.batchUpdate(User.class, "set nick=? where userid=?", valuesList);
        Assert.assertEquals(2, res.length);
        Assert.assertEquals(1, res[0]);
        Assert.assertEquals(1, res[1]);
        User dbUser = query.objById(User.class, user.getUserid());
        Assert.assertNotNull(dbUser);
        Assert.assertEquals(nick, dbUser.getNick());
        dbUser = query.objById(User.class, user1.getUserid());
        Assert.assertNotNull(dbUser);
        Assert.assertEquals(nick1, dbUser.getNick());
    }

    @Test
    public void t028_testBatchDelete() throws Exception {
        User user = (User) objMap.get("user");
        User user1 = (User) objMap.get("user1");
        List<Object[]> list = new ArrayList<Object[]>();
        list.add(new Object[]{user.getUserid()});
        list.add(new Object[]{user1.getUserid()});
        int[] res = query.batchDelete(User.class, "where userid=?", list);
        Assert.assertEquals(2, res.length);
        for (int i = 0; i < res.length; i++) {
            Assert.assertEquals(1, res[i]);
        }
        User dbUser = query.objById(User.class, user.getUserid());
        Assert.assertNull(dbUser);
        dbUser = query.objById(User.class, user1.getUserid());
        Assert.assertNull(dbUser);
    }

    @Test
    public void t029_testNoId_enum() {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderid(1);
        orderItem.setItemid(2);
        orderItem.setStatus(OrderItemStatus.NO);
        Number n = query.insertForNumber(orderItem);
        Assert.assertEquals(0, n.intValue());
        List<OrderItem> orderItems = query.list(OrderItem.class, null, null);
        Assert.assertEquals(1, orderItems.size());
    }

    @Test
    public void t030_testNoIdUpdateErr() {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderid(1);
        orderItem.setItemid(2);
        orderItem.setStatus(OrderItemStatus.NO);
        query.insertForNumber(orderItem);
        try {
            query.update(orderItem);
            Assert.fail("must fail for update err");
        } catch (HaloIdException e) {
        }
    }

    @Test
    public void t031_testNoIdDeleteErr() {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderid(1);
        orderItem.setItemid(2);
        orderItem.setStatus(OrderItemStatus.NO);
        try {
            query.delete(orderItem);
            Assert.fail("must fail for delete err");
        } catch (HaloIdException e) {
        }
    }

    @Test
    public void t033_testInsertIngore() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        User user = new User();
        user.setAddr("abddddc");
        user.setCreatetime(new Timestamp(cal.getTimeInMillis()));
        user.setIntro("intro");
        user.setNick("+++我的昵称我的昵称袁伟aabb");
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
        int userId = this.query.insertIgnore(user).intValue();
        Assert.assertNotEquals(0, userId);
        User userdb = query.objById(User.class, user.getUserid());
        this._validateUser(user, userdb);
    }

    @Test
    public void t034_testGetMap() throws Exception {
        User user = (User) objMap.get("user");
        Map<String, Object> map = this.query.getJdbcSupport().getMap("select " +
                "* from user where userid=?", new Object[]{user.getUserid()});
        Assert.assertEquals(user.getUserid(), ((Number) map.get("userid"))
                .intValue());
        Assert.assertEquals(1, ((Number) map.get("enableflag")).intValue());
    }

    @Test
    public void t035_testGetMapList() throws Exception {
        User user = (User) objMap.get("user");
        List<Map<String, Object>> mapList = this.query.getJdbcSupport()
                .getMapList("select " + "* from user where userid=?", new
                        Object[]{user.getUserid() + System.currentTimeMillis()});
        Assert.assertEquals(0, mapList.size());
    }

    @Test
    public void t036_testGetMapList1() throws Exception {
        User user = (User) objMap.get("user");
        List<Map<String, Object>> mapList = this.query.getJdbcSupport()
                .getMapList("select " + "* from user where userid=?", new
                        Object[]{user.getUserid()});
        Assert.assertNotEquals(0, mapList.size());

        Map<String, Object> map = mapList.get(0);
        Assert.assertEquals(user.getUserid(), ((Number) map.get("userid"))
                .intValue());
        Assert.assertEquals(1, ((Number) map.get("enableflag")).intValue());
    }

    @Test
    public void t038_testReplace2() {
        {
            Minfo info = new Minfo();
            info.setName("akwei");
            info.setMkey("uuk");
            this.query.insert(info);
        }
        {
            Minfo info = new Minfo();
            info.setName("akweiii");
            info.setMkey("uuk");
            try {
                this.query.insert(info);
                Assert.fail("must DuplicateKeyException");
            } catch (DuplicateKeyException e) {

            }
        }
    }

    @Test
    public void t039_testCasUpdate() throws Exception {
        User user = (User) objMap.get("user");
        user.setAddr("abcderg");
        Assert.assertEquals(user.getVer(), 0);
        Assert.assertEquals(1, this.query.casUpdate(user));
        Assert.assertEquals(user.getVer(), 1);
        User dbUser = this.query.objById(User.class, user.getUserid());
        Assert.assertEquals(user.getVer(), dbUser.getVer());
    }

    @Test
    public void t040_testCasUpdate1() throws Exception {
        User user = (User) objMap.get("user");
        Object snapshot = Query.snapshot(user);
        user.setAddr("abcderg");
        Assert.assertEquals(user.getVer(), 0);
        Assert.assertEquals(1, this.query.casUpdate(user, snapshot));
        Assert.assertEquals(user.getVer(), 1);
        User dbUser = this.query.objById(User.class, user.getUserid());
        Assert.assertEquals(user.getVer(), dbUser.getVer());
    }

    @Test
    public void t041_casUpdateSuccess() throws Exception {
        User user = (User) objMap.get("user");
        user.setAddr("akkkkk");
        long ver = user.getVer();
        int update = this.query.casUpdate(user);
        Assert.assertEquals(1, update);
        Assert.assertEquals(ver + 1, user.getVer());
    }

    @Test
    public void t042_casUpdateFail() throws Exception {
        User user = (User) objMap.get("user");
        user.setAddr("akkkkk");
        long ver = user.getVer();
        this.query.update(User.class, "set ver=999 where userid=?", new Object[]{user.getUserid()});
        int update = this.query.casUpdate(user);
        Assert.assertEquals(0, update);
        Assert.assertEquals(ver, user.getVer());
    }

    @Test
    public void t043_casUpdateSuccessWithSnapshot() throws Exception {
        User user = (User) objMap.get("user");
        Object snapshot = Query.snapshot(user);
        user.setAddr("akkkkk");
        long ver = user.getVer();
        int update = this.query.casUpdate(user, snapshot);
        Assert.assertEquals(1, update);
        Assert.assertEquals(ver + 1, user.getVer());
    }

    @Test
    public void t044_casUpdateFailWithSnapshot() throws Exception {
        User user = (User) objMap.get("user");
        Object snapshot = Query.snapshot(user);
        user.setAddr("akkkkk");
        long ver = user.getVer();
        this.query.update(User.class, "set ver=999 where userid=?", new Object[]{user.getUserid()});
        int update = this.query.casUpdate(user, snapshot);
        Assert.assertEquals(0, update);
        Assert.assertEquals(ver, user.getVer());
    }

    @Test
    public void t045_defaultSetting() throws Exception {
        HaloPropertiesDataSource haloDALDataSource = (HaloPropertiesDataSource) this.haloDALDataSource;
        Assert.assertEquals(HaloDALC3p0PropertiesDataSource.NAME, haloDALDataSource.getDataSourceClassName());
        Assert.assertEquals(DefSlaveSelectStrategy.class
                , haloDALDataSource.getSlaveSelectStrategy().getClass());

        haloDALDataSource.setSlaveSelectStrategy(new DefSlaveSelectStrategy());
        Assert.assertEquals(DefSlaveSelectStrategy.class
                , haloDALDataSource.getSlaveSelectStrategy().getClass());
    }

    private void _validateUser(User user, User dbUser2) {
        Assert.assertNotNull(dbUser2);
        Assert.assertEquals(user.getUserid(), dbUser2.getUserid());
        Assert.assertEquals(user.getAddr(), dbUser2.getAddr());
        Assert.assertEquals(user.getIntro(), dbUser2.getIntro());
        Assert.assertEquals(user.getNick(), dbUser2.getNick());
        Assert.assertEquals(user.getSex(), dbUser2.getSex());
        Assert.assertEquals(user.getUuid(), dbUser2.getUuid());
        Assert.assertEquals(user.getUuid2(), dbUser2.getUuid2());
        Assert.assertEquals(String.valueOf(user.getUuid3()), String.valueOf(dbUser2.getUuid3()));
        Assert.assertEquals(String.valueOf(user.getUuid4()), String.valueOf(dbUser2.getUuid4()));
        Assert.assertEquals(user.getUuid5(), dbUser2.getUuid5());
        Assert.assertEquals(user.getUuid6(), dbUser2.getUuid6());
        Assert.assertEquals(user.getUuid7(), dbUser2.getUuid7());
        Assert.assertEquals(user.getUuid8(), dbUser2.getUuid8());
        Assert.assertEquals(user.getUuid9(), dbUser2.getUuid9());
        Assert.assertEquals(user.getUuid10(), dbUser2.getUuid10());
        Assert.assertEquals(user.getUuid11(), dbUser2.getUuid11());
        Assert.assertEquals(user.getUuid12(), dbUser2.getUuid12());
        Assert.assertEquals(user.getCreatetime().getTime() / 1000, dbUser2.getCreatetime().getTime() / 1000);
        Assert.assertEquals(user.getUsersex(), dbUser2.getUsersex());
        Assert.assertEquals(user.isEnableflag(), dbUser2.isEnableflag());
    }
}
