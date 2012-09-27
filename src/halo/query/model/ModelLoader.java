package halo.query.model;

import java.io.IOException;
import java.io.InputStream;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * 加载所有Model子类，并植入代码
 * 
 * @author akwei
 */
public class ModelLoader {
	private String locationPattern;
	
	private ClassPool pool = ClassPool.getDefault();
	
	public ModelLoader() {
		pool.insertClassPath(new ClassClassPath(this.getClass()));
	}
	
	public void setLocationPattern(String locationPattern) {
		this.locationPattern = locationPattern;
	}
	
	public String getLocationPattern() {
		return locationPattern;
	}
	
	private final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
	
	public void init() throws IOException {
		Resource[] resources = this.resolver.getResources(locationPattern);
		for (Resource resource : resources) {
			InputStream is = resource.getInputStream();
			CtClass ctClass = pool.makeClass(is);
		}
	}
}
