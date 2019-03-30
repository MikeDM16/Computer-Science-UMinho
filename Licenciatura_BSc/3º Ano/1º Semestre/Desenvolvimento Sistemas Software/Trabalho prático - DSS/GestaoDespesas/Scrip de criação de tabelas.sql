-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema GestaoDespesas
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `GestaoDespesas` ;

-- -----------------------------------------------------
-- Schema GestaoDespesas
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `GestaoDespesas` DEFAULT CHARACTER SET utf8 ;
USE `GestaoDespesas` ;

-- -----------------------------------------------------
-- Table `GestaoDespesas`.`Conta`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GestaoDespesas`.`Conta` ;

CREATE TABLE IF NOT EXISTS `GestaoDespesas`.`Conta` (
  `idConta` INT NOT NULL AUTO_INCREMENT,
  `saldo` DOUBLE NOT NULL,
  PRIMARY KEY (`idConta`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `GestaoDespesas`.`Utilizador`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GestaoDespesas`.`Utilizador` ;

CREATE TABLE IF NOT EXISTS `GestaoDespesas`.`Utilizador` (
  `username` VARCHAR(45) NOT NULL,
  `nome` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`username`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `GestaoDespesas`.`Inquilino`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GestaoDespesas`.`Inquilino` ;

CREATE TABLE IF NOT EXISTS `GestaoDespesas`.`Inquilino` (
  `dataEntrada` DATE NOT NULL,
  `dataSaida` DATE NULL,
  `conta` INT NOT NULL,
  `utilizador` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`utilizador`),
  INDEX `fk_Inquilino_Utilizador1_idx` (`utilizador` ASC),
  CONSTRAINT `fk_Inquilino_Conta`
    FOREIGN KEY (`conta`)
    REFERENCES `GestaoDespesas`.`Conta` (`idConta`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Inquilino_Utilizador1`
    FOREIGN KEY (`utilizador`)
    REFERENCES `GestaoDespesas`.`Utilizador` (`username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `GestaoDespesas`.`Administrador`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GestaoDespesas`.`Administrador` ;

CREATE TABLE IF NOT EXISTS `GestaoDespesas`.`Administrador` (
  `utilizador` VARCHAR(45) NOT NULL,
  `estado` TINYINT(1) NOT NULL,
  PRIMARY KEY (`utilizador`),
  INDEX `fk_Administrador_Utilizador1_idx` (`utilizador` ASC),
  CONSTRAINT `fk_Administrador_Utilizador1`
    FOREIGN KEY (`utilizador`)
    REFERENCES `GestaoDespesas`.`Utilizador` (`username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `GestaoDespesas`.`Despesa`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GestaoDespesas`.`Despesa` ;

CREATE TABLE IF NOT EXISTS `GestaoDespesas`.`Despesa` (
  `ref` VARCHAR(45) NOT NULL,
  `dataLimite` DATE NOT NULL,
  `dataEmissao` DATE NOT NULL,
  `preco` DOUBLE NOT NULL,
  `estado` TINYINT(1) NOT NULL,
  PRIMARY KEY (`ref`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `GestaoDespesas`.`Extraordinária`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GestaoDespesas`.`Extraordinária` ;

CREATE TABLE IF NOT EXISTS `GestaoDespesas`.`Extraordinária` (
  `designacao` VARCHAR(65) NOT NULL,
  `despesa` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`despesa`),
  INDEX `fk_Extraordinária_Despesa1_idx` (`despesa` ASC),
  CONSTRAINT `fk_Extraordinária_Despesa1`
    FOREIGN KEY (`despesa`)
    REFERENCES `GestaoDespesas`.`Despesa` (`ref`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `GestaoDespesas`.`Prestacao`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GestaoDespesas`.`Prestacao` ;

CREATE TABLE IF NOT EXISTS `GestaoDespesas`.`Prestacao` (
  `idPrestacao` INT NOT NULL AUTO_INCREMENT,
  `valor` DOUBLE NOT NULL,
  `nrSeq` INT NOT NULL,
  `estado` TINYINT(1) NOT NULL,
  `despesa` VARCHAR(45) NOT NULL,
  `inquilino` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idPrestacao`),
  INDEX `fk_Prestacao_Extraordinária1_idx` (`despesa` ASC),
  CONSTRAINT `fk_Prestacao_Extraordinária1`
    FOREIGN KEY (`despesa`)
    REFERENCES `GestaoDespesas`.`Extraordinária` (`despesa`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `GestaoDespesas`.`Movimento`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GestaoDespesas`.`Movimento` ;

CREATE TABLE IF NOT EXISTS `GestaoDespesas`.`Movimento` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `descricao` VARCHAR(65) NOT NULL,
  `valor` DOUBLE NOT NULL,
  `data` DATETIME NOT NULL,
  `conta` INT NOT NULL,
  `prestacao` INT NULL,
  `despesa` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Movimento_Conta1_idx` (`conta` ASC),
  INDEX `fk_Movimento_Prestacao1_idx` (`prestacao` ASC),
  INDEX `fk_Movimento_Despesa1_idx` (`despesa` ASC),
  CONSTRAINT `fk_Movimento_Conta1`
    FOREIGN KEY (`conta`)
    REFERENCES `GestaoDespesas`.`Conta` (`idConta`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Movimento_Prestacao1`
    FOREIGN KEY (`prestacao`)
    REFERENCES `GestaoDespesas`.`Prestacao` (`idPrestacao`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Movimento_Despesa1`
    FOREIGN KEY (`despesa`)
    REFERENCES `GestaoDespesas`.`Despesa` (`ref`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `GestaoDespesas`.`Categoria`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GestaoDespesas`.`Categoria` ;

CREATE TABLE IF NOT EXISTS `GestaoDespesas`.`Categoria` (
  `designacao` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`designacao`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `GestaoDespesas`.`Recorrente`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GestaoDespesas`.`Recorrente` ;

CREATE TABLE IF NOT EXISTS `GestaoDespesas`.`Recorrente` (
  `despesa` VARCHAR(45) NOT NULL,
  `categoria` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`despesa`),
  INDEX `fk_Recorrente_Despesa1_idx` (`despesa` ASC),
  INDEX `fk_Recorrente_Categoria1_idx` (`categoria` ASC),
  CONSTRAINT `fk_Recorrente_Despesa1`
    FOREIGN KEY (`despesa`)
    REFERENCES `GestaoDespesas`.`Despesa` (`ref`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Recorrente_Categoria1`
    FOREIGN KEY (`categoria`)
    REFERENCES `GestaoDespesas`.`Categoria` (`designacao`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `GestaoDespesas`.`InquilinoCategoria`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GestaoDespesas`.`InquilinoCategoria` ;

CREATE TABLE IF NOT EXISTS `GestaoDespesas`.`InquilinoCategoria` (
  `inquilino` VARCHAR(45) NOT NULL,
  `categoria` VARCHAR(45) NOT NULL,
  `percentagem` DOUBLE NOT NULL,
  PRIMARY KEY (`inquilino`, `categoria`),
  INDEX `fk_Inquilino_has_Categoria_Inquilino1_idx` (`inquilino` ASC),
  INDEX `fk_InquilinoCategoria_Categoria1_idx` (`categoria` ASC),
  CONSTRAINT `fk_Inquilino_has_Categoria_Inquilino1`
    FOREIGN KEY (`inquilino`)
    REFERENCES `GestaoDespesas`.`Inquilino` (`utilizador`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_InquilinoCategoria_Categoria1`
    FOREIGN KEY (`categoria`)
    REFERENCES `GestaoDespesas`.`Categoria` (`designacao`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `GestaoDespesas`.`InquilinoExtraordinária`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GestaoDespesas`.`InquilinoExtraordinária` ;

CREATE TABLE IF NOT EXISTS `GestaoDespesas`.`InquilinoExtraordinária` (
  `inquilino` VARCHAR(45) NOT NULL,
  `despesa` VARCHAR(45) NOT NULL,
  `percentagem` DOUBLE NOT NULL,
  `estado` TINYINT(1) NOT NULL,
  PRIMARY KEY (`inquilino`, `despesa`),
  INDEX `fk_Inquilino_has_Extraordinária_Extraordinária1_idx` (`despesa` ASC),
  INDEX `fk_Inquilino_has_Extraordinária_Inquilino1_idx` (`inquilino` ASC),
  CONSTRAINT `fk_Inquilino_has_Extraordinária_Inquilino1`
    FOREIGN KEY (`inquilino`)
    REFERENCES `GestaoDespesas`.`Inquilino` (`utilizador`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Inquilino_has_Extraordinária_Extraordinária1`
    FOREIGN KEY (`despesa`)
    REFERENCES `GestaoDespesas`.`Extraordinária` (`despesa`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
