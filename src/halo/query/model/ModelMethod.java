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
					"public static int count(String sqlAfterTable, Object[] values)throws Exception{"
							+ "return query.count("
							+ classInMethod
							+ ", sqlAfterTable, values);"
							+ "}", ctClass));
		}
		try {
			ctClass.getDeclaredMethod("count", new CtClass[] { stringCls,
					stringCls,
					objectsCls });
		}
		catch (NotFoundException e) {
			list.add(createMethod(
					"public static int count(String tablePostfix, String sqlAfterTable,Object[] values)throws Exception{"
							+ "return query.count("
							+ classInMethod
							+ ", tablePostfix, sqlAfterTable, values);"
							+ "}", ctClass));
		}
		// objById
		try {
			ctClass.getDeclaredMethod("objById", new CtClass[] {
					objectCls });
		}
		catch (NotFoundException e) {
			list.add(createMethod(
					"public static Object objById(Object idValue) throws Exception{"
							+ "return query.objById(" + classInMethod
							+ ", idValue);" + "}",
					ctClass));
		}
		try {
			ctClass.getDeclaredMethod("objById", new CtClass[] {
					objectCls, stringCls });
		}
		catch (NotFoundException e) {
			list.add(createMethod(
					"public static Object objById(Object idValue, String tablePostfix) throws Exception{"
							+ "return query.objById("
							+ classInMethod
							+ ", tablePostfix, idValue);"
							+ "}", ctClass));
		}
		// obj
		try {
			ctClass.getDeclaredMethod("obj", new CtClass[] {
					stringCls, objectsCls });
		}
		catch (NotFoundException e) {
			list.add(createMethod(
					"public Object obj(String sqlAfterTable, Object[] values) throws Exception{"
							+ "return query.obj(" + classInMethod
							+ ", sqlAfterTable, values);"
							+ "}", ctClass));
		}
		try {
			ctClass.getDeclaredMethod("obj", new CtClass[] {
					stringCls, stringCls, objectsCls });
		}
		catch (NotFoundException e) {
			list.add(createMethod(
					"public Object obj(String tablePostfix, String sqlAfterTable, Object[] values) throws Exception{"
							+ "return query.obj("
							+ classInMethod
							+ ", tablePostfix, sqlAfterTable, values);"
							+ "}", ctClass));
		}
		// list
		try {
			ctClass.getDeclaredMethod("list", new CtClass[] {
					stringCls, objectsCls });
		}
		catch (NotFoundException e) {
			list.add(createMethod(
					"public java.util.List list(String sqlAfterTable, Object[] values) throws Exception{"
							+ "return query.list("
							+ classInMethod
							+ ", sqlAfterTable, values);"
							+ "}", ctClass));
		}
		try {
			ctClass.getDeclaredMethod("list", new CtClass[] {
					stringCls, stringCls, objectsCls });
		}
		catch (NotFoundException e) {
			list.add(createMethod(
					"public java.util.List list(String tablePostfix, String sqlAfterTable, Object[] values) throws Exception{"
							+ "return query.list("
							+ classInMethod
							+ ", tablePostfix, sqlAfterTable, values);"
							+ "}", ctClass));
		}
		// listMySQL
		try {
			ctClass.getDeclaredMethod("listMySQL", new CtClass[] {
					stringCls, intCls, intCls, objectsCls });
		}
		catch (NotFoundException e) {
			list.add(createMethod(
					"public static java.util.List listMySQL(String sqlAfterTable, int begin, int size, Object[] values) throws Exception{"
							+ "return query.listMySQL("
							+ classInMethod
							+ ", sqlAfterTable, begin, size, values);"
							+ "}", ctClass));
		}
		try {
			ctClass.getDeclaredMethod("listMySQL", new CtClass[] {
					stringCls, stringCls, intCls, intCls, objectsCls });
		}
		catch (NotFoundException e) {
			list.add(createMethod(
					"public static java.util.List listMySQL(String tablePostfix, String sqlAfterTable, int begin, int size, Object[] values) throws Exception{"
							+ "return query.listMySQL("
							+ classInMethod
							+ ", tablePostfix, sqlAfterTable, begin, size, values);"
							+ "}", ctClass));
		}
		// update
		try {
			ctClass.getDeclaredMethod("update", new CtClass[] {
					stringCls, objectsCls });
		}
		catch (NotFoundException e) {
			list.add(createMethod(
					"public int update(String updateSqlSeg, Object[] values) throws Exception{"
							+ "return query.update(" + classInMethod
							+ ", updateSqlSeg, values);"
							+ "}", ctClass));
		}
		try {
			ctClass.getDeclaredMethod("update", new CtClass[] {
					stringCls, stringCls, objectsCls });
		}
		catch (NotFoundException e) {
			list.add(createMethod(
					"public int update(String tablePostfix, String updateSqlSeg, Object[] values) throws Exception{"
							+ "return query.update("
							+ classInMethod
							+ ", tablePostfix, updateSqlSeg, values);"
							+ "}", ctClass));
		}
		return list;
	}
}
