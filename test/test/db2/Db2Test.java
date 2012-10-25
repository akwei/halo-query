package test.db2;

import halo.query.model.ModelLoader;

import java.sql.Timestamp;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import test.SuperBaseModelTest;
import test.bean.Db2TestModel;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/query-db2-test.xml" })
@Transactional
public class Db2Test extends SuperBaseModelTest {

	static {
		ModelLoader loader = new ModelLoader();
		loader.setModelBasePath("test");
		try {
			loader.makeModelClass();
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void insert() {
		Db2TestModel m = new Db2TestModel();
		m.setName("akweidbtest");
		m.create();
		if (m.getId() <= 0) {
			Assert.fail("id is <=0");
		}
	}

	@Test
	public void update() {
		Db2TestModel m = new Db2TestModel();
		m.setName("akweidbtest_update");
		m.setTime(new Timestamp(System.currentTimeMillis()));
		m.create();
		m.setName("akwei_update");
		m.update();
		Db2TestModel loadFromDb = Db2TestModel.objById(m.getId());
		Assert.assertNotNull(loadFromDb);
		Assert.assertEquals(m.getId(), loadFromDb.getId());
		Assert.assertEquals(m.getName(), loadFromDb.getName());
	}

	@Test
	public void delete() {
		Db2TestModel m = new Db2TestModel();
		m.setName("akweidbtest_update");
		m.setTime(new Timestamp(System.currentTimeMillis()));
		m.create();
		m.delete();
		Db2TestModel loadFromDb = Db2TestModel.objById(m.getId());
		Assert.assertNull(loadFromDb);
	}

	@Test
	public void list() {
		Db2TestModel m = new Db2TestModel();
		m.setName("akweidbtest_update");
		m.setTime(new Timestamp(System.currentTimeMillis()));
		m.create();
		List<Db2TestModel> list =
		        Db2TestModel.db2List(
		                "where ewallet_test.id=?",
		                "order by id desc", 0, 1, new Object[] { m.getId() });
		Assert.assertEquals(1, list.size());
	}

	@Test
	public void list2() {
		Db2TestModel m = new Db2TestModel();
		m.setName("akweidbtest_update");
		m.setTime(new Timestamp(System.currentTimeMillis()));
		m.create();
		Db2TestModel.getList();
	}

	@Test
	public void obj() {
		Db2TestModel m = new Db2TestModel();
		m.setName("akweidbtest_update");
		m.setTime(new Timestamp(System.currentTimeMillis()));
		m.create();
		Db2TestModel.getObj();
	}
}
