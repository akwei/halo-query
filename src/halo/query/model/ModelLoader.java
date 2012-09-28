package halo.query.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * 加载所有Model子类，并植入代码
 * 
 * @author akwei
 */
public class ModelLoader {

	private final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

	private final ClassLoader classLoader = Thread.currentThread()
			.getContextClassLoader();

	private String locationPattern;

	private String modelBasePath;

	private ClassPool pool = ClassPool.getDefault();

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

	public ModelLoader() {
		pool.insertClassPath(new ClassClassPath(this.getClass()));
	}

	public void makeModelClass() throws IOException, CannotCompileException,
			NotFoundException, ClassNotFoundException {
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
		Resource[] resources = this.resolver.getResources(locationPattern);
		for (Resource resource : resources) {
			InputStream is = null;
			CtClass ctClass = null;
			try {
				is = resource.getInputStream();
				ctClass = pool.makeClass(is);
				HaloModel haloModel = (HaloModel) ctClass
						.getAnnotation(HaloModel.class);
				if (haloModel != null) {
					String className = ctClass.getName();
					List<CtMethod> list = ModelMethod.addNewMethod(pool,
							className, ctClass);
					for (CtMethod ctMethod : list) {
						ctClass.addMethod(ctMethod);
					}
					ctClass.toClass(classLoader, classLoader.getClass()
							.getProtectionDomain()
							);
				}
			}
			catch (ClassNotFoundException e) {
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
			finally {
				if (ctClass != null) {
					ctClass.defrost();
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		ModelLoader loader = new ModelLoader();
		loader.setModelBasePath("test");
		loader.makeModelClass();
	}
}
