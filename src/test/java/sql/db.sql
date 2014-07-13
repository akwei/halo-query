create database if not exists querytest;
ALTER SCHEMA `querytest`  DEFAULT CHARACTER SET utf8 ;

drop table if exists querytest.member;
create table  querytest.member (
  memberuserid bigint(20) unsigned not null auto_increment,
  nick varchar(45) not null,
  groupid bigint(20) unsigned not null,
  userid bigint(20) unsigned not null,
  primary key  using btree (memberuserid)
) engine=innodb  default charset=utf8;


drop table if exists querytest.testuser;
create table  querytest.testuser (
  userid bigint(20) unsigned not null auto_increment,
  nick varchar(45) not null,
  createtime datetime not null,
  money double not null,
  purchase double not null,
  gender tinyint(1) unsigned not null,
  primary key  (userid)
) engine=innodb  default charset=utf8;

drop table if exists querytest.user_seq;
create  table querytest.user_seq (
  seq_id bigint not null auto_increment ,
  primary key (seq_id) 
) engine = myisam default charset = utf8;
insert into querytest.user_seq values(0);

drop table if exists querytest.user;

create table querytest.user (
  userid bigint(20) unsigned not null auto_increment,
  nick varchar(45) not null,
  sex int(10) unsigned default null,
  addr varchar(300) not null,
  intro varchar(300) not null,
  createtime datetime not null,
  uuid bigint(64) unsigned not null,
  uuid2 double default null,
  uuid3 double not null,
  uuid4 float not null,
  uuid5 float default null,
  uuid6 tinyint(4) not null,
  uuid7 tinyint(4) default null,
  uuid8 smallint(6) not null,
  uuid9 smallint(6) default null,
  uuid10 bigint(20) default null,
  uuid11 int(11) not null,
  uuid12 int(11) default null,
  primary key  (userid)
) engine=innodb default charset=utf8;


drop table if exists querytest.testuser00;
create table querytest.testuser00 (
  userid bigint(20) unsigned not null auto_increment,
  nick varchar(45) not null,
  createtime datetime not null,
  money double not null,
  purchase double not null,
  gender tinyint(1) unsigned not null,
  primary key  (userid)
) engine=innodb  default charset=utf8;
