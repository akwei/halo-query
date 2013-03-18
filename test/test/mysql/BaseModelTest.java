package test.mysql;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


import test.SuperBaseModelTest;
import test.bean.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/query-test.xml" })
@Transactional
public class BaseModelTest extends SuperBaseModelTest {

	private Map<String, Object> objMap;

	@After
	public void after() {
		User user = (User) objMap.get("user");
		User user1 = (User) objMap.get("user1");
		user.delete();
		user1.delete();
	}

	@Before
	public void before() {
		objMap = new HashMap<String, Object>();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0);
		User user = new User();
		user.setAddr("abc");
		user.setCreatetime(new Timestamp(cal.getTimeInMillis()));
		user.setIntro("intro");
		user.setNick("我的昵称我的昵称袁伟");
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
	public void update() {
		User user = (User) objMap.get("user");
		user.setNick("ooo");
		user.update();
		String nick = "akweiwei";
		User.update("set nick=? where userid=?",
		        new Object[] { nick, user.getUserid() });
		User dbUser = User.objById(user.getUserid());
		Assert.assertNotNull(dbUser);
		Assert.assertEquals(nick, dbUser.getNick());
	}

	@Test
	public void list() {
		User user = (User) objMap.get("user");
		List<User> list = User.list("where userid=?",
		        new Object[] { user.getUserid() });
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
		        new Object[] { user.getUserid() });
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
		        new Object[] { user.getUserid() });
		Assert.assertEquals(1, list.size());
		list = User.mysqlList("where sex=?", 0, 5,
		        new Object[] { user.getSex() });
		Assert.assertEquals(2, list.size());
	}

	@Test
	public void count() {
		User user = (User) objMap.get("user");
		int count = User.count("where userid=?",
		        new Object[] { user.getUserid() });
		Assert.assertEquals(1, count);
		count = User.count("where sex=?",
		        new Object[] { user.getSex() });
		Assert.assertEquals(2, count);
	}
}
