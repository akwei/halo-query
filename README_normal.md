#query方式操作

##Step 1 在spring中配置
```xml
<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
	<property name="driverClass" value="com.mysql.jdbc.Driver" />
	<property name="jdbcUrl" value="jdbc:mysql://127.0.0.1:3306/querytest?useUnicode=true&amp;characterEncoding=UTF-8" />
	<property name="user" value="root" />
	<property name="password" value="root" />
	<property name="idleConnectionTestPeriod" value="60" />
	<property name="maxPoolSize" value="10" />
	<property name="initialPoolSize" value="10" />
	<property name="minPoolSize" value="10" />
</bean>
<bean id="jdbcSupport" class="halo.query.JdbcSupport">
	<property name="dataSource" ref="dataSource" />
	<property name="debugSQL" value="true" />
</bean>
<bean id="query" class="halo.query.Query">
	<property name="jdbcSupport" ref="jdbcSupport" />
</bean>
<bean class="halo.query.HaloQuerySpringBeanUtil" />
```

##Step 2 创建与数据库表对应的实体类，表必须有唯一主键，不支持联合主键
```` java
@Table(name = "table_1")
class T1 {
	@Id//表示为数据表主键,主键目前只支持String long int
	@Column("user_id") //注明为数据库对应user_id字段
	private int userId;
	@column
	private String name;
	setter...
	getter...
}

@Table(name = "table_2")
@HaloModel
class T2 {
	@Id//表示为数据表主键
	@Column("to_id") //注明为数据库对应user_id字段
	private int toId;
	
	@Column("user_id")
	private int userId;
	
	public void setT1(T1 t1);
	public T1 getT1();
	
	@column
	private String name;
	setter...
	getter...
}
````


##Step 3 在代码中使用
halo.query.Query query = // getQuery from spring bean 

### insert
````java
query.insert(object)；
query.inserForNumber(object);//返回自增id，mysql id 自增方式，db2数据库使用sequence，请先配置
````

### delete
````java
query.delete(object);
````

### update
````java
query.update(object);
````

### 集合查询，不分页
````java
List<T1> list=query.list(T1.class,"where level=?",new Object[]{1});
````

### 根据id，查询对象
````java
T1 obj=query.objById(T1.class,idValue);
````

### 获得select count(*)结果
````java
int result = query.count(T1.class, "where name=? and bb=?",Object[]{"akweiwei",1});
````

### 获得 inner join 方式的 count(*)结果，只支持 inner join
````java
int result = query.count(new Object[]{T1.class,T2.class}, "where table_1.user_id=table_2.user_id and name=? and bb=?",Object[]{"akweiwei",1});
````

### mysql中集合查询，分页方式
````java
List<T1> list = query.mysqlList(T1.class, "where name=? and bb=? order by id desc",0,6,Object[]{"akweiwei",1});
````

### mysql中集合查询，分页方式，多表inner join查询。查询的集合类型必须放在T1.class的位置,表的别名为@table中的name,只支持 inner join
````java
List<T1> list = query.mysqlList(new Object[]{T1.class,T2.class}, "where table_1.user_id=table_2.user_id and name=? and bb=? order by table_1.user_id desc",0,6,Object[]{"akweiwei",1});
````

### 如果这些写法无法满足需求，可以直接使用spring jdbcTemplate的写法
````java
query.getJdbcSupport().insert | list | update | num
