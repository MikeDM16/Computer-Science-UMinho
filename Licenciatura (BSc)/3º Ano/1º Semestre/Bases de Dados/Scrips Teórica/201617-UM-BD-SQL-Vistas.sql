--
-- Unidade Curricular de Bases de Dados.
-- Sistemas de Dados Relacionais.
--
-- V I S T A S 
-- Operações de Descrição de Dados.
-- Criação, alteração e remoção de vistas.
--
-- Exemplos de Aplicação.
-- Belo, O., 2016.
--

-- Base de Dados de Trabalho.
USE Sakila;

--

-- VISTAS
--
-- Criacao da vista "vwListaEMails"
CREATE VIEW vwListaEMails AS
SELECT 'C' AS Tipo, customer_id AS Nr, 
	first_name AS Nome, last_name AS Apelido, 
	email, '______' AS Observacoes
	FROM Customer AS C;

-- Utilização da vista criada anteriormente
SELECT *
	FROM ListaEMails;

-- Utilização da vista criada com aplicação de filtros
SELECT *
	FROM ListaEMails
	WHERE Apelido LIKE 'Y%';

-- Utilizando a vista criada com aplicação de filtros e ordenação de resultados.
SELECT *
	FROM ListaEMails
	WHERE Apelido LIKE 'Y%'
	ORDER BY Apelido DESC;

-- Remoção da vista criada.
DROP VIEW ListaEMails;


-- Através da definição de uma vista, defina um objeto de dados que permita
-- criar uma única lista de contactos, com endereços postais, telefones e email
-- dos clientes, dos funcionários e dos fabricantes de equipamento, fazendo a separação
-- de cada registo consoante o tipo de contacto incorporado nesse objeto.
-- Criação da vista "vwListaContactos".
CREATE VIEW vwListaContactos AS
	SELECT 'FU' AS Tipo, FU.Id AS Codigo, FU.Nome AS Nome, 
		CONCAT(FU.rua, ', ', FU.localidade, ', ', FU.codpostal) AS Endereco,
		FU.eMail AS eMail
		FROM Funcionarios AS FU
	UNION
	SELECT 'CL', CL.Id, CL.Designacao, 
		CONCAT(CL.rua, ', ', CL.localidade, ', ', CL.codpostal),
		CL.eMail
		FROM Clientes AS CL
	UNION
	SELECT 'FA', Id, Designacao, '<Desconhecido>', '<Desconhecido>'
		FROM Fabricante
	ORDER BY Nome;

-- Remoção da vista do sistema.
DROP VIEW vwListaContactos;

-- Exemplos da utilização da vista criada numa query.
-- Numa query simples.
SELECT *
	FROM vwListaContactos;

-- Numa query com critérios de filtragem e de ordenação.
SELECT Codigo, Nome, eMail
	FROM vwListaContactos
	WHERE Tipo = 'FU'
	ORDER BY Codigo;

-- Numa query com uma subquery e uma operação de junção.
SELECT *
	FROM (SELECT * 
			FROM vwListaContactos 
			WHERE Tipo = 'FU') AS VW 
		INNER JOIN Funcionarios AS FU
		ON VW.Codigo = FU.id;

-- <fim>
--

