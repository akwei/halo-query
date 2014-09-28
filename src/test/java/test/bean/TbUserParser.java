package test.bean;

import halo.query.dal.DALParser;
import halo.query.dal.ParsedInfo;

import java.util.Map;

/**
 * Created by akwei on 9/28/14.
 */
public class TbUserParser implements DALParser {

    public static final TbUserParser instance = new TbUserParser();

    @Override
    public ParsedInfo parse(Map<String, Object> paramMap) {
        Integer userId = (Integer) paramMap.get("userId");
        ParsedInfo info = new ParsedInfo();
        if (userId % 2 == 0) {
            info.setDsKey("db0");
            info.setRealTableName("tb_user_0");
        }
        else {
            info.setDsKey("db1");
            info.setRealTableName("tb_user_1");
        }
        return info;
    }
}
