package test.mysql;

import halo.query.InsertFlag;
import halo.query.Query;
import halo.query.SqlBuilder;
import halo.query.UpdateSnapshotInfo;
import halo.query.mapping.EntityTableInfo;
import org.junit.Assert;
import org.junit.Test;
import test.SuperBaseModelTest;
import test.bean.MultiIdObj;
import test.bean.TestUser;

import java.lang.reflect.Field;
import java.util.Date;


public class EntityTableInfoTest extends SuperBaseModelTest {

    @Test
    public void idField() {
        EntityTableInfo<TestUser> info = new EntityTableInfo<>(
                TestUser.class);
        try {
            Field idField = TestUser.class.getDeclaredField("userid");
            Assert.assertEquals(idField, info.getIdFields().iterator().next());
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void sql() {
        Assert.assertEquals(
                "insert into testuser(userid,nick,createtime,gender,money,purchase,ver) values(?,?,?,?,?,?,?)",
                SqlBuilder.buildInsertSQL(TestUser.class, true, InsertFlag.INSERT_INTO, null));

        Assert.assertEquals(
                "replace into testuser(userid,nick,createtime,gender,money,purchase,ver) values(?,?,?,?,?,?,?)",
                SqlBuilder.buildInsertSQL(TestUser.class, true, InsertFlag.REPLACE_INTO, null));

        Assert.assertEquals(
                "insert ignore into testuser(userid,nick,createtime,gender,money,purchase,ver) values(?,?,?,?,?,?,?)",
                SqlBuilder.buildInsertSQL(TestUser.class, true, InsertFlag.INSERT_IGNORE_INTO, null));

        Assert.assertEquals(
                "insert into testuser(userid,nick,createtime,gender,money,purchase,ver) values(?,?,?,?,?,?,?) on duplicate key update nick=values(nick),createtime=values(createtime),gender=values(gender)",
                SqlBuilder.buildInsertSQL(TestUser.class, true, InsertFlag.INSERT_INTO_ON_DUPLICATE_KEY_UPDATE, new String[]{"nick", "createtime", "gender"}));

        Assert.assertEquals("delete from testuser where userid=?", SqlBuilder.buildDeleteSQL(TestUser.class));

        Assert.assertEquals(
                "update testuser set nick=?,createtime=?,gender=?,money=?,purchase=?,ver=? where userid=?",
                SqlBuilder.buildUpdateSQL(TestUser.class));


        //multi id
        Assert.assertEquals("insert into multiidobj(uid,oid,create_time) values(?,?,?)", SqlBuilder.buildInsertSQL(MultiIdObj.class, true, InsertFlag.INSERT_INTO, null));

        Assert.assertEquals(
                "update multiidobj set create_time=? where uid=? and oid=?",
                SqlBuilder.buildUpdateSQL(MultiIdObj.class));

        Assert.assertEquals("delete from multiidobj where uid=? and oid=?", SqlBuilder.buildDeleteSQL(MultiIdObj.class));

        //update delete
        Assert.assertEquals("delete from multiidobj where oid=?", SqlBuilder.buildDeleteSQL(MultiIdObj.class, "where oid=?"));
        Assert.assertEquals("update multiidobj set uid=?,oid=?,create_time=? where oid=?", SqlBuilder.buildUpdateSQL(MultiIdObj.class, "set uid=?,oid=?,create_time=? where oid=?"));

        //select
        Assert.assertEquals("select count(*) from testuser as testuser_ where userid=? and nick=?", SqlBuilder.buildCountSQL(TestUser.class, "where userid=? and nick=?"));
        Assert.assertEquals("select count(*) from testuser as testuser_,multiidobj as multiidobj_ where userid=? and nick=?", SqlBuilder.buildCountSQL(new Class[]{TestUser.class, MultiIdObj.class}, "where userid=? and nick=?"));

        Assert.assertEquals("select " +
                "multiidobj_.uid as multiidobj_uid," +
                "multiidobj_.oid as multiidobj_oid," +
                "multiidobj_.create_time as multiidobj_create_time" +
                " from multiidobj as multiidobj_ where uid=? and oid=?", SqlBuilder.buildListSQL(MultiIdObj.class, "where uid=? and oid=?"));

        Assert.assertEquals("select multiidobj_.uid as multiidobj_uid,multiidobj_.oid as multiidobj_oid,multiidobj_.create_time as multiidobj_create_time from multiidobj as multiidobj_ where uid=? and oid=? order by uid desc limit 1,10", SqlBuilder.buildMysqlListSQL(MultiIdObj.class, "where uid=? and oid=? order by uid desc", 1, 10));

        Assert.assertEquals("select " +
                "testuser_.userid as testuser_userid," +
                "testuser_.nick as testuser_nick," +
                "testuser_.createtime as testuser_createtime," +
                "testuser_.gender as testuser_gender," +
                "testuser_.money as testuser_money," +
                "testuser_.purchase as testuser_purchase," +
                "testuser_.ver as testuser_ver," +
                "multiidobj_.uid as multiidobj_uid," +
                "multiidobj_.oid as multiidobj_oid," +
                "multiidobj_.create_time as multiidobj_create_time" +
                " from testuser as testuser_,multiidobj as multiidobj_ where uid=? and oid=? order by uid desc limit 1,10", SqlBuilder.buildMysqlListSQL(new Class[]{TestUser.class, MultiIdObj.class}, "where uid=? and oid=? order by uid desc", 1, 10));

        Assert.assertEquals("select multiidobj_.uid as multiidobj_uid,multiidobj_.oid as multiidobj_oid,multiidobj_.create_time as multiidobj_create_time from multiidobj as multiidobj_ where uid=? and oid=? order by uid desc", SqlBuilder.buildListSQL(MultiIdObj.class, "where uid=? and oid=? order by uid desc"));

        Assert.assertEquals("where uid=? and oid=? for update", SqlBuilder.buildObjByIdsSQLSeg(MultiIdObj.class, new Object[]{10, 5}, true));

        Assert.assertEquals("where uid=? and oid=?", SqlBuilder.buildObjByIdsSQLSeg(MultiIdObj.class, new Object[]{10, 5}, false));


        {
            TestUser testUser = new TestUser();
            testUser.setUserid(115);
            testUser.setNick("akwei");
            testUser.setGender((byte) 0);
            testUser.setCreatetime(new Date());
            testUser.setMoney(19f);
            testUser.setPurchase(120.89f);
            TestUser sn = Query.snapshot(testUser);
            testUser.setNick("akweiwei");
            testUser.setGender((byte) 1);
            UpdateSnapshotInfo updateSnapshotInfo = SqlBuilder.buildUpdateSegSQLForSnapshot(testUser, sn, false);

            Assert.assertNotNull(updateSnapshotInfo);
            Assert.assertEquals("set nick=?,gender=? where userid=?", updateSnapshotInfo.getSqlSeg());
            Assert.assertEquals(3, updateSnapshotInfo.getValues().size());
            Assert.assertEquals(testUser.getNick(), updateSnapshotInfo.getValues().get(0));
            Assert.assertEquals((byte) 1, updateSnapshotInfo.getValues().get(1));
            Assert.assertEquals(115L, updateSnapshotInfo.getValues().get(2));
            sn = Query.snapshot(testUser);
            updateSnapshotInfo = SqlBuilder.buildUpdateSegSQLForSnapshot(testUser, sn, false);
            Assert.assertNull(updateSnapshotInfo);
        }

        //for cas update
        {
            TestUser testUser = new TestUser();
            testUser.setUserid(115);
            testUser.setNick("akwei");
            testUser.setGender((byte) 0);
            testUser.setCreatetime(new Date());
            testUser.setMoney(19f);
            testUser.setPurchase(120.89f);
            TestUser sn = Query.snapshot(testUser);
            testUser.setNick("akweiwei");
            testUser.setGender((byte) 1);
            UpdateSnapshotInfo updateSnapshotInfo = SqlBuilder.buildUpdateSegSQLForSnapshot(testUser, sn, true);

            Assert.assertNotNull(updateSnapshotInfo);
            Assert.assertEquals("set nick=?,gender=?,ver=? where userid=? and ver=?", updateSnapshotInfo.getSqlSeg());
            Assert.assertEquals(5, updateSnapshotInfo.getValues().size());
            Assert.assertEquals(testUser.getNick(), updateSnapshotInfo.getValues().get(0));
            Assert.assertEquals((byte) 1, updateSnapshotInfo.getValues().get(1));
            Assert.assertEquals(1L, updateSnapshotInfo.getValues().get(2));
            Assert.assertEquals(115L, updateSnapshotInfo.getValues().get(3));
            Assert.assertEquals(0L, updateSnapshotInfo.getValues().get(4));

            sn = Query.snapshot(testUser);
            updateSnapshotInfo = SqlBuilder.buildUpdateSegSQLForSnapshot(testUser, sn, false);
            Assert.assertNull(updateSnapshotInfo);
        }
    }

    @Test
    public void values() {
        EntityTableInfo<TestUser> info = new EntityTableInfo<>(
                TestUser.class);
        Assert.assertEquals(
                "insert into testuser(userid,nick,createtime,gender,money,purchase,ver) values(?,?,?,?,?,?,?)",
                SqlBuilder.buildInsertSQL(TestUser.class, true, InsertFlag.INSERT_INTO, null));
        Assert.assertEquals("delete from testuser where userid=?",
                SqlBuilder.buildDeleteSQL(TestUser.class));
        Assert.assertEquals(
                "update testuser set nick=?,createtime=?,gender=?,money=?,purchase=?,ver=? where userid=?",
                SqlBuilder.buildUpdateSQL(TestUser.class));
        TestUser testUser = new TestUser();
        testUser.setUserid(9);
        testUser.setCreatetime(new Date());
        testUser.setGender((byte) 1);
        testUser.setMoney(78.909);
        testUser.setNick("nickname");
        testUser.setPurchase(56.43f);
        Object idValue = info.getSqlMapper().getIdParams(testUser)[0];
        Assert.assertEquals(9L, idValue);
        Object[] insertValues = info.getSqlMapper()
                .getParamsForInsert(testUser, true);
        Assert.assertEquals(testUser.getUserid(), insertValues[0]);
        Assert.assertEquals(testUser.getNick(), insertValues[1]);
        Assert.assertEquals(testUser.getCreatetime(), insertValues[2]);
        Assert.assertEquals(testUser.getGender(), insertValues[3]);
        Assert.assertEquals(testUser.getMoney(), insertValues[4]);
        Assert.assertEquals(testUser.getPurchase(), insertValues[5]);
        Assert.assertEquals(testUser.getVer(), insertValues[6]);
    }

    @Test
    public void createInSQL() throws Exception {
        String sql = "id in(?,?,?)";
        Assert.assertEquals(sql, SqlBuilder.createInSql("id", 3));
    }
}