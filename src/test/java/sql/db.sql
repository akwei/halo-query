CREATE DATABASE `querytest` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `querytest`;

CREATE TABLE `member` (
  `memberuserid` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `nick`         VARCHAR(45)         NOT NULL,
  `groupid`      BIGINT(20) UNSIGNED NOT NULL,
  `userid`       BIGINT(20) UNSIGNED NOT NULL,
  PRIMARY KEY (`memberuserid`) USING BTREE
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8;

CREATE TABLE `order_item` (
  `orderid` INT(10) UNSIGNED NOT NULL,
  `itemid`  INT(10) UNSIGNED NOT NULL,
  `status`  INT(10) UNSIGNED NOT NULL
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8;

CREATE TABLE `role` (
  `role_id`     INT(11)     NOT NULL AUTO_INCREMENT,
  `role_name`   VARCHAR(45) NOT NULL,
  `role_desc`   VARCHAR(45) NOT NULL,
  `parent_id`   INT(11)     NOT NULL,
  `hierarchy`   VARCHAR(45) NOT NULL,
  `leaf_flag`   INT(11)     NOT NULL,
  `creater_id`  INT(11)     NOT NULL,
  `create_time` DATETIME    NOT NULL,
  `descr`       VARCHAR(45) NOT NULL,
  PRIMARY KEY (`role_id`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8;

CREATE TABLE `store` (
  `store_id`    INT(11)    NOT NULL,
  `merchant_id` INT(11)    NOT NULL,
  `create_time` BIGINT(20) NOT NULL,
  PRIMARY KEY (`store_id`, `merchant_id`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8;

CREATE TABLE `testuser` (
  `userid`     BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `nick`       VARCHAR(45)         NOT NULL,
  `createtime` DATETIME            NOT NULL,
  `money`      DOUBLE              NOT NULL,
  `purchase`   DOUBLE              NOT NULL,
  `gender`     TINYINT(1) UNSIGNED NOT NULL,
  PRIMARY KEY (`userid`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8;

CREATE TABLE `testuser00` (
  `userid`     BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `nick`       VARCHAR(45)         NOT NULL,
  `createtime` DATETIME            NOT NULL,
  `money`      DOUBLE              NOT NULL,
  `purchase`   DOUBLE              NOT NULL,
  `gender`     TINYINT(1) UNSIGNED NOT NULL,
  PRIMARY KEY (`userid`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8;

CREATE TABLE `user` (
  `userid`     BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `nick`       VARCHAR(45)         NOT NULL,
  `sex`        INT(10) UNSIGNED             DEFAULT NULL,
  `addr`       VARCHAR(300)        NOT NULL,
  `intro`      VARCHAR(300)        NOT NULL,
  `createtime` DATETIME            NOT NULL,
  `uuid`       BIGINT(64) UNSIGNED NOT NULL,
  `uuid2`      DOUBLE                       DEFAULT NULL,
  `uuid3`      DOUBLE              NOT NULL,
  `uuid4`      FLOAT               NOT NULL,
  `uuid5`      FLOAT                        DEFAULT NULL,
  `uuid6`      TINYINT(4)          NOT NULL,
  `uuid7`      TINYINT(4)                   DEFAULT NULL,
  `uuid8`      SMALLINT(6)         NOT NULL,
  `uuid9`      SMALLINT(6)                  DEFAULT NULL,
  `uuid10`     BIGINT(20)                   DEFAULT NULL,
  `uuid11`     INT(11)             NOT NULL,
  `uuid12`     INT(11)                      DEFAULT NULL,
  `usersex`    INT(10) UNSIGNED    NOT NULL,
  `enableflag` TINYINT(1) UNSIGNED NOT NULL,
  PRIMARY KEY (`userid`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8;

CREATE TABLE `user_seq` (
  `seq_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`seq_id`)
)
  ENGINE =MyISAM
  AUTO_INCREMENT =1133
  DEFAULT CHARSET =utf8;
