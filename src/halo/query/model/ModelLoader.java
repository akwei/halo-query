package halo.query.model;

import halo.query.javassistutil.JavassistUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * 加载所有Model子类，并植入代码
 * 
 * @author akwei
 */
public class ModelLoader {

	private static boolean loaded = false;

	private final Map<String, CtClass> map = new HashMap<String, CtClass>();

	private final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

	private final ClassLoader classLoader = Thread.currentThread()
	        .getContextClassLoader();

	private String locationPattern;

	private String modelBasePath;

	private Log log = LogFactory.getLog(ModelLoader.class);

	public void setModelBasePath(String modelBasePath) {
		this.modelBasePath = modelBasePath;
	}

	public String getModelBasePath() {
		return modelBasePath;
	}

	public void setLocationPattern(String locationPattern) {
		this.locationPattern = locationPattern;
	}

	public String getLocationPattern() {
		return locationPattern;
	}

	public void makeModelClass() throws IOException, CannotCompileException,
	        NotFoundException, ClassNotFoundException {
		if (loaded) {
			return;
		}
		loaded = true;
		if ((this.locationPattern == null || this.locationPattern.trim()
		        .length() == 0)
		        && (this.modelBasePath == null || this.modelBasePath.trim()
		                .length() == 0)) {
			throw new IllegalArgumentException(
			        "must set locationPattern or basePackage");
		}
		if (this.locationPattern == null) {
			this.locationPattern = "classpath:" + this.modelBasePath
			        + "/**/*.class";
		}
		log.info("halo-query locationPattern:" + this.locationPattern);
		Resource[] resources = this.resolver.getResources(locationPattern);
		for (Resource resource : resources) {
			InputStream is = null;
			CtClass ctClass = null;
			is = resource.getInputStream();
			ctClass = JavassistUtil.getClassPool().makeClass(is);
			if (ctClass.getName().equals(BaseModel.class.getName())) {
				continue;
			}
			this.createClasses(ctClass);
		}
	}

	/**
	 * 创建类
	 * 
	 * @param ctClass
	 * @return false:已经存在 true:创建成功
	 */
	public boolean createClass(CtClass ctClass) {
		try {
			log.info("javassist override class [" + ctClass.getName() + "]");
			String className = ctClass.getName();
			if (map.containsKey(className)) {
				return false;
			}
			List<CtMethod> list = ModelMethod.addNewMethod(
			        JavassistUtil.getClassPool(), className, ctClass);
			for (CtMethod ctMethod : list) {
				ctClass.addMethod(ctMethod);
			}
			ctClass.toClass(classLoader, classLoader.getClass()
			        .getProtectionDomain());
			map.put(className, ctClass);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			ctClass.defrost();
		}
		return true;
	}

	public void createClasses(CtClass ctClass) {
		List<CtClass> supList = this.getSuperClasses(ctClass);
		if (supList.isEmpty()) {
			try {
				CtClass c = ctClass.getSuperclass();
				if (c != null && c.getName().equals(BaseModel.class.getName())) {
					this.createClass(ctClass);
				}
				return;
			}
			catch (NotFoundException e) {
				throw new RuntimeException(e);
			}
		}
		Collections.reverse(supList);
		for (CtClass o : supList) {
			this.createClass(o);
		}
		this.createClass(ctClass);
	}

	/**
	 * 获得ctClass 的所有父类，除去BaseModel.class Object.class
	 * ctClass必须是BaseModel的子类，否则返回empty list
	 * 
	 * @param ctClass
	 * @return
	 * @throws NotFoundException
	 */
	public List<CtClass> getSuperClasses(CtClass ctClass) {
		try {
			List<CtClass> list = new ArrayList<CtClass>();
			CtClass superCls = ctClass.getSuperclass();
			while (true) {
				if (superCls == null) {
					break;
				}
				if (superCls.getName().equals(BaseModel.class.getName())) {
					break;
				}
				if (superCls.getName().equals(Object.class.getName())) {
					break;
				}
				list.add(superCls);
				superCls = superCls.getSuperclass();
			}
			if (list.isEmpty()) {
				return list;
			}
			CtClass last = list.get(list.size() - 1);
			if (last.getSuperclass().getName()
			        .equals(BaseModel.class.getName())) {
				return list;
			}
			return new ArrayList<CtClass>();
		}
		catch (NotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) throws Exception {
		ModelLoader loader = new ModelLoader();
		loader.setModelBasePath("test");
		loader.makeModelClass();
	}
}
