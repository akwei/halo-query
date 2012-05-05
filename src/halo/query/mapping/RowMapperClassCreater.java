package halo.query.mapping;

import halo.query.FieldTypeUtil;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.springframework.jdbc.core.RowMapper;

/**
 * 自动创建数据对象对应的rowmapper对象 暂时只支持
 * long,int,byte,short,float,char,double,String,java.util.Date类型以及对应的对象类型，支持null
 * ，支持BigDecimal,支持BigInteger
 * 
 * @author akwei
 */
public class RowMapperClassCreater extends ClassLoader implements Opcodes {

    public RowMapperClassCreater() {
        super(Thread.currentThread().getContextClassLoader());
    }

    @SuppressWarnings("unchecked")
    public <T> Class<RowMapper<T>> createRowMapperClass(EntityTableInfo<T> info) {
        ClassWriter classWriter = new ClassWriter(0);
        String mapperName = createMapperClassName(info.getClazz());
        String signName = mapperName.replaceAll("\\.", "/");
        classWriter.visit(V1_5, ACC_PUBLIC, signName, null, "java/lang/Object",
                new String[] { Type.getInternalName(RowMapper.class) });
        // 构造方法
        MethodVisitor methodVisitor = classWriter.visitMethod(ACC_PUBLIC,
                "<init>", "()V", null, null);
        methodVisitor.visitMaxs(1, 1);
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object",
                "<init>", "()V");
        methodVisitor.visitInsn(RETURN);
        methodVisitor.visitEnd();
        methodVisitor = classWriter
                .visitMethod(
                        ACC_PUBLIC,
                        "mapRow",
                        "(Ljava/sql/ResultSet;I)"
                                + Type.getDescriptor(Object.class),
                        null,
                        new String[] { Type.getInternalName(SQLException.class) });
        methodVisitor.visitMaxs(3, 4);
        methodVisitor.visitTypeInsn(NEW, Type.getInternalName(info.getClazz()));
        methodVisitor.visitInsn(DUP);
        methodVisitor.visitMethodInsn(INVOKESPECIAL,
                Type.getInternalName(info.getClazz()), "<init>", "()V");
        methodVisitor.visitVarInsn(ASTORE, 3);
        for (Field field : info.getTableFields()) {
            createResultSetGetValue(methodVisitor, info, field);
        }
        methodVisitor.visitVarInsn(ALOAD, 3);
        methodVisitor.visitInsn(ARETURN);
        methodVisitor.visitEnd();
        byte[] code = classWriter.toByteArray();
        try {
            this.loadClass(RowMapper.class.getName());
            Class<RowMapper<T>> mapperClass = (Class<RowMapper<T>>) this
                    .defineClass(mapperName, code, 0, code.length);
            return mapperClass;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T> void createResultSetGetValue(MethodVisitor methodVisitor,
            EntityTableInfo<T> info, Field field) {
        methodVisitor.visitVarInsn(ALOAD, 3);
        methodVisitor.visitVarInsn(ALOAD, 1);
        methodVisitor.visitLdcInsn(info.getFullColumn(field.getName()));
        MethodInfo resultSetMethodInfo = this.createResultSetMethodInfo(field);
        methodVisitor.visitMethodInsn(INVOKESTATIC,
                Type.getInternalName(RowMapperUtil.class),
                resultSetMethodInfo.getMethodName(),
                resultSetMethodInfo.getMethodDescr());
        MethodInfo setterMethodInfo = this.createSetterMethodInfo(field);
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
                Type.getInternalName(info.getClazz()),
                setterMethodInfo.getMethodName(),
                setterMethodInfo.getMethodDescr());
    }

    private MethodInfo createSetterMethodInfo(Field field) {
        FieldTypeUtil.checkFieldType(field);
        String type = field.getType().getName();
        MethodInfo methodInfo = new MethodInfo();
        methodInfo.setMethodName(this.createSetMethodString(field.getName()));
        if (type.equals(FieldTypeUtil.TYPE_INT)) {
            methodInfo.setMethodDescr("(I)V");
        }
        else if (type.equals(FieldTypeUtil.TYPE_SHORT)) {
            methodInfo.setMethodDescr("(S)V");
        }
        else if (type.equals(FieldTypeUtil.TYPE_BYTE)) {
            methodInfo.setMethodDescr("(B)V");
        }
        else if (type.equals(FieldTypeUtil.TYPE_LONG)) {
            methodInfo.setMethodDescr("(J)V");
        }
        else if (type.equals(FieldTypeUtil.TYPE_FLOAT)) {
            methodInfo.setMethodDescr("(F)V");
        }
        else if (type.equals(FieldTypeUtil.TYPE_DOUBLE)) {
            methodInfo.setMethodDescr("(D)V");
        }
        else if (type.equals(FieldTypeUtil.TYPE_STRING)) {
            methodInfo.setMethodDescr("(Ljava/lang/String;)V");
        }
        else if (type.equals(FieldTypeUtil.TYPE_DATE)) {
            methodInfo.setMethodDescr("(" + Type.getDescriptor(Date.class)
                    + ")V");
        }
        else if (type.equals(FieldTypeUtil.TYPE_BIGINTEGER)) {
            methodInfo.setMethodDescr("(Ljava/math/BigInteger;)V");
        }
        else if (type.equals(FieldTypeUtil.TYPE_OBJINT)) {
            methodInfo.setMethodDescr("(Ljava/lang/Integer;)V");
        }
        else if (type.equals(FieldTypeUtil.TYPE_OBJLONG)) {
            methodInfo.setMethodDescr("(Ljava/lang/Long;)V");
        }
        else if (type.equals(FieldTypeUtil.TYPE_OBJSHORT)) {
            methodInfo.setMethodDescr("(Ljava/lang/Short;)V");
        }
        else if (type.equals(FieldTypeUtil.TYPE_OBJBYTE)) {
            methodInfo.setMethodDescr("(Ljava/lang/Byte;)V");
        }
        else if (type.equals(FieldTypeUtil.TYPE_OBJFLOAT)) {
            methodInfo.setMethodDescr("(Ljava/lang/Float;)V");
        }
        else if (type.equals(FieldTypeUtil.TYPE_OBJDOUBLE)) {
            methodInfo.setMethodDescr("(Ljava/lang/Double;)V");
        }
        else if (type.equals(FieldTypeUtil.TYPE_BIGDECIMAL)) {
            methodInfo.setMethodDescr("(Ljava/math/BigDecimal;)V");
        }
        return methodInfo;
    }

    private MethodInfo createResultSetMethodInfo(Field field) {
        FieldTypeUtil.checkFieldType(field);
        MethodInfo methodInfo = new MethodInfo();
        String type = field.getType().getName();
        if (type.equals(FieldTypeUtil.TYPE_INT)) {
            Type.getDescriptor(ResultSet.class);
            methodInfo.setMethodName("getInt");
            methodInfo
                    .setMethodDescr("(Ljava/sql/ResultSet;Ljava/lang/String;)I");
        }
        else if (type.equals(FieldTypeUtil.TYPE_SHORT)) {
            methodInfo.setMethodName("getShort");
            methodInfo
                    .setMethodDescr("(Ljava/sql/ResultSet;Ljava/lang/String;)S");
        }
        else if (type.equals(FieldTypeUtil.TYPE_BYTE)) {
            methodInfo.setMethodName("getByte");
            methodInfo
                    .setMethodDescr("(Ljava/sql/ResultSet;Ljava/lang/String;)B");
        }
        else if (type.equals(FieldTypeUtil.TYPE_LONG)) {
            methodInfo.setMethodName("getLong");
            methodInfo
                    .setMethodDescr("(Ljava/sql/ResultSet;Ljava/lang/String;)J");
        }
        else if (type.equals(FieldTypeUtil.TYPE_FLOAT)) {
            methodInfo.setMethodName("getFloat");
            methodInfo
                    .setMethodDescr("(Ljava/sql/ResultSet;Ljava/lang/String;)F");
        }
        else if (type.equals(FieldTypeUtil.TYPE_DOUBLE)) {
            methodInfo.setMethodName("getDouble");
            methodInfo
                    .setMethodDescr("(Ljava/sql/ResultSet;Ljava/lang/String;)D");
        }
        else if (type.equals(FieldTypeUtil.TYPE_STRING)) {
            methodInfo.setMethodName("getString");
            methodInfo
                    .setMethodDescr("(Ljava/sql/ResultSet;Ljava/lang/String;)Ljava/lang/String;");
        }
        else if (type.equals(FieldTypeUtil.TYPE_DATE)) {
            methodInfo.setMethodName("getTimestamp");
            methodInfo
                    .setMethodDescr("(Ljava/sql/ResultSet;Ljava/lang/String;)Ljava/sql/Timestamp;");
        }
        else if (type.equals(FieldTypeUtil.TYPE_BIGINTEGER)) {
            methodInfo.setMethodName("getBigInteger");
            methodInfo
                    .setMethodDescr("(Ljava/sql/ResultSet;Ljava/lang/String;)Ljava/math/BigInteger;");
        }
        else if (type.equals(FieldTypeUtil.TYPE_OBJINT)) {
            methodInfo.setMethodName("getObjInt");
            methodInfo
                    .setMethodDescr("(Ljava/sql/ResultSet;Ljava/lang/String;)Ljava/lang/Integer;");
        }
        else if (type.equals(FieldTypeUtil.TYPE_OBJLONG)) {
            methodInfo.setMethodName("getObjLong");
            methodInfo
                    .setMethodDescr("(Ljava/sql/ResultSet;Ljava/lang/String;)Ljava/lang/Long;");
        }
        else if (type.equals(FieldTypeUtil.TYPE_OBJSHORT)) {
            methodInfo.setMethodName("getObjShort");
            methodInfo
                    .setMethodDescr("(Ljava/sql/ResultSet;Ljava/lang/String;)Ljava/lang/Short;");
        }
        else if (type.equals(FieldTypeUtil.TYPE_OBJBYTE)) {
            methodInfo.setMethodName("getObjByte");
            methodInfo
                    .setMethodDescr("(Ljava/sql/ResultSet;Ljava/lang/String;)Ljava/lang/Byte;");
        }
        else if (type.equals(FieldTypeUtil.TYPE_OBJFLOAT)) {
            methodInfo.setMethodName("getObjFloat");
            methodInfo
                    .setMethodDescr("(Ljava/sql/ResultSet;Ljava/lang/String;)Ljava/lang/Float;");
        }
        else if (type.equals(FieldTypeUtil.TYPE_OBJDOUBLE)) {
            methodInfo.setMethodName("getObjDouble");
            methodInfo
                    .setMethodDescr("(Ljava/sql/ResultSet;Ljava/lang/String;)Ljava/lang/Double;");
        }
        else if (type.equals(FieldTypeUtil.TYPE_BIGDECIMAL)) {
            methodInfo.setMethodName("getBigDecimal");
            methodInfo
                    .setMethodDescr("(Ljava/sql/ResultSet;Ljava/lang/String;)Ljava/math/BigDecimal;");
        }
        return methodInfo;
    }

    private String createSetMethodString(String fieldName) {
        return "set" + fieldName.substring(0, 1).toUpperCase()
                + fieldName.substring(1);
    }

    private String createMapperClassName(Class<?> clazz) {
        int idx = clazz.getName().lastIndexOf(".");
        String shortName = clazz.getName().substring(idx + 1);
        String pkgName = clazz.getName().substring(0, idx);
        return pkgName + "." + shortName + "AsmMapper";
    }

    private static class MethodInfo {

        private String methodName;

        private String methodDescr;

        public void setMethodDescr(String methodDescr) {
            this.methodDescr = methodDescr;
        }

        public String getMethodDescr() {
            return methodDescr;
        }

        public void setMethodName(String methodName) {
            this.methodName = methodName;
        }

        public String getMethodName() {
            return methodName;
        }
    }
}
