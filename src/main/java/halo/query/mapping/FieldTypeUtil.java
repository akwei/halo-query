package halo.query.mapping;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 用来判断是否是本框架支持的数据类型
 *
 * @author akwei
 */
public class FieldTypeUtil {

    public static final String TYPE_LONG = long.class.getName();

    public static final String TYPE_OBJLONG = Long.class.getName();

    public static final String TYPE_INT = int.class.getName();

    public static final String TYPE_OBJINT = Integer.class.getName();

    public static final String TYPE_BYTE = byte.class.getName();

    public static final String TYPE_OBJBYTE = Byte.class.getName();

    public static final String TYPE_SHORT = short.class.getName();

    public static final String TYPE_OBJSHORT = Short.class.getName();

    public static final String TYPE_FLOAT = float.class.getName();

    public static final String TYPE_OBJFLOAT = Float.class.getName();

    public static final String TYPE_DOUBLE = double.class.getName();

    public static final String TYPE_OBJDOUBLE = Double.class.getName();

    public static final String TYPE_BIGINTEGER = BigInteger.class.getName();

    public static final String TYPE_STRING = String.class.getName();

    public static final String TYPE_DATE = Date.class.getName();

    public static final String TYPE_TIMESTAMP = Timestamp.class.getName();

    public static final String TYPE_SQL_DATE = java.sql.Date.class.getName();

    public static final String TYPE_BIGDECIMAL = BigDecimal.class.getName();

    public static Set<String> fieldTypeSet = new HashSet<String>();

    public static Set<String> idFieldTypeSet = new HashSet<String>();

    static {
        fieldTypeSet.add(TYPE_INT);
        fieldTypeSet.add(TYPE_OBJINT);
        fieldTypeSet.add(TYPE_BYTE);
        fieldTypeSet.add(TYPE_OBJBYTE);
        fieldTypeSet.add(TYPE_SHORT);
        fieldTypeSet.add(TYPE_OBJSHORT);
        fieldTypeSet.add(TYPE_DOUBLE);
        fieldTypeSet.add(TYPE_OBJDOUBLE);
        fieldTypeSet.add(TYPE_FLOAT);
        fieldTypeSet.add(TYPE_OBJFLOAT);
        fieldTypeSet.add(TYPE_STRING);
        fieldTypeSet.add(TYPE_LONG);
        fieldTypeSet.add(TYPE_OBJLONG);
        fieldTypeSet.add(TYPE_BIGINTEGER);
        fieldTypeSet.add(TYPE_BIGDECIMAL);
        fieldTypeSet.add(TYPE_DATE);
        fieldTypeSet.add(TYPE_SQL_DATE);
        fieldTypeSet.add(TYPE_TIMESTAMP);
        // id
        idFieldTypeSet.add(TYPE_INT);
        idFieldTypeSet.add(TYPE_OBJINT);
        idFieldTypeSet.add(TYPE_LONG);
        idFieldTypeSet.add(TYPE_OBJLONG);
        idFieldTypeSet.add(TYPE_BIGINTEGER);
        idFieldTypeSet.add(TYPE_STRING);
    }

    /**
     * 判断field类型是否是被支持的类型
     *
     * @param field
     */
    public static void checkFieldType(Class clazz, Field field) {
        if (field.getType().isEnum()) {
            if (checkHaloQueryEnum(field)) {
                return;
            }
            throw new RuntimeException(clazz.getName() + "." + field.getName() + " type enum must implements " + HaloQueryEnum.class.getName());
        }
        if (!fieldTypeSet.contains(field.getType().getName())) {
            StringBuilder sb = new StringBuilder();
            for (String s : fieldTypeSet) {
                sb.append(s).append(" , ");
            }
            throw new RuntimeException(clazz.getName() + "." + field.getName() + " type not support [ " + field.getType().getName() + " ]" + " --- only support field type [ " + sb.toString() + " ]");
        }
    }

//    /**
//     * 判断field类型是否是被支持的类型
//     *
//     * @param field
//     */
//    public static void checkFieldType(Field field) {
//        if (!checkHaloQueryEnum(field)) {
//            throw new RuntimeException(field.getName() + " type enum must implements " + HaloQueryEnum.class.getName());
//        }
//        if (!fieldTypeSet.contains(field.getType().getName())) {
//            StringBuilder sb = new StringBuilder();
//            for (String s : fieldTypeSet) {
//                sb.append(s).append(" , ");
//            }
//            throw new RuntimeException(field.getName() + " type not support [ " + field.getType().getName() + " ]" + " --- only support field type [ " + sb.toString() + " ]");
//        }
//    }

    /**
     * 判断作为id的field是否是被支持的类型，目前id只支持int long Integer Long String BigInteger
     *
     * @param field
     */
    public static void checkIdFieldType(Field field) {
        if (checkHaloQueryEnum(field)) {
            return;
        }
        if (!idFieldTypeSet.contains(field.getType().getName())) {
            StringBuilder sb = new StringBuilder();
            for (String s : idFieldTypeSet) {
                sb.append(s).append(" , ");
            }
            throw new RuntimeException("id field type not support [ "
                    + field.getType().getName() + " ]"
                    + " --- only support field type [ " + sb.toString()
                    + "]");
        }
    }

    public static boolean checkHaloQueryEnum(Field field) {
        if (!field.getType().isEnum()) {
            return false;
        }
        Class<?>[] classes = field.getType().getInterfaces();
        if (classes == null) {
            return false;
        }
        for (Class clazz : classes) {
            if (clazz.equals(HaloQueryEnum.class)) {
                return true;
            }
        }
        return false;
    }
}