package halo.query.dal;

/**
 * Created by akwei on 6/23/14.
 */
public class DALParserUtil {
    /**
     * 解析sql路由，设置当前数据源key，返回解析后数据
     *
     * @param clazz     需要解析的 class
     * @param dalParser 解析器
     * @return
     */
    public static ParsedInfo process(Class clazz, DALParser dalParser) {
        if (dalParser == null) {
            return null;
        }
        DALInfo dalInfo = DALStatus.getDalInfo(clazz);
        if (dalInfo != null) {
            String dsKey = DALStatus.getDsKey();
            ParsedInfo parsedInfo = new ParsedInfo();
            parsedInfo.setDsKey(dsKey);
            parsedInfo.setRealTableName(dalInfo.getRealTable(clazz));
            return parsedInfo;
        }
        ParsedInfo parsedInfo = dalParser.parse(DALStatus.getParamMap());
        if (parsedInfo != null) {
            DALStatus.setDsKey(parsedInfo.getDsKey());
        }
        return parsedInfo;
    }
}
