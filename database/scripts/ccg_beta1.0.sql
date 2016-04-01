-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema ccgcontent
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema ccgcontent
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `ccgcontent` DEFAULT CHARACTER SET utf8 ;
USE `ccgcontent` ;

-- -----------------------------------------------------
-- Table `ccgcontent`.`ccgcontent`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ccgcontent`.`ccgcontent` (
  `contentID` INT(11) NOT NULL AUTO_INCREMENT,
  `contentTitle` VARCHAR(200) NULL DEFAULT NULL,
  `length` INT(11) NULL DEFAULT NULL,
  `filename` VARCHAR(200) NULL DEFAULT NULL,
  `url` VARCHAR(200) NULL DEFAULT NULL,
  `metatype` VARCHAR(50) NULL DEFAULT NULL,
  `content` MEDIUMTEXT NULL DEFAULT NULL,
  PRIMARY KEY (`contentID`),
  UNIQUE INDEX `contentID_UNIQUE` (`contentID` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8
COMMENT = 'content Storage';


-- -----------------------------------------------------
-- Table `ccgcontent`.`ccgarticle`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ccgcontent`.`ccgarticle` (
  `articleID` INT(11) NOT NULL AUTO_INCREMENT,
  `domain` VARCHAR(100) NULL DEFAULT NULL,
  `subdomain` VARCHAR(100) NULL DEFAULT NULL,
  `contentID` INT(11) NULL DEFAULT NULL,
  `title` VARCHAR(200) NULL DEFAULT NULL,
  `startposi` INT(11) NULL DEFAULT NULL,
  `endposi` INT(11) NULL DEFAULT NULL,
  `articleType` VARCHAR(50) NULL DEFAULT NULL,
  `rfpReference` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`articleID`),
  UNIQUE INDEX `articleID_UNIQUE` (`articleID` ASC),
  INDEX `fk_article_content_idx` (`contentID` ASC),
  CONSTRAINT `fk_article_content`
    FOREIGN KEY (`contentID`)
    REFERENCES `ccgcontent`.`ccgcontent` (`contentID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = utf8
COMMENT = 'article';


-- -----------------------------------------------------
-- Table `ccgcontent`.`ccgarticle1gram`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ccgcontent`.`ccgarticle1gram` (
  `articleID` INT(11) NOT NULL,
  `seq` INT(11) NOT NULL,
  `phrase` VARCHAR(200) NULL DEFAULT NULL,
  `startposi` INT(11) NULL DEFAULT NULL,
  `endposi` INT(11) NULL DEFAULT NULL,
  `categoryID` INT(11) NULL DEFAULT NULL,
  `subcategoryID` INT(11) NULL DEFAULT NULL,
  `origintxt` VARCHAR(200) NULL DEFAULT NULL,
  `istitle` CHAR(1) NULL DEFAULT NULL,
  `ismodified` CHAR(1) NULL DEFAULT NULL,
  `modifyDistance` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`articleID`, `seq`),
  CONSTRAINT `fk_1gram_article`
    FOREIGN KEY (`articleID`)
    REFERENCES `ccgcontent`.`ccgarticle` (`articleID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `ccgcontent`.`ccgarticlemetadata`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ccgcontent`.`ccgarticlemetadata` (
  `articleID` INT(11) NOT NULL,
  `type` VARCHAR(200) NULL DEFAULT NULL,
  `title` VARCHAR(200) NULL DEFAULT NULL,
  `company` VARCHAR(200) NULL DEFAULT NULL,
  `author` VARCHAR(200) NULL DEFAULT NULL,
  `createdTS` DATE NULL DEFAULT NULL,
  `lastupdateTS` DATE NULL DEFAULT NULL,
  `acceptstatus` VARCHAR(50) NULL DEFAULT NULL,
  `praisalscore` FLOAT NULL DEFAULT NULL,
  PRIMARY KEY (`articleID`),
  CONSTRAINT `fk_meta_article`
    FOREIGN KEY (`articleID`)
    REFERENCES `ccgcontent`.`ccgarticle` (`articleID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `ccgcontent`.`ccgcategory`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ccgcontent`.`ccgcategory` (
  `categoryID` INT(11) NOT NULL AUTO_INCREMENT,
  `articleID` INT(11) NULL DEFAULT NULL,
  `type` VARCHAR(200) NULL DEFAULT NULL,
  `startposi` INT(11) NULL DEFAULT NULL,
  `endposi` INT(11) NULL DEFAULT NULL,
  `categorytitle` VARCHAR(200) NULL DEFAULT NULL,
  `categorycontent` MEDIUMTEXT NULL DEFAULT NULL,
  `categoryseq` INT(11) NULL DEFAULT NULL,
  `categoryref` VARCHAR(100) NULL DEFAULT NULL,
  PRIMARY KEY (`categoryID`),
  UNIQUE INDEX `cateogryID_UNIQUE` (`categoryID` ASC),
  INDEX `fk_category_article_idx` (`articleID` ASC),
  CONSTRAINT `fk_category_article`
    FOREIGN KEY (`articleID`)
    REFERENCES `ccgcontent`.`ccgarticle` (`articleID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'main category';


-- -----------------------------------------------------
-- Table `ccgcontent`.`ccgmodel`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ccgcontent`.`ccgmodel` (
  `modelID` INT(11) NOT NULL AUTO_INCREMENT,
  `modelname` VARCHAR(200) NOT NULL,
  `domain` VARCHAR(100) NOT NULL,
  `subdomain` VARCHAR(100) NOT NULL,
  `modeltype` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`modelID`),
  UNIQUE INDEX `modelID_UNIQUE` (`modelID` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `ccgcontent`.`ccgsubcategory`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ccgcontent`.`ccgsubcategory` (
  `subcategoryID` INT(11) NOT NULL AUTO_INCREMENT,
  `articleID` INT(11) NULL DEFAULT NULL,
  `categoryID` INT(11) NULL DEFAULT NULL,
  `type` VARCHAR(45) NULL DEFAULT NULL,
  `subcategorytitle` VARCHAR(200) NULL DEFAULT NULL,
  `subcategorycontent` MEDIUMTEXT NULL DEFAULT NULL,
  `startposi` INT(11) NULL DEFAULT NULL,
  `endposi` INT(11) NULL DEFAULT NULL,
  `createdTS` DATE NULL DEFAULT NULL,
  `lastupdateTS` DATE NULL DEFAULT NULL,
  PRIMARY KEY (`subcategoryID`),
  INDEX `fk_subcategory_category_idx` (`categoryID` ASC),
  CONSTRAINT `fk_subcategory_category`
    FOREIGN KEY (`categoryID`)
    REFERENCES `ccgcontent`.`ccgcategory` (`categoryID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `ccgcontent`.`hibernate_sequence`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ccgcontent`.`hibernate_sequence` (
  `next_val` BIGINT(20) NULL DEFAULT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
