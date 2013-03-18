#Model操作方式，目前Model操作不支持join操作

##Step 1 在spring中配置
````xml
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
````

##Step 2
````java
//代码只需要在启动的时候调用一次，在使用单元测试的时候注意，不要有多次调用，否则会报错误信息
ModelLoader loader = new ModelLoader();
loader.setModelBasePath("test");//参数为扫描BaseModel子类的所在位置的目录，可以设置最大的目录，也可以设置Model的目录,例如: test/model
loader.makeModelClass();
````
##Step 3 创建与数据库表对应的实体类，表必须有唯一主键，不支持联合主键
```` java
@Table(name = "table_1")
class T extends BaseModel{
	@Id//表示为数据表主键,主键目前只支持String long int
	@Column("user_id") //注明为数据库对应user_id字段
	private int userId;
	@column
	private String name;
	setter...
	getter...
}


##Step 4
###insert
````java
T t=new T();
t.setUserId(9);
t.setName("akwei");
t.create();
````

###update
````java
t.update();
````

###delete by id
````java
t.delete();
````

##根据id=8到数据库查询此数据
````java
T t=T.objById(8);
````

###从mysql数据库中查询数据
````java
List<T> list=T.mysqlList("name=?", 0, 5, new Object[] { "akwei" });
````


##Step 5 如果你的project是web(不是必须)
###加载一个ModelListener，注意:ModelListener必须第一个加载
````xml
<context-param>
	<!-- BaseModel子类的base目录 -->
	<param-name>modelBasePath</param-name>
	<param-value>com/yibao/posvr/model</param-value>
</context-param>
<listener>
	<listener-class>halo.query.model.ModelListener</listener-class>
</listener>
````
###把Step 2的代码删除，用上面web.xml配置的方式来替换
````java
ModelLoader loader = new ModelLoader();
loader.setModelBasePath("test");
loader.makeModelClass();
````
