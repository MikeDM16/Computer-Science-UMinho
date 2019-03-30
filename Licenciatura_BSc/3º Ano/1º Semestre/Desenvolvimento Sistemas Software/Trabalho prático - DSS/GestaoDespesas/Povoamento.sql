-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: localhost    Database: gestaodespesas
-- ------------------------------------------------------
-- Server version	5.7.15-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `administrador`
--

DROP TABLE IF EXISTS `administrador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `administrador` (
  `utilizador` varchar(45) NOT NULL,
  `estado` tinyint(1) NOT NULL,
  PRIMARY KEY (`utilizador`),
  KEY `fk_Administrador_Utilizador1_idx` (`utilizador`),
  CONSTRAINT `fk_Administrador_Utilizador1` FOREIGN KEY (`utilizador`) REFERENCES `utilizador` (`username`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `administrador`
--

LOCK TABLES `administrador` WRITE;
/*!40000 ALTER TABLE `administrador` DISABLE KEYS */;
INSERT INTO `administrador` VALUES ('admin',1);
/*!40000 ALTER TABLE `administrador` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categoria`
--

DROP TABLE IF EXISTS `categoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `categoria` (
  `designacao` varchar(45) NOT NULL,
  PRIMARY KEY (`designacao`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categoria`
--

LOCK TABLES `categoria` WRITE;
/*!40000 ALTER TABLE `categoria` DISABLE KEYS */;
INSERT INTO `categoria` VALUES ('Água'),('Eletricidade'),('Gás'),('Internet');
/*!40000 ALTER TABLE `categoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `conta`
--

DROP TABLE IF EXISTS `conta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `conta` (
  `idConta` int(11) NOT NULL AUTO_INCREMENT,
  `saldo` double NOT NULL,
  PRIMARY KEY (`idConta`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `conta`
--

LOCK TABLES `conta` WRITE;
/*!40000 ALTER TABLE `conta` DISABLE KEYS */;
INSERT INTO `conta` VALUES (1,500),(2,0),(3,10),(4,500);
/*!40000 ALTER TABLE `conta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `despesa`
--

DROP TABLE IF EXISTS `despesa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `despesa` (
  `ref` varchar(45) NOT NULL,
  `dataLimite` date NOT NULL,
  `dataEmissao` date NOT NULL,
  `preco` double NOT NULL,
  `estado` tinyint(1) NOT NULL,
  PRIMARY KEY (`ref`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `despesa`
--

LOCK TABLES `despesa` WRITE;
/*!40000 ALTER TABLE `despesa` DISABLE KEYS */;
INSERT INTO `despesa` VALUES ('1','2017-01-24','2017-01-06',46.5,0),('2','2017-02-16','2017-01-06',250,0),('3','2017-01-06','2017-01-06',29.9,0);
/*!40000 ALTER TABLE `despesa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `extraordinária`
--

DROP TABLE IF EXISTS `extraordinária`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `extraordinária` (
  `designacao` varchar(65) NOT NULL,
  `despesa` varchar(45) NOT NULL,
  PRIMARY KEY (`despesa`),
  KEY `fk_Extraordinária_Despesa1_idx` (`despesa`),
  CONSTRAINT `fk_Extraordinária_Despesa1` FOREIGN KEY (`despesa`) REFERENCES `despesa` (`ref`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `extraordinária`
--

LOCK TABLES `extraordinária` WRITE;
/*!40000 ALTER TABLE `extraordinária` DISABLE KEYS */;
INSERT INTO `extraordinária` VALUES ('Televisão nova','2');
/*!40000 ALTER TABLE `extraordinária` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inquilino`
--

DROP TABLE IF EXISTS `inquilino`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inquilino` (
  `dataEntrada` date NOT NULL,
  `dataSaida` date DEFAULT NULL,
  `conta` int(11) NOT NULL,
  `utilizador` varchar(45) NOT NULL,
  PRIMARY KEY (`utilizador`),
  KEY `fk_Inquilino_Utilizador1_idx` (`utilizador`),
  KEY `fk_Inquilino_Conta` (`conta`),
  CONSTRAINT `fk_Inquilino_Conta` FOREIGN KEY (`conta`) REFERENCES `conta` (`idConta`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Inquilino_Utilizador1` FOREIGN KEY (`utilizador`) REFERENCES `utilizador` (`username`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inquilino`
--

LOCK TABLES `inquilino` WRITE;
/*!40000 ALTER TABLE `inquilino` DISABLE KEYS */;
INSERT INTO `inquilino` VALUES ('2017-01-06',NULL,3,'diogo'),('2017-01-06',NULL,4,'esmeralda'),('2017-01-06',NULL,2,'miguel'),('2017-01-06',NULL,1,'rui');
/*!40000 ALTER TABLE `inquilino` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inquilinocategoria`
--

DROP TABLE IF EXISTS `inquilinocategoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inquilinocategoria` (
  `inquilino` varchar(45) NOT NULL,
  `categoria` varchar(45) NOT NULL,
  `percentagem` double NOT NULL,
  PRIMARY KEY (`inquilino`,`categoria`),
  KEY `fk_Inquilino_has_Categoria_Inquilino1_idx` (`inquilino`),
  KEY `fk_InquilinoCategoria_Categoria1_idx` (`categoria`),
  CONSTRAINT `fk_InquilinoCategoria_Categoria1` FOREIGN KEY (`categoria`) REFERENCES `categoria` (`designacao`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Inquilino_has_Categoria_Inquilino1` FOREIGN KEY (`inquilino`) REFERENCES `inquilino` (`utilizador`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inquilinocategoria`
--

LOCK TABLES `inquilinocategoria` WRITE;
/*!40000 ALTER TABLE `inquilinocategoria` DISABLE KEYS */;
INSERT INTO `inquilinocategoria` VALUES ('diogo','Água',25),('diogo','Eletricidade',25),('diogo','Gás',25),('diogo','Internet',50),('esmeralda','Água',25),('esmeralda','Eletricidade',25),('esmeralda','Gás',25),('miguel','Água',25),('miguel','Eletricidade',25),('miguel','Gás',25),('miguel','Internet',25),('rui','Água',25),('rui','Eletricidade',25),('rui','Gás',25),('rui','Internet',25);
/*!40000 ALTER TABLE `inquilinocategoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inquilinoextraordinária`
--

DROP TABLE IF EXISTS `inquilinoextraordinária`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inquilinoextraordinária` (
  `inquilino` varchar(45) NOT NULL,
  `despesa` varchar(45) NOT NULL,
  `percentagem` double NOT NULL,
  `estado` tinyint(1) NOT NULL,
  PRIMARY KEY (`inquilino`,`despesa`),
  KEY `fk_Inquilino_has_Extraordinária_Extraordinária1_idx` (`despesa`),
  KEY `fk_Inquilino_has_Extraordinária_Inquilino1_idx` (`inquilino`),
  CONSTRAINT `fk_Inquilino_has_Extraordinária_Extraordinária1` FOREIGN KEY (`despesa`) REFERENCES `extraordinária` (`despesa`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Inquilino_has_Extraordinária_Inquilino1` FOREIGN KEY (`inquilino`) REFERENCES `inquilino` (`utilizador`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inquilinoextraordinária`
--

LOCK TABLES `inquilinoextraordinária` WRITE;
/*!40000 ALTER TABLE `inquilinoextraordinária` DISABLE KEYS */;
INSERT INTO `inquilinoextraordinária` VALUES ('diogo','2',25,0),('esmeralda','2',25,0),('miguel','2',25,0),('rui','2',25,0);
/*!40000 ALTER TABLE `inquilinoextraordinária` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movimento`
--

DROP TABLE IF EXISTS `movimento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `movimento` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `descricao` varchar(65) NOT NULL,
  `valor` double NOT NULL,
  `data` datetime NOT NULL,
  `conta` int(11) NOT NULL,
  `prestacao` int(11) DEFAULT NULL,
  `despesa` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_Movimento_Conta1_idx` (`conta`),
  KEY `fk_Movimento_Prestacao1_idx` (`prestacao`),
  KEY `fk_Movimento_Despesa1_idx` (`despesa`),
  CONSTRAINT `fk_Movimento_Conta1` FOREIGN KEY (`conta`) REFERENCES `conta` (`idConta`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Movimento_Despesa1` FOREIGN KEY (`despesa`) REFERENCES `despesa` (`ref`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Movimento_Prestacao1` FOREIGN KEY (`prestacao`) REFERENCES `prestacao` (`idPrestacao`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movimento`
--

LOCK TABLES `movimento` WRITE;
/*!40000 ALTER TABLE `movimento` DISABLE KEYS */;
INSERT INTO `movimento` VALUES (1,'Depósito',500,'2017-01-06 15:20:27',1,NULL,NULL),(2,'Depósito',500,'2017-01-06 15:21:05',4,NULL,NULL),(3,'Depósito',10,'2017-01-06 15:21:59',3,NULL,NULL);
/*!40000 ALTER TABLE `movimento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prestacao`
--

DROP TABLE IF EXISTS `prestacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prestacao` (
  `idPrestacao` int(11) NOT NULL AUTO_INCREMENT,
  `valor` double NOT NULL,
  `nrSeq` int(11) NOT NULL,
  `estado` tinyint(1) NOT NULL,
  `despesa` varchar(45) NOT NULL,
  `inquilino` varchar(45) NOT NULL,
  PRIMARY KEY (`idPrestacao`),
  KEY `fk_Prestacao_Extraordinária1_idx` (`despesa`),
  CONSTRAINT `fk_Prestacao_Extraordinária1` FOREIGN KEY (`despesa`) REFERENCES `extraordinária` (`despesa`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prestacao`
--

LOCK TABLES `prestacao` WRITE;
/*!40000 ALTER TABLE `prestacao` DISABLE KEYS */;
INSERT INTO `prestacao` VALUES (1,31.25,1,0,'2','diogo'),(2,31.25,2,0,'2','diogo'),(3,20.833333333333332,1,0,'2','esmeralda'),(4,20.833333333333332,2,0,'2','esmeralda'),(5,20.833333333333332,3,0,'2','esmeralda'),(6,31.25,1,0,'2','miguel'),(7,31.25,2,0,'2','miguel'),(8,20.833333333333332,1,0,'2','rui'),(9,20.833333333333332,2,0,'2','rui'),(10,20.833333333333332,3,0,'2','rui');
/*!40000 ALTER TABLE `prestacao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recorrente`
--

DROP TABLE IF EXISTS `recorrente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `recorrente` (
  `despesa` varchar(45) NOT NULL,
  `categoria` varchar(45) NOT NULL,
  PRIMARY KEY (`despesa`),
  KEY `fk_Recorrente_Despesa1_idx` (`despesa`),
  KEY `fk_Recorrente_Categoria1_idx` (`categoria`),
  CONSTRAINT `fk_Recorrente_Categoria1` FOREIGN KEY (`categoria`) REFERENCES `categoria` (`designacao`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Recorrente_Despesa1` FOREIGN KEY (`despesa`) REFERENCES `despesa` (`ref`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recorrente`
--

LOCK TABLES `recorrente` WRITE;
/*!40000 ALTER TABLE `recorrente` DISABLE KEYS */;
INSERT INTO `recorrente` VALUES ('1','Eletricidade'),('3','Internet');
/*!40000 ALTER TABLE `recorrente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `utilizador`
--

DROP TABLE IF EXISTS `utilizador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `utilizador` (
  `username` varchar(45) NOT NULL,
  `nome` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utilizador`
--

LOCK TABLES `utilizador` WRITE;
/*!40000 ALTER TABLE `utilizador` DISABLE KEYS */;
INSERT INTO `utilizador` VALUES ('admin','Administrador','admin'),('diogo','Diogo','diogo'),('esmeralda','Esmeralda','esmeralda'),('miguel','Miguel','miguel'),('rui','Rui','rui');
/*!40000 ALTER TABLE `utilizador` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-01-06 15:23:11
