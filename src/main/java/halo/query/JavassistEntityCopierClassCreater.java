package halo.query;

import javassist.*;

import java.lang.reflect.Field;

/**
 * 使用Javassist动态创建 {@link EntityCopier} 的实例
 *
 * @author akwei
 */
class JavassistEntityCopierClassCreater<T, E> {

    private final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    /**
     * {@link EntityCopier} 类型
     */
    private Class<?> mapperClass;

    private Class<T> fromClazz;

    private Class<E> toClazz;

    private static final ClassPool pool = ClassPool.getDefault();

    static {
        pool.insertClassPath(new ClassClassPath(JavassistEntityCopierClassCreater.class));
    }

    public static ClassPool getClassPool() {
        return pool;
    }

    public JavassistEntityCopierClassCreater(Class<T> fromClazz, Class<E> toClazz) {
        this.fromClazz = fromClazz;
        this.toClazz = toClazz;
        String className = this.createClassName();
        try {
            ClassPool pool = JavassistEntityCopierClassCreater.getClassPool();
            CtClass beanCopierClass = pool.get(EntityCopier.class.getName());
            CtClass cc;
            try {
                cc = pool.getCtClass(className);
                // 如果已经有同名类就赋值
                this.mapperClass = classLoader.loadClass(className);
            } catch (NotFoundException e) {
                // 没有找到，就创建新的class
                cc = pool.makeClass(className);
                cc.setInterfaces(new CtClass[]{beanCopierClass});
                //                SignatureAttribute.ClassSignature cs = new SignatureAttribute
                //                        .ClassSignature(new SignatureAttribute
                //                        .TypeParameter[]{new SignatureAttribute.TypeParameter
                //                        (from.getClass().getName()), new SignatureAttribute
                //                        .TypeParameter(toClazz.getName())});
                //                cc.setGenericSignature(cs.encode());
                String src = this.createMethodSrc();
                CtMethod method;
                method = CtNewMethod.make(src, cc);
                //create T E
                //                SignatureAttribute.TypeVariable tvarT = new SignatureAttribute
                //                        .TypeVariable(this.from.getClass().getName());
                //                SignatureAttribute.TypeVariable tvarE = new SignatureAttribute
                //                        .TypeVariable(this.toClazz.getName());
                //                SignatureAttribute.MethodSignature ms = new
                //                        SignatureAttribute.MethodSignature(null,
                //                        new SignatureAttribute.TypeVariable[]{tvarT, tvarE},
                //                        tvarE, null);
                //                method.setGenericSignature(ms.encode());
                cc.addMethod(method);
                this.mapperClass = cc.toClass(classLoader, classLoader
                                .getClass()
                                .getProtectionDomain()
                );
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } catch (CannotCompileException e) {
            throw new RuntimeException(e);
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Class<?> getMapperClass() {
        return mapperClass;
    }

    /**
     * 生成类名称
     *
     * @return
     */
    private String createClassName() {
        int idx = this.fromClazz.getName().lastIndexOf(".");
        int idx0 = toClazz.getName().lastIndexOf(".");
        String shortFromName = this.fromClazz.getName().substring(idx + 1);
        String shortToName = toClazz.getName().substring(idx0 + 1);
        String pkgName = this.fromClazz.getName().substring(0, idx);
        return pkgName + "." + shortFromName + "_" + shortToName +
                "Javassist$EntityCopier";
    }

    private String createMethodSrc() {
        StringBuilder sb = new StringBuilder("public void copy(Object from, Object to){");
        String fromClassName = this.fromClazz.getName();
        String toClazzName = toClazz.getName();
        sb.append(fromClassName + " _from = (" + fromClassName + ")from;");
        sb.append(toClazzName + " _to=(" + toClazzName + ")to;");
        Field[] fields = toClazz.getDeclaredFields();
        // 进行 obj.setter(from.getter....)等赋值操作
        for (Field field : fields) {
            String name = field.getName();
            try {
                this.fromClazz.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                continue;
            }
            String getter;
            if (field.getType().equals(boolean.class) || field.getType().equals(Boolean.class)) {
                getter = createSetterOrGetterMethodString("is", name);
            } else {
                getter = createSetterOrGetterMethodString("get", name);
            }
            String setter = createSetterOrGetterMethodString("set", name);
            if (!this.hasSetterMethod(setter, field) || !this.hasGetterMethod(getter)) {
                continue;
            }
            sb.append("_to." + setter + "(_from." + getter + "());");
        }
        sb.append("}");
        return sb.toString();
    }

    private boolean hasSetterMethod(String methodName, Field field) {
        try {
            toClazz.getDeclaredMethod(methodName, field.getType());
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    private boolean hasGetterMethod(String methodName) {
        try {
            toClazz.getDeclaredMethod(methodName);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    /**
     * 生成setter/getter方法名称，按照头字母大写规则进行生成，如果第二个字母为大写，那么首字母还是小写
     *
     * @param fieldName
     * @return
     */
    private String createSetterOrGetterMethodString(String prefix, String fieldName) {
        String first = fieldName.substring(0, 1);
        String second = fieldName.substring(1, 2);
        if (second.equals(second.toUpperCase())) {
            try {
                // 如果第二位为数字，第一位还是需要大写
                Integer.parseInt(second);
                return prefix + first.toUpperCase() + fieldName.substring(1);
            } catch (NumberFormatException e) {
                return prefix + fieldName;
            }
        }
        return prefix + first.toUpperCase() + fieldName.substring(1);
    }
}