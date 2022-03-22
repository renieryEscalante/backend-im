# backend-im

CREATE SCHEMA IF NOT EXISTS `marvel_db` DEFAULT CHARACTER SET utf8 ;
USE `marvel_db` ;

-- -----------------------------------------------------
-- Table `marvel_db`.`comics`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `marvel_db`.`comics` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `comic_code` BIGINT NULL DEFAULT NULL,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UK_gbt5294rldtgivxs8ge2ej5y6` (`comic_code` ASC) VISIBLE);


-- -----------------------------------------------------
-- Table `marvel_db`.`characters`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `marvel_db`.`characters` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `character_code` BIGINT NULL DEFAULT NULL,
  `description` TEXT NULL DEFAULT NULL,
  `image` VARCHAR(255) NULL DEFAULT NULL,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UK_n8297dpwmna4yra350yeit8id` (`character_code` ASC) VISIBLE);


-- -----------------------------------------------------
-- Table `marvel_db`.`character_comics`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `marvel_db`.`character_comics` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `character_id` BIGINT NULL DEFAULT NULL,
  `comic_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `FKphjddndvnx0evft332t9cku6b` (`character_id` ASC) VISIBLE,
  INDEX `FKi6t4i7fgos99mcvultfu7vgof` (`comic_id` ASC) VISIBLE,
  CONSTRAINT `FKi6t4i7fgos99mcvultfu7vgof`
    FOREIGN KEY (`comic_id`)
    REFERENCES `marvel_db`.`comics` (`id`),
  CONSTRAINT `FKphjddndvnx0evft332t9cku6b`
    FOREIGN KEY (`character_id`)
    REFERENCES `marvel_db`.`characters` (`id`))


-- -----------------------------------------------------
-- Table `marvel_db`.`series`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `marvel_db`.`series` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  `serie_code` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UK_4d53n0mkjw7fmyi6oh5coy07b` (`serie_code` ASC) VISIBLE);


-- -----------------------------------------------------
-- Table `marvel_db`.`character_series`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `marvel_db`.`character_series` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `character_id` BIGINT NULL DEFAULT NULL,
  `serie_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK1eksldnhcu3nmm1fe4j0q38pr` (`character_id` ASC) VISIBLE,
  INDEX `FKp466i5lnuxenf6x2f66himn6m` (`serie_id` ASC) VISIBLE,
  CONSTRAINT `FK1eksldnhcu3nmm1fe4j0q38pr`
    FOREIGN KEY (`character_id`)
    REFERENCES `marvel_db`.`characters` (`id`),
  CONSTRAINT `FKp466i5lnuxenf6x2f66himn6m`
    FOREIGN KEY (`serie_id`)
    REFERENCES `marvel_db`.`series` (`id`))


-- -----------------------------------------------------
-- Table `marvel_db`.`story_types`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `marvel_db`.`story_types` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UK_he7te93ys11eg2u5qckya4rxf` (`name` ASC) VISIBLE);


-- -----------------------------------------------------
-- Table `marvel_db`.`stories`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `marvel_db`.`stories` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  `story_code` BIGINT NULL DEFAULT NULL,
  `type_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UK_gw4vn84l9787tsu8xlfj9ajnv` (`story_code` ASC) VISIBLE,
  INDEX `FK5c088pauad91hqrk64bgp56w1` (`type_id` ASC) VISIBLE,
  CONSTRAINT `FK5c088pauad91hqrk64bgp56w1`
    FOREIGN KEY (`type_id`)
    REFERENCES `marvel_db`.`story_types` (`id`))


-- -----------------------------------------------------
-- Table `marvel_db`.`character_stories`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `marvel_db`.`character_stories` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `character_id` BIGINT NULL DEFAULT NULL,
  `story_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `FKnnujrbu9a8l22hlakn4plm8tb` (`character_id` ASC) VISIBLE,
  INDEX `FKjld1dbnsmsrffqwssomw0ni11` (`story_id` ASC) VISIBLE,
  CONSTRAINT `FKjld1dbnsmsrffqwssomw0ni11`
    FOREIGN KEY (`story_id`)
    REFERENCES `marvel_db`.`stories` (`id`),
  CONSTRAINT `FKnnujrbu9a8l22hlakn4plm8tb`
    FOREIGN KEY (`character_id`)
    REFERENCES `marvel_db`.`characters` (`id`))


-- -----------------------------------------------------
-- Table `marvel_db`.`creators`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `marvel_db`.`creators` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `creator_code` BIGINT NULL DEFAULT NULL,
  `first_name` VARCHAR(255) NULL DEFAULT NULL,
  `last_name` VARCHAR(255) NULL DEFAULT NULL,
  `middle_name` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))

