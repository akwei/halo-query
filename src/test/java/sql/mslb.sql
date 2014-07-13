CREATE SCHEMA `querytest_food` DEFAULT CHARACTER SET utf8 ;

CREATE TABLE `querytest_food`.`food` (
  `food_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `create_time` BIGINT NOT NULL,
  PRIMARY KEY (`food_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

create user 'haloquery'@'%' identified  by 'halo';

grant select , insert , update ,delete on querytest_food.* to 'haloquery'@'%';

flush privileges;

-- 添加 复制账户

  create user 'repl'@'%' identified by 'slavepass';

grant replication slave on *.* to 'repl'@'%';

-- slave server 执行 :

CHANGE MASTER TO MASTER_HOST='172.16.160.131', MASTER_USER='repl',MASTER_PASSWORD='slavepass',MASTER_LOG_FILE='mysql-bin',MASTER_LOG_POS=recorded_log_position;
