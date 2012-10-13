package test;

import halo.query.Query;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/query-test.xml" })
// @Transactional
public class QueryTest {

	@Resource
	private Query query;

	@Resource
	private UserServiceImpl userServiceImpl;

	@Test
	public void testUserServcice() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0);
		Date date = cal.getTime();
		User user = new User();
		user.setAddr("abc");
		user.setCreatetime(date);
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
		this.userServiceImpl.createUserTx(user);
	}

	@Test
	public void insert_select_update_delete() {
		try {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.MILLISECOND, 0);
			Date date = cal.getTime();
			User user = new User();
			user.setAddr("abc");
			user.setCreatetime(date);
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
		catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void insert_select_update_deleteForNull() {
		try {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.MILLISECOND, 0);
			Date date = cal.getTime();
			User user = new User();
			user.setAddr("abc");
			user.setCreatetime(date);
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
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void select() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0);
		Date d = cal.getTime();
		TestUser testUser = new TestUser();
		testUser.setCreatetime(d);
		testUser.setGender((byte) 1);
		testUser.setMoney(99.448f);
		testUser.setPurchase(89.345f);
		testUser.setNick("nickname");
		try {
			testUser.setUserid(query.insertForNumber(testUser, "00")
			        .longValue());
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
		Member m = new Member();
		m.setUserid(testUser.getUserid());
		m.setGroupid(99);
		m.setNick("membernick");
		try {
			m.setMemberUserId(query.insertForNumber(m).longValue());
			List<Member> list = query.mysqlList(Member.class, "where 1=1", 0,
			        10, null);
			Assert.assertEquals(1, list.size());
			Member o = list.get(0);
			Assert.assertEquals(m.getMemberUserId(), o.getMemberUserId());
			Assert.assertEquals(m.getUserid(), o.getUserid());
			Assert.assertEquals(m.getGroupid(), o.getGroupid());
			Assert.assertEquals(m.getNick(), o.getNick());
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void selectMultTable() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0);
		Date d = cal.getTime();
		TestUser testUser = new TestUser();
		testUser.setCreatetime(d);
		testUser.setGender((byte) 1);
		testUser.setMoney(99.448f);
		testUser.setPurchase(89.345f);
		testUser.setNick("nickname");
		try {
			testUser.setUserid(query.insertForNumber(testUser, "00")
			        .longValue());
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
		Member m = new Member();
		m.setUserid(testUser.getUserid());
		m.setGroupid(99);
		m.setNick("membernick");
		try {
			m.setMemberUserId(query.insertForNumber(m).longValue());
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
		try {
			List<Member> list = query.mysqlList(new Class[] { TestUser.class,
			        Member.class }, new String[] { "00", null },
			        "where testuser.userid=member.userid and member.userid=?",
			        0, 1,
			        new Object[] { m.getUserid() }, new RowMapper<Member>() {

				        public Member mapRow(ResultSet rs, int rowNum)
				                throws SQLException {
					        Member mm = query.getRowMapper(Member.class)
					                .mapRow(rs, rowNum);
					        TestUser tu = query.getRowMapper(TestUser.class)
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
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void selectMultTable2() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0);
		Date d = cal.getTime();
		TestUser testUser = new TestUser();
		testUser.setCreatetime(d);
		testUser.setGender((byte) 1);
		testUser.setMoney(99.448f);
		testUser.setPurchase(89.345f);
		testUser.setNick("nickname");
		try {
			testUser.setUserid(query.insertForNumber(testUser, "00")
			        .longValue());
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
		Member m = new Member();
		m.setUserid(testUser.getUserid());
		m.setGroupid(99);
		m.setNick("membernick");
		try {
			m.setMemberUserId(query.insertForNumber(m).longValue());
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
		try {
			List<Member> list = query
			        .mysqlListMulti(
			                new Class[] { Member.class,
			                        TestUser.class
			                },
			                new String[] { null, "00" },
			                "where testuser.userid=member.userid and member.userid=? order by member.userid asc",
			                0, 1,
			                new Object[] { m.getUserid() });
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
				Assert.assertEquals(o.getTestUser().getMember(), o);
			}
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
}
