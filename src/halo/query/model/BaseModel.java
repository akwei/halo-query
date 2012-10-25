package halo.query.model;

import halo.query.Query;

import java.util.List;

/**
 * 直接使用static方法的调用方式，可以省掉dao的部分代码,目前不支持inner join 方式的查询
 * 
 * @author akwei
 */
public class BaseModel {

	protected static Query query;

	private static final String exceptionMsg = "class must override this method";

	public void setQuery(Query query) {
		BaseModel.query = query;
	}

	public static Query getQuery() {
		return query;
	}

	/**
	 * 创建数据到数据库
	 */
	public void create() {
		query.insertForNumber(this);
	}

	/**
	 * 更新已存在的数据，数据必须有id
	 */
	public void update() {
		query.update(this);
	}

	/**
	 * 删除已存在的数据，数据必须有id
	 */
	public void delete() {
		query.delete(this);
	}

	/**
	 * @param afterFrom
	 * @param values
	 * @return
	 */
	public static int count(String afterFrom, Object[] values) {
		throw new RuntimeException(exceptionMsg);
	}

	/**
	 * @param idValue
	 * @return
	 */
	public static <T> T objById(Object idValue) {
		throw new RuntimeException(exceptionMsg);
	}

	/**
	 * @param afterFrom
	 * @param values
	 * @return
	 */
	public static <T> T obj(String afterFrom, Object[] values) {
		throw new RuntimeException(exceptionMsg);
	}

	/**
	 * @param afterFrom
	 * @param begin
	 * @param size
	 * @param values
	 * @return
	 */
	public static <T> List<T> mysqlList(String afterFrom, int begin, int size,
	        Object[] values) {
		throw new RuntimeException(exceptionMsg);
	}

	/**
	 * @param where
	 * @param orderBy
	 * @param begin
	 * @param size
	 * @param values
	 * @return
	 */
	public static <T> List<T> db2List(String where, String orderBy,
	        int begin, int size, Object[] values) {
		throw new RuntimeException(exceptionMsg);
	}

	/**
	 * @param afterFrom
	 * @param values
	 * @return
	 */
	public static <T> List<T> list(String afterFrom, Object[] values) {
		throw new RuntimeException(exceptionMsg);
	}

	/**
	 * @param updateSqlSeg
	 * @param values
	 * @return
	 */
	public static int update(String updateSqlSeg, Object[] values) {
		throw new RuntimeException(exceptionMsg);
	}
}
