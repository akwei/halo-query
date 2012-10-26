package test.bean;

import halo.query.dal.DALParser;
import halo.query.dal.ParsedInfo;
import halo.query.mapping.EntityTableInfo;

import java.util.Map;

public class TestUserParser implements DALParser {

	public ParsedInfo parse(EntityTableInfo<?> entityTableInfo,
	        Map<String, Object> map) {
		ParsedInfo info = new ParsedInfo();
		info.setDsKey(null);
		info.setRealTableName(entityTableInfo.getTableName() + "00");
		return info;
	}
}
