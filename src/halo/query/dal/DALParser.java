package halo.query.dal;

import halo.query.mapping.EntityTableInfo;

import java.util.Map;

/**
 * 分表分库解析器
 * 
 * @author akwei
 */
public interface DALParser {

	ParsedInfo parse(EntityTableInfo<?> entityTableInfo, Map<String, Object> map);
}
