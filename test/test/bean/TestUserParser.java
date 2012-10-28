package test.bean;

import halo.query.dal.DALParser;
import halo.query.dal.ParsedInfo;
import halo.query.mapping.EntityTableInfo;

import java.util.Map;

/**
 * TestUser的解析器
 * 
 * @author akwei
 */
public class TestUserParser implements DALParser {

	public ParsedInfo parse(EntityTableInfo<?> entityTableInfo,
	        Map<String, Object> paraMap) {
		ParsedInfo info = new ParsedInfo();
		info.setDsKey(null);// 真实的数据源key(参见xml配置)。如果使用默认数据源，设置null
		info.setRealTableName(entityTableInfo.getTableName() + "00");// 真实的表名称
		return info;
	}
}