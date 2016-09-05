CREATE DATABASE `querytest` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `querytest`;

CREATE TABLE `member` (
  `memberuserid` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `nick` varchar(45) NOT NULL,
  `groupid` bigint(20) unsigned NOT NULL,
  `userid` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`memberuserid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8;

CREATE TABLE `minfo` (
  `tid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `mkey` varchar(45) NOT NULL,
  PRIMARY KEY (`tid`),
  UNIQUE KEY `mkey_UNIQUE` (`mkey`)
) ENGINE=InnoDB AUTO_INCREMENT=93 DEFAULT CHARSET=utf8;

CREATE TABLE `order_item` (
  `orderid` int(10) unsigned NOT NULL,
  `itemid` int(10) unsigned NOT NULL,
  `status` int(10) unsigned NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(45) NOT NULL,
  `role_desc` varchar(45) NOT NULL,
  `parent_id` int(11) NOT NULL,
  `hierarchy` varchar(45) NOT NULL,
  `leaf_flag` int(11) NOT NULL,
  `creater_id` int(11) NOT NULL,
  `create_time` datetime NOT NULL,
  `descr` varchar(45) NOT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=956 DEFAULT CHARSET=utf8;

CREATE TABLE `store` (
  `store_id` int(11) NOT NULL,
  `merchant_id` int(11) NOT NULL,
  `create_time` bigint(20) NOT NULL,
  PRIMARY KEY (`store_id`,`merchant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `testuser` (
  `userid` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `nick` varchar(45) NOT NULL,
  `createtime` datetime NOT NULL,
  `money` double NOT NULL,
  `purchase` double NOT NULL,
  `gender` tinyint(1) unsigned NOT NULL,
  `ver` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB AUTO_INCREMENT=203 DEFAULT CHARSET=utf8;

CREATE TABLE `testuser00` (
  `userid` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `nick` varchar(45) NOT NULL,
  `createtime` datetime NOT NULL,
  `money` double NOT NULL,
  `purchase` double NOT NULL,
  `gender` tinyint(1) unsigned NOT NULL,
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user` (
  `userid` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `nick` varchar(45) NOT NULL,
  `sex` int(10) unsigned DEFAULT NULL,
  `addr` varchar(300) NOT NULL,
  `intro` varchar(300) NOT NULL,
  `createtime` datetime NOT NULL,
  `uuid` bigint(64) unsigned NOT NULL,
  `uuid2` double DEFAULT NULL,
  `uuid3` double NOT NULL,
  `uuid4` float NOT NULL,
  `uuid5` float DEFAULT NULL,
  `uuid6` tinyint(4) NOT NULL,
  `uuid7` tinyint(4) DEFAULT NULL,
  `uuid8` smallint(6) NOT NULL,
  `uuid9` smallint(6) DEFAULT NULL,
  `uuid10` bigint(20) DEFAULT NULL,
  `uuid11` int(11) NOT NULL,
  `uuid12` int(11) DEFAULT NULL,
  `usersex` int(10) unsigned NOT NULL,
  `enableflag` tinyint(1) unsigned NOT NULL,
  `ver` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB AUTO_INCREMENT=3341 DEFAULT CHARSET=utf8;

CREATE TABLE `user_seq` (
  `seq_id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`seq_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3312 DEFAULT CHARSET=utf8;
