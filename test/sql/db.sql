create database if not exists querytest;

DROP TABLE IF EXISTS querytest.member;
CREATE TABLE  querytest.member (
  memberuserid bigint(20) unsigned NOT NULL auto_increment,
  nick varchar(45) NOT NULL,
  groupid bigint(20) unsigned NOT NULL,
  userid bigint(20) unsigned NOT NULL,
  PRIMARY KEY  USING BTREE (memberuserid)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS querytest.testuser;
CREATE TABLE  querytest.testuser (
  userid bigint(20) unsigned NOT NULL auto_increment,
  nick varchar(45) NOT NULL,
  createtime datetime NOT NULL,
  money double NOT NULL,
  purchase double NOT NULL,
  gender tinyint(1) unsigned NOT NULL,
  PRIMARY KEY  (userid)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;


drop table if exists querytest.user;

CREATE TABLE querytest.user (
  userid bigint(20) unsigned NOT NULL auto_increment,
  nick varchar(45) NOT NULL,
  sex int(10) unsigned default NULL,
  addr varchar(300) NOT NULL,
  intro varchar(300) NOT NULL,
  createtime datetime NOT NULL,
  uuid bigint(64) unsigned NOT NULL,
  uuid2 double default NULL,
  uuid3 double NOT NULL,
  uuid4 float NOT NULL,
  uuid5 float default NULL,
  uuid6 tinyint(4) NOT NULL,
  uuid7 tinyint(4) default NULL,
  uuid8 smallint(6) NOT NULL,
  uuid9 smallint(6) default NULL,
  uuid10 bigint(20) default NULL,
  uuid11 int(11) NOT NULL,
  uuid12 int(11) default NULL,
  PRIMARY KEY  (userid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



