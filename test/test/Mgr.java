package test;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

public class Mgr {
	public static void main(String[] args) throws Exception {
		ClassPool pool = ClassPool.getDefault();
		// String superClassName = "halo.query.HaloModel";
		// CtClass ccSuperObj = pool.get(superClassName);
		CtClass ccObject = pool.get("java.lang.Object");
		String className = "test.UserModel";
		CtClass cc = pool.get(className);
		try {
			// CtMethod sm = ccSuperObj.getDeclaredMethod("objById",
			// new CtClass[] { ccObject });
			// ccSuperObj.removeMethod(sm);
			// ccSuperObj.toClass(Thread.currentThread().getContextClassLoader(),
			// Thread.currentThread().getContextClassLoader().getClass()
			// .getProtectionDomain());
			cc.getDeclaredMethod("objById", new CtClass[] { ccObject });
			System.out.println("objById(obj) is already defined in "
					+ className + ".");
		}
		catch (NotFoundException e) {
			CtMethod method = CtNewMethod.make(
					"public static Object objById(Object idValue) throws Exception{"
							+ "System.out.println(\"haha invoke.\");"
							+ "test.UserModel u = new test.UserModel();"
							+ "u.setName(\"akwei\"); " + "return u;" + "}", cc);
			cc.addMethod(method);
			cc.toClass(Thread.currentThread().getContextClassLoader(), Thread
					.currentThread().getContextClassLoader().getClass()
					.getProtectionDomain());
			System.out.println("objById(obj) was added.");
			Object obj = new Integer(1);
			UserModel user = UserModel.objById(obj);
			System.out.println(user.getName());
		}
	}
}
