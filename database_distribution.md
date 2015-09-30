#分布式数据库操作使用说明
###1数据源配置方式更改
```xml
	<bean id="dataSource" class="halo.datasource.DALDataSource">
		<property name="dataSourceMap">
			<map>
				<!-- 配置的第一个数据源为默认使用的数据源，当没有分布式需求的操作时，使用此数据源 -->
				<entry key="ds_mysql"><!-- 真实数据源的key，选择数据源的时候使用 -->
					<bean class="com.mchange.v2.c3p0.ComboPooledDataSource">
						<property name="driverClass" value="com.mysql.jdbc.Driver" />
						<property name="jdbcUrl" value="jdbc:mysql://127.0.0.1:3306/querytest?useUnicode=true&amp;characterEncoding=UTF-8" />
						<property name="user" value="root" />
						<property name="password" value="asdasd" />
						<property name="idleConnectionTestPeriod" value="60" />
						<property name="maxPoolSize" value="10" />
						<property name="initialPoolSize" value="10" />
						<property name="minPoolSize" value="10" />
					</bean>
				</entry>
				<entry key="ds_db2"><!-- 真实数据源的key，选择数据源的时候使用 -->
					<bean class="com.mchange.v2.c3p0.ComboPooledDataSource">
						<property name="driverClass" value="com.ibm.db2.jcc.DB2Driver" />
						<property name="jdbcUrl" value="jdbc:db2://172.17.102.9:50001/develop" />
						<property name="user" value="mobilebe" />
						<property name="password" value="8132430" />
						<property name="idleConnectionTestPeriod" value="60" />
						<property name="maxPoolSize" value="100" />
						<property name="initialPoolSize" value="50" />
						<property name="minPoolSize" value="20" />
					</bean>
				</entry>
			</map>
		</property>
	</bean>
```
###2写数据路由解析器
```java
package test.bean;

import halo.query.dal.DALParser;
import halo.query.dal.ParsedInfo;
import halo.query.mapping.EntityTableInfo;

import java.util.Map;

public class TestUserParser implements DALParser {
	/**
	 * 根据参数，进行解析，返回解析后的信息
	 * 
	 * @param entityTableInfo 参考{@link EntityTableInfo}
	 * @param paramMap 用户通过{@link DALStatus#setParamMap(Map)} 传递的数据，可以为null
	 * @return 参考{@link ParsedInfo}
	 */
	public ParsedInfo parse(EntityTableInfo<?> entityTableInfo,
	        Map<String, Object> paraMap) {
		ParsedInfo info = new ParsedInfo();
		info.setDsKey("ds_mysql");//真实的数据源key(参见xml配置)
		info.setRealTableName(entityTableInfo.getTableName() + "00");//真实的表名称
		return info;
	}
}

```
###3在需要支持分布式操作的实体上添加annotation，设置自定义解析器
```java
//dalParser 为数据路由的分析器，默认为BaseDALParser.class,是不做任何解析的
@Table(name = "testuser", dalParser = TestUserParser.class)
```
###4进行数据操作,如果没有参数设置，可以忽略此操作
每次调用query进行操作之前(注意：是每次)，先设置解析去需要的参数，
```java
Map<String, Object> map = new HashMap<String, Object>();
map.put("param0", 11);
map.put("param1", "hello");
DALStatus.setParamMap(map);
query.list/insert/update/count/delete .....

Map<String, Object> map = new HashMap<String, Object>();
map.put("param2", 119);
map.put("param3", "akwei");
DALStatus.setParamMap(map);
query.list/insert/update/count/delete .....
```
#使用举例
```java

    @Test
    public void example() throws Exception {
        //insert
        TbUser user = new TbUser();
        user.buildUserId();
        user.setName("akwei");
        DALStatus.addParam("userId", user.getUserId());
        query.insertForNumber(user);

        //select
        //查询userId=1的数据，需要设置路由需要的参数
        DALStatus.addParam("userId", user.getUserId());
        TbUser obj = query.objById(TbUser.class, user.getUserId());
        Assert.assertNotNull(obj);

        //手动指定路由位置
        DALInfo dalInfo = new DALInfo();
        dalInfo.setSpecify(true);//表示手动选择数据源
        if (user.getUserId() % 2 == 0) {
            dalInfo.setRealTable(TbUser.class, "tb_user_0");
            dalInfo.setDsKey("db0");
        } else {
            dalInfo.setRealTable(TbUser.class, "tb_user_1");
            dalInfo.setDsKey("db1");
        }
        DALStatus.setDalInfo(dalInfo);//设置指定的路由规则
        obj = query.objById(TbUser.class, user.getUserId());
        Assert.assertNotNull(obj);


        //update / delete
        DALStatus.addParam("userId", user.getUserId());
        user.setName("okok");
        query.update(user);

        DALStatus.addParam("userId", user.getUserId());
        query.deleteById(TbUser.class, new Object[]{user.getUserId()});

        DALStatus.addParam("userId", user.getUserId());
        query.delete(user);
    }
    
```

