package halo.query.dal;

import halo.query.mapping.EntityTableInfo;
import halo.query.mapping.EntityTableInfoFactory;

import java.util.Map;

/**
 * 分区工具类
 * Created by akwei on 6/23/14.
 */
@SuppressWarnings("unchecked")
public class DALParserUtil {

    /**
     * 解析sql路由，返回解析后数据
     *
     * @param clazz     对应类
     * @param dalParser 解析器
     * @param paramMap  需要的参数
     */
    public static void process(Class clazz, DALParser dalParser, Map<String, Object> paramMap) {
        DALInfo dalInfo = DALStatus.getDalInfo();
        if (dalInfo != null && dalInfo.isSpecify()) {
            return;
        }
        if (dalParser != null) {
            ParsedInfo parsedInfo = dalParser.parse(paramMap);
            if (parsedInfo != null) {
                dalInfo = new DALInfo();
                dalInfo.setRealTable(clazz, parsedInfo.getRealTableName());
                dalInfo.setDsKey(parsedInfo.getDsKey());
                DALStatus.setDalInfo(dalInfo);
            }
        }
    }

    /**
     * 手动设置数据在分区中的位置
     *
     * @param dsKey 数据源key
     * @param map   对象所在分区的真实表名称
     */
    public static void addDalInfoManual(String dsKey, Map<Class<?>, String> map) {
        DALInfo dalInfo = new DALInfo();
        dalInfo.setSpecify(true);
        dalInfo.setRealTableMap(map);
        dalInfo.setDsKey(dsKey);
        DALStatus.setDalInfo(dalInfo);
    }

    /**
     * 获得分表后的真实表名称
     *
     * @param clazz    对象class
     * @param paramMap 分区使用的参数
     * @param <T>      泛型
     * @return 对象分区后的表名称
     */
    public static <T> String getRealTableName(Class<T> clazz, Map<String, Object> paramMap) {
        DALStatus.addParamMap(paramMap);
        DALInfo dalInfo = process(clazz);
        return dalInfo.getRealTable(clazz);
    }

    /**
     * 解析sql路由，设置当前数据源key，返回解析后数据
     *
     * @param clazz 需要解析的类
     * @param <T>   泛型
     * @return 解析后的路由数据
     */
    public static <T> DALInfo process(Class<T> clazz) {
        EntityTableInfo<T> entityTableInfo = EntityTableInfoFactory.getEntityTableInfo(clazz);
        return process(entityTableInfo.getClazz(), entityTableInfo.getDalParser());
    }

    /**
     * 解析sql路由，设置当前数据源key，返回解析后数据
     *
     * @param clazz     需要解析的 class
     * @param dalParser 解析器
     * @return 解析后的路由数据
     */
    public static DALInfo process(Class clazz, DALParser dalParser) {
        process(clazz, dalParser, DALStatus.getParamMap());
        return DALStatus.getDalInfo();
    }
}
