package halo.query.mapping;

import halo.query.javassistutil.JavassistUtil;

import java.lang.reflect.Field;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

public class JavassitSQLMapperClassCreater {

	private final ClassLoader classLoader = Thread.currentThread()
	        .getContextClassLoader();

	private Class<?> mapperClass;

	public <T> JavassitSQLMapperClassCreater(EntityTableInfo<T> entityTableInfo)
	{
		super();
		String mapperClassName = this
		        .createMapperClassName(entityTableInfo.getClazz());
		try {
			ClassPool pool = JavassistUtil.getClassPool();
			CtClass sqlMapperClass = pool.get(SQLMapper.class.getName());
			CtClass cc;
			try {
				cc = pool.getCtClass(mapperClassName);
				this.mapperClass = classLoader.loadClass(mapperClassName);
			}
			catch (NotFoundException e) {
				cc = pool.makeClass(mapperClassName);
				cc.setInterfaces(new CtClass[] { sqlMapperClass });
				this.createGetIdParamMethod(entityTableInfo, cc);
				this.createGetParamsForInsertMethod(entityTableInfo, cc);
				this.createGetParamsForUpdateMethod(entityTableInfo, cc);
				this.mapperClass = cc.toClass(classLoader, classLoader
				        .getClass()
				        .getProtectionDomain()
				        );
			}
			catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
		catch (CannotCompileException e) {
			throw new RuntimeException(e);
		}
		catch (NotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public Class<?> getMapperClass() {
		return mapperClass;
	}

	private <T> void createGetIdParamMethod(EntityTableInfo<T> entityTableInfo,
	        CtClass cc) throws CannotCompileException {
		String className = entityTableInfo.getClazz().getName();
		StringBuilder sb = new StringBuilder(
		        "public Object getIdParam(Object t){");
		sb.append(className + " o =(" + className + ")t;");
		String paramListUtilClassName = ParamListUtil.class.getName();
		// return
		sb.append("return "
		        + paramListUtilClassName
		        + ".toObject(o."
		        + MethodNameUtil.createGetMethodString(entityTableInfo
		                .getIdField()
		                .getName())
		        + "());");
		sb.append("}");
		String src = sb.toString();
		CtMethod mapRowMethod = CtNewMethod.make(src, cc);
		cc.addMethod(mapRowMethod);
	}

	private <T> void createGetParamsForInsertMethod(
	        EntityTableInfo<T> entityTableInfo,
	        CtClass cc) throws CannotCompileException {
		String className = entityTableInfo.getClazz().getName();
		StringBuilder sb = new StringBuilder(
		        "public Object[] getParamsForInsert(Object t,boolean hasIdFieldValue){");
		sb.append(className + " o =(" + className + ")t;");
		// return
		String paramListUtilClassName = ParamListUtil.class.getName();
		sb.append("if(hasIdFieldValue)");
		// return code
		sb.append("return new Object[]{");
		for (Field field : entityTableInfo.getTableFields()) {
			sb.append(paramListUtilClassName + ".toObject(o."
			        + MethodNameUtil.createGetMethodString(field.getName())
			        + "()),");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("};");
		// return code end
		// return code
		sb.append("return new Object[]{");
		for (Field field : entityTableInfo.getTableFields()) {
			if (field.equals(entityTableInfo.getIdField())) {
				continue;
			}
			sb.append(paramListUtilClassName + ".toObject(o."
			        + MethodNameUtil.createGetMethodString(field.getName())
			        + "()),");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("};");
		// return code end
		sb.append("}");
		String src = sb.toString();
		CtMethod mapRowMethod = CtNewMethod.make(src, cc);
		cc.addMethod(mapRowMethod);
	}

	private <T> void createGetParamsForUpdateMethod(
	        EntityTableInfo<T> entityTableInfo,
	        CtClass cc) throws CannotCompileException {
		String className = entityTableInfo.getClazz().getName();
		StringBuilder sb = new StringBuilder(
		        "public Object[] getParamsForUpdate(Object t){");
		sb.append(className + " o =(" + className + ")t;");
		// return
		String paramListUtilClassName = ParamListUtil.class.getName();
		sb.append("return new Object[]{");
		for (Field field : entityTableInfo.getTableFields()) {
			if (!entityTableInfo.isIdField(field)) {
				sb.append(paramListUtilClassName + ".toObject(o."
				        + MethodNameUtil.createGetMethodString(field.getName())
				        + "()),");
			}
		}
		sb.append(paramListUtilClassName + ".toObject(o."
		        + MethodNameUtil.createGetMethodString(entityTableInfo
		                .getIdField().getName()) + "())");
		sb.append("};");
		sb.append("}");
		String src = sb.toString();
		CtMethod mapRowMethod = CtNewMethod.make(src, cc);
		cc.addMethod(mapRowMethod);
	}

	private String createMapperClassName(Class<?> clazz) {
		int idx = clazz.getName().lastIndexOf(".");
		String shortName = clazz.getName().substring(idx + 1);
		String pkgName = clazz.getName().substring(0, idx);
		return pkgName + "." + shortName + "HaloJavassist$SQLMapper";
	}
}
