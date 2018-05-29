-- MySQL Script generated by MySQL Workbench
-- Mon May 28 11:50:16 2018
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema project_management
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema project_management
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `project_management` DEFAULT CHARACTER SET latin1 ;
USE `project_management` ;

-- -----------------------------------------------------
-- Table `project_management`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `project_management`.`user` (
  `user_id` INT(11) NOT NULL AUTO_INCREMENT,
  `answer` VARCHAR(255) NULL DEFAULT NULL,
  `email` VARCHAR(255) NULL DEFAULT NULL,
  `function` VARCHAR(255) NULL DEFAULT NULL,
  `id_number` VARCHAR(255) NULL DEFAULT NULL,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  `password` VARCHAR(255) NULL DEFAULT NULL,
  `phone` VARCHAR(255) NULL DEFAULT NULL,
  `question` VARCHAR(255) NULL DEFAULT NULL,
  `system_user_state_active` BIT(1) NOT NULL,
  `user_profile` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 12
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `project_management`.`address`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `project_management`.`address` (
  `address_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `city` VARCHAR(255) NULL DEFAULT NULL,
  `country` VARCHAR(255) NULL DEFAULT NULL,
  `district` VARCHAR(255) NULL DEFAULT NULL,
  `street` VARCHAR(255) NULL DEFAULT NULL,
  `zip_code` VARCHAR(255) NULL DEFAULT NULL,
  `user_id` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`address_id`),
  INDEX `FKda8tuywtf0gb6sedwk7la1pgi` (`user_id` ASC),
  CONSTRAINT `FKda8tuywtf0gb6sedwk7la1pgi`
    FOREIGN KEY (`user_id`)
    REFERENCES `project_management`.`user` (`user_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 11
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `project_management`.`hibernate_sequence`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `project_management`.`hibernate_sequence` (
  `next_val` BIGINT(20) NULL DEFAULT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `project_management`.`project`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `project_management`.`project` (
  `project_id` INT(11) NOT NULL AUTO_INCREMENT,
  `available_calculation_methods` VARCHAR(255) NULL DEFAULT NULL,
  `budget` DOUBLE NOT NULL,
  `calculation_method` VARCHAR(255) NULL DEFAULT NULL,
  `description` VARCHAR(255) NULL DEFAULT NULL,
  `effort_unit` VARCHAR(255) NULL DEFAULT NULL,
  `finishdate` DATETIME NULL DEFAULT NULL,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  `project_cost` DOUBLE NOT NULL,
  `startdate` DATETIME NULL DEFAULT NULL,
  `status` INT(11) NOT NULL,
  `project_manager_user_id` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`project_id`),
  INDEX `FKmotjrvvtxmku97rw0ps16wcb4` (`project_manager_user_id` ASC),
  CONSTRAINT `FKmotjrvvtxmku97rw0ps16wcb4`
    FOREIGN KEY (`project_manager_user_id`)
    REFERENCES `project_management`.`user` (`user_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `project_management`.`project_collaborator`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `project_management`.`project_collaborator` (
  `project_collaborator_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `cost_per_effort` DOUBLE NOT NULL,
  `finish_date` DATETIME NULL DEFAULT NULL,
  `start_date` DATETIME NULL DEFAULT NULL,
  `status` BIT(1) NOT NULL,
  `user_id` INT(11) NULL DEFAULT NULL,
  `project_id` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`project_collaborator_id`),
  INDEX `FK204ox7kdaods57jda1ljy8von` (`user_id` ASC),
  INDEX `FKh2imufly44v42x4mjft0lsqau` (`project_id` ASC),
  CONSTRAINT `FK204ox7kdaods57jda1ljy8von`
    FOREIGN KEY (`user_id`)
    REFERENCES `project_management`.`user` (`user_id`),
  CONSTRAINT `FKh2imufly44v42x4mjft0lsqau`
    FOREIGN KEY (`project_id`)
    REFERENCES `project_management`.`project` (`project_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 7
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `project_management`.`task`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `project_management`.`task` (
  `db_task_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `cancel_date` DATETIME NULL DEFAULT NULL,
  `creation_date` DATETIME NULL DEFAULT NULL,
  `current_state` VARCHAR(255) NULL DEFAULT NULL,
  `deadline_interval` INT(11) NULL DEFAULT NULL,
  `description` VARCHAR(255) NULL DEFAULT NULL,
  `estimated_task_effort` DOUBLE NOT NULL,
  `estimated_task_start_date` DATETIME NULL DEFAULT NULL,
  `finish_date` DATETIME NULL DEFAULT NULL,
  `start_date` DATETIME NULL DEFAULT NULL,
  `start_date_interval` INT(11) NULL DEFAULT NULL,
  `task_budget` DOUBLE NOT NULL,
  `task_deadline` DATETIME NULL DEFAULT NULL,
  `taskid` VARCHAR(255) NULL DEFAULT NULL,
  `project_id` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`db_task_id`),
  INDEX `FKk8qrwowg31kx7hp93sru1pdqa` (`project_id` ASC),
  CONSTRAINT `FKk8qrwowg31kx7hp93sru1pdqa`
    FOREIGN KEY (`project_id`)
    REFERENCES `project_management`.`project` (`project_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 6
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `project_management`.`task_collaborator`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `project_management`.`task_collaborator` (
  `task_collab_db_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `finish_date` DATETIME NULL DEFAULT NULL,
  `start_date` DATETIME NULL DEFAULT NULL,
  `status` BIT(1) NOT NULL,
  `project_collaborator_id` BIGINT(20) NULL DEFAULT NULL,
  `task_id` BIGINT(20) NULL DEFAULT NULL,
  PRIMARY KEY (`task_collab_db_id`),
  INDEX `FK8jbq6xexfcofl0p0fsk181wcd` (`project_collaborator_id` ASC),
  INDEX `FKrj9hxbcoki9wkoc5yvwscbqeo` (`task_id` ASC),
  CONSTRAINT `FK8jbq6xexfcofl0p0fsk181wcd`
    FOREIGN KEY (`project_collaborator_id`)
    REFERENCES `project_management`.`project_collaborator` (`project_collaborator_id`),
  CONSTRAINT `FKrj9hxbcoki9wkoc5yvwscbqeo`
    FOREIGN KEY (`task_id`)
    REFERENCES `project_management`.`task` (`db_task_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 9
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `project_management`.`report`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `project_management`.`report` (
  `report_db_id` INT(11) NOT NULL AUTO_INCREMENT,
  `cost` DOUBLE NOT NULL,
  `date_of_update` DATETIME NULL DEFAULT NULL,
  `first_date_of_report` DATETIME NULL DEFAULT NULL,
  `reported_time` DOUBLE NOT NULL,
  `task_id` BIGINT(20) NULL DEFAULT NULL,
  `task_collaborator_id` BIGINT(20) NULL DEFAULT NULL,
  PRIMARY KEY (`report_db_id`),
  INDEX `FKshwly9i28i3bbp9r5caprb0w2` (`task_id` ASC),
  INDEX `FK7v3q38xrtjm4vxget6hggsc3w` (`task_collaborator_id` ASC),
  CONSTRAINT `FK7v3q38xrtjm4vxget6hggsc3w`
    FOREIGN KEY (`task_collaborator_id`)
    REFERENCES `project_management`.`task_collaborator` (`task_collab_db_id`),
  CONSTRAINT `FKshwly9i28i3bbp9r5caprb0w2`
    FOREIGN KEY (`task_id`)
    REFERENCES `project_management`.`task` (`db_task_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 12
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `project_management`.`task_task_dependency`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `project_management`.`task_task_dependency` (
  `task_db_task_id` BIGINT(20) NOT NULL,
  `task_dependency_db_task_id` BIGINT(20) NOT NULL,
  INDEX `FK1wr37edp4ny4f3xar0pf8x6yh` (`task_dependency_db_task_id` ASC),
  INDEX `FKl9d5gichumttc9vwexowystbn` (`task_db_task_id` ASC),
  CONSTRAINT `FK1wr37edp4ny4f3xar0pf8x6yh`
    FOREIGN KEY (`task_dependency_db_task_id`)
    REFERENCES `project_management`.`task` (`db_task_id`),
  CONSTRAINT `FKl9d5gichumttc9vwexowystbn`
    FOREIGN KEY (`task_db_task_id`)
    REFERENCES `project_management`.`task` (`db_task_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `project_management`.`task_team_request`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `project_management`.`task_team_request` (
  `task_request_db_id` INT(11) NOT NULL,
  `approval_date` DATETIME NULL DEFAULT NULL,
  `reject_date` DATETIME NULL DEFAULT NULL,
  `type` VARCHAR(255) NULL DEFAULT NULL,
  `project_collaborator_id` BIGINT(20) NULL DEFAULT NULL,
  `task_id` BIGINT(20) NULL DEFAULT NULL,
  PRIMARY KEY (`task_request_db_id`),
  INDEX `FKjea23ixdb2xvrgkfcvu6mq6i9` (`project_collaborator_id` ASC),
  INDEX `FK201bveir1npmltjm80k4mdo2j` (`task_id` ASC),
  CONSTRAINT `FK201bveir1npmltjm80k4mdo2j`
    FOREIGN KEY (`task_id`)
    REFERENCES `project_management`.`task` (`db_task_id`),
  CONSTRAINT `FKjea23ixdb2xvrgkfcvu6mq6i9`
    FOREIGN KEY (`project_collaborator_id`)
    REFERENCES `project_management`.`project_collaborator` (`project_collaborator_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;