package halo.query.dal;

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
     * @param paramMap 用户通过{@link DALStatus#setParamMap(Map)} 传递的数据，可以为null
     * @return 解析信息
     */
    ParsedInfo parse(Map<String, Object> paramMap);
}
