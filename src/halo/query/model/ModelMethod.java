package halo.query.model;

import halo.query.Query;

import java.util.ArrayList;
import java.util.List;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;

/**
 * 使用javassit自动生成方法代码
 * 
 * @author akwei
 */
public class ModelMethod {
	protected static Query query;
	
	private static CtMethod createMethod(String methodFunc, Class<?> clazz,
			CtClass ctClass) throws CannotCompileException {
		return CtNewMethod.make(methodFunc, ctClass);
	}
	
	public static List<CtMethod> addNewMethod(Class<?> clazz, CtClass ctClass)
			throws CannotCompileException {
		List<CtMethod> list = new ArrayList<CtMethod>();
		// count
		list.add(createMethod(
				"public static int count(String sqlAfterTable, Object[] values)throws Exception{"
						+ "return query.count(clazz, sqlAfterTable, values);"
						+ "}", clazz, ctClass));
		list.add(createMethod(
				"public static int count(String tablePostfix, String sqlAfterTable,Object[] values)throws Exception{"
						+ "return query.count(clazz, tablePostfix, sqlAfterTable, values);"
						+ "}", clazz, ctClass));
		// objById
		list.add(createMethod(
				"public static <T> T objById(Object idValue) throws Exception{"
						+ "return query.objById(clazz, idValue);" + "}", clazz,
				ctClass));
		list.add(createMethod(
				"public static <T> T objById(Object idValue, String tablePostfix) throws Exception{"
						+ "return query.objById(clazz, tablePostfix, idValue);"
						+ "}", clazz, ctClass));
		// obj
		list.add(createMethod(
				"public Object obj(String sqlAfterTable, Object[] values) throws Exception{"
						+ "return query.obj(clazz, sqlAfterTable, values);"
						+ "}", clazz, ctClass));
		list.add(createMethod(
				"public Object obj(String tablePostfix, String sqlAfterTable, Object[] values) throws Exception{"
						+ "return query.obj(clazz, tablePostfix, sqlAfterTable, values);"
						+ "}", clazz, ctClass));
		// list
		list.add(createMethod(
				"public java.util.List list(String sqlAfterTable, Object[] values) throws Exception{"
						+ "return query.list(clazz, sqlAfterTable, values);"
						+ "}", clazz, ctClass));
		list.add(createMethod(
				"public java.util.List list(String tablePostfix, String sqlAfterTable, Object[] values) throws Exception{"
						+ "return query.list(clazz, tablePostfix, sqlAfterTable, values);"
						+ "}", clazz, ctClass));
		// listMySQL
		list.add(createMethod(
				"public static java.util.List listMySQL(String sqlAfterTable, int begin, int size, Object[] values) throws Exception{"
						+ "return query.listMySQL(clazz, sqlAfterTable, begin, size, values);"
						+ "}", clazz, ctClass));
		list.add(createMethod(
				"public static java.util.List listMySQL(String tablePostfix, String sqlAfterTable, int begin, int size, Object[] values) throws Exception{"
						+ "return query.listMySQL(clazz, tablePostfix, sqlAfterTable, begin, size, values);"
						+ "}", clazz, ctClass));
		// update
		list.add(createMethod(
				"public int update(String updateSqlSeg, Object[] values) throws Exception{"
						+ "return query.update(clazz, updateSqlSeg, values);"
						+ "}", clazz, ctClass));
		list.add(createMethod(
				"public int update(String tablePostfix, String updateSqlSeg, Object[] values) throws Exception{"
						+ "return query.update(clazz, tablePostfix, updateSqlSeg, values);"
						+ "}", clazz, ctClass));
		return list;
	}
}
