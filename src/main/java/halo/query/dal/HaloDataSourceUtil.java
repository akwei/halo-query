package halo.query.dal;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 连接池工具类
 * Created by akwei on 9/27/14.
 */
public class HaloDataSourceUtil {

    //    private static final Log log = LogFactory.getLog(HaloDataSourceUtil.class);
    private static Logger logger = Logger.getLogger(HaloDataSourceUtil.class);

    public static final List<DataSource> originDataSourceList = new CopyOnWriteArrayList<>();

    static void destory(HaloDataSourceWrapper dataSourceWrapper) {
        try {
            logger.info("begin destory dataSource[" + dataSourceWrapper.getDsKey() + "] ... ... ...");
            DataSources.destroy(dataSourceWrapper.getDataSource());
        } catch (SQLException e) {
            logger.warn("can not destory datasource[" + dataSourceWrapper.getDsKey() + "]", e);
        }
    }

    private static Method getMethod(Class<?> clazz, String methodName) {
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

    static String createSetterMethodName(String fileName) {
        return "set" + fileName.substring(0, 1).toUpperCase() + fileName.substring(1);
    }

    private static Method getMethod(Class<?> clazz, String methodName, Class<?> paramType) throws NoSuchMethodException {
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

    static void methodInvoke(Object obj, String methodName, Object value) {
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

    static DataSource newDataSourceObj(String dataSourceClassName) {
        try {
            Class<?> aClass = Class.forName(dataSourceClassName);
            Object ins = aClass.getConstructor().newInstance();
            return (DataSource) ins;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    static DataSource createDataSource(String dataSourceClassName, Map<String, Object> cfgMap) {
        DataSource dataSource = HaloDataSourceUtil.newDataSourceObj(dataSourceClassName);
        Set<Map.Entry<String, Object>> set = cfgMap.entrySet();
        for (Map.Entry<String, Object> entry : set) {
            String methodName = HaloDataSourceUtil.createSetterMethodName(entry.getKey());
            HaloDataSourceUtil.methodInvoke(dataSource, methodName, entry.getValue());
        }
        originDataSourceList.add(dataSource);
        return dataSource;
    }

    static boolean isNotEmpty(String value) {
        return value != null && value.trim().length() > 0;
    }

    static boolean isEmpty(String value) {
        return value == null || value.trim().length() == 0;
    }

    /**
     * 获得所有原始连接池
     *
     * @return 连接池集合
     */
    public static List<DataSource> getAllOriginDataSources() {
        return new ArrayList<>(originDataSourceList);
    }

}
