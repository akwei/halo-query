#一个轻量级orm框架，自动组装ResultSet结果集。
##底层使用的是spring jdbcTemplate
##目前支持的数据库为mysql,db2
##mysql,db2测试通过
##支持分分布式数据库操作，支持单库jdbc事务，支持读写分离(请看database_distribution.md)

##需要注意的是，查询使用的sql，所有被查询的字段都具有别名，例如 
###table=user tableAlias=user_ column=userid alias=user_userid
###table=db.user tableAlias=db_user_ column=userid alias=db_user_userid

使用说明
### 1 在spring中配置
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
<bean class="halo.query.model.BaseModel">
	<property name="query" ref="query" />
</bean>
```

#### 如果使用web.xml linstener方式加载spring，那么请使用halo.query.model.ModelContextLoaderListener
#### 如果使用代码方式加载spring，那么请在你的代码之前，先调用如下代码，代码只需要调用一次
````java
ModelLoader loader = new ModelLoader();
loader.setModelBasePath("test");//参数为扫描BaseModel子类的所在位置的目录，可以设置最大的目录，也可以设置Model的目录,例如: test/model
loader.makeModelClass();
````

### 2 创建与数据库表对应的实体类，表必须有唯一主键，暂时不支持联合主键
```` java
@Table(name = "table_1",
		db2_sequence = "user_seq",// DB2 sequence,不需要可以不写
		oracle_sequence = "user_seq",// oracle sequence,不需要可以不写
		mysql_sequence = "user_seq",// mysql id 自增表,不需要可以不写
		mysql_sequence_column_name = "seq_id",// mysql id
												// 自增表中的自增字段,不需要可以不写，在使用mysql_sequence时，必须写
		sequence_ds_bean_id = "dataSource_for_id_generator"// 自增策略的数据源，不使用*_sequence时，可以不写
)
class T1 extends BaseModel{
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
class T2 extends BaseModel{
	@Id//表示为数据表主键
	@Column("to_id") //注明为数据库对应user_id字段
	private int toId;
	
	@RefKey(refClass = T1.class)//表示字段为与T1所对应表的逻辑外键，在使用 inner join查询时的关联条件，例如where table_1.user_id=table_2.user_id
	@Column("user_id")
	private int userId;
	
	private T1 t1;//如果定义了RefKey，那么必须有一个T1.class的field，默认的写法，开头字母小写的命名方式
	public void setT1(T1 t1);
	public T1 getT1();
	
	@column
	private String name;
	setter...
	getter...
}
````

### 3 在代码中使用
通过spring注入等方式获得
halo.query.Query query对象

#### insert
````java
query.insert(object)；
query.inserForNumber(object);//返回自增id，mysql id 自增方式，db2数据库使用sequence，请先配置
````

####delete
````java
query.delete(object);
````

#### update
````java
query.update(object);
````

#### 集合查询，不分页
````java
List<T1> list=query.list(T1.class,"where level=?",new Object[]{1});
````

#### 根据id，查询对象
````java
T1 obj=query.objById(T1.class,idValue);
````

#### 获得select count(*)结果
````java
int result = query.count(T1.class, "where name=? and bb=?",Object[]{"akweiwei",1});
````

#### 获得 inner join 方式的 count(*)结果，只支持 inner join
````java
int result = query.count(new Object[]{T1.class,T2.class}, "where table_1.user_id=table_2.user_id and name=? and bb=?",Object[]{"akweiwei",1});
````

#### mysql中集合查询，分页方式
````java
List<T1> list = query.mysqlList(T1.class, "where name=? and bb=? order by id desc",0,6,Object[]{"akweiwei",1});
````

#### mysql中集合查询，分页方式，多表inner join查询。查询的集合类型必须放在T1.class的位置,表的别名为@table中的name,只支持 inner join
````java
List<T1> list = query.mysqlList(new Object[]{T1.class,T2.class}, "where table_1.user_id=table_2.user_id and name=? and bb=? order by table_1.user_id desc",0,6,Object[]{"akweiwei",1});
````

#### 如果这些写法无法满足需求，可以直接使用spring jdbcTemplate的写法
````java
query.getJdbcSupport().insert | list | update | num
````

##如果类继承了 BaseModel并且使用了 @HaloModel，那么就可以使用如下方式来操作数据，此方式可以省掉dao,目前不支持inner join 方式的查询。

###1,在使用BaseModel子类之前必须先调用以下代码
```java
ModelLoader loader = new ModelLoader();
loader.setModelBasePath("test");//BaseModel子类的base目录
try {
	loader.makeModelClass();
}
catch (Exception e) {
	Assert.fail(e.getMessage());
}
```

## insert
```java
T t=new T();
t.setUserId(9);
t.setName("akwei");
t.create();
```

## update
```java
t.update();
```

## delete by id
```java
t.delete();
```

##根据id=8到数据库查询此数据
```java
T t=T.objById(8);
```

##从mysql数据库中查询数据
```java
List<T> list=T.mysqlList("name=?", 0, 5, new Object[] { "akwei" });
```


