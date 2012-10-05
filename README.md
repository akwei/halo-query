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

###1,在使用BaseModel子类之前必须先调用以下代码
ModelLoader loader = new ModelLoader();
loader.setModelBasePath("test");//BaseModel子类的base目录
try {
	loader.makeModelClass();
}
catch (Exception e) {
	Assert.fail(e.getMessage());
}

###2

##创建一条新数据保存到数据库
User user=new User();
user.setUserId(9);
user.setName("akwei");
user.create();

##更新数据库记录
user.update();

##从数据库删除此记录，根据id
user.delete();

##根据id=8到数据库查询此数据
User user=User.objById(8);

##从mysql数据库中查询数据
List<User> list=User.listMySQL("name=?", 0, 5, new Object[] { "akwei" });

##inner join查询，返回的集合中对象类型必须是join查询的第一个类型
List<Member> list=query.mysqlListMulti(
				new Class[] { Member.class,TestUser.class},
				"where testuser.userid=member.userid and member.userid=? order by member.userid asc",
				0, 1,
				new Object[] { userid }
				);
	

