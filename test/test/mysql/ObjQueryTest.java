package test.mysql;

import halo.query.ObjQuery;
import halo.query.Query;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import test.SuperBaseModelTest;
import test.bean.Member;
import test.bean.TestUser;
import test.bean.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/query-test.xml" })
@Transactional
public class ObjQueryTest extends SuperBaseModelTest {

	@Resource
	private Query query;

	@Test
	public void list() {
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
		ObjQuery objQuery = new ObjQuery(query);
		List<User> list = objQuery.from(User.class)
		        .whereEq("userid", user.getUserid())
		        .orderByDesc("userid").mysqlList();
		Assert.assertEquals(1, list.size());
	}

	@Test
	public void listMulti() {
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
		ObjQuery objQuery = new ObjQuery(query);
		List<TestUser> list = objQuery
		        .fromInnerJoinOn(TestUser.class, Member.class)
		        .whereEq("testuser_.gender", testUser.getGender())
		        .orderByDesc("testuser_.userid").limit(0, 5).mysqlList();
		Assert.assertEquals(1, list.size());
	}
}
