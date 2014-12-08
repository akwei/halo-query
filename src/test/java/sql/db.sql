CREATE DATABASE IF NOT EXISTS querytest;
ALTER SCHEMA `querytest`
DEFAULT CHARACTER SET utf8;

DROP TABLE IF EXISTS querytest.member;
CREATE TABLE querytest.member (
  memberuserid BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  nick         VARCHAR(45)         NOT NULL,
  groupid      BIGINT(20) UNSIGNED NOT NULL,
  userid       BIGINT(20) UNSIGNED NOT NULL,
  PRIMARY KEY USING BTREE (memberuserid)
)
  ENGINE =innodb
  DEFAULT CHARSET =utf8;


DROP TABLE IF EXISTS querytest.testuser;
CREATE TABLE querytest.testuser (
  userid     BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  nick       VARCHAR(45)         NOT NULL,
  createtime DATETIME            NOT NULL,
  money      DOUBLE              NOT NULL,
  purchase   DOUBLE              NOT NULL,
  gender     TINYINT(1) UNSIGNED NOT NULL,
  PRIMARY KEY (userid)
)
  ENGINE =innodb
  DEFAULT CHARSET =utf8;

DROP TABLE IF EXISTS querytest.user_seq;
CREATE TABLE querytest.user_seq (
  seq_id BIGINT NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (seq_id)
)
  ENGINE = myisam
  DEFAULT CHARSET = utf8;
INSERT INTO querytest.user_seq VALUES (0);

DROP TABLE IF EXISTS querytest.user;

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
  PRIMARY KEY (`userid`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8;


DROP TABLE IF EXISTS querytest.testuser00;
CREATE TABLE querytest.testuser00 (
  userid     BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  nick       VARCHAR(45)         NOT NULL,
  createtime DATETIME            NOT NULL,
  money      DOUBLE              NOT NULL,
  purchase   DOUBLE              NOT NULL,
  gender     TINYINT(1) UNSIGNED NOT NULL,
  PRIMARY KEY (userid)
)
  ENGINE =innodb
  DEFAULT CHARSET =utf8;

DROP TABLE IF EXISTS querytest.store;

CREATE TABLE querytest.`store` (
  `store_id`    INT(11)    NOT NULL,
  `merchant_id` INT(11)    NOT NULL,
  `create_time` BIGINT(20) NOT NULL,
  PRIMARY KEY (`store_id`, `merchant_id`)
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
