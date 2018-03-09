package halo.query.mapping;

import halo.query.annotation.Column;
import halo.query.javassistutil.JavassistUtil;
import javassist.*;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Field;

/**
 * 使用Javassist动态创建 {@link RowMapper}字节码数据，并加载到当前Classloader中
 *
 * @author akwei
 */
public class JavassitRowMapperClassCreater<T> {

    private final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    /**
     * {@link RowMapper}类对象
     */
    private Class<T> mapperClass;

    public JavassitRowMapperClassCreater(EntityTableInfo<T> entityTableInfo) {
        super();
        String mapperClassName = this.createMapperClassName(entityTableInfo.getClazz());
        try {
            ClassPool pool = JavassistUtil.getClassPool();
            CtClass rowMapperClass = pool.get(RowMapper.class.getName());
            try {
                pool.getCtClass(mapperClassName);
                // 如果已经有同名类就赋值
                this.mapperClass = (Class<T>) classLoader.loadClass(mapperClassName);
            } catch (NotFoundException e) {
                // 没有找到，就创建新的class
                CtClass cc = pool.makeClass(mapperClassName);
                cc.setInterfaces(new CtClass[]{rowMapperClass});
                String src = this.createMethodSrc(entityTableInfo);
                CtMethod mapRowMethod;
                mapRowMethod = CtNewMethod.make(src, cc);
                cc.addMethod(mapRowMethod);
                this.mapperClass = cc.toClass(classLoader, classLoader.getClass().getProtectionDomain());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } catch (CannotCompileException e) {
            throw new RuntimeException(e);
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Class<T> getMapperClass() {
        return mapperClass;
    }

    /**
     * 生成{@link RowMapper}的类名称
     *
     * @param clazz
     * @return
     */
    private String createMapperClassName(Class<?> clazz) {
        int idx = clazz.getName().lastIndexOf(".");
        String shortName = clazz.getName().substring(idx + 1);
        String pkgName = clazz.getName().substring(0, idx);
        return pkgName + "." + shortName + "HaloJavassist$RowMapper";
    }

    /**
     * 按照 {@link RowMapper}的接口定义，生成子类所需要的方法信息
     *
     * @param entityTableInfo
     * @return
     */
    private String createMethodSrc(EntityTableInfo<T> entityTableInfo) {
        StringBuilder sb = new StringBuilder(
                "public Object mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException{");
        // obj init
        sb.append(entityTableInfo.getClazz().getName() + " obj = new "
                + entityTableInfo.getClazz().getName() + "();");
        // 进行 obj.setter(rs.getString....)等赋值操作
        for (Field field : entityTableInfo.getTableFields()) {
            sb.append(this.createGetterSrc(entityTableInfo, field));
        }
        // return
        sb.append("return obj;");
        sb.append("}");
        return sb.toString();
    }

    /**
     * 生成对象属性赋值的代码片段
     *
     * @param entityTableInfo
     * @param field
     * @return
     */
    private String createGetterSrc(EntityTableInfo<T> entityTableInfo,
                                   Field field) {
//        FieldTypeUtil.checkFieldType(field);
        String type = field.getType().getName();
        String columnName = entityTableInfo.getColumnFull(field.getName());
        String rowMapperUtilClassName = RowMapperUtil.class.getName();
        Column column = field.getAnnotation(Column.class);
        if (FieldTypeUtil.checkHaloQueryEnum(field)) {
            return "obj." + this.createSetMethodString(field.getName())
                    + "(" +
                    type + "." + column.findEnumMethodName() + "(" +
                    rowMapperUtilClassName + ".getInt(rs,\"" + columnName + "\")" +
                    ")" +
                    ");";
        }
        if (type.equals(FieldTypeUtil.TYPE_INT)) {
            return "obj." + this.createSetMethodString(field.getName())
                    + "(" + rowMapperUtilClassName + ".getInt(rs,\""
                    + columnName + "\"));";
        } else if (type.equals(FieldTypeUtil.TYPE_SHORT)) {
            return "obj." + this.createSetMethodString(field.getName())
                    + "(" + rowMapperUtilClassName + ".getShort(rs,\""
                    + columnName + "\"));";
        } else if (type.equals(FieldTypeUtil.TYPE_BYTE)) {
            return "obj." + this.createSetMethodString(field.getName())
                    + "(" + rowMapperUtilClassName + ".getByte(rs,\""
                    + columnName + "\"));";
        } else if (type.equals(FieldTypeUtil.TYPE_LONG)) {
            return "obj." + this.createSetMethodString(field.getName())
                    + "(" + rowMapperUtilClassName + ".getLong(rs,\""
                    + columnName + "\"));";
        } else if (type.equals(FieldTypeUtil.TYPE_FLOAT)) {
            return "obj." + this.createSetMethodString(field.getName())
                    + "(" + rowMapperUtilClassName + ".getFloat(rs,\""
                    + columnName + "\"));";
        } else if (type.equals(FieldTypeUtil.TYPE_DOUBLE)) {
            return "obj." + this.createSetMethodString(field.getName())
                    + "(" + rowMapperUtilClassName + ".getDouble(rs,\""
                    + columnName + "\"));";
        } else if (type.equals(FieldTypeUtil.TYPE_STRING)) {
            return "obj." + this.createSetMethodString(field.getName())
                    + "(" + rowMapperUtilClassName + ".getString(rs,\""
                    + columnName + "\"));";
        } else if (type.equals(FieldTypeUtil.TYPE_DATE)) {
            return "obj." + this.createSetMethodString(field.getName())
                    + "(" + rowMapperUtilClassName + ".getTimestamp(rs,\""
                    + columnName + "\"));";
        } else if (type.equals(FieldTypeUtil.TYPE_SQL_DATE)) {
            return "obj." + this.createSetMethodString(field.getName())
                    + "(" + rowMapperUtilClassName + ".getDate(rs,\""
                    + columnName + "\"));";
        } else if (type.equals(FieldTypeUtil.TYPE_TIMESTAMP)) {
            return "obj." + this.createSetMethodString(field.getName())
                    + "(" + rowMapperUtilClassName + ".getTimestamp(rs,\""
                    + columnName + "\"));";
        } else if (type.equals(FieldTypeUtil.TYPE_BIGINTEGER)) {
            return "obj." + this.createSetMethodString(field.getName())
                    + "(" + rowMapperUtilClassName + ".getBigInteger(rs,\""
                    + columnName + "\"));";
        } else if (type.equals(FieldTypeUtil.TYPE_OBJINT)) {
            return "obj." + this.createSetMethodString(field.getName())
                    + "(" + rowMapperUtilClassName + ".getObjInt(rs,\""
                    + columnName + "\"));";
        } else if (type.equals(FieldTypeUtil.TYPE_OBJLONG)) {
            return "obj." + this.createSetMethodString(field.getName())
                    + "(" + rowMapperUtilClassName + ".getObjLong(rs,\""
                    + columnName + "\"));";
        } else if (type.equals(FieldTypeUtil.TYPE_OBJSHORT)) {
            return "obj." + this.createSetMethodString(field.getName())
                    + "(" + rowMapperUtilClassName + ".getObjShort(rs,\""
                    + columnName + "\"));";
        } else if (type.equals(FieldTypeUtil.TYPE_OBJBYTE)) {
            return "obj." + this.createSetMethodString(field.getName())
                    + "(" + rowMapperUtilClassName + ".getObjByte(rs,\""
                    + columnName + "\"));";
        } else if (type.equals(FieldTypeUtil.TYPE_OBJFLOAT)) {
            return "obj." + this.createSetMethodString(field.getName())
                    + "(" + rowMapperUtilClassName + ".getObjFloat(rs,\""
                    + columnName + "\"));";
        } else if (type.equals(FieldTypeUtil.TYPE_OBJDOUBLE)) {
            return "obj." + this.createSetMethodString(field.getName())
                    + "(" + rowMapperUtilClassName + ".getObjDouble(rs,\""
                    + columnName + "\"));";
        } else if (type.equals(FieldTypeUtil.TYPE_BIGDECIMAL)) {
            return "obj." + this.createSetMethodString(field.getName())
                    + "(" + rowMapperUtilClassName + ".getBigDecimal(rs,\""
                    + columnName + "\"));";
        } else if (type.equals(FieldTypeUtil.TYPE_BOOL)) {
            return "obj." + this.createSetMethodString(field.getName())
                    + "(" + rowMapperUtilClassName + ".getBoolean(rs,\""
                    + columnName + "\"));";
        }
        throw new RuntimeException("not supported field type class:"
                + entityTableInfo.getClazz().getName() + "."
                + field.getName());
    }

    private String createSetMethodString(String fieldName) {
        return MethodNameUtil.createSetMethodString(fieldName);
    }
}