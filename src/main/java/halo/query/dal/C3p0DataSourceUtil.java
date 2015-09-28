package halo.query.dal;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.SQLException;

/**
 * Created by akwei on 9/27/14.
 */
public class C3p0DataSourceUtil {

    private static final Log log = LogFactory.getLog(C3p0DataSourceUtil.class);

    public static void destory(DataSource dataSource) {
        try {
            DataSources.destroy(dataSource);
        } catch (SQLException e) {
            log.warn("can not destory datasource", e);
        }
    }

    public static Method getMethod(Class<?> clazz, String methodName) {
        try {
            return getMethod(clazz, methodName, String.class);
        } catch (NoSuchMethodException e) {
            try {
                return getMethod(clazz, methodName, int.class);
            } catch (NoSuchMethodException e1) {
                throw new RuntimeException(e1);
            }
        }
    }

    public static String createSetterMethodName(String fileName) {
        return "set" + fileName.substring(0, 1).toUpperCase() + fileName.substring(1);
    }

    public static Method getMethod(Class<?> clazz, String methodName, Class<?> paramType) throws NoSuchMethodException {
        try {
            return clazz.getMethod(methodName, paramType);
        } catch (NoSuchMethodException e) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass.equals(Object.class)) {
                throw e;
            }
            return getMethod(clazz.getSuperclass(), methodName, paramType);
        }
    }

    public static void methodInvoke(Object obj, String methodName, Object value) {
        Method method = getMethod(ComboPooledDataSource.class, methodName);
        Class<?>[] paramTypes = method.getParameterTypes();
        Class<?> paramType = paramTypes[0];
        try {
            if (paramType.equals(String.class)) {
                method.invoke(obj, value);
            } else {
                int i;
                if (value instanceof Integer) {
                    i = (Integer) value;
                } else {
                    i = Integer.parseInt((String) value);
                }
                method.invoke(obj, i);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
