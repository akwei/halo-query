package halo.query.javassistutil;

import javassist.ClassClassPath;
import javassist.ClassPool;

public class JavassistUtil {

	private static final ClassPool pool = ClassPool.getDefault();
	static {
		pool.insertClassPath(new ClassClassPath(JavassistUtil.class));
	}

	private JavassistUtil() {
	}

	public static ClassPool getClassPool() {
		return pool;
	}
}
