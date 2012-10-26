package test.bean;

import halo.query.dal.DALParser;
import halo.query.dal.ParsedInfo;
import halo.query.mapping.EntityTableInfo;

import java.util.Map;

public class Db2Parser implements DALParser {

	public ParsedInfo parse(EntityTableInfo<?> entityTableInfo,
	        Map<String, Object> map) {
		ParsedInfo info = new ParsedInfo();
		info.setDsKey("ds_db2");
		info.setRealTableName(entityTableInfo.getTableName());
		return info;
	}
}