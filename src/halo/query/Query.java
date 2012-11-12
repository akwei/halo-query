package halo.query;

import halo.query.idtool.IdGenerator;
import halo.query.mapping.EntityTableInfo;
import halo.query.mapping.EntityTableInfoFactory;
import halo.query.mapping.SQLMapper;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;

public class Query {

	protected JdbcSupport jdbcSupport;

	protected IdGenerator idGenerator;

	/**
	 * 进行多表查询时使用的RowMapper,只能用在inner join的sql中
	 * 
	 * @author akwei
	 * @param <T>
	 */
	static class MultiTableRowMapper<T> implements RowMapper<T> {

		private Query query;

		public void setQuery(Query query) {
			this.query = query;
		}

		private Class<?>[] clazzes;

		public void setClazzes(Class<?>[] clazzes) {
			this.clazzes = clazzes;
		}

		@SuppressWarnings("unchecked")
		public T mapRow(ResultSet rs, int rowNum) throws SQLException {
			Map<String, Object> map = new HashMap<String, Object>(2);
			RowMapper<?> rowMaper = null;
			Object o = null;
			for (Class<?> clazz : clazzes) {
				rowMaper = query.getRowMapper(clazz);
				o = rowMaper.mapRow(rs, rowNum);
				map.put(o.getClass().getName(), o);
			}
			Set<Entry<String, Object>> set = map.entrySet();
			for (Entry<String, Object> e : set) {
				EntityTableInfo<?> info = query
				        .getEntityTableInfo(e.getValue().getClass());
				for (Entry<String, Object> entry : set) {
					if (entry.equals(e)) {
						continue;
					}
					if (info.hasRefByClass(entry.getValue().getClass())) {
						info.setRefObjectValue(e.getValue(), entry.getValue());
					}
				}
			}
			return (T) (map.get(clazzes[0].getName()));
		}
	}

	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	/**
	 * select count(*) 查询。查询中的表别名必须与表名相同
	 * 
	 * @param clazzes 查询对象类型数组
	 * @param afterFrom from table 之后的sql,例如select * from table where uid=?
	 *        order name desc, afterFrom为where uid=? order name desc
	 * @param values 参数化查询值
	 * @return
	 */
	public <T> int count(Class<?>[] clazzes, String afterFrom, Object[] values) {
		StringBuilder sb = new StringBuilder("select count(*) from ");
		for (Class<?> clazz : clazzes) {
			EntityTableInfo<T> info = this.getEntityTableInfo(clazz);
			String parsedTableName = info.parseDAL();
			if (parsedTableName != null) {
				sb.append(parsedTableName);
			}
			else {
				sb.append(info.getTableName());
			}
			sb.append(" as ");
			sb.append(info.getTableAlias());
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(" ");
		sb.append(afterFrom);
		return jdbcSupport.num(sb.toString(), values).intValue();
	}

	/**
	 * select count(*) 查询
	 * 
	 * @param clazz 查询对象类型
	 * @param afterFrom from table 之后的sql,例如select * from table where uid=?
	 *        order name desc, afterFrom为where uid=? order name desc
	 * @param values 参数化查询值
	 * @return
	 */
	public <T> int count(Class<T> clazz, String afterFrom, Object[] values) {
		EntityTableInfo<T> info = this.getEntityTableInfo(clazz);
		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) from ");
		String parsedTableName = info.parseDAL();
		if (parsedTableName != null) {
			sql.append(parsedTableName);
		}
		else {
			sql.append(info.getTableName());
		}
		sql.append(" ");
		if (afterFrom != null) {
			sql.append(afterFrom);
		}
		return jdbcSupport.num(sql.toString(), values).intValue();
	}

	public <T> List<T> list(Class<T> clazz, String afterFrom, Object[] values,
	        RowMapper<T> rowMapper) {
		EntityTableInfo<T> info = this.getEntityTableInfo(clazz);
		StringBuilder sql = new StringBuilder();
		sql.append("select ");
		sql.append(info.getSelectedFieldSQL());
		sql.append(" from ");
		String parsedTableName = info.parseDAL();
		if (parsedTableName != null) {
			sql.append(parsedTableName);
		}
		else {
			sql.append(info.getTableName());
		}
		sql.append(" as ");
		sql.append(info.getTableAlias());
		sql.append(" ");
		if (afterFrom != null) {
			sql.append(afterFrom);
		}
		return jdbcSupport.list(sql.toString(), values, rowMapper);
	}

	public <T> List<T> list(Class<T> clazz, String afterFrom, Object[] values) {
		return this.list(clazz, afterFrom, values, this.getRowMapper(clazz));
	}

	/**
	 * db2 sql inner join 分页查询。表的别名与表名相同
	 * 
	 * @param clazzes 需要inner join的类
	 * @param where 条件表达式
	 * @param orderBy 排序表达式
	 * @param begin 分页开始位置
	 * @param size 分页获取数量
	 * @param values 参数话查询的值
	 * @param rowMapper spring {@link RowMapper} 对象
	 * @return 查询集合
	 */
	public <T> List<T> db2List(Class<?>[] clazzes, String where,
	        String orderBy, int begin, int size, Object[] values,
	        RowMapper<T> rowMapper) {
		EntityTableInfo<T> info = null;
		StringBuilder sql = new StringBuilder();
		sql.append("select * from ( select ");
		for (Class<?> clazz : clazzes) {
			info = this.getEntityTableInfo(clazz);
			sql.append(info.getSelectedFieldSQL());
			sql.append(",");
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(" ,rownumber() over (");
		if (orderBy != null) {
			sql.append(orderBy);
		}
		sql.append(") as rowid from ");
		for (Class<?> clazz : clazzes) {
			info = this.getEntityTableInfo(clazz);
			String parsedTableName = info.parseDAL();
			if (parsedTableName != null) {
				sql.append(parsedTableName);
			}
			else {
				sql.append(info.getTableName());
			}
			sql.append(" as ");
			sql.append(info.getTableAlias());
			sql.append(",");
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(" ");
		if (where != null) {
			sql.append(where);
		}
		sql.append(") temp where temp.rowid >= ");
		sql.append(begin + 1);
		sql.append(" and temp.rowid <= ");
		sql.append(begin + size);
		return jdbcSupport.list(sql.toString(), values, rowMapper);
	}

	/**
	 * db2 sql inner join 查询高级方式.返回值集合中元素类型为clazzes[0]
	 * 
	 * @param clazzes 需要查询的类数组
	 * @param where where sql
	 * @param orderBy order by sql
	 * @param begin 分页开始位置
	 * @param size 分页获取数量
	 * @param values 参数化查询值
	 * @return 返回值集合中元素类型为clazzes[0]
	 */
	public <T> List<T> db2List(Class<?>[] clazzes, String where,
	        String orderBy, int begin, int size, Object[] values) {
		MultiTableRowMapper<T> mapper = new MultiTableRowMapper<T>();
		mapper.setQuery(this);
		mapper.setClazzes(clazzes);
		return this.db2List(clazzes, where, orderBy, begin, size,
		        values, mapper);
	}

	/**
	 * db2 sql分页查询。返回集合类型为clazz
	 * 
	 * @param clazz 需要查询的类
	 * @param where where表达式
	 * @param orderBy order by表达式
	 * @param begin 分页开始位置
	 * @param size 分页获取数量
	 * @param values 参数化查询值
	 * @return
	 */
	public <T> List<T> db2List(Class<T> clazz, String where, String orderBy,
	        int begin, int size, Object[] values) {
		return this.db2List(clazz, where, orderBy, begin, size, values,
		        this.getRowMapper(clazz));
	}

	/**
	 * db2 sql分页查询。返回集合类型为clazz
	 * 
	 * @param clazz 需要查询的类
	 * @param where where表达式
	 * @param orderBy order by表达式
	 * @param begin 分页开始位置
	 * @param size 分页获取数量
	 * @param values 参数化查询值
	 * @param rowMapper spring {@link RowMapper}
	 * @return
	 */
	public <T> List<T> db2List(Class<T> clazz, String where, String orderBy,
	        int begin, int size, Object[] values, RowMapper<T> rowMapper) {
		EntityTableInfo<T> info = this.getEntityTableInfo(clazz);
		StringBuilder sql = new StringBuilder();
		sql.append("select * from ( select ");
		sql.append(info.getSelectedFieldSQL());
		sql.append(" ,rownumber() over (");
		if (orderBy != null) {
			sql.append(orderBy);
		}
		sql.append(") as rowid from ");
		String parsedTableName = info.parseDAL();
		if (parsedTableName != null) {
			sql.append(parsedTableName);
		}
		else {
			sql.append(info.getTableName());
		}
		sql.append(" as ");
		sql.append(info.getTableAlias());
		sql.append(" ");
		if (where != null) {
			sql.append(where);
		}
		sql.append(") temp where temp.rowid >= ");
		sql.append(begin + 1);
		sql.append(" and temp.rowid <= ");
		sql.append(begin + size);
		return jdbcSupport.list(sql.toString(), values, rowMapper);
	}

	/**
	 * delete sql.根据条件删除.例如: delete table where field0=? and ....
	 * 
	 * @param clazz 要删除的对象类型
	 * @param afterFrom delete table 之后的语句,例如:delete table where
	 *        field0=?,afterFrom为where field0=?
	 * @param values 参数化查询值
	 * @return
	 */
	public <T> int delete(Class<T> clazz, String afterFrom, Object[] values) {
		EntityTableInfo<T> info = this.getEntityTableInfo(clazz);
		StringBuilder sql = new StringBuilder();
		sql.append("delete from ");
		String parsedTableName = info.parseDAL();
		if (parsedTableName != null) {
			sql.append(parsedTableName);
		}
		else {
			sql.append(info.getTableName());
		}
		sql.append(" ");
		if (afterFrom != null) {
			sql.append(afterFrom);
		}
		return this.jdbcSupport.update(sql.toString(), values);
	}

	/**
	 * delete sql,返回删除的记录数量
	 * 
	 * @param t 要删除的对象，必须有id
	 * @return
	 */
	public <T> int delete(T t) {
		SQLMapper<T> mapper = this.getSqlMapper(t.getClass());
		return this.deleteById(t.getClass(), mapper.getIdParam(t));
	}

	/**
	 * delete sql,根据id删除。返回删除的记录数量
	 * 
	 * @param clazz 要删除的对象的类型
	 * @param idValue id的参数
	 * @return @ sql操作失败的异常
	 */
	public <T> int deleteById(Class<T> clazz, Object idValue) {
		EntityTableInfo<T> info = this.getEntityTableInfo(clazz);
		return this.jdbcSupport.update(info.getDeleteSQL(),
		        new Object[] { idValue });
	}

	/**
	 * insert sql
	 * 
	 * @param t insert的对象
	 */
	public <T> void insert(T t) {
		EntityTableInfo<T> info = this.getEntityTableInfo(t.getClass());
		SQLMapper<T> mapper = this.getSqlMapper(t.getClass());
		this.jdbcSupport.insert(info.getInsertSQL(true),
		        mapper.getParamsForInsert(t, true), false);
	}

	/**
	 * insert sql,返回自增数字id
	 * 
	 * @param t insert的对象
	 * @return
	 */
	public <T> Number insertForNumber(T t) {
		EntityTableInfo<T> info = this.getEntityTableInfo(t.getClass());
		SQLMapper<T> mapper = this.getSqlMapper(t.getClass());
		Object idValue;
		try {
			idValue = info.getIdField().get(t);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		// id 为数字时，只支持 int long
		if (this.isNumberIdType(info.getIdField())) {
			if (idValue == null) {
				idValue = 0;
			}
			Number num = (Number) idValue;
			// id = 0,需要获得自增id
			if (num.longValue() <= 0) {
				// sequence 获取id
				if (info.isHasSequence()) {
					long id = this.idGenerator.nextKey(info
					        .getDataFieldMaxValueIncrementer());
					this.setIdValue(t, info.getIdField(), id);
					this.jdbcSupport.insert(
					        info.getInsertSQL(true),
					        mapper.getParamsForInsert(t, true), false);
					return id;
				}
				// 为自增id方式
				Number n = (Number) (this.jdbcSupport.insert(
				        info.getInsertSQL(false),
				        mapper.getParamsForInsert(t, false), true));
				this.setIdValue(t, info.getIdField(), n);
				return n;
			}
			// id>0,不需要赋值，返回0
			this.jdbcSupport.insert(info.getInsertSQL(true),
			        mapper.getParamsForInsert(t, true), false);
			return 0;
		}
		// 非数字id时,不需要赋值
		this.jdbcSupport.insert(info.getInsertSQL(true),
		        mapper.getParamsForInsert(t, true), false);
		return 0;
	}

	private boolean isNumberIdType(Field field) {
		Class<?> cls = field.getType();
		if (cls.equals(int.class)) {
			return true;
		}
		if (cls.equals(Integer.class)) {
			return true;
		}
		if (cls.equals(long.class)) {
			return true;
		}
		if (cls.equals(Long.class)) {
			return true;
		}
		if (cls.equals(BigInteger.class)) {
			return true;
		}
		return false;
	}

	private <T> void setIdValue(T t, Field idField, Number n) {
		try {
			if (idField.getType().equals(Integer.class)
			        || idField.getType().equals(int.class)) {
				idField.set(t, n.intValue());
			}
			else if (idField.getType().equals(Long.class)
			        || idField.getType().equals(long.class)) {
				idField.set(t, n.longValue());
			}
			else {
				idField.set(t, n.longValue());
			}
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * mysql的分页查询。查询中的表别名必须与表名相同
	 * 
	 * @param clazzes 查询对象类型数组
	 * @param afterFrom from table 之后的sql,例如select * from table where uid=?
	 *        order name desc, afterFrom为where uid=? order name
	 * @param begin
	 * @param size
	 * @param values 参数化查询值
	 * @param rowMapper
	 * @return
	 */
	public <T> List<T> mysqlList(Class<?>[] clazzes, String afterFrom,
	        int begin, int size, Object[] values, RowMapper<T> rowMapper) {
		StringBuilder sql = new StringBuilder("select ");
		EntityTableInfo<T> info;
		for (Class<?> clazz : clazzes) {
			info = this.getEntityTableInfo(clazz);
			sql.append(info.getSelectedFieldSQL());
			sql.append(",");
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(" from ");
		for (Class<?> clazz : clazzes) {
			info = this.getEntityTableInfo(clazz);
			String parsedTableName = info.parseDAL();
			if (parsedTableName != null) {
				sql.append(parsedTableName);
			}
			else {
				sql.append(info.getTableName());
			}
			sql.append(" as ");
			sql.append(info.getTableAlias());
			sql.append(",");
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(" ");
		sql.append(afterFrom);
		if (size > 0) {
			sql.append(" limit ");
			sql.append(begin);
			sql.append(",");
			sql.append(size);
		}
		return jdbcSupport.list(sql.toString(), values, rowMapper);
	}

	/**
	 * mysql的分页查询。
	 * 
	 * @param clazz 查询对象类型
	 * @param afterFrom from table 之后的sql,例如select * from table where uid=?
	 *        order name desc, afterFrom为where uid=? order name
	 * @param begin
	 * @param size
	 * @param values 参数化查询值
	 * @return
	 */
	public <T> List<T> mysqlList(Class<T> clazz, String afterFrom,
	        int begin, int size, Object[] values) {
		return this.mysqlList(clazz, afterFrom, begin, size,
		        values, this.getRowMapper(clazz));
	}

	/**
	 * mysql的分页查询。
	 * 
	 * @param clazz 查询对象类型
	 * @param afterFrom from table 之后的sql,例如select * from table where uid=?
	 *        order name desc, afterFrom为where uid=? order name
	 * @param begin
	 * @param size
	 * @param values 参数化查询值
	 * @param rowMapper
	 * @return
	 */
	public <T> List<T> mysqlList(Class<T> clazz, String afterFrom,
	        int begin, int size, Object[] values, RowMapper<T> rowMapper)
	{
		EntityTableInfo<T> info = this.getEntityTableInfo(clazz);
		StringBuilder sql = new StringBuilder();
		sql.append("select ");
		sql.append(info.getSelectedFieldSQL());
		sql.append(" from ");
		String parsedTableName = info.parseDAL();
		if (parsedTableName != null) {
			sql.append(parsedTableName);
		}
		else {
			sql.append(info.getTableName());
		}
		sql.append(" as ");
		sql.append(info.getTableAlias());
		sql.append(" ");
		if (afterFrom != null) {
			sql.append(afterFrom);
		}
		if (size > 0) {
			sql.append(" limit ");
			sql.append(begin);
			sql.append(",");
			sql.append(size);
		}
		return jdbcSupport.list(sql.toString(), values, rowMapper);
	}

	public <T> List<T> mysqlList(Class<?>[] clazzes, String afterFrom,
	        int begin, int size, Object[] values) {
		MultiTableRowMapper<T> mapper = new MultiTableRowMapper<T>();
		mapper.setQuery(this);
		mapper.setClazzes(clazzes);
		return this.mysqlList(clazzes, afterFrom, begin, size, values, mapper);
	}

	/**
	 * select sql 返回对象
	 * 
	 * @param clazz 查询对象类型
	 * @param afterFrom from table 之后的sql,例如select * from table where uid=?
	 *        order name desc, afterFrom为where uid=? order name desc
	 * @param values 参数化查询值
	 * @return
	 */
	public <T> T obj(Class<T> clazz, String afterFrom, Object[] values) {
		return this.obj(clazz, afterFrom, values, this.getRowMapper(clazz));
	}

	/**
	 * select sql 返回对象
	 * 
	 * @param clazz 查询对象类型
	 * @param afterFrom from table 之后的sql,例如select * from table where uid=?
	 *        order name desc, afterFrom为where uid=? order name desc
	 * @param values 参数化查询值
	 * @param rowMapper
	 * @return
	 */
	public <T> T obj(Class<T> clazz, String afterFrom, Object[] values,
	        RowMapper<T> rowMapper) {
		EntityTableInfo<T> info = this.getEntityTableInfo(clazz);
		StringBuilder sql = new StringBuilder();
		sql.append("select ");
		sql.append(info.getSelectedFieldSQL());
		sql.append(" from ");
		String parsedTableName = info.parseDAL();
		if (parsedTableName != null) {
			sql.append(parsedTableName);
		}
		else {
			sql.append(info.getTableName());
		}
		sql.append(" as ");
		sql.append(info.getTableAlias());
		sql.append(" ");
		if (afterFrom != null) {
			sql.append(afterFrom);
		}
		List<T> list = jdbcSupport.list(sql.toString(), values, rowMapper);
		if (list.isEmpty()) {
			return null;
		}
		if (list.size() == 1) {
			return list.get(0);
		}
		throw new IncorrectResultSizeDataAccessException(1, list.size());
	}

	/**
	 * select sql 根据id查询，返回对象
	 * 
	 * @param clazz 查询对象类型
	 * @param idValue id参数
	 * @return
	 */
	public <T> T objById(Class<T> clazz, Object idValue) {
		return this.objById(clazz, idValue, this.getRowMapper(clazz));
	}

	/**
	 * select sql 根据id查询，返回对象
	 * 
	 * @param clazz 查询对象类型
	 * @param idValue id参数
	 * @param rowMapper
	 * @return
	 */
	public <T> T objById(Class<T> clazz, Object idValue, RowMapper<T> rowMapper) {
		EntityTableInfo<T> info = this.getEntityTableInfo(clazz);
		String afterFrom = "where " + info.getIdColumnName() + "=?";
		return this.obj(clazz, afterFrom, new Object[] { idValue }, rowMapper);
	}

	/**
	 * update sql，返回更新的记录数量。只更新选中的字段 例如: update table set field0=?,field1=?
	 * where field3=?
	 * 
	 * @param clazz 需要更新的类
	 * @param updateSqlSeg sql片段,为update table 之后的sql。例如：set field0=?,field1=?
	 *        where field3=?
	 * @param values 参数化查询值
	 * @return
	 */
	public <T> int update(Class<T> clazz, String updateSqlSeg, Object[] values) {
		EntityTableInfo<T> info = this.getEntityTableInfo(clazz);
		StringBuilder sql = new StringBuilder();
		sql.append("update ");
		String parsedTableName = info.parseDAL();
		if (parsedTableName != null) {
			sql.append(parsedTableName);
		}
		else {
			sql.append(info.getTableName());
		}
		sql.append(" ");
		sql.append(updateSqlSeg);
		return this.jdbcSupport.update(sql.toString(), values);
	}

	/**
	 * update sql ,返回更新的记录数量
	 * 
	 * @param t update的对象
	 * @return
	 */
	public <T> int update(T t) {
		EntityTableInfo<T> info = this.getEntityTableInfo(t.getClass());
		SQLMapper<T> mapper = this.getSqlMapper(t.getClass());
		return this.jdbcSupport.update(info.getUpdateSQL(),
		        mapper.getParamsForUpdate(t));
	}

	public void setJdbcSupport(JdbcSupport jdbcSupport) {
		this.jdbcSupport = jdbcSupport;
	}

	@SuppressWarnings("unchecked")
	public <T> EntityTableInfo<T> getEntityTableInfo(Class<?> clazz) {
		return (EntityTableInfo<T>) EntityTableInfoFactory
		        .getEntityTableInfo(clazz);
	}

	public JdbcSupport getJdbcSupport() {
		return jdbcSupport;
	}

	@SuppressWarnings("unchecked")
	public <T> RowMapper<T> getRowMapper(Class<T> clazz) {
		return (RowMapper<T>) this.getEntityTableInfo(clazz).getRowMapper();
	}

	@SuppressWarnings("unchecked")
	public <T> SQLMapper<T> getSqlMapper(Class<?> clazz) {
		return (SQLMapper<T>) this.getEntityTableInfo(clazz).getSqlMapper();
	}
}