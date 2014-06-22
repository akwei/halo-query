package halo.query;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class HaloQuerySpringBeanUtil implements ApplicationContextAware {

	private static HaloQuerySpringBeanUtil springBeanUtilIns = null;

	private ApplicationContext applicationContext;

	public static HaloQuerySpringBeanUtil instance() {
		if (springBeanUtilIns == null) {
			throw new RuntimeException("please inject "
			        + HaloQuerySpringBeanUtil.class.getName() + " by spring");
		}
		return springBeanUtilIns;
	}

	public HaloQuerySpringBeanUtil() {
		springBeanUtilIns = this;
	}

	public Object getBean(String beanName) {
		try {
			return this.applicationContext.getBean(beanName);
		}
		catch (BeansException e) {
			throw new RuntimeException("can not find " + beanName, e);
		}
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
}
