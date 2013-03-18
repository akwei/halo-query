

#一个轻量级orm框架，自动组装ResultSet结果集。
##底层使用的是spring jdbcTemplate
##目前支持的数据库为mysql,db2
##mysql,db2测试通过
##支持分分布式数据库操作，支持单库jdbc事务，支持读写分离(请看database_distribution.md)

##需要注意的是，查询使用的sql，所有被查询的字段都具有别名，例如 
###table=user tableAlias=user_ column=userid alias=user_userid
###table=db.user tableAlias=db_user_ column=userid alias=db_user_userid

使用方式有2中
##1 [Model操作方式](https://github.com/akwei/halo-query/blob/master/README_model.md "Model操作方式")
##2 [query操作方式](https://github.com/akwei/halo-query/blob/master/README_normal.md "query操作方式")

##如果你的project是web，[如何配置web.xml](https://github.com/akwei/halo-query/blob/master/README_web.md "如何配置web.xml")