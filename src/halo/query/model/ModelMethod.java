package halo.query.model;

import java.util.ArrayList;
import java.util.List;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

/**
 * 使用javassit自动生成方法代码
 * 
 * @author akwei
 */
public class ModelMethod {

	private static CtMethod createMethod(String methodFunc,
	        CtClass ctClass) throws CannotCompileException {
		return CtNewMethod.make(methodFunc, ctClass);
	}

	public static List<CtMethod> addNewMethod(ClassPool pool, String className,
	        CtClass ctClass)
	        throws CannotCompileException, NotFoundException {
		List<CtMethod> list = new ArrayList<CtMethod>();
		CtClass stringCls = pool.get("java.lang.String");
		CtClass objectsCls = pool.get("java.lang.Object[]");
		CtClass objectCls = pool.get("java.lang.Object");
		CtClass intCls = pool.get("int");
		String classInMethod = className + ".class";
		// count
		try {
			ctClass.getDeclaredMethod("count", new CtClass[] { stringCls,
			        objectsCls });
		}
		catch (NotFoundException e) {
			list.add(createMethod(
			        "public static int count(String sqlAfterTable, Object[] values){"
			                + "return query.count("
			                + classInMethod
			                + ", sqlAfterTable, values);"
			                + "}", ctClass));
		}
		// objById
		try {
			ctClass.getDeclaredMethod("objById", new CtClass[] {
			        objectCls });
		}
		catch (NotFoundException e) {
			list.add(createMethod(
			        "public static Object objById(Object idValue) {"
			                + "return query.objById(" + classInMethod
			                + ", idValue);" + "}",
			        ctClass));
		}
		// obj
		try {
			ctClass.getDeclaredMethod("obj", new CtClass[] {
			        stringCls, objectsCls });
		}
		catch (NotFoundException e) {
			list.add(createMethod(
			        "public static Object obj(String sqlAfterTable, Object[] values) {"
			                + "return query.obj(" + classInMethod
			                + ", sqlAfterTable, values);"
			                + "}", ctClass));
		}
		// mysqlList
		try {
			ctClass.getDeclaredMethod("mysqlList", new CtClass[] {
			        stringCls, intCls, intCls, objectsCls });
		}
		catch (NotFoundException e) {
			list.add(createMethod(
			        "public static java.util.List mysqlList(String sqlAfterTable, int begin, int size, Object[] values) {"
			                + "return query.mysqlList("
			                + classInMethod
			                + ", sqlAfterTable, begin, size, values);"
			                + "}", ctClass));
		}
		// db2List
		try {
			ctClass.getDeclaredMethod("db2List", new CtClass[] {
			        stringCls, intCls, intCls, objectsCls });
		}
		catch (NotFoundException e) {
			list.add(createMethod(
			        "public static java.util.List db2List(String where, String orderBy, int begin, int size, Object[] values) {"
			                + "return query.db2List("
			                + classInMethod
			                + ", where, orderBy, begin, size, values);"
			                + "}", ctClass));
		}
		// update
		try {
			ctClass.getDeclaredMethod("update", new CtClass[] {
			        stringCls, objectsCls });
		}
		catch (NotFoundException e) {
			list.add(createMethod(
			        "public static int update(String updateSqlSeg, Object[] values) {"
			                + "return query.update(" + classInMethod
			                + ", updateSqlSeg, values);"
			                + "}", ctClass));
		}
		try {
			ctClass.getDeclaredMethod("list", new CtClass[] { stringCls,
			        objectsCls });
		}
		catch (NotFoundException e) {
			list.add(createMethod(
			        "public static java.util.List list(String afterFrom, Object[] values) {"
			                + "return query.list("
			                + classInMethod
			                + ", afterFrom, values);"
			                + "}", ctClass));
		}
		return list;
	}
}
