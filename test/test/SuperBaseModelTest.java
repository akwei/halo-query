package test;

import halo.query.model.ModelLoader;

import org.junit.Assert;
import org.junit.Test;

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
