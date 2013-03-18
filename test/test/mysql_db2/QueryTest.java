package test.mysql_db2;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


import test.SuperBaseModelTest;
import test.bean.Db2TestModel;
import test.bean.TestUser2;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/query-test-mysql_and_db2.xml" })
@Transactional
public class QueryTest extends SuperBaseModelTest {

	@Test
	public void test() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0);
		Date d = cal.getTime();
		TestUser2 testUser = new TestUser2();
		testUser.setCreatetime(d);
		testUser.setGender((byte) 1);
		testUser.setMoney(99.448f);
		testUser.setPurchase(89.345f);
		testUser.setNick("nickname");
		testUser.create();
		if (testUser.getUserid() <= 0) {
			Assert.fail("id is <=0");
		}
		Db2TestModel m = new Db2TestModel();
		m.setName("akwei");
		m.setStr(null);
		m.setT1(999);
		m.setT2(null);
		m.setTime2(new Timestamp(System.currentTimeMillis()));
		m.create();
		if (m.getId() <= 0) {
			Assert.fail("id is <=0");
		}
	}
}
