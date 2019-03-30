-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `mydb` ;

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`Pizzas`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`Pizzas` ;

CREATE TABLE IF NOT EXISTS `mydb`.`Pizzas` (
  `Nr` INT NOT NULL,
  `Designação` VARCHAR(75) NULL,
  `Apresentação` TEXT NULL,
  `Preço` DECIMAL(8,2) NULL,
  `Observações` TEXT NULL,
  PRIMARY KEY (`Nr`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Funções`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`Funções` ;

CREATE TABLE IF NOT EXISTS `mydb`.`Funções` (
  `Nr` INT NOT NULL,
  `Designação` VARCHAR(50) NULL,
  PRIMARY KEY (`Nr`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Funcionários`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`Funcionários` ;

CREATE TABLE IF NOT EXISTS `mydb`.`Funcionários` (
  `Nr` INT NOT NULL,
  `Nome` VARCHAR(75) NOT NULL,
  `Função` INT NOT NULL,
  `Observações` TEXT NULL,
  PRIMARY KEY (`Nr`),
  INDEX `fk_Funcionários_Funções1_idx` (`Função` ASC),
  CONSTRAINT `fk_Funcionários_Funções1`
    FOREIGN KEY (`Função`)
    REFERENCES `mydb`.`Funções` (`Nr`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Processos`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`Processos` ;

CREATE TABLE IF NOT EXISTS `mydb`.`Processos` (
  `Nr` INT NOT NULL,
  `Designação` VARCHAR(75) NOT NULL,
  `Descrição` TEXT NULL,
  `Gestor` INT NOT NULL,
  `Observações` TEXT NULL,
  PRIMARY KEY (`Nr`),
  INDEX `fk_Processos_Funcionários1_idx` (`Gestor` ASC),
  CONSTRAINT `fk_Processos_Funcionários1`
    FOREIGN KEY (`Gestor`)
    REFERENCES `mydb`.`Funcionários` (`Nr`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Tarefas`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`Tarefas` ;

CREATE TABLE IF NOT EXISTS `mydb`.`Tarefas` (
  `Nr` INT NOT NULL,
  `Designação` VARCHAR(75) NULL,
  PRIMARY KEY (`Nr`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Máquinas`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`Máquinas` ;

CREATE TABLE IF NOT EXISTS `mydb`.`Máquinas` (
  `Nr` INT NOT NULL,
  `Designação` VARCHAR(75) NOT NULL,
  `CustoHora` DECIMAL(8,2) NOT NULL,
  `Observações` TEXT NULL,
  PRIMARY KEY (`Nr`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`MatériaPrima`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`MatériaPrima` ;

CREATE TABLE IF NOT EXISTS `mydb`.`MatériaPrima` (
  `Nr` INT NOT NULL,
  `Designação` VARCHAR(75) NOT NULL,
  `Referência` VARCHAR(50) NULL,
  `Unidade` INT NOT NULL,
  `Preço` DECIMAL(8,2) NULL,
  `Observações` TEXT NULL,
  PRIMARY KEY (`Nr`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`PizzasProdução`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`PizzasProdução` ;

CREATE TABLE IF NOT EXISTS `mydb`.`PizzasProdução` (
  `Nr` INT NOT NULL,
  `Ano` INT NOT NULL,
  `Quantidade` INT NOT NULL,
  PRIMARY KEY (`Nr`, `Ano`),
  CONSTRAINT `fk_PizzasProdução_Pizzas`
    FOREIGN KEY (`Nr`)
    REFERENCES `mydb`.`Pizzas` (`Nr`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`MatériaPrimaPizzas`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`MatériaPrimaPizzas` ;

CREATE TABLE IF NOT EXISTS `mydb`.`MatériaPrimaPizzas` (
  `Pizza` INT NOT NULL,
  `MatériaPrima` INT NOT NULL,
  `Quantidade` DECIMAL(8,2) NOT NULL,
  `Incorporação` TEXT NULL,
  PRIMARY KEY (`Pizza`, `MatériaPrima`),
  INDEX `fk_MatériaPrimaPizzas_MatériaPrima1_idx` (`MatériaPrima` ASC),
  CONSTRAINT `fk_MatériaPrimaPizzas_Pizzas1`
    FOREIGN KEY (`Pizza`)
    REFERENCES `mydb`.`Pizzas` (`Nr`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_MatériaPrimaPizzas_MatériaPrima1`
    FOREIGN KEY (`MatériaPrima`)
    REFERENCES `mydb`.`MatériaPrima` (`Nr`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`PizzasProcessos`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`PizzasProcessos` ;

CREATE TABLE IF NOT EXISTS `mydb`.`PizzasProcessos` (
  `Pizza` INT NOT NULL,
  `Processo` INT NOT NULL,
  `NrSequência` INT NOT NULL,
  PRIMARY KEY (`Pizza`, `Processo`),
  INDEX `fk_PizzasProcessos_Processos1_idx` (`Processo` ASC),
  CONSTRAINT `fk_PizzasProcessos_Processos1`
    FOREIGN KEY (`Processo`)
    REFERENCES `mydb`.`Processos` (`Nr`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_PizzasProcessos_Pizzas1`
    FOREIGN KEY (`Pizza`)
    REFERENCES `mydb`.`Pizzas` (`Nr`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`ProcessosTarefas`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`ProcessosTarefas` ;

CREATE TABLE IF NOT EXISTS `mydb`.`ProcessosTarefas` (
  `Processo` INT NOT NULL,
  `Tarefa` INT NOT NULL,
  `NrSequência` INT NULL,
  `Observações` TEXT NULL,
  PRIMARY KEY (`Processo`, `Tarefa`),
  INDEX `fk_ProcessosTarefas_Tarefas1_idx` (`Tarefa` ASC),
  CONSTRAINT `fk_ProcessosTarefas_Processos1`
    FOREIGN KEY (`Processo`)
    REFERENCES `mydb`.`Processos` (`Nr`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ProcessosTarefas_Tarefas1`
    FOREIGN KEY (`Tarefa`)
    REFERENCES `mydb`.`Tarefas` (`Nr`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`ProduçãoPizzas`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`ProduçãoPizzas` ;

CREATE TABLE IF NOT EXISTS `mydb`.`ProduçãoPizzas` (
  `Pizza` INT NOT NULL,
  `Processo` INT NOT NULL,
  `Tarefa` INT NOT NULL,
  `Funcionário` INT NOT NULL,
  `Máquina` INT NOT NULL,
  `DataHoraProdução` DATETIME NOT NULL,
  `Duração` INT NOT NULL,
  `Quantidade` INT NOT NULL,
  PRIMARY KEY (`Pizza`, `Processo`, `Tarefa`, `Funcionário`, `Máquina`, `DataHoraProdução`),
  INDEX `fk_ProduçãoPizzas_Processos1_idx` (`Processo` ASC),
  INDEX `fk_ProduçãoPizzas_Tarefas1_idx` (`Tarefa` ASC),
  INDEX `fk_ProduçãoPizzas_Funcionários1_idx` (`Funcionário` ASC),
  INDEX `fk_ProduçãoPizzas_Máquinas1_idx` (`Máquina` ASC),
  CONSTRAINT `fk_ProduçãoPizzas_Pizzas1`
    FOREIGN KEY (`Pizza`)
    REFERENCES `mydb`.`Pizzas` (`Nr`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ProduçãoPizzas_Processos1`
    FOREIGN KEY (`Processo`)
    REFERENCES `mydb`.`Processos` (`Nr`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ProduçãoPizzas_Tarefas1`
    FOREIGN KEY (`Tarefa`)
    REFERENCES `mydb`.`Tarefas` (`Nr`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ProduçãoPizzas_Funcionários1`
    FOREIGN KEY (`Funcionário`)
    REFERENCES `mydb`.`Funcionários` (`Nr`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ProduçãoPizzas_Máquinas1`
    FOREIGN KEY (`Máquina`)
    REFERENCES `mydb`.`Máquinas` (`Nr`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
