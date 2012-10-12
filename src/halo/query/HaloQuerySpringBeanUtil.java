package halo.query;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class HaloQuerySpringBeanUtil implements ApplicationContextAware {

	private static HaloQuerySpringBeanUtil springBeanUtilIns = null;

	private ApplicationContext applicationContext;

	public static HaloQuerySpringBeanUtil instance() {
		return springBeanUtilIns;
	}

	public HaloQuerySpringBeanUtil() {
		springBeanUtilIns = this;
	}

	public Object getBean(String beanName) {
		return this.applicationContext.getBean(beanName);
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
}
