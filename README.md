#一个轻量级orm框架，自动组装ResultSet结果集。
###简单的使用insert update delete select操作。

##insert
query.insert(object)；

##delete
query.delete(object);

##update
query.update(object);

##select
List<Object> list=query.list(Object.class,"where level=?",new Object[]{1});

Object obj=query.objById(Object.class,idValue);

#例子
##在test下有sql脚本，单元测试使用例子