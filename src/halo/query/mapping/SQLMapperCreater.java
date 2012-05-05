package halo.query.mapping;


import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.springframework.jdbc.core.RowMapper;

/**
 * 创建对象所对应的sql，本类使用了asm字节码处理，生成class
 * insert,update,delete所需要的信息,目前对象只支持long,int,byte,short,float,char,double
 * ,String,java.util.Date类型的值,id类型只支持long,int,String类型
 * 
 * @author akwei
 */
public class SQLMapperCreater extends ClassLoader implements Opcodes {

    public SQLMapperCreater() {
        super(Thread.currentThread().getContextClassLoader());
    }

    private String createMapperClassName(Class<?> clazz) {
        int idx = clazz.getName().lastIndexOf(".");
        String shortName = clazz.getName().substring(idx + 1);
        String pkgName = clazz.getName().substring(0, idx);
        return pkgName + "." + shortName + "AsmSqlUpdateMapper";
    }

    @SuppressWarnings("unchecked")
    public <T> Class<SQLMapper<T>> createSqlUpdateMapperClass(
            EntityTableInfo<T> info) {
        ClassWriter classWriter = new ClassWriter(0);
        String mapperName = createMapperClassName(info.getClazz());
        String mapperClassName = mapperName.replaceAll("\\.", "/");
        String signature = Type.getDescriptor(Object.class)
                + Type.getInternalName(SQLMapper.class) + "<"
                + Type.getDescriptor(info.getClazz()) + ">;";
        classWriter.visit(V1_5, ACC_PUBLIC, mapperClassName, signature,
                "java/lang/Object",
                new String[] { Type.getInternalName(SQLMapper.class) });
        // 构造方法
        MethodVisitor methodVisitor = classWriter.visitMethod(ACC_PUBLIC,
                "<init>", "()V", null, null);
        methodVisitor.visitMaxs(1, 1);
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object",
                "<init>", "()V");
        methodVisitor.visitInsn(RETURN);
        methodVisitor.visitEnd();
        visitGetIdParam(classWriter, methodVisitor, info);
        visitGetParamsForInsert(classWriter, methodVisitor, info);
        visitGetParamsForUpdate(classWriter, methodVisitor, info);
        visitBridgeGetIdParam(classWriter, methodVisitor, mapperClassName, info);
        visitBridgeGetParamsForInsert(classWriter, methodVisitor,
                mapperClassName, info);
        visitBridgeGetParamsForUpdate(classWriter, methodVisitor,
                mapperClassName, info);
        byte[] code = classWriter.toByteArray();
        try {
            this.loadClass(RowMapper.class.getName());
            Class<SQLMapper<T>> mapperClass = (Class<SQLMapper<T>>) this
                    .defineClass(mapperName, code, 0, code.length);
            return mapperClass;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T> void visitGetIdParam(ClassWriter classWriter,
            MethodVisitor methodVisitor, EntityTableInfo<T> info) {
        if (info.getIdField() == null) {
            throw new RuntimeException("no id field in "
                    + info.getClazz().getName());
        }
        MethodVisitor _methodVisitor = methodVisitor;
        _methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "getIdParam", "("
                + Type.getDescriptor(info.getClazz()) + ")Ljava/lang/Object;",
                null, null);
        _methodVisitor.visitMaxs(2, 2);
        _methodVisitor.visitVarInsn(ALOAD, 1);
        visitGetIdParamInvokeForField(_methodVisitor, info);
        _methodVisitor.visitInsn(ARETURN);
        _methodVisitor.visitEnd();
    }

    private String getGetMethodName(Field field) {
        String fieldName = field.getName();
        return "get" + fieldName.substring(0, 1).toUpperCase()
                + fieldName.substring(1);
    }

    /**
     * id目前只支持int long String
     * 
     * @param <T>
     * @param methodVisitor
     * @param field
     * @param info
     */
    private <T> void visitGetIdParamInvokeForField(MethodVisitor methodVisitor,
            EntityTableInfo<T> info) {
        String type = getFieldReturnType(info.getIdField());
        String methodName = getGetMethodName(info.getIdField());
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
                Type.getInternalName(info.getClazz()), methodName,
                "()" + Type.getDescriptor(info.getIdField().getType()));
        FieldTypeUtil.checkIdFieldType(info.getIdField());
        if (type.equals(FieldTypeUtil.TYPE_INT)) {
            methodVisitor.visitMethodInsn(INVOKESTATIC,
                    Type.getInternalName(ParamListUtil.class), "toObject",
                    "(I)Ljava/lang/Object;");
        }
        else if (type.equals(FieldTypeUtil.TYPE_OBJINT)) {
            methodVisitor.visitMethodInsn(INVOKESTATIC,
                    Type.getInternalName(ParamListUtil.class), "toObject",
                    "(Ljava/lang/Integer;)Ljava/lang/Object;");
        }
        else if (type.equals(FieldTypeUtil.TYPE_LONG)) {
            methodVisitor.visitMethodInsn(INVOKESTATIC,
                    Type.getInternalName(ParamListUtil.class), "toObject",
                    "(J)Ljava/lang/Object;");
        }
        else if (type.equals(FieldTypeUtil.TYPE_OBJLONG)) {
            methodVisitor.visitMethodInsn(INVOKESTATIC,
                    Type.getInternalName(ParamListUtil.class), "toObject",
                    "(Ljava/lang/Long;)Ljava/lang/Object;");
        }
        else if (type.equals(FieldTypeUtil.TYPE_BIGDECIMAL)) {
            methodVisitor.visitMethodInsn(INVOKESTATIC,
                    Type.getInternalName(ParamListUtil.class), "toObject",
                    "(Ljava/math/BigDecimal;)Ljava/lang/Object;");
        }
        else if (type.equals(FieldTypeUtil.TYPE_BIGINTEGER)) {
            methodVisitor.visitMethodInsn(INVOKESTATIC,
                    Type.getInternalName(ParamListUtil.class), "toObject",
                    "(Ljava/math/BigInteger;)Ljava/lang/Object;");
        }
        else if (type.equals(FieldTypeUtil.TYPE_STRING)) {
            methodVisitor.visitMethodInsn(INVOKESTATIC,
                    Type.getInternalName(ParamListUtil.class), "toObject",
                    "(Ljava/lang/String;)Ljava/lang/Object;");
        }
    }

    private <T> void visitGetParamsForInsert(ClassWriter classWriter,
            MethodVisitor methodVisitor, EntityTableInfo<T> info) {
        MethodVisitor _methodVisitor = methodVisitor;
        _methodVisitor = classWriter.visitMethod(ACC_PUBLIC,
                "getParamsForInsert", "(" + Type.getDescriptor(info.getClazz())
                        + ")" + Type.getDescriptor(Object[].class), null, null);
        _methodVisitor.visitMaxs(3, 3);
        _methodVisitor.visitTypeInsn(NEW,
                Type.getInternalName(ParamListUtil.class));
        _methodVisitor.visitInsn(DUP);
        _methodVisitor.visitMethodInsn(INVOKESPECIAL,
                Type.getInternalName(ParamListUtil.class), "<init>", "()V");
        _methodVisitor.visitVarInsn(ASTORE, 2);
        _methodVisitor.visitVarInsn(ALOAD, 2);
        for (Field f : info.getTableFields()) {
            visitGetParamsForInsertAndUpdate(_methodVisitor, f, info.getClazz());
        }
        _methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
                Type.getInternalName(ParamListUtil.class), "toObjects",
                "()[Ljava/lang/Object;");
        _methodVisitor.visitInsn(ARETURN);
        _methodVisitor.visitEnd();
    }

    private <T> void visitGetParamsForUpdate(ClassWriter classWriter,
            MethodVisitor methodVisitor, EntityTableInfo<T> info) {
        MethodVisitor _methodVisitor = methodVisitor;
        _methodVisitor = classWriter.visitMethod(ACC_PUBLIC,
                "getParamsForUpdate", "(" + Type.getDescriptor(info.getClazz())
                        + ")" + Type.getDescriptor(Object[].class), null, null);
        _methodVisitor.visitMaxs(3, 3);
        _methodVisitor.visitTypeInsn(NEW,
                Type.getInternalName(ParamListUtil.class));
        _methodVisitor.visitInsn(DUP);
        _methodVisitor.visitMethodInsn(INVOKESPECIAL,
                Type.getInternalName(ParamListUtil.class), "<init>", "()V");
        _methodVisitor.visitVarInsn(ASTORE, 2);
        _methodVisitor.visitVarInsn(ALOAD, 2);
        List<Field> list = new ArrayList<Field>();
        for (Field f : info.getTableFields()) {
            if (!info.isIdField(f)) {
                list.add(f);
            }
        }
        list.add(info.getIdField());
        for (Field f : list) {
            visitGetParamsForInsertAndUpdate(_methodVisitor, f, info.getClazz());
        }
        _methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
                Type.getInternalName(ParamListUtil.class), "toObjects",
                "()[Ljava/lang/Object;");
        _methodVisitor.visitInsn(ARETURN);
        _methodVisitor.visitEnd();
    }

    private <T> void visitGetParamsForInsertAndUpdate(
            MethodVisitor methodVisitor, Field field, Class<T> clazz) {
        MethodVisitor _methodVisitor = methodVisitor;
        _methodVisitor.visitVarInsn(ALOAD, 1);
        String type = getFieldReturnType(field);
        _methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
                Type.getInternalName(clazz), getGetMethodName(field), "()"
                        + Type.getDescriptor(field.getType()));
        FieldTypeUtil.checkFieldType(field);
        if (type.equals(FieldTypeUtil.TYPE_INT)) {
            _methodVisitor
                    .visitMethodInsn(INVOKEVIRTUAL,
                            Type.getInternalName(ParamListUtil.class),
                            "addInt", "(I)V");
        }
        else if (type.equals(FieldTypeUtil.TYPE_LONG)) {
            _methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
                    Type.getInternalName(ParamListUtil.class), "addLong",
                    "(J)V");
        }
        else if (type.equals(FieldTypeUtil.TYPE_SHORT)) {
            _methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
                    Type.getInternalName(ParamListUtil.class), "addShort",
                    "(S)V");
        }
        else if (type.equals(FieldTypeUtil.TYPE_BYTE)) {
            _methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
                    Type.getInternalName(ParamListUtil.class), "addByte",
                    "(B)V");
        }
        else if (type.equals(FieldTypeUtil.TYPE_FLOAT)) {
            _methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
                    Type.getInternalName(ParamListUtil.class), "addFloat",
                    "(F)V");
        }
        else if (type.equals(FieldTypeUtil.TYPE_DOUBLE)) {
            _methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
                    Type.getInternalName(ParamListUtil.class), "addDouble",
                    "(D)V");
        }
        else if (type.equals(FieldTypeUtil.TYPE_STRING)) {
            _methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
                    Type.getInternalName(ParamListUtil.class), "addString",
                    "(Ljava/lang/String;)V");
        }
        else if (type.equals(FieldTypeUtil.TYPE_DATE)) {
            _methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
                    Type.getInternalName(ParamListUtil.class), "addDate",
                    "(Ljava/util/Date;)V");
        }
        else if (type.equals(FieldTypeUtil.TYPE_OBJINT)) {
            _methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
                    Type.getInternalName(ParamListUtil.class), "addObjInt", "("
                            + Type.getDescriptor(Integer.class) + ")V");
        }
        else if (type.equals(FieldTypeUtil.TYPE_OBJLONG)) {
            _methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
                    Type.getInternalName(ParamListUtil.class), "addObjLong",
                    "(" + Type.getDescriptor(Long.class) + ")V");
        }
        else if (type.equals(FieldTypeUtil.TYPE_OBJBYTE)) {
            _methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
                    Type.getInternalName(ParamListUtil.class), "addObjByte",
                    "(" + Type.getDescriptor(Byte.class) + ")V");
        }
        else if (type.equals(FieldTypeUtil.TYPE_OBJDOUBLE)) {
            _methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
                    Type.getInternalName(ParamListUtil.class), "addObjDouble",
                    "(" + Type.getDescriptor(Double.class) + ")V");
        }
        else if (type.equals(FieldTypeUtil.TYPE_OBJFLOAT)) {
            _methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
                    Type.getInternalName(ParamListUtil.class), "addObjFloat",
                    "(" + Type.getDescriptor(Float.class) + ")V");
        }
        else if (type.equals(FieldTypeUtil.TYPE_OBJSHORT)) {
            _methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
                    Type.getInternalName(ParamListUtil.class), "addObjShort",
                    "(" + Type.getDescriptor(Short.class) + ")V");
        }
        else if (type.equals(FieldTypeUtil.TYPE_BIGINTEGER)) {
            _methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
                    Type.getInternalName(ParamListUtil.class), "addBigInteger",
                    "(" + Type.getDescriptor(BigInteger.class) + ")V");
        }
        else if (type.equals(FieldTypeUtil.TYPE_BIGDECIMAL)) {
            _methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
                    Type.getInternalName(ParamListUtil.class), "addBigDecimal",
                    "(" + Type.getDescriptor(BigDecimal.class) + ")V");
        }
        _methodVisitor.visitVarInsn(ALOAD, 2);
    }

    private <T> void visitBridgeGetIdParam(ClassWriter classWriter,
            MethodVisitor methodVisitor, String mapperClassName,
            EntityTableInfo<T> info) {
        MethodVisitor _methodVisitor = methodVisitor;
        _methodVisitor = classWriter.visitMethod(ACC_PUBLIC + ACC_BRIDGE
                + ACC_SYNTHETIC, "getIdParam",
                "(Ljava/lang/Object;)Ljava/lang/Object;", null, null);
        _methodVisitor.visitMaxs(2, 2);
        _methodVisitor.visitVarInsn(ALOAD, 0);
        _methodVisitor.visitVarInsn(ALOAD, 1);
        _methodVisitor.visitTypeInsn(CHECKCAST,
                Type.getInternalName(info.getClazz()));
        _methodVisitor.visitMethodInsn(INVOKEVIRTUAL, mapperClassName,
                "getIdParam", "(" + Type.getDescriptor(info.getClazz())
                        + ")Ljava/lang/Object;");
        _methodVisitor.visitInsn(ARETURN);
    }

    private <T> void visitBridgeGetParamsForUpdate(ClassWriter classWriter,
            MethodVisitor methodVisitor, String mapperClassName,
            EntityTableInfo<T> objectSqlInfo) {
        MethodVisitor _methodVisitor = methodVisitor;
        _methodVisitor = classWriter.visitMethod(ACC_PUBLIC + ACC_BRIDGE
                + ACC_SYNTHETIC, "getParamsForUpdate",
                "(Ljava/lang/Object;)[Ljava/lang/Object;", null, null);
        _methodVisitor.visitMaxs(2, 2);
        _methodVisitor.visitVarInsn(ALOAD, 0);
        _methodVisitor.visitVarInsn(ALOAD, 1);
        _methodVisitor.visitTypeInsn(CHECKCAST,
                Type.getInternalName(objectSqlInfo.getClazz()));
        _methodVisitor.visitMethodInsn(INVOKEVIRTUAL, mapperClassName,
                "getParamsForUpdate",
                "(" + Type.getDescriptor(objectSqlInfo.getClazz())
                        + ")[Ljava/lang/Object;");
        _methodVisitor.visitInsn(ARETURN);
    }

    private <T> void visitBridgeGetParamsForInsert(ClassWriter classWriter,
            MethodVisitor methodVisitor, String mapperClassName,
            EntityTableInfo<T> info) {
        MethodVisitor _methodVisitor = methodVisitor;
        _methodVisitor = classWriter.visitMethod(ACC_PUBLIC + ACC_BRIDGE
                + ACC_SYNTHETIC, "getParamsForInsert",
                "(Ljava/lang/Object;)[Ljava/lang/Object;", null, null);
        _methodVisitor.visitMaxs(2, 2);
        _methodVisitor.visitVarInsn(ALOAD, 0);
        _methodVisitor.visitVarInsn(ALOAD, 1);
        _methodVisitor.visitTypeInsn(CHECKCAST,
                Type.getInternalName(info.getClazz()));
        _methodVisitor.visitMethodInsn(INVOKEVIRTUAL, mapperClassName,
                "getParamsForInsert", "(" + Type.getDescriptor(info.getClazz())
                        + ")[Ljava/lang/Object;");
        _methodVisitor.visitInsn(ARETURN);
    }

    private String getFieldReturnType(Field field) {
        return field.getType().getName();
    }
}
