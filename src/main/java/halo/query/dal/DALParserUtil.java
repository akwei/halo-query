package halo.query.dal;

import java.util.Map;

/**
 * Created by akwei on 6/23/14.
 */
public class DALParserUtil {

    /**
     * 解析sql路由，返回解析后数据
     *
     * @param clazz     对应类
     * @param dalParser 解析器
     * @param paramMap  需要的参数
     */
    public static void process(Class clazz, DALParser dalParser, Map<String,
            Object> paramMap) {
        if (dalParser == null) {
            throw new IllegalArgumentException("dalParser must be not null");
        }
        DALInfo dalInfo = DALStatus.getDalInfo();
        if (dalInfo == null) {
            dalInfo = new DALInfo();
        }
        ParsedInfo parsedInfo = dalParser.parse(paramMap);
        if (parsedInfo != null) {
            dalInfo.setRealTable(clazz, parsedInfo.getRealTableName());
            dalInfo.setDsKey(parsedInfo.getDsKey());
            DALStatus.setDalInfo(dalInfo);
        }
    }

    /**
     * 设定sql路由
     *
     * @param dalInfo 路由设置
     */
    public static void process(DALInfo dalInfo) {
        DALStatus.setDalInfo(dalInfo);
    }
}
