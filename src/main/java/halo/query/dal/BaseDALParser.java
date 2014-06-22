package halo.query.dal;

import halo.query.mapping.EntityTableInfo;

import java.util.Map;

public class BaseDALParser implements DALParser {

	public ParsedInfo parse(EntityTableInfo<?> entityTableInfo,
	        Map<String, Object> map) {
		return null;
	}
}