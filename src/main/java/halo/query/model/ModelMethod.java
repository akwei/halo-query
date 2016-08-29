package halo.query.model;

import javassist.*;

import java.util.ArrayList;
import java.util.List;

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
        CtClass objectListCls = pool.get("java.util.List");
        CtClass objectCls = pool.get("java.lang.Object");
        CtClass intCls = pool.get("int");
        String classInMethod = className + ".class";
        // count
        try {
            ctClass.getDeclaredMethod("count", new CtClass[]{stringCls,
                    objectsCls});
        }
        catch (NotFoundException e) {
            list.add(createMethod(
                    "public static int count(String sqlAfterTable, Object[] values){"
                            + "return getQuery().count("
                            + classInMethod
                            + ", sqlAfterTable, values);"
                            + "}", ctClass));
        }
        // count2
        try {
            ctClass.getDeclaredMethod("count2", new CtClass[]{stringCls,
                    objectListCls});
        }
        catch (NotFoundException e) {
            list.add(createMethod(
                    "public static int count2(String sqlAfterTable, java.util.List values){"
                            + "return getQuery().count2("
                            + classInMethod
                            + ", sqlAfterTable, values);"
                            + "}", ctClass));
        }
        // objById
        try {
            ctClass.getDeclaredMethod("objById", new CtClass[]{
                    objectCls});
        }
        catch (NotFoundException e) {
            list.add(createMethod(
                    "public static Object objById(Object idValue) {"
                            + "return getQuery().objById(" + classInMethod
                            + ", idValue);" + "}",
                    ctClass));
        }
        // objByIds
        try {
            ctClass.getDeclaredMethod("objByIds", new CtClass[]{
                    objectsCls});
        }
        catch (NotFoundException e) {
            list.add(createMethod(
                    "public static Object objByIds(Object[] idValues) {"
                            + "return getQuery().objByIds(" + classInMethod
                            + ", idValues);" + "}",
                    ctClass));
        }
        // obj
        try {
            ctClass.getDeclaredMethod("obj", new CtClass[]{
                    stringCls, objectsCls});
        }
        catch (NotFoundException e) {
            list.add(createMethod(
                    "public static Object obj(String afterFrom, Object[] values) {"
                            + "return getQuery().obj(" + classInMethod
                            + ", afterFrom, values);"
                            + "}", ctClass));
        }
        // obj2
        try {
            ctClass.getDeclaredMethod("obj2", new CtClass[]{
                    stringCls, objectsCls});
        }
        catch (NotFoundException e) {
            list.add(createMethod(
                    "public static Object obj2(String afterFrom, java.util.List values) {"
                            + "return getQuery().obj2(" + classInMethod
                            + ", afterFrom, values);"
                            + "}", ctClass));
        }
        // mysqlList
        try {
            ctClass.getDeclaredMethod("mysqlList", new CtClass[]{
                    stringCls, intCls, intCls, objectsCls});
        }
        catch (NotFoundException e) {
            list.add(createMethod(
                    "public static java.util.List mysqlList(String afterFrom, int begin, int size, Object[] values) {"
                            + "return getQuery().mysqlList("
                            + classInMethod
                            + ", afterFrom, begin, size, values);"
                            + "}", ctClass));
        }
        // mysqlList2
        try {
            ctClass.getDeclaredMethod("mysqlList2", new CtClass[]{
                    stringCls, intCls, intCls, objectListCls});
        }
        catch (NotFoundException e) {
            list.add(createMethod(
                    "public static java.util.List mysqlList2(String afterFrom, int begin, int size, java.util.List values) {"
                            + "return getQuery().mysqlList2("
                            + classInMethod
                            + ", afterFrom, begin, size, values);"
                            + "}", ctClass));
        }
        // update
        try {
            ctClass.getDeclaredMethod("update", new CtClass[]{
                    stringCls, objectsCls});
        }
        catch (NotFoundException e) {
            list.add(createMethod(
                    "public static int update(String updateSqlSeg, Object[] values) {"
                            + "return getQuery().update(" + classInMethod
                            + ", updateSqlSeg, values);"
                            + "}", ctClass));
        }
        // update2
        try {
            ctClass.getDeclaredMethod("update2", new CtClass[]{
                    stringCls, objectListCls});
        }
        catch (NotFoundException e) {
            list.add(createMethod(
                    "public static int update2(String updateSqlSeg, java.util.List values) {"
                            + "return getQuery().update2(" + classInMethod
                            + ", updateSqlSeg, values);"
                            + "}", ctClass));
        }
        // list
        try {
            ctClass.getDeclaredMethod("list", new CtClass[]{stringCls,
                    objectsCls});
        }
        catch (NotFoundException e) {
            list.add(createMethod(
                    "public static java.util.List list(String afterFrom, Object[] values) {"
                            + "return getQuery().list("
                            + classInMethod
                            + ", afterFrom, values);"
                            + "}", ctClass));
        }
        // list2
        try {
            ctClass.getDeclaredMethod("list2", new CtClass[]{stringCls,
                    objectListCls});
        }
        catch (NotFoundException e) {
            list.add(createMethod(
                    "public static java.util.List list2(String afterFrom, java.util.List values) {"
                            + "return getQuery().list2("
                            + classInMethod
                            + ", afterFrom, values);"
                            + "}", ctClass));
        }
        // listInValues
        try {
            ctClass.getDeclaredMethod("listInValues",
                    new CtClass[]{stringCls, stringCls, objectsCls,
                            objectsCls});
        }
        catch (NotFoundException e) {
            list.add(createMethod(
                    "public static java.util.List listInValues(String afterFrom, String inColumn, Object[] values, Object[] inValues) {"
                            + "return getQuery().listInValues("
                            + classInMethod
                            + ", afterFrom, inColumn, values, inValues);"
                            + "}", ctClass));
        }
        // listInValues2
        try {
            ctClass.getDeclaredMethod("listInValues2",
                    new CtClass[]{stringCls, stringCls, objectListCls,
                            objectListCls});
        }
        catch (NotFoundException e) {
            list.add(createMethod(
                    "public static java.util.List listInValues2(String afterFrom, String inColumn, java.util.List values, java.util.List inValues) {"
                            + "return getQuery().listInValues2("
                            + classInMethod
                            + ", afterFrom, inColumn, values, inValues);"
                            + "}", ctClass));
        }
        // map
        try {
            ctClass.getDeclaredMethod("map", new CtClass[]{stringCls,
                    stringCls, objectsCls, objectsCls});
        }
        catch (NotFoundException e) {
            list.add(createMethod(
                    "public static java.util.Map map(String afterFrom, String inColumn, Object[] values, Object[] inValues) {"
                            + "return getQuery().map("
                            + classInMethod
                            + ", afterFrom, inColumn, values, inValues);"
                            + "}", ctClass));
        }

        // map2
        try {
            ctClass.getDeclaredMethod("map2", new CtClass[]{stringCls,
                    stringCls, objectListCls, objectListCls});
        }
        catch (NotFoundException e) {
            list.add(createMethod(
                    "public static java.util.Map map2(String afterFrom, String inColumn, java.util.List values, java.util.List inValues) {"
                            + "return getQuery().map2("
                            + classInMethod
                            + ", afterFrom, inColumn, values, inValues);"
                            + "}", ctClass));
        }
        return list;
    }
}
