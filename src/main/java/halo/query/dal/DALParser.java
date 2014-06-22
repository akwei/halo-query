package halo.query.dal;

import halo.query.mapping.EntityTableInfo;

import java.util.Map;

/**
 * 分表分库解析器
 * 
 * @author akwei
 */
public interface DALParser {

	/**
	 * 根据参数，进行解析，返回解析后的信息
	 * 
	 * @param entityTableInfo 参考{@link EntityTableInfo}
	 * @param paramMap 用户通过{@link DALStatus#setParamMap(Map)} 传递的数据，可以为null
	 * @return 参考{@link ParsedInfo}
	 */
	ParsedInfo parse(EntityTableInfo<?> entityTableInfo,
	        Map<String, Object> paramMap);
}
