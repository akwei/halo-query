package halo.query.model;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ModelListener implements ServletContextListener {

	public void contextInitialized(ServletContextEvent event) {
		ModelLoader loader = new ModelLoader();
		String modelBasePath = event.getServletContext().getInitParameter(
		        "modelBasePath");
		loader.setModelBasePath(modelBasePath);
		try {
			loader.makeModelClass();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		// 目前不支持程序销毁
	}
}
