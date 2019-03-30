--
-- Unidade Curricular de Bases de Dados.
-- Sistemas de Dados Relacionais.
--
-- T A B E L A S
-- Operações de Descrição de Dados.
-- Criação, alteração e remoção de tabelas.
--
-- Exemplos de Aplicação.
-- Belo, O., 2016.
--

-- Base de Dados de Trabalho.
USE Sakila;

-- T A B E L A S
-- Criação de tabelas num esquema de uma base de dados
--
-- Criação da tabela "Producers".
CREATE TABLE Producers (
	producer_id INT NOT NULL, 
	first_name VARCHAR(45) NOT NULL,
	last_name VARCHAR(45) NOT NULL,
	PRIMARY KEY (producer_id))
ENGINE = InnoDB;

-- Consulta do esquema da tabela "Producers".
DESC Producers;

-- Alteração do esquema da tabela "Producers", com a adição de um novo atributo.
ALTER TABLE Producers
	ADD NrOfFilmsProduced INT NOT NULL;

-- Modificação das propriedades de um atributo da tabela "Producers".
ALTER TABLE Producers
	MODIFY NrOfFilmsProduced INT NOT NULL DEFAULT 0;

-- Alteração do esquema da tabela "Producers", com a remoção de um dos seus atributos.
ALTER TABLE Producers
	DROP NrOfFilmsProduced;

-- Remoção da tabelas "Producers" da Base de Dados.
DROP TABLE Producers;

--
-- Criação da tabela "FilmProducers".
CREATE TABLE FilmProducers (
	producer_id INT NOT NULL, 
	film_id SMALLINT NOT NULL, 
	PRIMARY KEY (producer_id, film_id),
	FOREIGN KEY (producer_id)
		REFERENCES producers (producer_id),
	FOREIGN KEY (film_id)
		REFERENCES film (film_id)
)
ENGINE = InnoDB;

-- Consulta do esquema da tabela "FilmProducers".
DESC Producers;

-- Remoção da tabela "FilmProducers" da Base de Dados.
DROP TABLE FilmProducers;


-- Definição de restrições sobre tabelas. Alguns exemplos de aplicação, apenas para demonstração.
-- Exemplo com atribuição de um nome e restrição a aplicar sobre a tabela.
-- NOTA: A tabela "Veiculos" não existe na base de dados "Sakila".
CREATE TABLE Veiculos (
	Id INT NOT NULL,
	Matricula VARCHAR(8) NOT NULL,
	MarcaModelo VARCHAR(50) NOT NULL,
	Tipo CHAR(1) NOT NULL,
	CapacidadeCarga DECIMAL (12,2) DEFAULT 100,
	Funcionario INT,
	LugarParque VARCHAR(100),
	PRIMARY KEY (Id),
	FOREIGN KEY (Funcionario)
		REFERENCES Funcionarios (Id)
		ON DELETE NO ACTION
		ON UPDATE NO ACTION,
	CONSTRAINT chk_Veiculos 
		CHECK (Tipo IN ('N','U') AND CapacidadeCarga <= 1000)
);

-- No caso da tabela ter sido criada anteriormente, apenas teríamos que executar a seguinte 
-- instrução para definir a mesma restrição.
ALTER TABLE Veiculos
	ADD CONSTRAINT chk_Veiculos 
		CHECK (Tipo IN ('N','U') AND CapacidadeCarga <= 1000);

-- Porém, se não quisermos atribuir um nome a uma restrição que queremos estabelecer, então 
-- podemos executar simplesmente a seguinte instrução.
ALTER TABLE Veiculos
	ADD CHECK (Tipo IN ('N','U'));


-- Operações de manipulação de dados envolvendo os objetos da bases 
-- de dados criados anteriormente.
--
-- Povoamento da tabela "Producers".
INSERT INTO Producers
	(producer_id, first_name, last_name)
	VALUES (1,'José','Zacarias');
INSERT INTO Producers
	VALUES (2,'Maria','Josefa');
INSERT INTO Producers
	(first_name, last_name, producer_id)
	VALUES ('Maria José','Zajo', 3);
INSERT INTO Producers
	(producer_id, first_name, last_name)
	VALUES (4,'Afonso','Pardo'),
		(5,'Rui','Neco'),
		(6,'Ana','Rocha'),
		(7,'Catarina','Pinho');

-- Atualização de dados na tabela "Producers".
UPDATE Producers
	SET last_name = 'Zorro'
	WHERE producer_id = '3';

-- Remoção de dados na tabela "Producers".
DELETE FROM Producers
	WHERE producer_id = '3';
	
-- Consuta da informação colocada na tabela "Producers"
SELECT *
	FROM Producers;


--
-- <fim>
