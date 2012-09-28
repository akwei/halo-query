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

#增加简化操作
例如:
@Table(name = "user")
@HaloModel
public class User extends BaseModel

User user=new User();
user.setUserId(9);
user.setName("akwei");
user.create();//创建一条新数据保存到数据库

user.update();//更新数据库记录

user.delete();//从数据库删除此记录，根据id

User user=User.objById(8);//根据id=8到数据库查询此数据

List<User> list=User.listMySQL("name=?", 0, 5, new Object[] { "akwei" });//从mysql数据库中查询数据