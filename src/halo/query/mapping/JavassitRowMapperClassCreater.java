package halo.query.mapping;

import halo.query.javassistutil.JavassistUtil;

import java.lang.reflect.Field;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

import org.springframework.jdbc.core.RowMapper;

public class JavassitRowMapperClassCreater {

	private final ClassLoader classLoader = Thread.currentThread()
			.getContextClassLoader();

	private Class<?> mapperClass;

	public <T> JavassitRowMapperClassCreater(EntityTableInfo<T> entityTableInfo)
	{
		super();
		String mapperClassName = this
				.createMapperClassName(entityTableInfo.getClazz());
		try {
			ClassPool pool = JavassistUtil.getClassPool();
			CtClass rowMapperClass = pool.get(RowMapper.class.getName());
			CtClass cc;
			try {
				cc = pool.getCtClass(mapperClassName);
				this.mapperClass = classLoader.loadClass(mapperClassName);
			}
			catch (NotFoundException e) {
				cc = pool.makeClass(mapperClassName);
				cc.setInterfaces(new CtClass[] { rowMapperClass });
				String src = this.createMethodSrc(entityTableInfo);
				CtMethod mapRowMethod;
				mapRowMethod = CtNewMethod.make(src, cc);
				cc.addMethod(mapRowMethod);
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

	private String createMapperClassName(Class<?> clazz) {
		int idx = clazz.getName().lastIndexOf(".");
		String shortName = clazz.getName().substring(idx + 1);
		String pkgName = clazz.getName().substring(0, idx);
		return pkgName + "." + shortName + "HaloJavassist$RowMapper";
	}

	private <T> String createMethodSrc(EntityTableInfo<T> entityTableInfo) {
		StringBuilder sb = new StringBuilder(
				"public Object mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException{");
		// obj init
		sb.append(entityTableInfo.getClazz().getName() + " obj = new "
				+ entityTableInfo.getClazz().getName() + "();");
		// set rs
		for (Field field : entityTableInfo.getTableFields()) {
			sb.append(this.createGetterSrc(entityTableInfo, field));
		}
		// return
		sb.append("return obj;");
		sb.append("}");
		return sb.toString();
	}

	private <T> String createGetterSrc(EntityTableInfo<T> entityTableInfo,
			Field field) {
		FieldTypeUtil.checkFieldType(field);
		String type = field.getType().getName();
		String columnName = entityTableInfo.getFullColumn(field.getName());
		String rowMapperUtilClassName = RowMapperUtil.class.getName();
		if (type.equals(FieldTypeUtil.TYPE_INT)) {
			return "obj." + this.createSetMethodString(field.getName())
					+ "(" + rowMapperUtilClassName + ".getInt(rs,\""
					+ columnName + "\"));";
		}
		else if (type.equals(FieldTypeUtil.TYPE_SHORT)) {
			return "obj." + this.createSetMethodString(field.getName())
					+ "(" + rowMapperUtilClassName + ".getShort(rs,\""
					+ columnName + "\"));";
		}
		else if (type.equals(FieldTypeUtil.TYPE_BYTE)) {
			return "obj." + this.createSetMethodString(field.getName())
					+ "(" + rowMapperUtilClassName + ".getByte(rs,\""
					+ columnName + "\"));";
		}
		else if (type.equals(FieldTypeUtil.TYPE_LONG)) {
			return "obj." + this.createSetMethodString(field.getName())
					+ "(" + rowMapperUtilClassName + ".getLong(rs,\""
					+ columnName + "\"));";
		}
		else if (type.equals(FieldTypeUtil.TYPE_FLOAT)) {
			return "obj." + this.createSetMethodString(field.getName())
					+ "(" + rowMapperUtilClassName + ".getFloat(rs,\""
					+ columnName + "\"));";
		}
		else if (type.equals(FieldTypeUtil.TYPE_DOUBLE)) {
			return "obj." + this.createSetMethodString(field.getName())
					+ "(" + rowMapperUtilClassName + ".getDouble(rs,\""
					+ columnName + "\"));";
		}
		else if (type.equals(FieldTypeUtil.TYPE_STRING)) {
			return "obj." + this.createSetMethodString(field.getName())
					+ "(" + rowMapperUtilClassName + ".getString(rs,\""
					+ columnName + "\"));";
		}
		else if (type.equals(FieldTypeUtil.TYPE_DATE)) {
			return "obj." + this.createSetMethodString(field.getName())
					+ "(" + rowMapperUtilClassName + ".getTimestamp(rs,\""
					+ columnName + "\"));";
		}
		else if (type.equals(FieldTypeUtil.TYPE_BIGINTEGER)) {
			return "obj." + this.createSetMethodString(field.getName())
					+ "(" + rowMapperUtilClassName + ".getBigInteger(rs,\""
					+ columnName + "\"));";
		}
		else if (type.equals(FieldTypeUtil.TYPE_OBJINT)) {
			return "obj." + this.createSetMethodString(field.getName())
					+ "(" + rowMapperUtilClassName + ".getObjInt(rs,\""
					+ columnName + "\"));";
		}
		else if (type.equals(FieldTypeUtil.TYPE_OBJLONG)) {
			return "obj." + this.createSetMethodString(field.getName())
					+ "(" + rowMapperUtilClassName + ".getObjLong(rs,\""
					+ columnName + "\"));";
		}
		else if (type.equals(FieldTypeUtil.TYPE_OBJSHORT)) {
			return "obj." + this.createSetMethodString(field.getName())
					+ "(" + rowMapperUtilClassName + ".getObjShort(rs,\""
					+ columnName + "\"));";
		}
		else if (type.equals(FieldTypeUtil.TYPE_OBJBYTE)) {
			return "obj." + this.createSetMethodString(field.getName())
					+ "(" + rowMapperUtilClassName + ".getObjByte(rs,\""
					+ columnName + "\"));";
		}
		else if (type.equals(FieldTypeUtil.TYPE_OBJFLOAT)) {
			return "obj." + this.createSetMethodString(field.getName())
					+ "(" + rowMapperUtilClassName + ".getObjFloat(rs,\""
					+ columnName + "\"));";
		}
		else if (type.equals(FieldTypeUtil.TYPE_OBJDOUBLE)) {
			return "obj." + this.createSetMethodString(field.getName())
					+ "(" + rowMapperUtilClassName + ".getObjDouble(rs,\""
					+ columnName + "\"));";
		}
		else if (type.equals(FieldTypeUtil.TYPE_BIGDECIMAL)) {
			return "obj." + this.createSetMethodString(field.getName())
					+ "(" + rowMapperUtilClassName + ".getBigDecimal(rs,\""
					+ columnName + "\"));";
		}
		throw new RuntimeException("not supported field type class:"
				+ entityTableInfo.getClazz().getName() + "."
				+ field.getName());
	}

	private String createSetMethodString(String fieldName) {
		return "set" + fieldName.substring(0, 1).toUpperCase()
				+ fieldName.substring(1);
	}
}