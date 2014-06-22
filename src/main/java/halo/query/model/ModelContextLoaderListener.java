package halo.query.model;

import javax.servlet.ServletContextEvent;

import org.springframework.web.context.ContextLoaderListener;

public class ModelContextLoaderListener extends ContextLoaderListener {

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ModelLoader loader = new ModelLoader();
		String modelBasePath = event.getServletContext().getInitParameter(
				"modelBasePath");
		loader.setModelBasePath(modelBasePath);
		try {
			loader.makeModelClass();
			super.contextInitialized(event);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
