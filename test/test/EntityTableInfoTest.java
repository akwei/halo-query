package test;

import halo.query.mapping.EntityTableInfo;

import java.lang.reflect.Field;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

public class EntityTableInfoTest {

	@Test
	public void idField() {
		EntityTableInfo<TestUser> info = new EntityTableInfo<TestUser>(
		        TestUser.class);
		try {
			Field idField = TestUser.class.getDeclaredField("userid");
			Assert.assertEquals(idField, info.getIdField());
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void sql() {
		EntityTableInfo<TestUser> info = new EntityTableInfo<TestUser>(
		        TestUser.class);
		Assert.assertEquals(
		        "insert into testuser(userid,nick,createtime,gender,money,purchase) values(?,?,?,?,?,?)",
		        info.getInsertSQL(null, true));
		Assert.assertEquals("delete from testuser where userid=?",
		        info.getDeleteSQL(null));
		Assert.assertEquals(
		        "update testuser set nick=?,createtime=?,gender=?,money=?,purchase=? where userid=?",
		        info.getUpdateSQL(null));
	}

	@Test
	public void values() {
		EntityTableInfo<TestUser> info = new EntityTableInfo<TestUser>(
		        TestUser.class);
		Assert.assertEquals(
		        "insert into testuser(userid,nick,createtime,gender,money,purchase) values(?,?,?,?,?,?)",
		        info.getInsertSQL(null, true));
		Assert.assertEquals("delete from testuser where userid=?",
		        info.getDeleteSQL(null));
		Assert.assertEquals(
		        "update testuser set nick=?,createtime=?,gender=?,money=?,purchase=? where userid=?",
		        info.getUpdateSQL(null));
		TestUser testUser = new TestUser();
		testUser.setUserid(9);
		testUser.setCreatetime(new Date());
		testUser.setGender((byte) 1);
		testUser.setMoney(78.909);
		testUser.setNick("nickname");
		testUser.setPurchase(56.43f);
		Object idValue = info.getSqlMapper().getIdParam(testUser);
		Assert.assertEquals(9L, idValue);
		Object[] insertValues = info.getSqlMapper()
		        .getParamsForInsert(testUser, true);
		Assert.assertEquals(testUser.getUserid(), insertValues[0]);
		Assert.assertEquals(testUser.getNick(), insertValues[1]);
		Assert.assertEquals(testUser.getCreatetime(), insertValues[2]);
		Assert.assertEquals(testUser.getGender(), insertValues[3]);
		Assert.assertEquals(testUser.getMoney(), insertValues[4]);
		Assert.assertEquals(testUser.getPurchase(), insertValues[5]);
	}
}