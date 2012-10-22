package test;

import halo.query.model.ModelLoader;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/query-test.xml" })
@Transactional
public class SuperBaseModelTest {

	static {
		ModelLoader loader = new ModelLoader();
		loader.setModelBasePath("test");
		try {
			loader.makeModelClass();
		}
		catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void hello() {
	}
}
