package test.bean;

import halo.query.dal.DALParser;
import halo.query.dal.ParsedInfo;

import java.util.Map;

/**
 * Created by akwei on 9/28/14.
 */
public class TbUserIdSeqParser implements DALParser {

    public static final TbUserIdSeqParser instance = new TbUserIdSeqParser();

    @Override
    public ParsedInfo parse(Map<String, Object> paramMap) {
        ParsedInfo info = new ParsedInfo();
        info.setDsKey("db_seq");
        info.setRealTableName("user_seq");
        return info;
    }
}
