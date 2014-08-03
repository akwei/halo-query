package halo.datasource;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * 通过properties文件进行数据源配置
 * Created by akwei on 7/13/14.
 */
public class HaloC3p0PropertiesDataSourceWrapper extends HaloDataSourceWrapper implements InitializingBean {
    private static final String DOT = ".";
    private final Log log = LogFactory.getLog(HaloC3p0PropertiesDataSourceWrapper.class);
    private String name;
    private String prefix;

    public void setName(String name) {
        this.name = name;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(this.name);
        Map<String, Object> map = new HashMap<String, Object>();
        Set<String> set = resourceBundle.keySet();
        for (String key : set) {
            String value = resourceBundle.getString(key);
            if (key.startsWith(this.prefix)) {
                map.put(key, value);
            }
        }
        this.create(this.prefix, map);
    }

    /**
     * 从map中创建连接池
     */
    public void create(String prefix, Map<String, Object> map) {
        this.setKey(prefix);
        this.setDataSource(this.createDataSource(prefix, map));
    }

    /**
     * @param prefix
     * @return
     */
    private DataSource createDataSource(String prefix, Map<String, Object> cfgMap) {
        String begin = prefix + DOT;
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        Set<Map.Entry<String, Object>> set = cfgMap.entrySet();
        for (Map.Entry<String, Object> entry : set) {
            String key = entry.getKey();
            if (key.startsWith(begin)) {
                String fileName = key.substring(begin.length());
                String methodName = this.createSetterMethodName(fileName);
                String value = (String) entry.getValue();
                this.methodInvoke(dataSource, methodName, value);
            }
        }
        return dataSource;
    }

    private Method getMethod(Class<?> clazz, String methodName) {
        try {
            return this.getMethod(clazz, methodName, String.class);
        }
        catch (NoSuchMethodException e) {
            try {
                return this.getMethod(clazz, methodName, int.class);
            }
            catch (NoSuchMethodException e1) {
                throw new RuntimeException(e1);
            }
        }
    }

    private String createSetterMethodName(String fileName) {
        return "set" + fileName.substring(0, 1).toUpperCase() + fileName.substring(1);
    }

    private Method getMethod(Class<?> clazz, String methodName, Class<?> paramType) throws NoSuchMethodException {
        try {
            return clazz.getMethod(methodName, paramType);
        }
        catch (NoSuchMethodException e) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass.equals(Object.class)) {
                throw e;
            }
            return this.getMethod(clazz.getSuperclass(), methodName, paramType);
        }
    }

    private void methodInvoke(Object obj, String methodName, String value) {
        Method method = this.getMethod(ComboPooledDataSource.class, methodName);
        Class<?>[] paramTypes = method.getParameterTypes();
        Class<?> paramType = paramTypes[0];
        try {
            if (paramType.equals(String.class)) {
                method.invoke(obj, value);
            }
            else {
                method.invoke(obj, Integer.valueOf(value));
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void destory() {
        try {
            DataSources.destroy(this.getDataSource());
        }
        catch (SQLException e) {
            log.warn(e.getMessage());
        }
    }
}
