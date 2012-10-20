package test;

import halo.query.model.ModelLoader;

import java.util.List;

import org.junit.Assert;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration({ "/query-db2-test.xml" })
//@Transactional
public class Db2Test {

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

	public void insert() {
		Db2TestModel m = new Db2TestModel();
		m.setName("akweidbtest");
		m.create();
		if (m.getId() <= 0) {
			Assert.fail("id is <=0");
		}
	}

	public void update() {
		Db2TestModel m = new Db2TestModel();
		m.setName("akweidbtest_update");
		m.create();
		m.setName("akwei_update");
		m.update();
		Db2TestModel loadFromDb = Db2TestModel.objById(m.getId());
		Assert.assertNotNull(loadFromDb);
		Assert.assertEquals(m.getId(), loadFromDb.getId());
		Assert.assertEquals(m.getName(), loadFromDb.getName());
	}

	public void delete() {
		Db2TestModel m = new Db2TestModel();
		m.setName("akweidbtest_update");
		m.create();
		m.delete();
		Db2TestModel loadFromDb = Db2TestModel.objById(m.getId());
		Assert.assertNull(loadFromDb);
	}

	public void list() {
		Db2TestModel m = new Db2TestModel();
		m.setName("akweidbtest_update");
		m.create();
		List<Db2TestModel> list = Db2TestModel.db2List(
		        "where ewallet_test.id=?",
		        "order by id desc", 0, 1, new Object[] { m.getId() });
		Assert.assertEquals(1, list.size());
	}
}
