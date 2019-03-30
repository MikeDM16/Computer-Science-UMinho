-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema InterTrain
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema InterTrain
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `InterTrain` DEFAULT CHARACTER SET utf8 ;
USE `InterTrain` ;

-- -----------------------------------------------------
-- Table `Cliente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Cliente` (
  `NrCliente` INT NOT NULL AUTO_INCREMENT,
  `Nome` VARCHAR(64) NOT NULL,
  `Sexo` CHAR(1) NOT NULL,
  `DN` DATE NOT NULL,
  `CC` VARCHAR(8) NOT NULL,
  `NIF` VARCHAR(9) NOT NULL,
  `Telefone` VARCHAR(15) NOT NULL,
  `Email` VARCHAR(128) NOT NULL,
  `Password` VARCHAR(15) NOT NULL,
  PRIMARY KEY (`NrCliente`),
  UNIQUE INDEX `CC_UNIQUE` (`CC` ASC),
  UNIQUE INDEX `NIF_UNIQUE` (`NIF` ASC),
  UNIQUE INDEX `Email_UNIQUE` (`Email` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Comboio`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Comboio` (
  `idComboio` INT NOT NULL AUTO_INCREMENT,
  `Tipo` VARCHAR(64) NOT NULL,
  `Carruagens` INT NOT NULL,
  PRIMARY KEY (`idComboio`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Viagem`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Viagem` (
  `idViagem` INT NOT NULL AUTO_INCREMENT,
  `Data` DATE NOT NULL,
  `Preco` FLOAT NOT NULL,
  `HoraOrigem` TIME NOT NULL,
  `LocalidadeOrigem` VARCHAR(128) NOT NULL,
  `HoraDestino` TIME NOT NULL,
  `LocalidadeDestino` VARCHAR(128) NOT NULL,
  `Comboio` INT NOT NULL,
  `Duracao` TIME NOT NULL,
  PRIMARY KEY (`idViagem`),
  INDEX `fk_Viagem_Comboio1_idx` (`Comboio` ASC),
  CONSTRAINT `fk_Viagem_Comboio1`
    FOREIGN KEY (`Comboio`)
    REFERENCES `Comboio` (`idComboio`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Lugar`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Lugar` (
  `Nr` INT NOT NULL,
  `Carruagem` INT NOT NULL,
  `Classe` INT NOT NULL,
  `Mesa` TINYINT(1) NOT NULL,
  `Janela` TINYINT(1) NOT NULL,
  `Tomada` TINYINT(1) NOT NULL,
  `Comboio` INT NOT NULL,
  PRIMARY KEY (`Nr`, `Carruagem`, `Comboio`),
  INDEX `fk_Lugar_Comboio1_idx` (`Comboio` ASC),
  CONSTRAINT `fk_Lugar_Comboio1`
    FOREIGN KEY (`Comboio`)
    REFERENCES `Comboio` (`idComboio`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Reserva`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Reserva` (
  `idReserva` INT NOT NULL AUTO_INCREMENT,
  `Valor` FLOAT NOT NULL,
  `DataEmissao` DATE NOT NULL,
  `Desconto` INT NOT NULL DEFAULT 0,
  `NrLugar` INT NOT NULL,
  `Carruagem` INT NOT NULL,
  `Viagem` INT NOT NULL,
  `Cliente` INT NOT NULL,
  PRIMARY KEY (`idReserva`),
  INDEX `fk_Reserva_Viagem1_idx` (`Viagem` ASC),
  INDEX `fk_Reserva_Cliente1_idx` (`Cliente` ASC),
  INDEX `fk_Reserva_Lugar1_idx` (`NrLugar` ASC, `Carruagem` ASC),
  CONSTRAINT `fk_Reserva_Viagem1`
    FOREIGN KEY (`Viagem`)
    REFERENCES `Viagem` (`idViagem`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Reserva_Cliente1`
    FOREIGN KEY (`Cliente`)
    REFERENCES `Cliente` (`NrCliente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Reserva_Lugar1`
    FOREIGN KEY (`NrLugar` , `Carruagem`)
    REFERENCES `Lugar` (`Nr` , `Carruagem`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
