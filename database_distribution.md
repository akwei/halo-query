#分布式数据库操作使用说明
###1数据源配置方式更改
```xml
    <bean id="dataSource"
          class="halo.query.dal.HaloDALC3p0PropertiesDataSource"
          destroy-method="destory">
        <property name="name" value="dal2"/>
    </bean>
```
```
dal2.properties
#default表示默认连接的数据源，必须有
#global.开头表示全局设置，每个数据源可以使用全局设置也可以独立使用自己的设置

default=db0
global.maxPoolSize=10
global.idleConnectionTestPeriod=60
global.minPoolSize=10
global.initialPoolSize=10
global.driverClass=com.mysql.jdbc.Driver
global.user=root
global.password=asdasd
global.jdbcUrl=jdbc:mysql://{0}?useUnicode=true&characterEncoding=UTF-8

db_seq={\
        "jdbcUrl" : "jdbc:mysql://127.0.0.1:3306/db_seq?useUnicode=true&characterEncoding=UTF-8",\
        "password" : "asdasd",\
        "maxPoolSize" : 10,\
        "idleConnectionTestPeriod" : 60,\
        "minPoolSize" : 10,\
        "initialPoolSize" : 10,\
        "driverClass" : "com.mysql.jdbc.Driver",\
        "user" : "root",\
        "ds_slave":["db0_slave"]\
      }

#ds_slave 表示此数据源可以使用的slave数据源
db0={"url":"127.0.0.1:3306/db0", "ds_slave":["db0_slave"]}
db0_slave={"url":"127.0.0.1:3306/db0_slave"}

db1={\
        "jdbcUrl" : "jdbc:mysql://127.0.0.1:3306/db1?useUnicode=true&characterEncoding=UTF-8",\
        "password" : "asdasd",\
        "maxPoolSize" : 10,\
        "idleConnectionTestPeriod" : 60,\
        "minPoolSize" : 10,\
        "initialPoolSize" : 10,\
        "driverClass" : "com.mysql.jdbc.Driver",\
        "user" : "root",\
        "ds_slave":["db1_slave"]\
      }
db1_slave={\
        "jdbcUrl" : "jdbc:mysql://127.0.0.1:3306/db1_slave?useUnicode=true&characterEncoding=UTF-8",\
        "password" : "asdasd",\
        "maxPoolSize" : 10,\
        "idleConnectionTestPeriod" : 60,\
        "minPoolSize" : 10,\
        "initialPoolSize" : 10,\
        "driverClass" : "com.mysql.jdbc.Driver",\
        "user" : "root"\
      }


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

