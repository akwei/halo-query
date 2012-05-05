package halo.query.mapping;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class FieldTypeUtil {

    public static final String TYPE_LONG = "long";

    public static final String TYPE_OBJLONG = Long.class.getName();

    public static final String TYPE_INT = "int";

    public static final String TYPE_OBJINT = Integer.class.getName();

    public static final String TYPE_BYTE = "byte";

    public static final String TYPE_OBJBYTE = Byte.class.getName();

    public static final String TYPE_SHORT = "short";

    public static final String TYPE_OBJSHORT = Short.class.getName();

    public static final String TYPE_FLOAT = "float";

    public static final String TYPE_OBJFLOAT = Float.class.getName();

    public static final String TYPE_DOUBLE = "double";

    public static final String TYPE_OBJDOUBLE = Double.class.getName();

    public static final String TYPE_BIGINTEGER = BigInteger.class.getName();

    public static final String TYPE_STRING = String.class.getName();

    public static final String TYPE_DATE = Date.class.getName();

    public static final String TYPE_BIGDECIMAL = BigDecimal.class.getName();

    public static Set<String> fieldTypeSet = new HashSet<String>();

    public static Set<String> idFieldTypeSet = new HashSet<String>();
    static {
        fieldTypeSet.add("int");
        fieldTypeSet.add("long");
        fieldTypeSet.add("byte");
        fieldTypeSet.add("float");
        fieldTypeSet.add("short");
        fieldTypeSet.add("double");
        fieldTypeSet.add("java.lang.String");
        fieldTypeSet.add("java.util.Date");
        fieldTypeSet.add(Integer.class.getName());
        fieldTypeSet.add(Long.class.getName());
        fieldTypeSet.add(Short.class.getName());
        fieldTypeSet.add(Double.class.getName());
        fieldTypeSet.add(Float.class.getName());
        fieldTypeSet.add(Byte.class.getName());
        fieldTypeSet.add(BigInteger.class.getName());
        fieldTypeSet.add(BigDecimal.class.getName());
        // id
        idFieldTypeSet.add("int");
        idFieldTypeSet.add("long");
        idFieldTypeSet.add(Integer.class.getName());
        idFieldTypeSet.add(Long.class.getName());
        idFieldTypeSet.add(BigInteger.class.getName());
        idFieldTypeSet.add("java.lang.String");
    }

    public static void checkFieldType(Field field) {
        if (!fieldTypeSet.contains(field.getType().getName())) {
            throw new RuntimeException("field type not support [ "
                    + field.getType().getName() + " ]"
                    + " --- only support field type [int , " + "long , "
                    + "short , " + "byte , " + "float , " + "double , "
                    + "java.lang.String , " + "java.util.Date , "
                    + Integer.class.getName() + " , " + Long.class.getName()
                    + " , " + Short.class.getName() + " , "
                    + Double.class.getName() + " , " + Float.class.getName()
                    + " , " + Byte.class.getName() + " , "
                    + BigInteger.class.getName() + " , "
                    + BigDecimal.class.getName() + "]");
        }
    }

    public static void checkIdFieldType(Field field) {
        if (!idFieldTypeSet.contains(field.getType().getName())) {
            throw new RuntimeException("id field type not support [ "
                    + field.getType().getName() + " ]"
                    + " --- only support field type [int , " + "long , "
                    + "java.lang.String" + Integer.class.getName() + " , "
                    + Long.class.getName() + " , " + BigInteger.class.getName()
                    + "]");
        }
    }
}