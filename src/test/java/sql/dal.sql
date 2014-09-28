CREATE SCHEMA `db0` DEFAULT CHARACTER SET utf8 ;
CREATE SCHEMA `db1` DEFAULT CHARACTER SET utf8 ;
CREATE SCHEMA `db0_slave` DEFAULT CHARACTER SET utf8 ;
CREATE SCHEMA `db1_slave` DEFAULT CHARACTER SET utf8 ;
CREATE SCHEMA `db_seq` DEFAULT CHARACTER SET utf8 ;

CREATE TABLE `db_seq`.`user_seq` (
  `seq_id` BIGINT NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`seq_id`))
  ENGINE = MyISAM;

INSERT INTO `db_seq`.`user_seq` VALUES (0);

CREATE TABLE `db0`.`tb_user_0` (
  `userid` BIGINT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`userid`))
  ENGINE = InnoDB;

CREATE TABLE `db1`.`tb_user_1` (
  `userid` BIGINT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`userid`))
  ENGINE = InnoDB;

CREATE TABLE `db0_slave`.`tb_user_0` (
  `userid` BIGINT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`userid`))
  ENGINE = InnoDB;

CREATE TABLE `db1_slave`.`tb_user_1` (
  `userid` BIGINT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`userid`))
  ENGINE = InnoDB;
