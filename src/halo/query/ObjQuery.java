package halo.query;

import halo.query.mapping.EntityTableInfo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 对象化查询方式
 * 
 * @author akwei
 */
public class ObjQuery {

	private Query query;

	private int begin = 0;

	private int size = 0;

	private List<Class<?>> clazzList = new ArrayList<Class<?>>(2);

	private List<String> paramSqlSegList = new ArrayList<String>(2);

	private List<String> innerJoinSegList = new ArrayList<String>(2);

	private List<Object> paramValues = new ArrayList<Object>(2);

	private List<String> orderBySegList = new ArrayList<String>(1);

	private List<Object> setParamValues = new ArrayList<Object>(2);

	private List<String> setSqlSegList = new ArrayList<String>(2);

	public ObjQuery(Query query) {
		this.query = query;
	}

	/**
	 * 单表查询使用
	 * 
	 * @param clazz 需要查询的类
	 * @return
	 */
	public ObjQuery from(Class<?> clazz) {
		for (Class<?> cls : this.clazzList) {
			if (cls.equals(clazz)) {
				return this;
			}
		}
		this.clazzList.add(clazz);
		return this;
	}

	/**
	 * 多表查询使用
	 * 
	 * @param clazz0 需要查询的类
	 * @param clazz1 需要查询的类
	 * @return
	 */
	public ObjQuery fromInnerJoinOn(Class<?> clazz0, Class<?> clazz1) {
		boolean hasClazz0 = false;
		boolean hasClazz1 = false;
		for (Class<?> cls : this.clazzList) {
			if (cls.equals(clazz0)) {
				hasClazz0 = true;
			}
			else if (cls.equals(clazz1)) {
				hasClazz1 = true;
			}
		}
		if (!hasClazz0) {
			this.clazzList.add(clazz0);
		}
		if (!hasClazz1) {
			this.clazzList.add(clazz1);
		}
		EntityTableInfo<?> info0 = this.query.getEntityTableInfo(clazz0);
		EntityTableInfo<?> info1 = this.query.getEntityTableInfo(clazz1);
		List<RefKeyInfo> refKeyInfos = info0.getRefKeysByClass(clazz1);
		for (RefKeyInfo refKeyInfo : refKeyInfos) {
			Field field = refKeyInfo.getField();
			this.innerJoinSegList.add(info0.getColumnFullNameByFieldName(field.getName())
			        + "=" + info1.getColumnFullNameByFieldName(refKeyInfo.getFieldName()));
		}
		return this;
	}

	public ObjQuery whereEq(String column, Object value) {
		paramSqlSegList.add(column + "=?");
		paramValues.add(value);
		return this;
	}

	public ObjQuery whereNotEq(String column, Object value) {
		paramSqlSegList.add(column + "!=?");
		paramValues.add(value);
		return this;
	}

	public ObjQuery whereGreaterAndEq(String column, Object value) {
		paramSqlSegList.add(column + "=>?");
		paramValues.add(value);
		return this;
	}

	public ObjQuery whereGreaterThan(String column, Object value) {
		paramSqlSegList.add(column + ">?");
		paramValues.add(value);
		return this;
	}

	public ObjQuery whereLessThan(String column, Object value) {
		paramSqlSegList.add(column + "<?");
		paramValues.add(value);
		return this;
	}

	public ObjQuery whereLessAndEq(String column, Object value) {
		paramSqlSegList.add(column + "<=?");
		paramValues.add(value);
		return this;
	}

	public ObjQuery orderByAsc(String column) {
		orderBySegList.add(column + " asc");
		return this;
	}

	public ObjQuery orderByDesc(String column) {
		orderBySegList.add(column + " desc");
		return this;
	}

	public ObjQuery limit(int begin, int size) {
		this.begin = begin;
		this.size = size;
		return this;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> mysqlList() {
		if (clazzList.isEmpty()) {
			throw new RuntimeException("no class for query");
		}
		StringBuilder sb = new StringBuilder();
		if (!this.paramSqlSegList.isEmpty() || !this.innerJoinSegList.isEmpty()) {
			sb.append("where ");
		}
		for (String s : this.innerJoinSegList) {
			sb.append(s).append(" and ");
		}
		for (String s : this.paramSqlSegList) {
			sb.append(s).append(" and ");
		}
		sb.delete(sb.length() - 5, sb.length());
		if (!this.orderBySegList.isEmpty()) {
			sb.append(" order by ");
			for (String s : this.orderBySegList) {
				sb.append(s).append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		Object[] values = null;
		if (!this.paramValues.isEmpty()) {
			values = this.paramValues.toArray(new Object[this.paramValues
			        .size()]);
		}
		// 单表查询
		if (clazzList.size() == 1) {
			return this.query.mysqlList((Class<T>) clazzList.get(0),
			        sb.toString(), begin, size,
			        values);
		}
		if (this.size == 0) {
			throw new RuntimeException(
			        "not support multi table inner join no limit size");
		}
		// 多表查询
		return this.query.mysqlListMulti(this.clazzList
		        .toArray(new Class[this.clazzList.size()]),
		        sb.toString(), begin, size, values);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> db2List() {
		if (clazzList.isEmpty()) {
			throw new RuntimeException("no class for query");
		}
		StringBuilder where = new StringBuilder();
		if (!this.paramSqlSegList.isEmpty() || !this.innerJoinSegList.isEmpty()) {
			where.append("where ");
		}
		for (String s : this.innerJoinSegList) {
			where.append(s).append(" and ");
		}
		for (String s : this.paramSqlSegList) {
			where.append(s).append(" and ");
		}
		where.delete(where.length() - 5, where.length());
		StringBuilder orderBy = new StringBuilder();
		if (!this.orderBySegList.isEmpty()) {
			orderBy.append(" order by ");
			for (String s : this.orderBySegList) {
				orderBy.append(s).append(",");
			}
			orderBy.deleteCharAt(orderBy.length() - 1);
		}
		Object[] values = null;
		if (!this.paramValues.isEmpty()) {
			values = this.paramValues.toArray(new Object[this.paramValues
			        .size()]);
		}
		// 单表查询
		if (clazzList.size() == 1) {
			if (this.size > 0) {
				return this.query.db2List(
				        (Class<T>) clazzList.get(0), where.toString(),
				        orderBy.toString(), begin, size, values);
			}
			return this.query.list((Class<T>) clazzList.get(0),
			        where.append(orderBy)
			                .toString(), values);
		}
		// 多表查询
		if (this.size > 0) {
			return this.query.db2ListMulti(this.clazzList
			        .toArray(new Class[this.clazzList.size()]),
			        where.toString(), orderBy.toString(), begin, size, values);
		}
		throw new RuntimeException(
		        "not support multi table inner join no limit size");
	}

	public ObjQuery set(String column, Object value) {
		this.setSqlSegList.add(column + "=?");
		this.setParamValues.add(value);
		return this;
	}

	public int update() {
		if (clazzList.isEmpty()) {
			throw new RuntimeException("no class for query");
		}
		StringBuilder sb = new StringBuilder("set ");
		for (String s : this.setSqlSegList) {
			sb.append(s).append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		if (!this.paramSqlSegList.isEmpty()) {
			sb.append(" where ");
		}
		for (String s : this.paramSqlSegList) {
			sb.append(s).append(" and ");
		}
		sb.delete(sb.length() - 5, sb.length());
		Object[] values = null;
		this.paramValues.addAll(this.setParamValues);
		if (!this.paramValues.isEmpty()) {
			values = this.paramValues.toArray(new Object[this.paramValues
			        .size()]);
		}
		return this.query.update(this.clazzList.get(0), sb.toString(), values);
	}

	public int delete() {
		if (clazzList.isEmpty()) {
			throw new RuntimeException("no class for query");
		}
		StringBuilder sb = new StringBuilder();
		if (!this.paramSqlSegList.isEmpty()) {
			sb.append("where ");
		}
		for (String s : this.paramSqlSegList) {
			sb.append(s).append(" and ");
		}
		sb.delete(sb.length() - 5, sb.length());
		Object[] values = null;
		this.paramValues.addAll(this.setParamValues);
		if (!this.paramValues.isEmpty()) {
			values = this.paramValues.toArray(new Object[this.paramValues
			        .size()]);
		}
		return this.query.delete(this.clazzList.get(0), sb.toString(), values);
	}
}
